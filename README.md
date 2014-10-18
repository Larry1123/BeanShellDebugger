BeanShellDebugger
==========

This is a Canary port of this plugin :)

BeanShell 2.05b is included in this plugin.

All changes are licenced Under Apache v2 License.
All distributions are licenced under GNU GENERAL PUBLIC LICENSE: Version 3.

This readme will be updated in due time.

Old Readme
==========

This fork removes the BeanShell servers and requires the permission beanshelldebugger.shell in order to use the bshd command.
Additionally, you shouldn't need to put the BeanShell jar in your root bukkit directory.
Below is the untouched README from the original repository; some parts do not apply to this fork.


OVERVIEW:
BeanShellDebugger gives you complete access to the internal state of
a live bukkit server using BeanShell (http://www.beanshell.org/). 
BSHD is a great tool for debugging plugins and bukkit core.

BeanShell is extremely powerful. Read its docs to learn its subtle
ways.

INSTALLATION:
1. Download BeanShell 2.0b4 from http://www.beanshell.org/download.html
2. Put bsh-2.0b4.jar into your root bukkit directory
3. Put BeanShellDebugger.jar into your bukkit plugin directory

USAGE:
Connect to BeanShell one of four ways
1. Web - http://localhost:1337
2. Telnet - telnet localhost 1338
3. Server console - bshd java command;
4. MC Client - /bshd java command;

A few shortcut symbols have been defined within the BeanShell runtime:
pluginLoader, pluginManager, classLoader, and server. Additionally,
a symbol is defined for each loaded plugin instance and named the
class name of the plugin.


Most useful command: print(someJavaExpression());

EXTENSION:
Any .bsh scripts added to the BeanShellDebugger plugin data folder
(in the plugins directory) will be loaded into the BeanShell
environment and can be used in your queries. This is a great place
to write snippets that automate your common commands.