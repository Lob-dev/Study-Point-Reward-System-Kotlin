package demo.point.edge.common.lock

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.context.expression.MethodBasedEvaluationContext
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.ParameterNameDiscoverer
import org.springframework.core.annotation.Order
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.stereotype.Component

/**
 * <pre>
 * @DistributedLock(prefix = "POINT:", key = "#request.userId", leaseTime = 3)
 * @GetMapping("/test")
 * fun test(@RequestBody request: Request) {}
 *
 * data class Request(val userId: Long)
 * </pre>
 *
 * @see DistributedLock
 */
@Order(1)
@Aspect
@Component
class DistributedLockAspect(
    private val redissonClient: RedissonClient,
) {
    private val expressionParser: ExpressionParser = SpelExpressionParser()
    private val nameDiscoverer: ParameterNameDiscoverer = DefaultParameterNameDiscoverer()

    @Pointcut(value = "@annotation(demo.point.edge.common.lock.DistributedLock)")
    fun distributedLockPointCut() {
    }

    @Around("distributedLockPointCut()")
    @Throws(Throwable::class)
    fun process(joinPoint: ProceedingJoinPoint): Any? {
        val method = (joinPoint.signature as MethodSignature).method
        if (joinPoint.args.isEmpty()) {
            throw RuntimeException("DistributedLockAspect.process(), arguments is empty")
        }

        val evaluationContext = MethodBasedEvaluationContext(joinPoint.args[0], method, joinPoint.args, nameDiscoverer)
        val annotation = method.getAnnotation(DistributedLock::class.java)
        val key = expressionParser.parseExpression(annotation.key)
            .getValue(evaluationContext)
            .toString()

        val lockName = "${annotation.prefix}${key}${annotation.suffix}"
        val rLock: RLock = redissonClient.getLock(lockName)
        try {
            if (!rLock.tryLock(annotation.waitTime, annotation.leaseTime, annotation.timeUnit)) {
                throw BusinessException(ErrorStatus.LOCK_TIMEOUT, "it was tryLock timeout. lock name = $lockName")
            }

            return joinPoint.proceed()
        } finally {
            if (rLock.isLocked) {
                rLock.unlock()
            }
        }
    }
}