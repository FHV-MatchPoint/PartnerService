package at.fhv.matchpoint.partnerservice.events;

import at.fhv.matchpoint.partnerservice.utils.MemberVisitor;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;

public class MemberUnlockedEvent extends MemberEvent{
    @Override
    public void accept(MemberVisitor v) throws MemberNotFoundException {
        v.visit(this);
    }
}
