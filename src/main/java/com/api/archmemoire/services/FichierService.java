package com.api.archmemoire.services;

import com.api.archmemoire.dto.Encrypted;
import com.api.archmemoire.dto.FichierDto;
import com.api.archmemoire.entities.Fichier;
import com.api.archmemoire.entities.Utilisateur;
import com.api.archmemoire.exceptions.NotFoundException;
import com.api.archmemoire.repositories.FichierRepo;
import com.api.archmemoire.repositories.UtilisateurRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Service
public class FichierService {

    private FichierRepo fichierRepo;
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    public FichierService(FichierRepo fichierRepo, UtilisateurRepo utilisateurRepo) {
        this.fichierRepo = fichierRepo;
        this.utilisateurRepo = utilisateurRepo;
    }

    public FichierDto sendFile(Fichier fichier) throws Exception {
        Utilisateur utilisateur = utilisateurRepo.findByEmail(fichier.getEmailExpediteur()).orElse(null);
        if (utilisateur == null){
            throw new NotFoundException("L'expediteur du fichier est inconnu");
        }else if (utilisateur.getActive().equals(false)){
            throw new NotFoundException("Utilisateur(expediteur) desactiver");
        }
        Utilisateur user = utilisateurRepo.findById(fichier.getUtilisateur().getId()).orElse(null);
        if (user.getActive().equals(false)){
            throw new NotFoundException("Utilisateur(destinataire) du fichier est inconnu");
        }
        KeyPair keyPair = GeneratorKey.generateKeyPair();
        if (keyPair != null){
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            ChiffrementService chiffrementService = new ChiffrementService();
            Encrypted encrypted = chiffrementService.encryptContent(fichier.getNom(), publicKey);
            String encryptedMessage = encrypted.getData();

            byte[] signature = FichierSignature.signMail(fichier.getNom(), privateKey);

            fichier.setSignature(signature);
            byte[] encryptPrivateKey = KeyEncryption.encryptPrivateKey(privateKey);
            byte[] encryptPublicKey = KeyEncryption.encryptPublicKey(publicKey);
            fichier.setPublicKey(encryptPublicKey);
            fichier.setPrivateKey(encryptPrivateKey);
            fichier.setSecretKey(encrypted.getSecretKey());
            fichier.setEmailExpediteur(utilisateur.getEmail());
            fichier.setNom(encryptedMessage);
        }

        fichierRepo.save(fichier);
        FichierDto fichierDto = new FichierDto();
        fichierDto.setId(fichier.getId());
        fichierDto.setEmailExpediteur(utilisateur.getEmail());
        fichierDto.setNom(fichier.getNom());
        fichierDto.setDate(fichier.getDate());
        fichierDto.setUrlsJointPieces(fichier.getUrlJointPieces());
        return fichierDto;
    }

    public List<FichierDto> boiteDeReception(Long id) throws Exception {
        Utilisateur utilisateur = utilisateurRepo.findById(id).orElse(null);
//        String password = "koire@0312";
        if (utilisateur == null){
            throw new NotFoundException("Aucun compte avec l'id : " + id + "n'a ete trouve");
        }
        List<FichierDto> mailDtoList = new ArrayList<>();
        for (Fichier fichier : fichierRepo.findAll()){
            Utilisateur user = utilisateurRepo.findById(fichier.getUtilisateur().getId()).orElse(null);
            if (user.getEmail().equals(utilisateur.getEmail())){
                String decryptedMessage = "";

                PrivateKey privateKey = KeyEncryption.decryptPrivateKey(fichier.getPrivateKey());
                PublicKey publicKey = KeyEncryption.decryptPublicKey(fichier.getPublicKey());
                boolean isValid = FichierSignature.verifySignature(fichier.getNom(), fichier.getSignature(), publicKey);
                if (!isValid){
                    ChiffrementService chiffrementService = new ChiffrementService();
                    decryptedMessage = chiffrementService.decryptContent(fichier.getNom(), fichier.getSecretKey(), privateKey);
                }else {
                    throw new Exception("Fichier corrompu");
                }
                FichierDto fichierDto = new FichierDto();
                fichierDto.setId(fichier.getId());
                fichierDto.setNom(decryptedMessage);
                fichierDto.setDate(fichier.getDate());
                fichierDto.setEmailExpediteur(fichier.getEmailExpediteur());
                fichierDto.setUrlsJointPieces(fichier.getUrlJointPieces());
                mailDtoList.add(fichierDto);
            }
        }
        return mailDtoList;
    }

    public List<FichierDto> boiteEnvoi(Long id) throws Exception {
        Utilisateur utilisateur = utilisateurRepo.findById(id).orElse(null);
//        String password = "koire@0312";
        if (utilisateur == null){
            throw new NotFoundException("Aucun compte avec l'id : " + id + "n'a ete trouve");
        }
        List<FichierDto> mailDtoList = new ArrayList<>();
        for (Fichier fichier : fichierRepo.findAll()){
            Utilisateur user = utilisateurRepo.findByEmail(fichier.getEmailExpediteur()).orElse(null);
            if (user.getEmail().equals(utilisateur.getEmail())){
                String decryptedMessage = "";

                PrivateKey privateKey = KeyEncryption.decryptPrivateKey(fichier.getPrivateKey());
                PublicKey publicKey = KeyEncryption.decryptPublicKey(fichier.getPublicKey());
                boolean isValid = FichierSignature.verifySignature(fichier.getNom(), fichier.getSignature(), publicKey);
                if (!isValid){
                    ChiffrementService chiffrementService = new ChiffrementService();
                    decryptedMessage = chiffrementService.decryptContent(fichier.getNom(), fichier.getSecretKey(), privateKey);
                }else {
                    throw new Exception("Fichier corrompu");
                }
                FichierDto fichierDto = new FichierDto();
                fichierDto.setId(fichier.getId());
                fichierDto.setNom(decryptedMessage);
                fichierDto.setDate(fichier.getDate());
                fichierDto.setEmailExpediteur(fichier.getEmailExpediteur());
                fichierDto.setUrlsJointPieces(fichier.getUrlJointPieces());
                mailDtoList.add(fichierDto);
            }
        }
        return mailDtoList;
    }


    public String deleteFile(Long id) throws Exception {
        if (!fichierRepo.existsById(id)) {
            throw new NotFoundException("Aucun fichier avec l'id : " + id + "n'a ete trouve");
        }
        fichierRepo.deleteById(id);
        throw new Exception("Fichier supprimer");
    }

    public String deleteAllFileUser(Long id) throws Exception {
        if (!utilisateurRepo.existsById(id)){
            throw new NotFoundException("Aucun utilisateur avec l'id : " + id + "n'a ete trouve");
        }
        List<FichierDto> fichierDtoList = boiteDeReception(id);
        List<Fichier> fichierList = new ArrayList<>();
        for (FichierDto fichierDto : fichierDtoList){
            Fichier fichier = fichierRepo.findById(fichierDto.getId()).orElse(null);
            if (fichier != null){
                fichierList.add(fichier);
            }
        }
        fichierRepo.deleteAll(fichierList);
        throw new RuntimeException("Suppresion reussi");
    }
}
