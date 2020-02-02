package vs.restmongo.band.exceptions;

public class BandNotFoundException extends RuntimeException {
    public BandNotFoundException(String message) {
        super(message);
    }
}
