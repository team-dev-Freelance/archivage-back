package com.api.archmemoire.controllers;

import com.api.archmemoire.dto.FichierDto;
import com.api.archmemoire.entities.Fichier;
import com.api.archmemoire.services.FichierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/user/fichier")
public class FichierController {

    private FichierService fichierService;

    @Autowired
    public FichierController(FichierService fichierService) {
        this.fichierService = fichierService;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<FichierDto> sendFile(@PathVariable Long id, @RequestBody Fichier fichier) throws Exception {
        return new ResponseEntity<>(fichierService.sendFile(id, fichier), HttpStatus.OK);
    }

    @GetMapping("/boite/{id}")
    public ResponseEntity<List<FichierDto>> getFileListByUser(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(fichierService.boiteDeReception(id), HttpStatus.OK);
    }

    @DeleteMapping("/deleteOneFile/{id}")
    public ResponseEntity<String> deleteOneFile(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(fichierService.deleteFile(id), HttpStatus.OK);
    }

    @PutMapping("/deleteBoiteUser/{id}")
    public ResponseEntity<String> deleteBoiteUser(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(fichierService.deleteAllFileUser(id), HttpStatus.OK);
    }
}
