# Log4jPatcher

A Java Agent based mitigation for Log4j2 JNDI exploits.

This agent employs 2 patches:  
- Disabling all Lookup conversions (on supported Log4j versions) 
  in `org.apache.logging.log4j.core.pattern.MessagePatternConverter` by setting `noLookups` to true in the constructor.
- Disabling the `org.apache.logging.log4j.core.lookup.JndiLookup` class by just returning `null`
  in its `lookup` function.


### To use
Download the latest release available from GitHub:
https://github.com/CreeperHost/Log4jPatcher/releases

Add `-javaagent:Log4jPatcher.jar` as a JVM argument.

For Minecraft users:
The full path to the jar needs to be added in the above argument unless the jar is put into the instance (or .minecraft) folder.
This jar does not go into the mods folder.

You can find a guide for Minecraft here:
https://www.creeperhost.net/wiki/books/minecraft-java-edition/page/mitigating-cve-2021-44228-in-minecraft
