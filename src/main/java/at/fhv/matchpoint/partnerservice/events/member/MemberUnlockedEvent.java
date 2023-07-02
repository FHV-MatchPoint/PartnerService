package at.fhv.matchpoint.partnerservice.events.member;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MessageAlreadyProcessedException;
import jakarta.persistence.Entity;

@JsonTypeName("MemberUnlockedEvent")
@Entity
public class MemberUnlockedEvent extends MemberEvent{
    @Override
    public void accept(MemberVisitor v) throws MemberNotFoundException, MessageAlreadyProcessedException {
        v.visit(this);
    }
}
