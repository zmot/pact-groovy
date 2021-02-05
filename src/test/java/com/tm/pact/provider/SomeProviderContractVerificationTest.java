package com.tm.pact.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;


@Provider("Some Provider")
@Consumer("Some Consumer")
@PactBroker(host = "localhost", port = "80",
        enablePendingPacts="true", providerTags = "MAIN")
public class SomeProviderContractVerificationTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp(PactVerificationContext context) {
        System.setProperty("pact.provider.tag", "MAIN");
        System.setProperty("pact.provider.version", "5.2.0");
        System.setProperty("pact.verifier.publishResults", "true");

        wireMockServer = new WireMockServer(8081);
        configureFor(8081);
        wireMockServer.start();
        stubFor(post(urlMatching("/hello"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withBody("{\n" +
                                        "  \"greeting\": \"Hello Tom!\"\n" +
                                        "}")
                                .withHeader("Content-Type", "application/json")
                )
        );

        context.setTarget(new HttpTestTarget("localhost", 8081));

    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
