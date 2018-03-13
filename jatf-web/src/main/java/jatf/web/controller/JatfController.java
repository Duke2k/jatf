/*
  This file is part of JATF.
  <p>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.web.controller;

import java.util.Set;

import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jatf.api.constraints.ConstraintsService;
import jatf.common.ArchitectureTestService;
import junit.framework.TestResult;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class JatfController {

	private ArchitectureTestService architectureTestService;
	private ConstraintsService constraintsService;

	@Autowired
	public JatfController(ArchitectureTestService architectureTestService, ConstraintsService constraintsService) {
		this.architectureTestService = architectureTestService;
		this.constraintsService = constraintsService;
	}

	@RequestMapping(path = JatfRoutes.PATH_RUN_TESTS, method = RequestMethod.PUT)
	public TestResult runTests(@RequestBody Class<?> clazz,
														 @RequestParam(value = JatfRoutes.PARAM_TEST_NAMES) String testNamesJson)
			throws InitializationError, ClassNotFoundException {
		TestResult testResult = new TestResult();
		Gson gson = new Gson();
		String[] testNames = gson.fromJson(testNamesJson, String[].class);
		Set<String> availableTestNames = architectureTestService.getTestNames();
		for (String testName : testNames) {
			if (availableTestNames.contains(testName)) {
				architectureTestService.runTest(testName, clazz);
			} else {
				throw new IllegalArgumentException(testName + " is an invalid test name");
			}
		}
		return testResult;
	}
}
