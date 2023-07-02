package at.fhv.matchpoint.partnerservice.utils.exceptions;

public class DateTimeFormatException extends ResponseException {

    public DateTimeFormatException(){
        super(422, "Date has to be in the Format: dd-MM-yyyy\n" +
                "Time has to be in the Format: HH:mm");
    }
}
