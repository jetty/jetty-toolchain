# Jetty SetUID Component

## Configuration

Add the `setuid` start module to your jetty.base

```
$ cd /path/to/my-jetty-base
$ java -jar /opt/jetty-home/start.jar --add-module=setuid
```

Then configure the userid you want in the `${jetty.base}/start.d/setuid.ini` file



