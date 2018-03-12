package jatf.common;

import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;

import jatf.api.constraints.ConstraintsService;

@Service
public class ArchitectureTestService {

	private ArchitectureTestDataProvider dataProvider;

	@Autowired
	public ArchitectureTestService(ConstraintsService constraintsService) {
		dataProvider = new ArchitectureTestDataProvider(constraintsService.getConstraints());
	}

	public void addClassToTest(@Nonnull String testName, @Nonnull Class<?> clazz) {
		dataProvider.addClassToTest(testName, clazz);
	}

	public void runTests(@Nonnull String testName, @Nonnull RunNotifier notifier) throws ClassNotFoundException, InitializationError {
		Set<Class<?>> classesToTest = dataProvider.getClassesFor(testName);
		DataProviderRunner runner = new DataProviderRunner(Class.forName(testName));
		runner.run(notifier);
	}
}
