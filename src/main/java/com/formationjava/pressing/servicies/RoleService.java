package com.formationjava.pressing.servicies;

import com.formationjava.pressing.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RoleService {

    /**
     * Save a role.
     *
     * @param role l'entité à enregistrer.
     * @return l'entité persistante.
     */
    Role save(Role role);

    /**
     * Updates a role.
     *
     * @param role l'entité à mettre à jour.
     * @return l'entité persistante.
     */
    Role update(Role role);


    /**
     * Met partiellement à jour un rôle.
     *
     * @param role l'entité à mettre à jour partiellement.
     * @return l'entité persistante.
     */
    Optional<Role> partialUpdate(Role role);

    /**
     * Obtenez tout le rôle.
     *
     * @param pageable les informations de pagination.
     * @return la liste des entités.
     */
    Page<Role> findAll(Pageable pageable);

    /**
     * Obtenez le rôle "id".
     *
     * @param id le id de l'entité.
     * @return l'entité.
     */
    Optional<Role> findOne(Long id);

    /**
     * Suppression "id" role.
     *
     * @param id l'id de l'entité.
     */
    void delete(Long id);
}
