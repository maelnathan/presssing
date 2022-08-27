package com.formationjava.pressing.servicies.impl;

import com.formationjava.pressing.entities.Role;
import com.formationjava.pressing.repositories.RoleRepository;
import com.formationjava.pressing.servicies.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final Logger log= LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        log.debug("l'element a enregistrer est : {} ", role);
        // Mettre la version du role une première fois à 1
        role.setVersion(1);
        // Activer le role
        role.setActivated(true);
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        log.debug("l'element a modifier est : {} ", role);
        role.setVersion(role.getVersion()+1);
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> partialUpdate(Role role) {
        log.debug("Demande de mise à jour partielle du rôle : {}", role);
        return roleRepository
                .findById(role.getId())
                .map(existingRole -> {
                    if (role.getName() != null) {
                        existingRole.setName(role.getName());
                    }
                    if (role.getVersion() != null) {
                        existingRole.setVersion(role.getVersion()+1);
                    }
                    if (role.getActivated() != null) {
                        existingRole.setActivated(role.getActivated());
                    }
                    return existingRole;
                })
                .map(roleRepository::save);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Optional<Role> findOne(Long id) {
        return roleRepository.findById(id);
    }

   /*@Override
    public Optional<Role> delete(Long id) {
       // Recuperer l'objet à supprimer
       Optional<Role> role = roleRepository.findById(id);
       //Mettre l'objet à supprimer dans roleToDelete et le upprimer s'il est present
       roleRepository.findById(id).ifPresent(existRole->{
           roleRepository.delete(existRole);
           log.debug("Role supprimé: {}", role);
       });
        return role;
    }*/

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Civility : {}", id);
        roleRepository.deleteById(id);
    }

}
