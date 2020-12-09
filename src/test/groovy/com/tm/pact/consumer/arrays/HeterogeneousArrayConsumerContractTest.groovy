package com.tm.pact.consumer.arrays


import au.com.dius.pact.consumer.groovy.PactBuilder
import groovyx.net.http.RESTClient
import org.junit.jupiter.api.Test

class HeterogeneousArrayConsumerContractTest {
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
                        items ([
                                {
                                    id integer(1)
                                    type equalTo('REGULAR')
                                    price decimal(2.34)
                                },
                                {
                                    id integer(2)
                                    type equalTo('DISCOUNTED')
                                    discountedPrice decimal(5.67)
                                }
                        ])
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
