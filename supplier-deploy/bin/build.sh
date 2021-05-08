#!/usr/bin/env bash
cd ../../
mvn clean install
cd x-ent-deploy
mvn clean package

## start program command
## java -jar huace_mvideo_portalapi.jar --spring.profiles.active=prod (dev,test)
