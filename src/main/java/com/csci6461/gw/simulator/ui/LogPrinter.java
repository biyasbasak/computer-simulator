package com.csci6461.gw.simulator.ui;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.Filter;  
import org.apache.logging.log4j.core.Layout;  
import org.apache.logging.log4j.core.LogEvent;  
import org.apache.logging.log4j.core.appender.AbstractAppender;  
import org.apache.logging.log4j.core.config.plugins.Plugin;  
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;  
import org.apache.logging.log4j.core.config.plugins.PluginElement;  
import org.apache.logging.log4j.core.config.plugins.PluginFactory;  
import org.apache.logging.log4j.core.layout.PatternLayout;

import javafx.application.Platform;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;  
import java.util.concurrent.locks.ReadWriteLock;  
import java.util.concurrent.locks.ReentrantReadWriteLock;

// http://blog.pikodat.com/2015/10/11/frontend-logging-with-javafx/
@Plugin(name = "LogPrinter", category = "Core", elementType = "appender", printObject = true)
public class LogPrinter extends AbstractAppender {
    private static TextFlow textFlow;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();

    protected LogPrinter(String name, Filter filter, Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @PluginFactory
    public static LogPrinter createLogPrinter(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter 
    ) {
        if(layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new LogPrinter(name, filter, layout, true);
    }

    public void append(LogEvent event) {
        readLock.lock();

        final String message = new String(getLayout().toByteArray(event));
        try {
            Platform.runLater(() -> {
                try {
                    if(textFlow != null) {
                        Text text = new Text(message);
                        textFlow.getChildren().add(text);
                    }
                } catch (final Throwable t) {
                    System.out.printf("Error while appending: %s\n", t.getMessage());
                }
            });
        } catch (final IllegalStateException ex) {
            ex.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public static void setTextFlow(TextFlow tf) {
        textFlow = tf;
    }
}
