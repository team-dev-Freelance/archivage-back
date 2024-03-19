package com.api.archmemoire.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Fichier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOM", length = 4096)
    private String nom;

    @Column
    private String emailExpediteur;

    @Column
    private Date date = new Date();

    @Column(length = 4096)
    private byte[] signature;

    @Column(length = 4096)
    private byte[] privateKey;

    @Column(length = 4096)
    private byte[] publicKey;

    @Column(length = 4096)
    private byte[] secretKey;

    @Column
    private String urlJointPieces;

    @ManyToOne
    @JoinColumn(name = "UTILISATEUR_ID")
    private Utilisateur utilisateur;
}
