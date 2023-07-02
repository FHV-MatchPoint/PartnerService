package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.events.member.MemberAddedEvent;
import at.fhv.matchpoint.partnerservice.events.member.MemberLockedEvent;
import at.fhv.matchpoint.partnerservice.events.member.MemberUnlockedEvent;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;

public interface MemberVisitor {

    void visit(MemberLockedEvent event) throws MemberNotFoundException;
    void visit(MemberUnlockedEvent event) throws MemberNotFoundException;
    void visit(MemberAddedEvent event);

}
