/*
 * Copyright 2014 ElecEntertainment
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.larry1123.beanshell;

import net.larry1123.elec.util.config.ConfigBase;
import net.larry1123.elec.util.config.ConfigField;
import net.larry1123.elec.util.config.ConfigFile;
import net.larry1123.util.api.plugin.UtilPlugin;
import net.visualillusionsent.utils.PropertiesFile;

/**
 * @author Larry1123
 * @since 10/18/2014 - 5:32 AM
 */
public class BeanShellConfig implements ConfigBase {

    private final ConfigFile configManager;
    private final UtilPlugin plugin;

    @ConfigField(name = "Server-Port", comments = {"What port to host the web front of BeanShell on.", "This will also open a telnet port on the next port number.", "Set this to 0 to disable it."})
    protected int serverPort = 0;

    public BeanShellConfig(UtilPlugin plugin) {
        this.plugin = plugin;
        configManager = new ConfigFile(this);
        getConfigManager().save();
    }

    @Override
    public PropertiesFile getPropertiesFile() {
        return getPlugin().getConfig();
    }

    public int getServerPort() {
        return serverPort;
    }

    /**
     * Will update everything with any changes in Config file
     */
    void reload() {
        getConfigManager().reload();
    }

    public ConfigFile getConfigManager() {
        return configManager;
    }

    public UtilPlugin getPlugin() {
        return plugin;
    }

}
