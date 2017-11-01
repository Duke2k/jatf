/**
 * This file is part of JATF.
 * <p>
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * <p>
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.rulegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import jatf.common.rules.ArchitectureTestRule;
import jatf.common.rules.MustUseRule;

public class EmptyMustUseRuleGeneratorTest {

	@Test
	public void emptyMustUseRuleGenerator_Valid() {
		EmptyMustUseRuleGenerator generator = new EmptyMustUseRuleGenerator();
		Class<?>[] classes = new Class[0];
		ArchitectureTestRule rule1 = generator.generateArchitectureTestRuleFor(classes);
		assertEquals(rule1.patterns().length, 0);
		assertEquals(rule1.dependencies().length, 0);
		assertFalse(rule1.omitConventions());
		assertFalse(rule1.omitMetrics());
		MustUseRule rule2 = generator.generateRule();
		assertEquals(rule2.types().length, 0);
	}
}
