package jatf.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jatf.api.constraints.ConstraintsService;

@Service
public class ArchitectureTestService {

	private ArchitectureTestDataProvider dataProvider;
	private ConstraintsService constraintsService;

	@Autowired
	public ArchitectureTestService(ConstraintsService constraintsService) {
		this.constraintsService = constraintsService;
		dataProvider = new ArchitectureTestDataProvider(constraintsService.getConstraints());
	}
}
