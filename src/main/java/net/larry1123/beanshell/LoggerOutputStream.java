package net.larry1123.beanshell;

import net.larry1123.elec.util.logger.EELogger;
import net.larry1123.util.api.plugin.UtilPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Larry1123
 * @since 10/20/2014 - 10:14 AM
 */
public class LoggerOutputStream extends ByteArrayOutputStream {

    private final UtilPlugin plugin;
    private final boolean error;

    public LoggerOutputStream(UtilPlugin plugin, boolean error) {
        super();
        this.plugin = plugin;
        this.error = error;
    }

    @Override
    public void flush() throws IOException {
        String message = toString().trim();
        if (!message.equals("")) {
            if (isError()) {
                getLogger().error(message);
            }
            else {
                getLogger().info(message);
            }
        }
        reset();
    }

    protected EELogger getLogger() {
        return getPlugin().getLogger("BeanShellOutput");
    }

    public UtilPlugin getPlugin() {
        return plugin;
    }

    public boolean isError() {
        return error;
    }

}
