package at.fhv.matchpoint.partnerservice.infrastructure;

import at.fhv.matchpoint.partnerservice.domain.readmodel.PartnerRequestReadModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PartnerRequestReadModelRepository implements PanacheRepositoryBase<PartnerRequestReadModel, String> {

}
