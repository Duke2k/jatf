package jatf.api.constraints;

import java.util.HashMap;

import javax.annotation.Nonnull;

public class HashMapBasedConstraints extends HashMap<Constraint, String> implements Constraints {

	@Override
	@Nonnull
	public String valueOf(@Nonnull Constraint constraint) {
		String result = get(constraint);
		return result == null ? "" : result;
	}
}
