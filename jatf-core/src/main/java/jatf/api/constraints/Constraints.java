package jatf.api.constraints;

import javax.annotation.Nonnull;
import java.util.Map;

public interface Constraints extends Map<Constraint, String> {

    String valueOf(@Nonnull Constraint constraint);
}
