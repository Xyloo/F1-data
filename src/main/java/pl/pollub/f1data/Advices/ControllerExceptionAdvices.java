package pl.pollub.f1data.Advices;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.InvalidPasswordException;
import pl.pollub.f1data.Exceptions.UserNotFoundException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;
import pl.pollub.f1data.Models.MessageResponse;

/**
 * This class is responsible for handling exceptions thrown by controllers.
 *
 */
@ControllerAdvice
public class ControllerExceptionAdvices {

    /**
     * This method handles EmailExistsException and returns a 409 error with the exception message.
     * @param ex exception
     * @return response entity with HTTP 409 and error message
     */
    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?>  handleEmailExistsException(EmailExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: " + ex.getMessage()));
    }

    /**
     * This method handles InvalidPasswordException and returns a 409 error with the exception message.
     * @param ex exception
     * @return response entity with HTTP 409 and error message
     */
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?>  handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: " + ex.getMessage()));
    }

    /**
     * This method handles UsernameExistsException and returns a 409 error with the exception message.
     * @param ex exception
     * @return response entity with HTTP 409 and error message
     */
    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?>  handleUsernameExistsException(UsernameExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: " + ex.getMessage()));
    }

    /**
     * This method handles UserNotFoundException and returns a 400 error with the exception message.
     * @param ex exception
     * @return response entity with HTTP 400 and error message
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?>  handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: " + ex.getMessage()));
    }

    /**
     * This method handles TransactionSystemException and returns a 400 error with the exception message.
     * The message is extracted from the exception and contains all constraint violations.
     * @param ex exception
     * @return response entity with HTTP 400 and error message
     */
    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable cause = ex.getCause().getCause();
        if (cause instanceof ConstraintViolationException) {
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation<?> violation : ((ConstraintViolationException) cause).getConstraintViolations()) {
                message.append(violation.getMessage()).append('\n');
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + message));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: " + ex.getMessage()));
    }

    /**
     * This method handles DataIntegrityViolationException and returns a 400 error with the exception message.
     * @param ex exception
     * @return response entity with HTTP 400 and error message
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: " + ex.getMessage()));
    }

    /**
     * This method handles AccessDeniedException and returns a 403 error with the exception message.
     * @param ex exception
     * @return response entity with HTTP 403 and error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Error: Access denied."));
    }

    /**
     * This method handles BadCredentialsException and returns a 401 error with the exception message.
     * @param ex exception
     * @return response entity with HTTP 401 and error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Username and/or password are incorrect."));
    }

    /**
     * This method handles MethodArgumentNotValidException and returns a 400 error with the exception message.
     * This exception is thrown when a request body is invalid.
     * @param ex exception
     * @return response entity with HTTP 400 and error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: " + ex.getBindingResult().getFieldError().getDefaultMessage()));
    }

    /**
     * This method handles Exception and returns a 400 error with the exception message.
     * This is a fallback method.
     * @param ex exception
     * @return response entity with HTTP 400 and error message
     */

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: " + ex.getMessage()));
    }

}
