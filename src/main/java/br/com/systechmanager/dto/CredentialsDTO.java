package br.com.systechmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CredentialsDTO {
    private String username;
    private String password;
}
