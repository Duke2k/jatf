package jatf.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstraintRepository extends JpaRepository<Constraint, Long> {

  Constraint findByName(String name);
}
