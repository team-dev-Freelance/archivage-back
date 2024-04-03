package com.api.archmemoire.controllers;

import com.api.archmemoire.dto.Password;
import com.api.archmemoire.dto.UtilisateurDto;
import com.api.archmemoire.entities.Role;
import com.api.archmemoire.entities.Utilisateur;
import com.api.archmemoire.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/user")
public class UtilisateurController {

    private UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/user/add")
    public ResponseEntity<UtilisateurDto> saveUser(@RequestBody Utilisateur utilisateur){
        return new ResponseEntity<>(utilisateurService.addUser(utilisateur), HttpStatus.OK);
    }

    @GetMapping("/user/findAll")
    public ResponseEntity<List<UtilisateurDto>> getAllUsers(){
        return new ResponseEntity<>(utilisateurService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/user/findById/{id}")
    public ResponseEntity<UtilisateurDto> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(utilisateurService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/user/findByEmail")
    public ResponseEntity<UtilisateurDto> getUserByEmail(@RequestParam("email") String email){
        return new ResponseEntity<>(utilisateurService.getByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/user/findByRole")
    public ResponseEntity<List<UtilisateurDto>> getUserByRole(@RequestParam("role") Role role){
        return new ResponseEntity<>(utilisateurService.getAllUserByRole(role), HttpStatus.OK);
    }

    @GetMapping("/user/changePassword/{id}")
    public ResponseEntity<String> changedPassword(@PathVariable Long id, @RequestBody Password password) throws Exception {
        return new ResponseEntity<>(utilisateurService.changePassword(id, password.getOldPassword(), password.getNewPassword()), HttpStatus.OK);
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<UtilisateurDto> updateUserById(@PathVariable Long id, @RequestBody Utilisateur utilisateur){
        return new ResponseEntity<>(utilisateurService.editUser(id, utilisateur), HttpStatus.OK);
    }

    @PutMapping("/user/deleteUserById/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        return new ResponseEntity<>(utilisateurService.deleteUser(id), HttpStatus.OK);
    }
}
