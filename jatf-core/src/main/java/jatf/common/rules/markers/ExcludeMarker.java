package jatf.common.rules.markers;

import jatf.annotations.Exclude;
import jatf.common.IArchitectureTest;

import java.lang.annotation.Annotation;

public class ExcludeMarker extends RuleBasedMarker<Exclude> {

    public Class<? extends IArchitectureTest>[] tests;

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
            public Class<? extends IArchitectureTest>[] tests() {
                return tests;
            }
        };
    }
}
