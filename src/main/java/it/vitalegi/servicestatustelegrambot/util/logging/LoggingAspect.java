package it.vitalegi.servicestatustelegrambot.util.logging;

import java.util.concurrent.atomic.AtomicLong;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

	private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	private static AtomicLong ID = new AtomicLong();

	@Around("@annotation(LogExecutionTime)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		String id = getId();

		long startTime = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			logger.debug(messageStart(id, joinPoint.getSignature().getName()));
		}
		if (logger.isDebugEnabled()) {
			for (int i = 0; i < joinPoint.getArgs().length; i++) {
				logger.debug("{} Argument [{}]: {}", id, i, joinPoint.getArgs()[i]);
			}
		}
		try {
			Object out = joinPoint.proceed();
			logger.info(messageEndOk(id, joinPoint.getSignature().getName(), System.currentTimeMillis() - startTime));
			if (logger.isDebugEnabled()) {
				logger.debug("{} Result: {}", id, out);
			}
			return out;
		} catch (Throwable e) {
			logger.error(messageEndError(id, joinPoint.getSignature().getName(), System.currentTimeMillis() - startTime,
					e.getClass().getName(), e.getMessage()));
			throw e;
		}
	}

	protected String getId() {
		return ID.incrementAndGet() + "";
	}

	protected String messageStart(String id, String methodNme) {
		return String.format("%s - Start Invoke: %s", id, methodNme);
	}

	protected String messageEndOk(String id, String methodNme, long duration) {
		return String.format("%s - End Invoke:   %s | Result: %s | Duration: %dms", //
				id, methodNme, "OK", duration);
	}

	protected String messageEndError(String id, String methodNme, long duration, String error, String errorMessage) {
		return String.format("%s - End Invoke:   %s | Result: %s | Duration: %dms | Error: %s | Message: %s", //
				id, methodNme, "KO", duration, error, errorMessage);
	}

	public void logMethodStart() {

		String id = ID.incrementAndGet() + "";

		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		logger.info("{} Start Invoke: {}", id, stackTrace[1].getMethodName());
	}

	public void logMethodEnd() {

		String id = ID.incrementAndGet() + "";

		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		logger.info("{} End Invoke:   {}", id, stackTrace[1].getMethodName());
	}

}