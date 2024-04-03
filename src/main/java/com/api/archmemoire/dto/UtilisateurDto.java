package com.api.archmemoire.dto;

import com.api.archmemoire.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDto {

    private Long id;
    private String nom;
    private String email;
    private Boolean active;
    private Role role;
}
