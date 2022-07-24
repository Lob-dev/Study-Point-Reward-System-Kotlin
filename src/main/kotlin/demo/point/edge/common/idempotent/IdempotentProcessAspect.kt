package demo.point.edge.common.idempotent

import demo.point.edge.common.BusinessException
import demo.point.edge.common.ErrorStatus
import demo.point.edge.common.annotation.IdempotentProcess
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
 * @Idempotent(prefix = "#request.userId", suffix = "#request.historyId")
 * @GetMapping("/test")
 * fun test(@RequestBody request: CancelPointRequest) {
 *     println("request = $request")
 * }
 * </pre>
 *
 * @see IdempotentProcess
 */
@Order(0)
@Aspect
@Component
class IdempotentProcessAspect(
    private val messageProcessService: MessageProcessService,
) {
    private val expressionParser: ExpressionParser = SpelExpressionParser()
    private val nameDiscoverer: ParameterNameDiscoverer = DefaultParameterNameDiscoverer()

    @Pointcut(value = "@annotation(demo.point.edge.common.annotation.Idempotent)")
    fun idempotentPointCut() {
    }

    @Around("idempotentPointCut()")
    @Throws(Throwable::class)
    fun process(joinPoint: ProceedingJoinPoint): Any? {
        val method = (joinPoint.signature as MethodSignature).method
        if (joinPoint.args.isEmpty()) {
            throw RuntimeException("DistributedLockAspect.process(), arguments is empty")
        }

        val evaluationContext = MethodBasedEvaluationContext(joinPoint.args[0], method, joinPoint.args, nameDiscoverer)
        val annotation = method.getAnnotation(IdempotentProcess::class.java)
        val prefix = expressionParser.parseExpression(annotation.prefix)
            .getValue(evaluationContext)
            .toString()

        val suffix = expressionParser.parseExpression(annotation.suffix)
            .getValue(evaluationContext)
            .toString()

        val processKey = "PROCESS:${prefix}:${suffix}"
        if (messageProcessService.checkDuplicateProcess(processKey)) {
            throw BusinessException(
                ErrorStatus.DUPLICATE_REQUEST,
                "it was duplicate request. prefix = $prefix, suffix = $suffix"
            )
        }

        val currentProcess = messageProcessService.createProcessBy(processKey)
        try {
            return joinPoint.proceed()
        } finally {
            messageProcessService.deleteProcessBy(currentProcess)
        }
    }
}