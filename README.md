# Log4jPatcher

A Java Agent based mitigation for Log4j2 JNDI exploits.

This agent employs 2 patches:  
- Disabling all Lookup conversions (on supported Log4j versions) 
  in `org.apache.logging.log4j.core.pattern.MessagePatternConverter` by setting `noLookups` to true in the constructor.
- Disabling the `org.apache.logging.log4j.core.lookup.JndiLookup` class by just returning `null`
  in its `lookup` function.


### To use
Add `-javaagent:Log4jPatcher.jar` as a JVM argument.
