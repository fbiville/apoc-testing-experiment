package com.neo4j.apoc.testing;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ExampleTest {

    @Container
    private static final Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:4.0")
            .withAdminPassword(null)
            .withPlugins(MountableFile.forClasspathResource(String.format("/apoc-%s-all.jar", apocVersion())));

    @Test
    void runs_apoc() {
        try (Session session = GraphDatabase.driver(neo4jContainer.getBoltUrl()).session()) {
            Integer result = session.readTransaction(tx ->
                    tx.run("RETURN apoc.coll.max([1,2,3,4,5]) AS output;").single().get("output").asInt());

            assertThat(result).isEqualTo(5);
        }
    }

    private static String apocVersion() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ExampleTest.class.getResourceAsStream("/apoc.version")))) {
            return reader.readLine().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
