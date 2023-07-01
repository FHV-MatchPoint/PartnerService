package at.fhv.matchpoint.partnerservice.events.member;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;

@JsonTypeName("MemberLockedEvent")
public class MemberLockedEvent extends MemberEvent{
    @Override
    public void accept(MemberVisitor v) throws MemberNotFoundException {
        v.visit(this);
    }
}
