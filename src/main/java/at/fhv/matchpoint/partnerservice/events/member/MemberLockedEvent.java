package at.fhv.matchpoint.partnerservice.events.member;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MessageAlreadyProcessedException;
import jakarta.persistence.Entity;

@JsonTypeName("MemberLockedEvent")
@Entity
public class MemberLockedEvent extends MemberEvent{
    @Override
    public void accept(MemberVisitor v) throws MemberNotFoundException, MessageAlreadyProcessedException {
        v.visit(this);
    }
}
