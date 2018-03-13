package jatf.common;

import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;

import jatf.api.constraints.ConstraintsService;
import jatf.api.tests.TestNamesHelper;

@Service
public class ArchitectureTestService {

	private ArchitectureTestDataProvider dataProvider;

	@Autowired
	public ArchitectureTestService(ConstraintsService constraintsService) {
		dataProvider = new ArchitectureTestDataProvider(constraintsService.getConstraints());
	}

	public void runTest(@Nonnull String testName, @Nonnull Class<?> clazz) throws ClassNotFoundException, InitializationError {
		dataProvider.addClassToTest(testName, clazz);
		DataProviderRunner runner = new DataProviderRunner(Class.forName(testName));
		RunNotifier notifier = new RunNotifier();
		runner.run(notifier);
	}

	@Nonnull
	public Set<String> getTestNames() {
		return TestNamesHelper.getTestNames();
	}
}
