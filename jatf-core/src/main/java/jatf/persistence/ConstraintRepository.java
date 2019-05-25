package jatf.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;

@Repository
public interface ConstraintRepository extends JpaRepository<Constraint, Long> {

  @Nullable
  Constraint findByName(String name);
}
