package at.fhv.matchpoint.partnerservice.infrastructure.secretquarkuslaborwherethemagicismade;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@IfBuildProfile("test")
public class AlexAndJustinIgnoreThis implements DontLookAtThis {

    public Boolean iSaidIgnoreThisAndStopLookingAtThisFunctionItIsJustSomeQuarkusMagic(){
        return true;
    }
    
}
