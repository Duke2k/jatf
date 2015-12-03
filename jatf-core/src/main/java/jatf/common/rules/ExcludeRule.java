package jatf.common.rules;

import jatf.api.rules.ClassAnnotationBasedRule;
import jatf.common.rules.conditions.AlwaysTrue;
import jatf.common.rules.markers.ExcludeMarker;

public final class ExcludeRule extends ClassAnnotationBasedRule<ExcludeMarker, AlwaysTrue> {

    public ExcludeRule(Class<?>[] classes, Class<?>[] tests) {
        super(classes, new AlwaysTrue(), new ExcludeMarker());
        marker.tests = tests;
    }

    public Class<?>[] tests() {
        return marker.tests;
    }
}
