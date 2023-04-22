package at.fhv.matchpoint.partnerservice.application;

public interface LockPartnerRequestService {

    void lockPartnerRequestByMemberId(String memberId) throws Exception;
    void lockPartnerRequestByClubId(String clubId) throws Exception;

}
