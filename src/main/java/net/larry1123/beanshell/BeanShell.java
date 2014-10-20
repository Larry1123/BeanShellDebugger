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
import net.larry1123.util.CanaryUtil;
import net.larry1123.util.api.plugin.UtilPlugin;
import net.larry1123.util.api.plugin.commands.Command;
import org.slf4j.Logger;
import org.slf4j.MarkerFactory;

import java.io.File;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.file.Path;

public class BeanShell extends UtilPlugin {

    public Interpreter bsh;
    public Logger log;

    protected BshCommand bshCommand;
    protected BeanShellConfig config;

    @Override
    public boolean enable() {
        config = new BeanShellConfig(this);
        log = getLogger();
        log.info("Starting BeanShell");
        PrintStream out = new PrintStream(new LoggerOutputStream(this, false), true);
        PrintStream err = new PrintStream(new LoggerOutputStream(this, true), true);
        bsh = new Interpreter(new StringReader(""), out, err, false, null);
        try {
            // Set up debug environment with globals
            bsh.set("server", Canary.getServer());
            bsh.set("bans", Canary.bans());
            bsh.set("channels", Canary.channels());
            bsh.set("commands", Canary.commands());
            bsh.set("userAndGroup", Canary.usersAndGroups());
            bsh.set("warps", Canary.warps());
            bsh.set("kits", Canary.kits());
            bsh.set("whiteList", Canary.whitelist());
            bsh.set("ops", Canary.ops());
            bsh.set("reserveList", Canary.reservelist());
            bsh.set("hooks", Canary.hooks());
            bsh.set("db", Canary.db());
            bsh.set("pluginManager", Canary.manager());
            bsh.set("permissions", Canary.permissionManager());
            bsh.set("help", Canary.help());
            bsh.set("factory", Canary.factory());
            bsh.set("scoreboards", Canary.scoreboards());
            bsh.set("motd", Canary.motd());
            bsh.set("playerSelector", Canary.playerSelector());
            bsh.set("logger", getLogger());
            bsh.set("classLoader", getClass().getClassLoader());

            // Create an alias for each plugin name using its class name
            for (Plugin p : Canary.manager().getPlugins()) {
                log.info("Regisering object " + p.getName());
                bsh.set(p.getName(), p);
            }
            // Source any .bsh files in the plugin directory
            Path sourcesPath = getPluginDataFolder().toPath().resolve("scripts");
            Files.createParentDirs(sourcesPath.toFile());
            for (File file : Files.fileTreeTraverser().children(sourcesPath.toFile())) {
                Path path = file.toPath();
                String filename = path.getFileName().toString();
                if (Files.getFileExtension(filename).equals(".bsh")) {
                    log.info("Sourcing file " + Files.getNameWithoutExtension(filename));
                    bsh.source(path.toString());
                }
                else {
                    log.info("*** skipping " + path.toAbsolutePath().toString());
                }
            }
            bsh.eval("setAccessibility(true)"); // turn off access restrictions
            if (config.getServerPort() != 0) {
                bsh.eval("server(" + config.getServerPort() +  ")");
                log.info("BeanShell web console at http://localhost:" + config.getServerPort());
                log.info("BeanShell telnet console at localhost:" + (config.getServerPort() + 1));
            }
            // Register the bsh command
            bshCommand = new BshCommand(bsh);
            if (!regCommand(bshCommand)) {
                String message = "Unable to register Command!";
                log.error(message);
                enableFailed(message);
                return false;
            }
        }
        catch (Exception e) {
            log.error("Error in BeanShell. ", e);
            enableFailed(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public void disable() {
        // TODO likely something to do here
    }

    protected boolean regCommand(Command command) {
        try {
            CanaryUtil.commands().registerCommand(command, this);
            command.setLoaded(true);
            return true;
        }
        catch (CommandDependencyException e) {
            getLogger().error(MarkerFactory.getMarker("Commands"), "Failed to add command: " + command.getCommandData().getAliases()[0], e);
            command.setLoaded(false);
            return false;
        }
    }

}
