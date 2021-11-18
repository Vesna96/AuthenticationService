package Authentication.Service.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserAlreadyInOrganizationException extends Exception {
    private String message;
}
