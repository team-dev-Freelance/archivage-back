package com.api.archmemoire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichierDto {

    private Long id;
    private String emailExpediteur;
    private String nom;
    private String urlsJointPieces;
    private Date date;
}
