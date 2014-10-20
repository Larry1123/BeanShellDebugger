package net.larry1123.beanshell;

import bsh.EvalError;
import bsh.Interpreter;
import net.canarymod.hook.system.PluginDisableHook;
import net.canarymod.hook.system.PluginEnableHook;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;
import net.larry1123.elec.util.logger.EELogger;
import net.larry1123.util.api.plugin.UtilPlugin;

/**
 * @author Larry1123
 * @since 10/20/2014 - 11:47 AM
 */
public class BeanShellListener implements PluginListener {

    protected final UtilPlugin plugin;
    protected final Interpreter bsh;

    public BeanShellListener(UtilPlugin plugin, Interpreter bsh) {
        this.plugin = plugin;
        this.bsh = bsh;
    }

    public void pluginEnable(PluginEnableHook hook) {
        try {
            Plugin plugin = hook.getPlugin();
            getLogger().info("Regisering object " + plugin.getName());
            bsh.set(plugin.getName(), plugin);
        }
        catch (EvalError evalError) {
            getLogger().error("Error adding plugin", evalError);
        }
    }

    public void pluginDisable(PluginDisableHook hook) {
        try {
            Plugin plugin = hook.getPlugin();
            getLogger().info("Unregisering object " + plugin.getName() + ", Plugin is now disabled.");
            bsh.unset(plugin.getName());
        }
        catch (EvalError evalError) {
            getLogger().error("Error removeing plugin", evalError);
        }
    }

    public EELogger getLogger() {
        return plugin.getLogger();
    }

}
