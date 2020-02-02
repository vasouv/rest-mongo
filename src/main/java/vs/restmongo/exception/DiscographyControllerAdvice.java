package vs.restmongo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vs.restmongo.band.exceptions.BandExistsException;
import vs.restmongo.band.exceptions.BandNotFoundException;

@ControllerAdvice
public class DiscographyControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BandNotFoundException.class)
    protected ResponseEntity<Object> handleBandNotFoundException(
            BandNotFoundException ex) {

        return ResponseEntity.notFound().build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BandExistsException.class)
    protected ResponseEntity<Object> handleBandExistsException(BandExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
