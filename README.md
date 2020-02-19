# 3pc Gralde dreipc.java.common.plugins.Plugins 
Author: Nadav Babai @ 3pc GmbH

A test project to support the 3pc gradle plugins

 
More information is available on the gradle [website](https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_markers)


### Introduction to Gradle dreipc.java.common.plugins.Plugins main idea
In order to save time and redundant code, the main idea is to use what so called: binary dreipc.java.common.plugins.plugin. 
Binary dreipc.java.common.plugins.Plugins must implement the PlugIn Interface and they are located on a custom repository. 

The main idea is to publish the plugins as a maven group. Eventually the user can decide which version and which plugins to import. 

```
plugins {
   id 'dreipc.java.common.plugins:dreipc.java.common.plugins.docker:1.0'   
}  
```
![Example From The Website (https://docs.gradle.org/current/userguide/img/pluginMarkers.png)](https://docs.gradle.org/current/userguide/img/pluginMarkers.png)

Another example to implementation: 
https://github.com/TechPrimers/gradle-custom-dreipc.java.common.plugins.plugin-example

Example to build custom dreipc.java.common.plugins.plugin 
https://github.com/jonathanhood/gradle-dreipc.java.common.plugins.plugin-example