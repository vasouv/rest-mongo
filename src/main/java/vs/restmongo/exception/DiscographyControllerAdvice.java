package vs.restmongo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vs.restmongo.band.BandNotFoundException;

@ControllerAdvice
public class DiscographyControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BandNotFoundException.class)
    protected ResponseEntity<Object> handleBandNotFoundException(
            BandNotFoundException ex) {

        return ResponseEntity.notFound().build();
    }

}
