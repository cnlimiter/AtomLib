package cn.evolvefield.mods.atomlib.utils;

import cn.evolvefield.mods.atomlib.AtomLib;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.zip.Deflater;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/14 19:50
 * Version: 1.0
 */
public class LogUtil {
    public static void log(Level loggingLevel, String message, Object... args) {
        AtomLib.LOGGER.log(loggingLevel, message, args);

    }

    public static void log(Level loggingLevel, String message, Throwable throwable) {

        AtomLib.LOGGER.log(loggingLevel, message, throwable);

    }

    public static void logInfo(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    public static void logError(String message, Object... args) {
        log(Level.ERROR, message, args);
    }

    public static void logError(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    public static Logger getLogger(String catalog, String file, String name) {
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        final Configuration configuration = loggerContext.getConfiguration();
        PatternLayout layout = PatternLayout.newBuilder().withPattern("[%d{dd-MMM-yyyy HH:mm:ss}] [%t] [%c]:%m%n").withConfiguration(configuration).build();
        RollingFileAppender appender = RollingFileAppender.newBuilder()
                .setConfiguration(configuration)
                .withFileName("./logs/atom/" + catalog + "/" + file + ".log")
                .withFilePattern("./logs/atom/" + catalog + "/" + file + "-%d{yyyy-MM-dd}.%i.log.gz")
                .setName(name)
                .withAppend(true)
                .withImmediateFlush(true)
                .withBufferedIo(true)
                .withBufferSize(8192)
                .withCreateOnDemand(false)
                .withLocking(false)
                .setLayout(layout)
                .withPolicy(CompositeTriggeringPolicy.createPolicy(SizeBasedTriggeringPolicy.createPolicy("10 M"), TimeBasedTriggeringPolicy.createPolicy("1", null)))
                .withStrategy(DefaultRolloverStrategy.createStrategy(Integer.MAX_VALUE + "", "1", "max", Deflater.NO_COMPRESSION + "", null, true, configuration)).build();
        appender.start();
        configuration.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef(name, null, null);
        LoggerConfig loggerConfig = LoggerConfig.createLogger(true, Level.INFO, name, "true", new AppenderRef[]{ref}, null, configuration, null);
        configuration.addLogger(name, loggerConfig);
        loggerContext.getLogger(name).addAppender(appender);
        loggerContext.updateLoggers();

        return loggerContext.getLogger(name);
    }
}
