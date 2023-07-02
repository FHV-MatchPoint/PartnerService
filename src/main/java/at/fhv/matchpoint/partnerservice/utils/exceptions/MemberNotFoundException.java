package at.fhv.matchpoint.partnerservice.utils.exceptions;

public class MemberNotFoundException extends Exception {

    public MemberNotFoundException() {
        super("Member not found in Stream. Memberservice fucked up.");
    }
}
