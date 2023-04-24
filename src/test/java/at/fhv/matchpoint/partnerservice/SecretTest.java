package at.fhv.matchpoint.partnerservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.fhv.matchpoint.partnerservice.infrastructure.secretquarkuslaborwherethemagicismade.DontLookAtThis;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class SecretTest {

    @Inject DontLookAtThis service;

    @Test
    public void stoplockingatcodeyournotsupposedto(){
        assertTrue(service.iSaidIgnoreThisAndStopLookingAtThisFunctionItIsJustSomeQuarkusMagic());
    }
    
}
