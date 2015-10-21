package jatf.common;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ArchitectureTestConfiguration {

    private Map<String, String> properties = newHashMap();

    @Nonnull
    public String getProperty(@Nonnull String name) {
        String value = properties.get(name);
        return value != null ? value : "";
    }
}