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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@RequestMapping(path = JatfRoutes.RUN_TESTS, method = RequestMethod.PUT)
	public TestResult runTests(@RequestBody Class<?> clazz,
														 @RequestParam(value = JatfRoutes.PARAM_TESTS_TO_CLASSES_MAP) String testsToClassesJson) {
		TestResult testResult = new TestResult();
		// TODO

		return testResult;
	}
}
