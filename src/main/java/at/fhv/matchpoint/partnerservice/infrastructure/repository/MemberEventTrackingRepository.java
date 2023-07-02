package at.fhv.matchpoint.partnerservice.infrastructure.repository;

import at.fhv.matchpoint.partnerservice.events.member.MemberEvent;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemberEventTrackingRepository implements PanacheRepositoryBase<MemberEvent, String> {
    
}
