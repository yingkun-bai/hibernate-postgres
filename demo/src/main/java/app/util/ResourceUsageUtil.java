package app.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceUsageUtil {
	public static void logResourceUsage(Phase phase) {
		// Get memory usage
		Runtime runtime = Runtime.getRuntime();
		long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		long maxMemory = runtime.maxMemory();

		// Get CPU usage
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		double systemLoad = osBean.getSystemLoadAverage();

		// Log the information
		log.info("{} - Memory Used: {} MB", phase, usedMemory / (1024 * 1024));
		log.info("{} - Max Memory: {} MB", phase, maxMemory / (1024 * 1024));
		log.info("{} - System Load Average: {}", phase, systemLoad);
	}

	public enum Phase {
		BEFORE_TEST, AFTER_TEST
	}
}
