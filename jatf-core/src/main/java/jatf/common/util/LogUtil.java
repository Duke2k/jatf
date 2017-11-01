package jatf.common.util;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

public class LogUtil {

	@Nonnull
	public static Logger getLogger(@Nonnull Class<?> clazz) {
		return Logger.getLogger(clazz.getName());
	}

	@Nonnull
	public static Logger getLogger() {
		return Logger.getGlobal();
	}
}
