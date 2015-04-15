package net.larry1123.beanshell;

import net.larry1123.elec.util.io.stream.out.AbstractLoggerOutputStream;
import net.larry1123.util.api.plugin.UtilPlugin;
import org.slf4j.Logger;

/**
 * @author Larry1123
 * @since 10/20/2014 - 10:14 AM
 */
public class LoggerOutputStream extends AbstractLoggerOutputStream {

    private final UtilPlugin plugin;
    private final boolean error;

    public LoggerOutputStream(UtilPlugin plugin, boolean error) {
        super();
        this.plugin = plugin;
        this.error = error;
    }

    @Override
    public void log(String message) {
        if (isError()) {
            getLogger().error(message);
        }
        else {
            getLogger().info(message);
        }
    }

    public Logger getLogger() {
        return getPlugin().getLogger("BeanShellOutput");
    }

    public UtilPlugin getPlugin() {
        return plugin;
    }

    public boolean isError() {
        return error;
    }

}
