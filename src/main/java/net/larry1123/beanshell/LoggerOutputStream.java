package net.larry1123.beanshell;

import net.larry1123.elec.util.logger.EELogger;
import net.larry1123.util.api.plugin.UtilPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Larry1123
 * @since 10/20/2014 - 10:14 AM
 */
public class LoggerOutputStream extends OutputStream {

    protected StringBuilder stringBuilder = new StringBuilder();
    protected ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    protected UtilPlugin plugin;
    protected boolean error;

    public LoggerOutputStream(UtilPlugin plugin, boolean error) {
        this.plugin = plugin;
        this.error = error;
    }

    @Override
    public void write(int b) throws IOException {
        byteArrayOutputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        String message = byteArrayOutputStream.toString().trim();
        if (!message.equals("")) {
            if (error) {
                getLogger().error(message);
            }
            else {
                getLogger().info(message);
            }
        }
        byteArrayOutputStream.reset();
    }

    @Override
    public void close() throws IOException {
        byteArrayOutputStream.close();
    }

    protected EELogger getLogger() {
        return plugin.getSubLogger("BeanShellOutput");
    }

}
