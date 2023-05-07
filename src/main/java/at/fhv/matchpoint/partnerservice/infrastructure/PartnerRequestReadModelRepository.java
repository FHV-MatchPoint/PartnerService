package at.fhv.matchpoint.partnerservice.infrastructure;

import java.time.LocalDate;
import java.util.List;

import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class PartnerRequestReadModelRepository implements PanacheRepositoryBase<PartnerRequestReadModel, String> {

    public List<PartnerRequestReadModel> getOpenPartnerRequests(String memberId, String clubId, LocalDate from, LocalDate to) {
        return null;
    }

    public List<PartnerRequestReadModel> getPartnerRequestsByMemberId(String memberId) {
        return null;
    }

}
