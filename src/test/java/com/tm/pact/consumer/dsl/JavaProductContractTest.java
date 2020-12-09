package com.tm.pact.consumer.dsl;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.RequestResponsePact;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static au.com.dius.pact.consumer.PactVerificationResult.Ok;
import static org.junit.Assert.assertTrue;

public class JavaProductContractTest {
    @Test
    public void testPact() {
        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("Some Consumer")
                .hasPactWith("Some Provider")
                .uponReceiving("a request to get products")
                .path("/products")
                .method("POST")
                .body(new PactDslJsonBody()
                        .uuid("productId", "25b30263-2c2f-4891-a13d-3570abcad538")
                        .object("dimensions")
                            .decimalType("width", 10.05)
                            .decimalType("length", 10.05)
                            .decimalType("height", 10.05)
                        .closeObject()
                        .eachLike("alternatives")
                                .uuid("productId", "79d50b92-320d-49b2-8eff-e8c2ce680430")
                                .integerType("count", 1)
                                .closeObject()
                        .closeArray()
                )
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
