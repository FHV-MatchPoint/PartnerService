package at.fhv.matchpoint.partnerservice.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;

import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import at.fhv.matchpoint.partnerservice.events.request.PartnerRequestEvent;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PartnerRequestReadModelRepository implements PanacheRepositoryBase<PartnerRequestReadModel, String> {

    public List<PartnerRequestReadModel> getOpenPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to) {
        return find("SELECT p from PartnerRequestReadModel p where ownerId != ?1 and clubId = ?2 and startTime >= ?3 and endTime <= ?4 and status = OPEN", memberId, clubId, from, to).list();
    }

    public List<PartnerRequestReadModel> getPartnerRequestsByMemberId(String memberId) {
        return find("SELECT p from PartnerRequestReadModel p where ownerId = ?", memberId).list();
    }

    public void handleEvent(PartnerRequestEvent event) {
    }

}
