package at.fhv.matchpoint.partnerservice.application.dto;

public class MemberDTO {

    private String memberId;
    private String fistName;
    private String lastName;

    private MemberDTO(String memberId, String fistName, String lastName) {
        this.memberId = memberId;
        this.fistName = fistName;
        this.lastName = lastName;
    }

    public static MemberDTO buildDTO(String memberId){
        return new MemberDTO(memberId, "NOYET", "IMPLEMENTED");
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    

    

}
