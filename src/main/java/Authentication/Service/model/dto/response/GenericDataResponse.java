package Authentication.Service.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenericDataResponse<T> {

    private int status;
    private String message;
    private String error;
    private T data;
}
