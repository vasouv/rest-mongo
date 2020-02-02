package vs.restmongo.band.exceptions;

import javax.validation.constraints.NotBlank;

public class BandExistsException extends RuntimeException {

    public BandExistsException(@NotBlank String message) {
        super(message);
    }
}
