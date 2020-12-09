package com.tm.pact.consumer.arrays


import au.com.dius.pact.consumer.groovy.PactBuilder
import groovyx.net.http.RESTClient
import org.junit.jupiter.api.Test

class HomogeneousArrayConsumerContractTest {
    @Test
    void "Some pact between Some Consumer and Some Provider"() {

        new PactBuilder()
                .with {
                    serviceConsumer "Some Consumer"
                    hasPactWith "Some Provider"
                    port 8080

                    uponReceiving('a get items request')
                    withAttributes(method: 'get', path: '/items')
                    willRespondWith(
                            status: 200,
                            headers: ['Content-Type': 'application/json'],
                    )
                    withBody {
                        items eachLike {
                            id integer(1)
                            type regexp('REGULAR|DISCOUNTED', 'REGULAR')
                            price decimal(2.34)
                            discountedPrice decimal(5.67)
                        }
                    }

                    runTestAndVerify {
                        def client = new RESTClient('http://localhost:8080/')
                        def response = client.get(
                                path: '/items',
                                requestContentType: 'application/json'
                        )
                        assert response.status == 200
                        assert response.contentType == 'application/json'
                        println response.data
//                        assert response.data == ['greeting': 'Hello Tom!']
                    }
                }
    }
}
