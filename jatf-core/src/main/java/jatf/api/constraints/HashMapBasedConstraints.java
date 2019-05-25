package jatf.api.constraints;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class HashMapBasedConstraints extends HashMap<Constraint, String> implements Constraints {

  @Override
  @Nonnull
  public String valueOf(@Nonnull Constraint constraint) {
    String result = get(constraint);
    return result == null ? "" : result;
  }
}
