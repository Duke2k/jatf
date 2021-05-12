# jatf

Java Architecture Testing Framework (JATF)

This framework allows developers to quickly integrate code quality tests as dynamic unit tests, without the need of
static code analysis tools. It uses annotations and rules to determine which classes of your project are tested for
conventions, metrics, dependencies, or patterns. The framework is currently the first prototype, as a result of my
master's thesis. The project is licensed under GPLv3 (see http://gplv3.fsf.org/).

## Installation

To install, simply add a dependency to jatf-tests as follows:
<code>
<pre><dependency>
<groupId>com.github.duke2k.jatf</groupId>
<artifactId>jatf-tests</artifactId>
</dependency></pre>
</code>
Afterwards, define a test class in your project which runs the default test suite, as follows:
<code>
import jatf.suites.AllArchitectureTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AllArchitectureTests.class})
...
</code>
You will also need to define a bean which implements <i>Constraints</i>, as follows:
<code>
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns="http://www.springframework.org/schema/beans"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
<bean id="constraints" class="(full class name)"/>
</beans>
</code>
and then
<code>
import jatf.api.constraints.Constraint; import jatf.api.constraints.Constraints; import java.util.HashMap;

public class (class name) extends HashMap<Constraint, String> implements Constraints { ... }
</code>
For reference as of how this class should be implemented, refer to jatf.common.ArchitectureTestDefaultConstraints. I'm
sure you get the idea.
