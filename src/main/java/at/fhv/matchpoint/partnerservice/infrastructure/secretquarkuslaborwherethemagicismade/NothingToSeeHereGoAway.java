package at.fhv.matchpoint.partnerservice.infrastructure.secretquarkuslaborwherethemagicismade;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@DefaultBean
public class NothingToSeeHereGoAway implements DontLookAtThis {

    @Override
    public Boolean iSaidIgnoreThisAndStopLookingAtThisFunctionItIsJustSomeQuarkusMagic() {
        return false;
    }
    
}
