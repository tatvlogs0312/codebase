package org.example.codebase.model.key_cloak;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeyCloakCredentialsDTO {
    private String type;
    private String value;
    private Boolean temporary;
}
