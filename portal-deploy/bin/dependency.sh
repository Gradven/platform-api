#!/usr/bin/env bash
cd ../../
mvn dependency:tree

## start program command
## java -jar huace_mvideo_portalapi.jar --spring.profiles.active=prod (dev,test)
