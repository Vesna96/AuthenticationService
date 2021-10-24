package Authentication.Service.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenExpiredException extends Exception {
    private String message;
}
