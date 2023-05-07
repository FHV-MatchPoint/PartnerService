package at.fhv.matchpoint.partnerservice.infrastructure;

import java.util.Optional;

import at.fhv.matchpoint.partnerservice.domain.model.Member;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MemberRepository implements PanacheRepositoryBase<Member, String> {

    public Optional<Member> verify(String memberId) {
        Optional<Member> optMember = findByIdOptional(memberId);

        if(optMember.isPresent() && optMember.get().isLocked.equals(false)){
            return optMember;
        }        
        return Optional.empty();
    }

    public Optional<Member> verify(String memberId, String clubId) {
        Optional<Member> optMember = findByIdOptional(memberId);

        if(optMember.isPresent() && optMember.get().isLocked.equals(false) && optMember.get().clubId.equals(clubId)){
            return optMember;
        }        
        return Optional.empty();
    }
    
}
