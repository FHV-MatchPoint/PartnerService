package at.fhv.matchpoint.partnerservice.infrastructure;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemberRepository implements PanacheRepositoryBase<Member, String> {
    
}
