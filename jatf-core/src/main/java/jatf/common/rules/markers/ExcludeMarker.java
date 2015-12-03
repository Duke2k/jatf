package jatf.common.rules.markers;

import jatf.annotations.Exclude;

import java.lang.annotation.Annotation;

public class ExcludeMarker extends RuleBasedMarker<Exclude> {

    public Class<?>[] tests;

    @Override
    public Class<Exclude> annotationType() {
        return Exclude.class;
    }

    @Override
    public Exclude createAnnotation() {
        return new Exclude() {

            @Override
            public String toString() {
                return annotationType().getSimpleName();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Exclude.class;
            }

            @Override
            public Class<?>[] tests() {
                return tests;
            }
        };
    }
}
