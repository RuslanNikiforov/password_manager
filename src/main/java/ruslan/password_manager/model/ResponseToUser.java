package ruslan.password_manager.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseToUser {

    private String username;

    private String message;

    private boolean hasErrors;

}
