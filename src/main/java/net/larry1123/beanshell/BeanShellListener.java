package net.larry1123.beanshell;

import bsh.EvalError;
import bsh.Interpreter;
import net.canarymod.hook.HookHandler;
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

    private final UtilPlugin plugin;
    private final Interpreter bsh;

    public BeanShellListener(UtilPlugin plugin, Interpreter bsh) {
        this.plugin = plugin;
        this.bsh = bsh;
    }

    @HookHandler
    public void pluginEnable(PluginEnableHook hook) {
        try {
            Plugin plugin = hook.getPlugin();
            getLogger().info("Registering object " + plugin.getName() + ". Plugin has been enabled.");
            getBsh().set(plugin.getName(), plugin);
        }
        catch (EvalError evalError) {
            getLogger().error("Error adding plugin", evalError);
        }
    }

    @HookHandler
    public void pluginDisable(PluginDisableHook hook) {
        try {
            Plugin plugin = hook.getPlugin();
            getBsh().unset(plugin.getName());
            getLogger().info("Unregistered object " + plugin.getName() + ". Plugin was disabled.");
        }
        catch (EvalError evalError) {
            getLogger().error("Error removing plugin", evalError);
        }
    }

    public EELogger getLogger() {
        return getPlugin().getLogger();
    }

    public UtilPlugin getPlugin() {
        return plugin;
    }

    public Interpreter getBsh() {
        return bsh;
    }

}
