package jatf.api.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestsHelperTest {

	@Test
	public void getDefaultConstraintsType() {
		assertEquals("ArchitectureTestDefaultConstraints", TestsHelper.getDefaultConstraintsType().getSimpleName());
	}

	@Test
	public void getDefaultConstraints() {
		assertEquals("ArchitectureTestDefaultConstraints", TestsHelper.getDefaultConstraints().getClass().getSimpleName());
	}
}