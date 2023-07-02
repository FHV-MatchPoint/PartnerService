package at.fhv.matchpoint.partnerservice.infrastructure.repository;

import at.fhv.matchpoint.partnerservice.events.request.PartnerRequestEvent;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PartnerRequestEventTrackingRepository implements PanacheRepositoryBase<PartnerRequestEvent, String> {
}
