package Authentication.Service.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ResponseMessage {

    private String responseMessage;
    private int status;
    private String error;

}
