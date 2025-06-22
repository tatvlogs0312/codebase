package org.example.codebase.model.key_cloak;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeyCloakNewUserReqDTO {
    private String firstName;
    private String lastName;
    private String username;
    private Boolean enabled;
    private List<KeyCloakCredentialsDTO> credentials;
}
