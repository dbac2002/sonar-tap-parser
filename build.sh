#! /bin/bash

echo "############################"
echo "Building tap parser artifact" 
mvn clean compile test assembly:single

echo "############################"
echo "Copy artifact to analysers lib dir"
cp target/sonar-tap-parser-*-jar-with-dependencies.jar ../sonar-analyser/lib/sonar-tap-parser.jar
