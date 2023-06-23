package at.fhv.matchpoint.partnerservice.events.member;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;

public class MemberAddedEvent extends MemberEvent{

    public String name;
    public String clubId;

    @Override
    public void accept(MemberVisitor v) throws MemberNotFoundException {
        v.visit(this);
    }
}
