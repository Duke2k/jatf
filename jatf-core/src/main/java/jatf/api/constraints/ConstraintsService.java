package jatf.api.constraints;

import jatf.common.ArchitectureTestDefaultConstraints;
import jatf.persistence.ConstraintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Service
public class ConstraintsService {

  private ConstraintRepository constraintRepository;

  @Autowired
  public ConstraintsService(ConstraintRepository constraintRepository) {
    this.constraintRepository = constraintRepository;
  }

  private Constraint getConstraint(@Nonnull String name) {
    return mapToApiConstraint(constraintRepository.findByName(name));
  }

  @Nullable
  public String getValue(@Nonnull Constraint constraint) {
    jatf.persistence.Constraint persistenceConstraint = constraintRepository.findByName(constraint.name());
    if (persistenceConstraint != null) {
      return persistenceConstraint.getValue();
    }
    return null;
  }

  public void updateConstraints() {
    for (Map.Entry<Constraint, String> defaultConstraint : new ArchitectureTestDefaultConstraints().entrySet()) {
      if (getConstraint(defaultConstraint.getKey().name()) != null) {
        String value = getValue(defaultConstraint.getKey());
        if (value == null || !value.equals(defaultConstraint.getValue())) {
          jatf.persistence.Constraint persistenceConstraintOrNull =
              constraintRepository.findByName(defaultConstraint.getKey().name());
          if (persistenceConstraintOrNull == null) {
            persistenceConstraintOrNull = mapToPersistenceConstraint(defaultConstraint.getKey(), defaultConstraint.getValue());
          } else {
            persistenceConstraintOrNull.setValue(defaultConstraint.getValue());
          }
          constraintRepository.save(persistenceConstraintOrNull);
        }
      }
    }
  }

  @Nonnull
  public Constraints getConstraints() {
    updateConstraints();
    List<jatf.persistence.Constraint> allConstraints = constraintRepository.findAll();
    Constraints result = new HashMapBasedConstraints();
    for (jatf.persistence.Constraint persistenceConstraint : allConstraints) {
      result.put(Constraint.valueOf(persistenceConstraint.getName()), persistenceConstraint.getValue());
    }
    return result;
  }

  @Nullable
  private Constraint mapToApiConstraint(@Nullable jatf.persistence.Constraint persistenceConstraint) {
    if (persistenceConstraint != null) {
      return Constraint.valueOf(persistenceConstraint.getName());
    }
    return null;
  }

  @Nonnull
  private jatf.persistence.Constraint mapToPersistenceConstraint(@Nonnull Constraint apiConstraint, @Nonnull String value) {
    jatf.persistence.Constraint persistenceConstraint = new jatf.persistence.Constraint();
    persistenceConstraint.setName(apiConstraint.name());
    persistenceConstraint.setValue(value);
    return persistenceConstraint;
  }
}
