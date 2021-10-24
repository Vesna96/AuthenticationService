package Authentication.Service.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAlreadyExistsException extends Exception{
    private String message;
}
