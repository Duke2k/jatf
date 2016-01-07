package jatf.common.rules;

import jatf.api.rules.ClassAnnotationBasedRule;
import jatf.common.IArchitectureTest;
import jatf.common.rules.conditions.AlwaysTrue;
import jatf.common.rules.markers.ExcludeMarker;

public final class ExcludeRule extends ClassAnnotationBasedRule<ExcludeMarker, AlwaysTrue> {

    public ExcludeRule(Class<?>[] classes, Class<? extends IArchitectureTest>[] tests) {
        super(classes, new AlwaysTrue(), new ExcludeMarker());
        marker.tests = tests;
    }

    public Class<? extends IArchitectureTest>[] tests() {
        return marker.tests;
    }
}
