## An automatic screen capture application.

**It's main idea is to take screenshots whenever something changes on the screen.**

**Focus on the task, not bothered by screenshots recording in the background.**

**Record test cases, documentation, or whatever you want.**

### Features

* easily take, remove, and reorder screenshots
* organize in sessions
* annotate with labels and descriptions
* edit with your image editor
* automatically mark mouse clicks
* find by keywords
* use best compression format per screenshot (JPG, PNG)
* no screenshot on mouse movement (mouse movement is not considered a change)
* create HTML presentation

Published under GPL open-source license. Copyright (C) Marcin Nikliborc.

==================

Clicktrace depends on JNativeHook, which at the moment is not published on any public Maven repo. So just download the library in version
1.1.4 from here:
https://code.google.com/p/jnativehook/downloads/list

and execute:
mvn install:install-file -Dfile=lib/JNativeHook.jar -DgroupId=jnativehook -DartifactId=jnativehook -Dversion=1.1.4 -Dpackaging=jar
