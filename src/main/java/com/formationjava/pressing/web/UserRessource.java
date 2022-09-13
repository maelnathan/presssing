package com.formationjava.pressing.web;

import com.formationjava.pressing.entities.User;
import com.formationjava.pressing.repositories.UserRepository;
import com.formationjava.pressing.servicies.UserService;
import com.formationjava.pressing.servicies.dto.UserDto;
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
import org.springframework.http.RequestEntity;
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
public class UserRessource {
    private final Logger log= LoggerFactory.getLogger(UserRessource.class);
    private static final String NOM_ENTITY = "user";
    @Value("${spring.application.name}")
    private String applicationName;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserRessource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody User userDto) throws URISyntaxException {
        if(userDto.getId() != null){
            log.info("L'id du role ne doit pas être null");
        } else if (userRepository.findOneByEmail(userDto.getEmail()).isPresent()) {
            log.info("L'e-mamil existe déjà");
        } else if (userRepository.findOneByLogin(userDto.getLogin()).isPresent()) {
            log.info("Le login existe déjà");
        }
        System.out.println(userDto);
        User user = userService.save(userDto);
        return ResponseEntity
                .created(new URI("/api/users/" + user.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName,true,NOM_ENTITY,user.getId().toString()))
                .body(user);
    }
    @PostMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody User user) throws URISyntaxException {
        log.debug("Demande REST pour mettre à jour le user : {}, {}", id, user);
        if (user.getId() == null) {
            throw new BadRequestAlertException("id invalide", NOM_ENTITY, "idnulle");
        }
        if (!Objects.equals(id, user.getId())) {
            throw new BadRequestAlertException("ID invalide", NOM_ENTITY, "idinvalid");
        }

        if (!userRepository.existsById(id)) {
            throw new BadRequestAlertException("Entité non présent", NOM_ENTITY, "idnonPresent");
        }

        User result = userService.update(user);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, NOM_ENTITY, user.getId().toString()))
                .body(result);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        Page<User> page = userService.findAll(pageable);

        System.out.println(page);

        return ResponseEntity.ok().body(page.getContent());

        /*HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());*/
    }

    @GetMapping("/alluser")
    public ResponseEntity<List<User>> getAll(){
        List<User> user = userRepository.findAll();
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping(value = "/user/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<User> partialUpdateUser(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody User user) {
        log.debug("Demande REST de mise à jour partielle du rôle partiellement : {}, {}", id, user);
        if (user.getId() == null) {
            throw new BadRequestAlertException("ID Invalide", NOM_ENTITY, "idnulle");
        }
        if (!Objects.equals(id, user.getId())) {
            throw new BadRequestAlertException("ID Invalide", NOM_ENTITY, "idinvalide");
        }

        if (!userRepository.existsById(id)) {
            throw new BadRequestAlertException("Entité introuvable", NOM_ENTITY, "idintrouvable");
        }

        Optional<User> result = userService.partialUser(user);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, NOM_ENTITY, user.getId().toString())
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.debug("Demande pour obtenir le user : {}", id);
        Optional<User> user = userService.findOne(id);
        return ResponseUtil.wrapOrNotFound(user);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Optional<User>> deleteUser(@PathVariable Long id) {
        log.debug("Demande REST pour supprimer un role : {}", id);

        Optional<User> userExist = userService.findOne(id);

        userExist.ifPresent(userExisteToDelete->{
            userService.delete(userExisteToDelete.getId());
        });

        return ResponseUtil.wrapOrNotFound(Optional.of(userExist));
    }
}
