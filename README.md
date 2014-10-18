BeanShell
==========

[![Build Status](https://ci.larry1123.net/job/Canary-BeanShell/badge/icon)](https://ci.larry1123.net/job/Canary-BeanShell/)  
[Latest Build](https://ci.larry1123.net/job/Canary-BeanShell/lastBuild/)  
[Latest Successful Build](https://ci.larry1123.net/job/Canary-BeanShell/lastSuccessfulBuild/)

BeanShell gives you complete access to the internal state of a live canary server using [BeanShell](http://www.beanshell.org/).
BSH is a great tool for debugging plugins and canary core.
BeanShell is extremely powerful. Read its docs to learn its subtle ways.

This is a Canary [port](https://github.com/orthoplex64/BeanShellDebugger). :)

BeanShell 2.05b is included in this plugin.

Usage
-------------

You can use this by simply using the /bsh command like a shell.
The command requires the permission "bsh.shell".

Some shortcut symbols have been added listed below.
Additionally, a symbol is defined for each loaded plugin instance and named the class name of the plugin.

        server -> Canary.getServer()
        bans -> Canary.bans()
        channels -> Canary.channels()
        commands -> Canary.commands()
        userAndGroup -> Canary.usersAndGroups()
        warps -> Canary.warps()
        kits -> Canary.kits()
        whiteList -> Canary.whitelist()
        ops -> Canary.ops()
        reserveList -> Canary.reservelist()
        hooks -> Canary.hooks()
        db -> Canary.db()
        pluginManager -> Canary.manager()
        permissions -> Canary.permissionManager()
        help -> Canary.help()
        factory -> Canary.factory()
        scoreboards -> Canary.scoreboards()
        motd -> Canary.motd()
        playerSelector -> Canary.playerSelector()
        logger -> getLogger()
        classLoader -> getClass().getClassLoader()


Extensions
-------------
Any .bsh scripts added to the BeanShell plugin data folder will be loaded into the BeanShell environment.
All loaded scripts can be used in your queries.
This is a great place to write snippets that automate your common commands.

Some sample scrips are in the scripts directory.

License Info
-------------
Changes are licenced Under Apache v2 License.
All distributions are licenced under GNU GENERAL PUBLIC LICENSE: Version 3.
