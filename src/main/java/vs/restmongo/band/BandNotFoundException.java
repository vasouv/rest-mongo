package vs.restmongo.band;

public class BandNotFoundException extends RuntimeException {
    public BandNotFoundException(String message) {
        super(message);
    }
}
