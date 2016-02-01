# sonar-tap-parser

### What is this repository for? ###
* Parsing .tap files into the generic xml file from http://docs.sonarqube.org/display/PLUG/Generic+Test+Coverage

### How to execute ###
* the jar (with dependencies) can be built via **mvn clean assembly:single**
* the jar can be executed with the following parameters *-o output xml file* and *-i tap input file*

### Format ###
The parser recognizes the following markers

    (not) ok [TEXT] - NAME
    # marks a new test with NAME the name and ok/not ok determines successful or failed test

    message: >
    MESSAGE_LINE
     # marks a message for a failed test. the next line is read as the actual message

    stack: >
     MULTI_LINE_OF_STACK
      # marks the stacktrace of a failed test. All lines until 'Log' appears are read as stack trace

    SPEC: FILE_PATH
    # marks the source file, containing the test
  
  A full example looks like

    not ok 51 PhantomJS 1.9 - SOME_NAME
      message: >
       expected true to be false
      stack: >
       AssertionError: expected true to be false
         at http://localhost:7357/static/desktop/test/lib/chai/chai.js:210
         at http://localhost:7357/static/desktop/test/lib/chai/chai.js:593
         at addProperty (http://localhost:7357/static/desktop/test/lib/chai/chai.js:4117)
         at doAsserterAsyncAndAddThen (http://localhost:7357/static/desktop/test/lib/chai/chai-as-promised.js:298)
         at http://localhost:7357/static/desktop/test/lib/chai/chai-as-promised.js:288
         at http://localhost:7357/static/desktop/test/lib/chai/chai.js:5273
      Log: |
       WARNING: Tried to load angular more than once.
       SPEC: PATH_TO_SOURCE_FILE
      ...
    ok 51 PhantomJS 1.9 - SOME_NAME_2
     ---
      Log: |
      WARNING: Tried to load angular more than once.
      SPEC: SUCCESS
      ...

**currently no duration is read from the tap file, thus all durations in the resulting xml is set to 0**
