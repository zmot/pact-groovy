package com.tm.pact;

import au.com.dius.pact.provider.junitsupport.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@Provider("SomeProvider")
@ExtendWith(MockitoExtension.class)
public class SomeProviderContractVerificationTest {
    @Mock
    private SomeExternalDependency someExternalDependency;

    @BeforeEach
    void setUpExternalDependencies() {
        when(someExternalDependency.getPersonalizedGreeting(anyString())).thenReturn("Hello Xyz!");
    }
}
