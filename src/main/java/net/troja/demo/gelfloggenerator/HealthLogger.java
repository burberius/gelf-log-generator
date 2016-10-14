package net.troja.demo.gelfloggenerator;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HealthLogger {
    private final static Logger LOGGER = LogManager.getLogger(HealthLogger.class);
    private final MBeanServer mbs;
    private ObjectName name;
    private final OperatingSystemMXBean mxBean;
    private final Runtime runtime;

    public HealthLogger() {
        LOGGER.info("Starting health logger");
        mbs = ManagementFactory.getPlatformMBeanServer();
        runtime = Runtime.getRuntime();
        try {
            name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        } catch (final MalformedObjectNameException | NullPointerException e) {
            LOGGER.warn("Could not get operating system bean!", e);
            name = null;
        }
        mxBean = ManagementFactory.getOperatingSystemMXBean();
        LOGGER.info("System: " + mxBean.getName() + " " + mxBean.getArch() + " " + mxBean.getVersion() + " " + mxBean.getAvailableProcessors() + " CPUs ");
    }

    @Scheduled(fixedRate = 60000)
    public void logHealth() {
        if (LOGGER.isInfoEnabled()) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Health state: CPU ").append(getProcessCpuLoad()).append(" Load ").append(mxBean.getSystemLoadAverage());
            builder.append(" Free Mem ").append(runtime.freeMemory()).append(" Max Mem ").append(runtime.maxMemory());
            builder.append(" Total Mem ").append(runtime.totalMemory());
            LOGGER.info(builder.toString());
        }
    }

    private double getProcessCpuLoad() {
        if (name == null) {
            return Double.NaN;
        }
        AttributeList list = null;
        try {
            list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
        } catch (InstanceNotFoundException | ReflectionException e) {
            LOGGER.warn("Could not get ProcessCpuLoad attribute!", e);
        }

        if ((list == null) | list.isEmpty()) {
            return Double.NaN;
        }

        final Attribute att = (Attribute) list.get(0);
        final Double value = (Double) att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0) {
            return Double.NaN;
        }
        // returns a percentage value with 1 decimal point precision
        return ((int) (value * 1000) / 10.0);
    }
}
