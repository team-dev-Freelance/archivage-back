package com.api.archmemoire.repositories;

import com.api.archmemoire.entities.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichierRepo extends JpaRepository<Fichier,Long> {
}
