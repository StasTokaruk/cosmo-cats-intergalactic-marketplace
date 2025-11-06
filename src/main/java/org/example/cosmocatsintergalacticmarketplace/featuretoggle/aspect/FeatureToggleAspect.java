package org.example.cosmocatsintergalacticmarketplace.featuretoggle.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.cosmocatsintergalacticmarketplace.featuretoggle.FeatureToggleProperties;
import org.example.cosmocatsintergalacticmarketplace.featuretoggle.annotation.FeatureToggleAnnotation;
import org.example.cosmocatsintergalacticmarketplace.featuretoggle.exception.FeatureNotAvailableException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureToggleAspect {

    private final FeatureToggleProperties props;

    public FeatureToggleAspect(FeatureToggleProperties props) {
        this.props = props;
    }

    @Around("@annotation(featureToggle)")
    public Object around(ProceedingJoinPoint pjp, FeatureToggleAnnotation featureToggle) throws Throwable {
        String featureKey = featureToggle.value();
        boolean enabled = props.isEnabled(featureKey);

        if (!enabled) {
            throw new FeatureNotAvailableException(featureKey);
        }
        return pjp.proceed();
    }
}
