package ebi.ac.uk.person.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

//An Aspect to check user billing (if user has to pay money for every api call)
@Aspect
@Component
public class BillingAspect {
    private final Logger logger = LoggerFactory.getLogger(BillingAspect.class);

    @Around("within(ebi.ac.uk..*) && @annotation(ebi.ac.uk.person.annotation.UserBilling)")
    public Object checkUserBilling(ProceedingJoinPoint call) throws Throwable {

        //Implementation is intentionally left blank

        logger.info("Checking user billing...");
        MethodSignature signature = (MethodSignature) call.getSignature();
        Method method = signature.getMethod();
        return call.proceed();
    }
}
