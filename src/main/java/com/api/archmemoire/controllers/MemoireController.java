package com.api.archmemoire.controllers;

import com.api.archmemoire.dto.RequestMemoire;
import com.api.archmemoire.entities.Jury;
import com.api.archmemoire.entities.Memoire;
import com.api.archmemoire.repositories.EtudiantRepo;
import com.api.archmemoire.repositories.JuryRepo;
import com.api.archmemoire.services.MemoireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/memoire")
@CrossOrigin("*")
public class MemoireController {

    private MemoireService memoireService;
    private EtudiantRepo etudiantRepo;
    private JuryRepo juryRepo;

    @Autowired
    public MemoireController(MemoireService memoireService, EtudiantRepo etudiantRepo, JuryRepo juryRepo) {
        this.memoireService = memoireService;
        this.etudiantRepo = etudiantRepo;
        this.juryRepo = juryRepo;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Memoire> addMemoire(@ModelAttribute RequestMemoire request) throws IOException {
        Memoire memoire = new Memoire();
        if (!request.getFile().isEmpty()){
            String dir = System.getProperty("user.dir");
            String url = dir+"/src/main/resources/assets/"+request.getFile().getOriginalFilename();
            memoire.setUrlFile(url);
            File convertFile = new File(url);
            convertFile.createNewFile();
            try(FileOutputStream out = new FileOutputStream(convertFile)){
                out.write(request.getFile().getBytes());
            }catch (Exception exe){
                exe.printStackTrace();
            }
        }
        memoire.setEtudiant(etudiantRepo.findById(request.getEtudiantId()).orElse(null));
        memoire.setTheme(request.getTheme());
        memoire.setKeyworlds(request.getKeyworlds());
        List<Jury> juryList = new ArrayList<>();
        for (Long jury : request.getJuryId()){
            juryList.add(juryRepo.findById(jury).orElse(null));
        }
        memoire.setJury(juryList);

        return new ResponseEntity<>(memoireService.saveMemoire(memoire), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Memoire>> getAllMemoire(){
        return new ResponseEntity<>(memoireService.getAllMemoire(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Memoire> getMemoireById(@PathVariable Long id){
        return new ResponseEntity<>(memoireService.getMemoireById(id), HttpStatus.OK);
    }

    @GetMapping("/findByKeyworld")
    public ResponseEntity<List<Memoire>> getListMemoireByKeyworld(@RequestParam String keyworld){
        return new ResponseEntity<>(memoireService.getListMemoireByKeyWorlds(keyworld), HttpStatus.OK);
    }

    @GetMapping("/findByOption")
    public ResponseEntity<List<Memoire>> getMemoireByOption(@RequestParam String label){
        return new ResponseEntity<>(memoireService.getListMemoireByOption(label), HttpStatus.OK);
    }

    @GetMapping("/findByEtudiant")
    public ResponseEntity<List<Memoire>> getMemoireByEtudiant(@RequestParam String matricule){
        return new ResponseEntity<>(memoireService.getListMemoireByEtudiant(matricule), HttpStatus.OK);
    }

    @GetMapping("/findByParcours")
    public ResponseEntity<List<Memoire>> getMemoireByParcours(@RequestParam String label, @RequestParam String anneeAca){
        return new ResponseEntity<>(memoireService.getListMemoireByParcours(label, anneeAca), HttpStatus.OK);
    }

    @GetMapping("/findByDepartement")
    public ResponseEntity<List<Memoire>> getMemoireByDepartement(@RequestParam String code, @RequestParam String anneeAca){
        return new ResponseEntity<>(memoireService.getListMemoireByDepartement(code, anneeAca), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Memoire> updateMemoire(@PathVariable Long id, @RequestBody Memoire memoire){
        return new ResponseEntity<>(memoireService.editMemoire(id, memoire), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteMemoire(@PathVariable Long id){
        return new ResponseEntity<>(memoireService.dropMemoire(id), HttpStatus.OK);
    }
}
