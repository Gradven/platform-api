#!/usr/bin/env bash
cd ../src/main/resources/config/
rm application.yml
echo "spring:" >> application.yml
echo "  profiles:" >> application.yml
echo "    active: dev" >> application.yml

cd ../../../../../
mvn clean install
cd portal-deploy
mvn clean package

