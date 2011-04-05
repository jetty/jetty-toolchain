
This setup is akin to the Codehaus Mojo project.

The modules contained in this toolchain project change very infrequently 
and at different rates.  Instead of re-releasing unchanged modules over
and over to release a module in this project do the following:

> cd jetty-test-helper    (cd into the project you want to release)
> mvn -Dusername=<user> release:prepare
> mvn release:perform

Then go to oss.sonatype.org/nexus to close and release the project.

Should there be changes to the deployment locations or metadata you
want to change deploy a new parent from the jetty-toolchain project
and update the module you want to release pom.xml parent declaration.
You don't need to update all of them and release when that happens, 
it will sort itself out when they need released.

Note, the tlp provided in this directory is purely to build everything 
if that is what you want to do, you can't release from the top level
as that would mess up the per artifact tags and besides, the version
on the tlp is amusingly direct that you don't want to do that.

questions, ask jesse
