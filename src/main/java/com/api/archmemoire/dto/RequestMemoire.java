package com.api.archmemoire.dto;

import com.api.archmemoire.entities.Etudiant;
import com.api.archmemoire.entities.Jury;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMemoire {
    private String theme;
    private MultipartFile file;
    private List<String> keyworlds = new ArrayList<>();
    private Long etudiantId;
    private List<Long> juryId = new ArrayList<>();

}
