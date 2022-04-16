Wildcat Conceptual Architectural Template (WildCAT)
=======================
This is the code portion of my senior project.

## Dependencies
### JDK 11
This project was tested against Java 11. It may work on older or newer versions of Java, but I can't guarantee it.

### SBT or mill
This project uses SBT, the most common build tool for Scala. You can download it [here](https://www.scala-sbt.org/download.html).

## Running WildCAT
Once the dependencies are downloaded, it should be as simple as running the tests with sbt:
```sh
sbt test
```

All tests should greenlight. Here's the output from the last test run I did before committing this README:
```
[info] Run completed in 11 seconds, 585 milliseconds.
[info] Total number of tests run: 13
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 13, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 13 s, completed Apr 16, 2022, 4:32:15 PM
```