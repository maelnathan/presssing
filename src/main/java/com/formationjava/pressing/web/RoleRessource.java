package com.formationjava.pressing.web;

import com.formationjava.pressing.entities.Role;
import com.formationjava.pressing.repositories.RoleRepository;
import com.formationjava.pressing.servicies.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class RoleRessource {
    private final Logger log= LoggerFactory.getLogger(RoleRessource.class);
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public RoleRessource(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws URISyntaxException {
        if(role.getId() != null){
            log.info("Ce role existe déjà.");
        }
        Role roleResult = roleService.save(role);
        return new ResponseEntity<>(roleResult, HttpStatus.CREATED);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Role role) throws URISyntaxException {
        if(role.getId() == null){
            log.info("Impossible d'effectuer la modification d'un id null.");
        }

        if(!Objects.equals(id, role.getId())){
            log.info("les id ne sont pas identique.");
        }

        if(!roleRepository.existsById(id)){
            log.info("Objet role est introuvable.");
        }

        Role roleResult = roleService.update(role);
        return new ResponseEntity<>(roleResult, HttpStatus.OK);
    }
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRole(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        Page<Role> pageRole = roleService.findAll(pageable);
        return ResponseEntity.ok().body(pageRole.getContent());
    }

}
