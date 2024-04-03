package com.api.archmemoire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFile {
    private String nom;
    private String emailExpediteur;
    private MultipartFile file;
    private Long userId;
}
