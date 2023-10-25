package pl.pollub.f1data.Advices;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class UserControllerAdvices {

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?>  handleEmailExistsException(EmailExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: " + ex.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?>  handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: " + ex.getMessage()));
    }

    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?>  handleUsernameExistsException(UsernameExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: " + ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?>  handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: " + ex.getMessage()));
    }

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Duplicate entry. Username or email already exists."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Error: Access denied."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Username and/or password are incorrect."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: " + ex.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error: " + ex.getMessage()));
    }

}
