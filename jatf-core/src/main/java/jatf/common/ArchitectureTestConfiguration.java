package jatf.common;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static jatf.common.ArchitectureTestRunListener.report;

public class ArchitectureTestConfiguration {

    private Map<String, String> properties = newHashMap();

    @Nonnull
    public String getProperty(@Nonnull String name) {
        String value = properties.get(name);
        return value != null ? value : "";
    }

    @SuppressWarnings("unused")
    public void setProperty(@Nonnull String name, @Nonnull String value) {
        report(String.format("Configuration '%s' = %s", name, value));
        properties.put(name, value);
    }
}
