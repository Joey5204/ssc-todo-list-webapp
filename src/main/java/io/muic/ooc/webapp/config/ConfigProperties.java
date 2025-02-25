package io.muic.ooc.webapp.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ConfigProperties {

    private String databaseDriverClassName;
    private String databaseConnectionUrl;
    private String databaseUsername;
    private String databasePassword;
}
