package org.example.codebase.model.key_cloak;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeycloakUpdatePasswordReqDTO {
    private String type;
    private Boolean temporary;
    private String value;
}
