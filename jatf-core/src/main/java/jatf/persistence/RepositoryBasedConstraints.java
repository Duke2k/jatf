package jatf.persistence;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jatf.api.constraints.Constraint;
import jatf.api.constraints.Constraints;

@Component
public class RepositoryBasedConstraints implements Constraints {

	private ConstraintRepository repository;

	@Autowired
	public RepositoryBasedConstraints(ConstraintRepository repository) {
		this.repository = repository;
	}

	@Override
	public String valueOf(@Nonnull Constraint constraint) {
		return repository.findByName(constraint.name()).getValue();
	}

	@Override
	public int size() {
		return (int) repository.count();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public String get(Object key) {
		return null;
	}

	@Override
	public String put(Constraint key, String value) {
		return null;
	}

	@Override
	public String remove(Object key) {
		return null;
	}

	@Override
	public void putAll(Map<? extends Constraint, ? extends String> m) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Set<Constraint> keySet() {
		return null;
	}

	@Override
	public Collection<String> values() {
		return null;
	}

	@Override
	public Set<Entry<Constraint, String>> entrySet() {
		return null;
	}
}
