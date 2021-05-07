#!/usr/bin/env bash

set -x

version=$(mvn -q -pl test-containers resources:testResources && cat test-containers/target/test-classes/apoc.version)

cd $(mktemp -d)
wget https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/"${version}"/apoc-"${version}"-all.jar
mvn install:install-file \
  -DgroupId=org.neo4j.procedure \
  -DartifactId=apoc \
  -Dversion="${version}" \
  -Dpackaging=jar \
  -Dclassifier=all \
  -Dfile=apoc-"${version}"-all.jar