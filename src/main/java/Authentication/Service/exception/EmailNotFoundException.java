package Authentication.Service.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailNotFoundException extends Exception {
    private String message;

}
