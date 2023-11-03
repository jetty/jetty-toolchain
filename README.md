# This repository is scheduled to be ARCHIVED.

### Currently Migrated
* jetty-setuid -> [/jetty/jetty.setuid](https://github.com/jetty/jetty.setuid)
* jetty-schemas -> [/jetty/jetty.schemas](https://github.com/jetty/jetty.schemas)
* jetty-xhtml-schemas -> [/jetty/jetty.schemas.xhtml](https://github.com/jetty/jetty.schemas.xhtml)
* jetty-test-helper -> [/jetty/jetty.test.helper](https://github.com/jetty/jetty.test.helper)
* jetty-test-policy -> [/jetty/jetty.test.helper](https://github.com/jetty/jetty.test.policy)
* jetty-build-support -> [/jetty/jetty.test.helper](https://github.com/jetty/jetty.build.support)
* jetty-javax-websocket-api -> [/jetty/jetty.websocket.api/tree/javax-websocket-api](https://github.com/jetty/jetty.websocket.api/tree/javax-websocket-api)
* jetty-jakarta-websocket-api -> [/jetty/jetty.websocket.api](https://github.com/jetty/jetty.websocket.api)
* jetty-javax-servlet-api -> [/jetty/jetty.servlet.api/tree/javax-servlet-api](https://github.com/jetty/jetty.servlet.api/tree/javax-servlet-api)
* jetty-jakarta-servlet-api -> [/jetty/jetty.servlet.api](https://github.com/jetty/jetty.servlet.api)



Build Toolchain for the Eclipse Jetty Project build
===================================================

The modules contained in this toolchain project change very infrequently
and at different rates.  Instead of re-releasing unchanged modules over
and over to release a module in this project do the following:

    cd jetty-test-helper    (cd into the project you want to release)
    mvn -Peclipse-release -Dusername=<user> release:prepare
    mvn release:perform

Then go to https://oss.sonatype.org to close and release the project.

Should there be changes to the deployment locations or metadata you
want to change deploy a new parent from the jetty.toolchain project
and update the module you want to release pom.xml parent declaration.
You don't need to update all of them and release when that happens,
it will sort itself out when they need released.

Note, the tlp provided in this directory is purely to build everything
if that is what you want to do, you can't release from the top level
as that would mess up the per artifact tags and besides, the version
on the tlp is amusingly direct that you don't want to do that.

questions, ask jmcconnell in the #jetty channel on irc.freenode.net
https://webchat.freenode.net/

