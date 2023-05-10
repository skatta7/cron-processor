
# Cron Processor

## Built on
* Java 11.0.18
* SBT 1.8.2 
* Scala 2.13

To run the test cases
```
sbt "testOnly com.cron.CronProcessorSpec"
```

To run the code locally using sbt (in sbt space is a argument separator)
```
update 'runWithSbt' in CronProcessor.scala to 'true' and 
use command like "run */15::0::1,15::*::10::/usr/bin/find"
```

To package a fat jar
```
sbt assembly
```

To run as jar, after fat jar is generated
```
java -jar cron_processor-assembly-0.1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
```