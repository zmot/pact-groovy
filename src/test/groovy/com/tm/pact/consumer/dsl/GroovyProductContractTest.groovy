package com.tm.pact.consumer.dsl


import au.com.dius.pact.consumer.groovy.PactBuilder
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import org.junit.jupiter.api.Test

class GroovyProductContractTest {
    @Test
    void "Products pact between Some Consumer and Some Provider"() {

        new PactBuilder()
                .with {
                    serviceConsumer "Some Consumer"
                    hasPactWith "Some Provider"
                    port 8080

                    uponReceiving('a get products request')
                    withAttributes(method: 'post', path: '/products')
                    withBody {
                        productId uuid('25b30263-2c2f-4891-a13d-3570abcad538')
                        dimensions {
                            width decimal(10.05)
                            length decimal(50.21)
                            height decimal(2.50)
                        }
                        alternatives eachLike({
                            productId string('79d50b92-320d-49b2-8eff-e8c2ce680430')
                            count integer(1)
                        })
                    }
                    willRespondWith(
                            status: 200,
                            headers: ['Content-Type': 'application/json'],
                    )
                    withBody{
                        []
                    }

                    runTestAndVerify {
                        def json = """
{
  "productId": "25b30263-2c2f-4891-a13d-3570abcad538",
  "dimensions": {
    "width": 10.05,
    "length": 50.21,
    "height": 2.50
  },
  "alternatives": [
    {
      "productId": "79d50b92-320d-49b2-8eff-e8c2ce680430",
      "count": 1
    },
    {
      "productId": "43a34303-eb0b-4c4f-8da5-a4a82b2d3bb3",
      "count": 2
    }
  ]
}
                        """
                        def body = new JsonSlurper().parseText(json);
                        def client = new RESTClient('http://localhost:8080/')
                        def response = client.post(
                                path: '/products',
                                body: body,
                                requestContentType: 'application/json'
                        )
                        assert response.status == 200
                        assert response.contentType == 'application/json'
                    }
                }
    }
}
