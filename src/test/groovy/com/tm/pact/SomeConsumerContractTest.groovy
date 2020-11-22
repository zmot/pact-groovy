package com.tm.pact


import au.com.dius.pact.consumer.groovy.PactBuilder
import groovyx.net.http.RESTClient
import org.junit.jupiter.api.Test

class SomeConsumerContractTest {
    @Test
    void "Some pact between SomeConsumer and SomeProvider"() {

        new PactBuilder()
                .with {
                    serviceConsumer "SomeConsumer"
                    hasPactWith "SomeProvider"
                    port 8080

                    uponReceiving('a retrieve hello request')
                    withAttributes(method: 'post', path: '/hello')
                    withBody {
                        name string("Tom")
                    }
                    willRespondWith(
                            status: 200,
                            headers: ['Content-Type': 'application/json'],
                    )
                    withBody {
                        greeting string("Hello Tom!")
                    }

                    runTestAndVerify {
                        def client = new RESTClient('http://localhost:8080/')
                        def response = client.post(
                                path: '/hello',
                                body: [name: 'Tom'],
                                requestContentType: 'application/json'
                        )
                        assert response.status == 200
                        assert response.contentType == 'application/json'
                        assert response.data == ['greeting': 'Hello Tom!']
                    }
                }
    }
}
