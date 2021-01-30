package com.tm.pact.provider;

import au.com.dius.pact.provider.junitsupport.Provider;
import com.tm.pact.SomeExternalDependency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@Provider("Some Provider")
@ExtendWith(MockitoExtension.class)
public class SomeProviderContractVerificationMockingTest {
    @Mock
    private SomeExternalDependency someExternalDependency;

    @BeforeEach
    void setUpExternalDependencies() {
        when(someExternalDependency.getPersonalizedGreeting("Tom")).thenReturn("Hello Tom!");
    }
}
