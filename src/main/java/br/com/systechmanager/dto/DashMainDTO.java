
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
public class DashMainDTO {
    private int userId;
    private int companyId;
}
