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
import net.canarymod.Translator;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.CommandBlock;
import net.canarymod.chat.MessageReceiver;
import net.larry1123.util.api.plugin.commands.Command;
import net.larry1123.util.api.plugin.commands.CommandData;
import net.visualillusionsent.utils.LocaleHelper;
import net.visualillusionsent.utils.PropertiesFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class BshdCommand implements Command {

    private final CommandData command = new CommandData(new String[] {"bshd"}, new String[] {"beanshelldebugger.shell"}, "TODO", "TODO");
    private final LocaleHelper translator = Translator.getInstance();
    private boolean loaded = false;

    private BeanShellDebugger plugin;
    private Interpreter bsh;


    public BshdCommand(BeanShellDebugger plugin, Interpreter bsh) {
        this.plugin = plugin;
        this.bsh = bsh;
    }

    public boolean isAllowedPlayer(MessageReceiver sender) {
        if (sender instanceof Server || sender instanceof CommandBlock) {
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PropertiesFile config = plugin.getModuleConfig("AllowedPlayers");
            return config.getBoolean(player.getUUIDString(), false) || player.isAdmin();
        }
        return false;
    }

    @Override
    public CommandData getCommandData() {
        return command;
    }

    @Override
    public LocaleHelper getTranslator() {
        return translator;
    }

    @Override
    public boolean isForced() {
        return false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void setLoaded(boolean loadedness) {
        loaded = loadedness;
    }

    @Override
    public void execute(MessageReceiver caller, String[] parameters) {
        if (!isAllowedPlayer(caller)) {
            caller.notice("You are not allowed to use this command.");
        }
        // build the code string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i]);
            if (i + 1 < parameters.length) {
                sb.append(" ");
            }
        }
        String statements = sb.toString();
        try {
            //				Socket s = new Socket("localhost", 1338);
            //				s.setSoTimeout(10);
            //				BufferedReader in = new BufferedReader
            //				  (new InputStreamReader(s.getInputStream()));
            //				PrintWriter out = new PrintWriter
            //				  (s.getOutputStream(), true /* autoFlush */);
            //
            //				out.println(sb.toString());
            //
            //				boolean more = true;
            //				int ln = 0;
            //				try {
            //					while (more) {
            //					    String line = in.readLine();
            //					    if (line == null)
            //					    	more = false;
            //					    else if (ln == 0) {
            //					    	ln++;
            //						} else if (ln == 1) {
            //					    	sender.sendMessage(line.substring(6));
            //					    	ln++;
            //					    } else {
            //					    	sender.sendMessage(line);
            //					    }
            //					 }
            //				} catch (SocketTimeoutException e) {
            //
            //				}
            //
            //				s.close();
            //				in.close();
            //				out.close();
            Object evalResult = bsh.eval(statements);
            caller.message("\u00a79Result: \u00a7f" + (evalResult == null ? "null" : evalResult));
        }
        catch (Throwable thr) {
            StringWriter writer = new StringWriter();
            thr.printStackTrace(new PrintWriter(writer, true));
            caller.notice("\u00a7cError: \u00a7f" + writer.toString());
        }
    }

    @Override
    public List<String> tabComplete(MessageReceiver messageReceiver, String[] args) {
        // TODO
        return null;
    }
}
