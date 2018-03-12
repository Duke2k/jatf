package jatf.api.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jatf.persistence.ConstraintRepository;

@Service
public class ConstraintsService {

	private ConstraintRepository constraintRepository;

	@Autowired
	public ConstraintsService(ConstraintRepository constraintRepository) {
		this.constraintRepository = constraintRepository;
	}
}
