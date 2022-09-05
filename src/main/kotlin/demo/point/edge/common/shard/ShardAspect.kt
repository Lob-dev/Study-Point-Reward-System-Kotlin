package demo.point.edge.common.shard

import demo.point.edge.common.Constant.Companion.SHARDING_ORDER
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.expression.MethodBasedEvaluationContext
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.ParameterNameDiscoverer
import org.springframework.core.annotation.Order
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.stereotype.Component

/**
 * <pre>
 * @Sharding(key = "#request.userId")
 * @GetMapping("/test")
 * fun test(@RequestBody request: PointCurrentAvailableFindRequest): Page<PointHistory> {
 *     val condition: Condition = JPointHistory.POINT_HISTORY.USER_ID.eq(request.userId)
 *     return pointHistoryService.getPage(PageRequest.of(0, 10), condition)
 * }
 * </pre>
 *
 * @see Sharding
 */
@Order(SHARDING_ORDER)
@Aspect
@Component
class ShardAspect {
    private val expressionParser: ExpressionParser = SpelExpressionParser()
    private val nameDiscoverer: ParameterNameDiscoverer = DefaultParameterNameDiscoverer()

    @Pointcut(value = "@annotation(demo.point.edge.common.shard.Sharding)")
    fun shardingPointCut() {
    }

    @Around("shardingPointCut()")
    @Throws(Throwable::class)
    fun process(joinPoint: ProceedingJoinPoint): Any? {
        val method = (joinPoint.signature as MethodSignature).method
        if (joinPoint.args.isEmpty()) {
            throw RuntimeException("DistributedLockAspect.process(), arguments is empty")
        }

        val evaluationContext = MethodBasedEvaluationContext(joinPoint.args[0], method, joinPoint.args, nameDiscoverer)
        val annotation = method.getAnnotation(Sharding::class.java)
        val key = expressionParser.parseExpression(annotation.key)
                .getValue(evaluationContext)
                .toString()

        return try {
            TransactionShardManager.setCurrentTransactionIsSharding(true)
            ShardKeyHolder.setKey(key.toLong())
            joinPoint.proceed()
        } finally {
            ShardKeyHolder.refresh()
            TransactionShardManager.refresh()
        }
    }
}