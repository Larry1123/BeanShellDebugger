//Copyright (C) 2011  Ryan Michela
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

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

import bsh.Interpreter;
import com.google.common.io.Files;
import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.plugin.Plugin;
import net.larry1123.util.api.plugin.UtilPlugin;
import net.larry1123.util.api.plugin.commands.Command;
import org.slf4j.MarkerFactory;

import java.io.File;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Path;

public class BeanShell extends UtilPlugin {

    private Interpreter bsh;
    private BshCommand bshCommand;
    private BeanShellConfig config;
    private BeanShellListener listener;

    @Override
    public boolean enable() {
        setBeanShellConfig(new BeanShellConfig(this));
        getLogger().info("Starting BeanShell");
        PrintStream out = new PrintStream(new LoggerOutputStream(this, false), true);
        PrintStream err = new PrintStream(new LoggerOutputStream(this, true), true);
        setBsh(new Interpreter(new StringReader(""), out, err, false, null));
        try {
            // Set up debug environment with globals
            getBsh().set("server", Canary.getServer());
            getBsh().set("bans", Canary.bans());
            getBsh().set("channels", Canary.channels());
            getBsh().set("commands", Canary.commands());
            getBsh().set("userAndGroup", Canary.usersAndGroups());
            getBsh().set("warps", Canary.warps());
            getBsh().set("kits", Canary.kits());
            getBsh().set("whiteList", Canary.whitelist());
            getBsh().set("ops", Canary.ops());
            getBsh().set("reserveList", Canary.reservelist());
            getBsh().set("hooks", Canary.hooks());
            getBsh().set("db", Canary.db());
            getBsh().set("pluginManager", Canary.pluginManager());
            getBsh().set("permissions", Canary.permissionManager());
            getBsh().set("help", Canary.help());
            getBsh().set("factory", Canary.factory());
            getBsh().set("scoreboards", Canary.scoreboards());
            getBsh().set("motd", Canary.motd());
            getBsh().set("playerSelector", Canary.playerSelector());
            getBsh().set("logger", getLogger());
            getBsh().set("classLoader", getClass().getClassLoader());

            // Create an alias for each plugin name using its class name
            for (Plugin plugin : Canary.pluginManager().getPlugins()) {
                getLogger().info("Regisering object " + plugin.getName());
                getBsh().set(plugin.getName(), plugin);
            }
            // Source any .bsh files in the plugin directory
            Path sourcesPath = getPluginDataFolder().toPath().resolve("scripts");
            Files.createParentDirs(sourcesPath.toFile());
            for (File file : Files.fileTreeTraverser().children(sourcesPath.toFile())) {
                Path path = file.toPath();
                String filename = path.getFileName().toString();
                if (Files.getFileExtension(filename).equals(".bsh")) {
                    getLogger().info("Sourcing file " + Files.getNameWithoutExtension(filename));
                    getBsh().source(path.toString());
                }
                else {
                    getLogger().info("*** skipping " + path.toAbsolutePath().toString());
                }
            }
            getBsh().eval("setAccessibility(true)"); // turn off access restrictions
            if (getBeanShellConfig().getServerPort() != 0) {
                getBsh().eval("server(" + getBeanShellConfig().getServerPort() + ")");
                getLogger().info("BeanShell web console at http://localhost:" + getBeanShellConfig().getServerPort());
                getLogger().info("BeanShell telnet console at localhost:" + (getBeanShellConfig().getServerPort() + 1));
            }
            // Register the bsh command
            setBshCommand(new BshCommand(getBsh()));
            if (!regCommand(getBshCommand())) {
                String message = "Unable to register Command!";
                getLogger().error(message);
                enableFailed(message);
                return false;
            }

            // Register Plugin Enable/Disable Listener
            setListener(new BeanShellListener(this, getBsh()));
            registerListener(getListener());
        }
        catch (Exception e) {
            getLogger().error("Error in BeanShell. ", e);
            enableFailed(e.toString());
            return false;
        }
        enabled();
        return true;
    }

    @Override
    public void disable() {
        Canary.commands().unregisterCommands(this);
        Canary.hooks().unregisterPluginListeners(this);
    }

    protected boolean regCommand(Command command) {
        try {
            registerCommand(command);
            return true;
        }
        catch (CommandDependencyException e) {
            getLogger().error(MarkerFactory.getMarker("Commands"), "Failed to add command: " + command.getCommandData().getAliases()[0], e);
            command.setLoaded(false);
            return false;
        }
    }

    public Interpreter getBsh() {
        return bsh;
    }

    public void setBsh(Interpreter bsh) {
        this.bsh = bsh;
    }

    public BshCommand getBshCommand() {
        return bshCommand;
    }

    public void setBshCommand(BshCommand bshCommand) {
        this.bshCommand = bshCommand;
    }

    public BeanShellConfig getBeanShellConfig() {
        return config;
    }

    public void setBeanShellConfig(BeanShellConfig config) {
        this.config = config;
    }

    public BeanShellListener getListener() {
        return listener;
    }

    public void setListener(BeanShellListener listener) {
        this.listener = listener;
    }

}
