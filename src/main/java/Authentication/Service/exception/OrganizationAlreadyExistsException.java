package Authentication.Service.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrganizationAlreadyExistsException extends Exception{
    private String message;
}
