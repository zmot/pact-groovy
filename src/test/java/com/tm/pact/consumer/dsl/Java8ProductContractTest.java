package com.tm.pact.consumer.dsl;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.RequestResponsePact;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static au.com.dius.pact.consumer.PactVerificationResult.Ok;
import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.Assert.assertTrue;

public class Java8ProductContractTest {
    @Test
    public void testPact() {
        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("Some Consumer")
                .hasPactWith("Some Provider")
                .uponReceiving("a request to get products")
                .path("/products")
                .method("POST")
                .body(newJsonBody(
                        root -> {
                            root.uuid("productId", UUID.fromString("25b30263-2c2f-4891-a13d-3570abcad538"));
                            root.object("dimensions", dimension -> {
                                dimension.decimalType("width", 10.05);
                                dimension.decimalType("length", 10.05);
                                dimension.decimalType("height", 10.05);
                            });
                            root.eachLike("alternatives", alternative -> {
                                alternative.uuid("productId", UUID.fromString("79d50b92-320d-49b2-8eff-e8c2ce680430"));
                                alternative.numberType("count", 1);
                            });
                        })
                        .build())
                .willRespondWith()
                .status(200)
                .body("{}")
                .toPact();


        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult result = runConsumerTest(pact, config, (mockServer, context) -> {
            RestAssured.port = mockServer.getPort();
            String json = new String(this.getClass().getResourceAsStream("/dsl/request.json").readAllBytes());

            String x = RestAssured.with()
                    .header(new Header("Content-Type", "application/json"))
                    .body(json)
                    .post(mockServer.getUrl() + "/products").asString();
            return null;
        });

        assertTrue(result instanceof Ok);
    }
}
