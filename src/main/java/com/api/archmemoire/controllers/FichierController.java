package com.api.archmemoire.controllers;

import com.api.archmemoire.dto.FichierDto;
import com.api.archmemoire.dto.RequestFile;
import com.api.archmemoire.entities.Fichier;
import com.api.archmemoire.repositories.UtilisateurRepo;
import com.api.archmemoire.services.FichierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/user/fichier")
public class FichierController {

    private FichierService fichierService;
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    public FichierController(FichierService fichierService, UtilisateurRepo utilisateurRepo) {
        this.fichierService = fichierService;
        this.utilisateurRepo = utilisateurRepo;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<FichierDto> sendFile(@ModelAttribute RequestFile request) throws Exception {

        Fichier fichier = new Fichier();
        if (!request.getFile().isEmpty()){
            String dir = System.getProperty("user.dir");
            String url = dir+"/src/main/resources/assets/"+request.getFile().getOriginalFilename();
            fichier.setUrlJointPieces(url);
            File convertFile = new File(url);
            convertFile.createNewFile();
            try(FileOutputStream out = new FileOutputStream(convertFile)){
                out.write(request.getFile().getBytes());
            }catch (Exception exe){
                exe.printStackTrace();
            }
        }
        fichier.setNom(request.getNom());
        fichier.setEmailExpediteur(request.getEmailExpediteur());
        fichier.setUtilisateur(utilisateurRepo.findById(request.getUserId()).orElse(null));

        return new ResponseEntity<>(fichierService.sendFile(fichier), HttpStatus.OK);
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
