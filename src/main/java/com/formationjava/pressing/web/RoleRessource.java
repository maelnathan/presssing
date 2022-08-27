package com.formationjava.pressing.web;

import com.formationjava.pressing.entities.Role;
import com.formationjava.pressing.repositories.RoleRepository;
import com.formationjava.pressing.servicies.RoleService;
import com.formationjava.pressing.tools.HeaderUtil;
import com.formationjava.pressing.tools.PaginationUtil;
import com.formationjava.pressing.tools.ResponseUtil;
import com.formationjava.pressing.web.error.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RoleRessource {
    private final Logger log= LoggerFactory.getLogger(RoleRessource.class);
    private static final String NOM_ENTITY = "role";
    @Value("${spring.application.name}")
    private String applicationName;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public RoleRessource(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    /**
     * {@code POST  /roles} : Créer un nouveau role.
     *
     * @param role le role à créer
     * @return le {@link ResponseEntity} avec statut {@code 201 (Created)} et le corps role, ou statut {@code 400 (Bad Request)} avec l'ID du role existe.
     * @throws URISyntaxException si l'URL est incorrect.
     */
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws URISyntaxException {
        if(role.getId() != null){
            throw new BadRequestAlertException("Un nouveau rôle ne peut pas déjà avoir un ID", NOM_ENTITY, "idexists");
        }
        Role result = roleService.save(role);
        return ResponseEntity
                .created(new URI("/api/roles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName,true,NOM_ENTITY,result.getId().toString()))
                .body(role);
    }

    /**
     * {@code PUT  /rols/:id} : mise à jour du role.
     *
     * @param id l'id du role à modifier.
     * @param role le role à modifier.
     * @return le {@link ResponseEntity} avec statut {@code 200 (OK)} et le role à modifier,
     * ou avec statut {@code 400 (Bad Request)} si le role n'est pas valide,
     * ou avec statut {@code 500 (Internal Server Error)} si le role n'est pas modifiable.
     * @throws URISyntaxException si l'URL n'est pas correct.
     */
    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Role role) {
        log.debug("Demande REST pour mettre à jour le role : {}, {}", id, role);
        if (role.getId() == null) {
            throw new BadRequestAlertException("id invalide", NOM_ENTITY, "idnulle");
        }
        if (!Objects.equals(id, role.getId())) {
            throw new BadRequestAlertException("ID invalide", NOM_ENTITY, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entité non présent", NOM_ENTITY, "idnonPresent");
        }

        Role result = roleService.update(role);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, NOM_ENTITY, role.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /roles/:id} : Mises à jour partielles des champs d'un rôle existant, le champ ignorera s'il est nul
     *
     * @param id le id du role à enregistrer.
     * @param role le role à mettre à jour.
     * @return le {@link ResponseEntity} avec statut {@code 200 (OK)} et avec le corps du role à mettre à jour,
     * ou avec statut {@code 400 (Bad Request)} si le role n'est pas valide,
     * or avec statut {@code 404 (Not Found)} le role n'est pas trouvé,
     * or avec statut {@code 500 (Internal Server Error)} le role ne peut pas être mise à jour.
     */
    @PatchMapping(value = "/roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Role> partialUpdateRole(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Role role) {
        log.debug("Demande REST de mise à jour partielle du rôle partiellement : {}, {}", id, role);
        if (role.getId() == null) {
            throw new BadRequestAlertException("ID Invalide", NOM_ENTITY, "idnulle");
        }
        if (!Objects.equals(id, role.getId())) {
            throw new BadRequestAlertException("ID Invalide", NOM_ENTITY, "idinvalide");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entité introuvable", NOM_ENTITY, "idintrouvable");
        }

        Optional<Role> result = roleService.partialUpdate(role);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, NOM_ENTITY, role.getId().toString())
        );
    }


    /**
     * {@code GET  /roles} : Obtenir tous les roles.
     *
     * @param pageable le pargination de l'information.
     * @return le {@link ResponseEntity} avec statut {@code 200 (OK)} et la liste des roles dans le corps .
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRole(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        Page<Role> page = roleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /role/:id} : Obtenir le role par son "id".
     *
     * @param id l' id du role à retrouver.
     * @return le {@link ResponseEntity} avec statut {@code 200 (OK)} avec le corps du role, ou avec statut {@code 404 (Not Found)}.
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        log.debug("Demande pour obtenir le role : {}", id);
        Optional<Role> role = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(role);
    }

    /**
     * {@code DELETE  /roles/:id} : Suppression du role par "id".
     *
     * @param id l' id du role à supprimer.
     * @return le {@link ResponseEntity} avec statut {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Optional<Role>> deleteRole(@PathVariable Long id) {
        log.debug("Demande REST pour supprimer un role : {}", id);

        Optional<Role> roleExist = roleService.findOne(id);

        roleExist.ifPresent(exitRoleToDelete->{
            roleService.delete(exitRoleToDelete.getId());
        });

        return ResponseUtil.wrapOrNotFound(Optional.of(roleExist));
    }


    @DeleteMapping("/roles/{id}")
    public ResponseEntity deleteRole(@PathVariable(value = "id", required = false) final Long id) {

        if(!roleRepository.existsById(id)){
            log.info("Impossible d'effectuer la suppression d'un id inexistant.");
        }

        roleService.delete(id);
        return new ResponseEntity<>("La suppression a été effectuée", HttpStatus.OK);
    }
    @GetMapping("/roles/{id}")
    public ResponseEntity<Optional<Role>> getOneRole(@PathVariable(value = "id", required = false) final Long id) {

        if(!roleRepository.existsById(id)){
            log.info("Impossible d'afficher un role dont l'id est inexistant.");
        }

        Optional<Role> role = roleService.findOne(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
