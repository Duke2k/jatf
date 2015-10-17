package jatf.rulegenerators;

import jatf.common.rules.ArchitectureTestRule;
import jatf.common.rules.MustUseRule;
import jatf.common.rules.RuleGenerator;
import jatf.common.rules.markers.ArchitectureTestMarker;
import jatf.common.rules.markers.MustUseMarker;

import javax.annotation.Nonnull;

public class EmptyMustUseRuleGenerator implements RuleGenerator<MustUseMarker, MustUseRule> {
    @Nonnull
    public ArchitectureTestRule generateArchitectureTestRuleFor(@Nonnull Class<?>[] classes) {
        return new ArchitectureTestRule(new Class[0], new ArchitectureTestMarker());
    }

    @Nonnull
    public MustUseRule generateRule() {
        return new MustUseRule(new Class[0], new MustUseMarker());
    }
}
