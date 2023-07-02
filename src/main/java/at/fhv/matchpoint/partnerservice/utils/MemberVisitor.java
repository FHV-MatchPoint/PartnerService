package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.events.member.MemberAddedEvent;
import at.fhv.matchpoint.partnerservice.events.member.MemberLockedEvent;
import at.fhv.matchpoint.partnerservice.events.member.MemberUnlockedEvent;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MemberNotFoundException;
import at.fhv.matchpoint.partnerservice.utils.exceptions.MessageAlreadyProcessedException;

public interface MemberVisitor {

    void visit(MemberLockedEvent event) throws MemberNotFoundException, MessageAlreadyProcessedException;
    void visit(MemberUnlockedEvent event) throws MemberNotFoundException, MessageAlreadyProcessedException;
    void visit(MemberAddedEvent event);

}
