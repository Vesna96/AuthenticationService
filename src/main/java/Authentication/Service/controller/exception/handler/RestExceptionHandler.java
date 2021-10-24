package Authentication.Service.controller.exception.handler;

import Authentication.Service.exception.*;
import Authentication.Service.model.dto.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleEmailNotFoundException(EmailNotFoundException e) {
        return new ResponseEntity<>(ResponseMessage.builder()
                .responseMessage(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ResponseMessage> handleTokenExpiredException(TokenExpiredException e) {
        return new ResponseEntity<>(ResponseMessage.builder()
                .responseMessage(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseMessage> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(ResponseMessage.builder()
                .responseMessage(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OrganizationAlreadyExistsException.class)
    public ResponseEntity<ResponseMessage> handleOrganizationAlreadyExistsException(OrganizationAlreadyExistsException e) {
        return new ResponseEntity<>(ResponseMessage.builder()
                .responseMessage(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyInOrganizationException.class)
    public ResponseEntity<ResponseMessage> handleUserAlreadyInOrganizationException(UserAlreadyInOrganizationException e) {
        return new ResponseEntity<>(ResponseMessage.builder()
                .responseMessage(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .build(), HttpStatus.CONFLICT);
    }

}
