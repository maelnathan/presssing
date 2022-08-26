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
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        log.debug("l'element a modifier est : {} ", role);
        return roleRepository.save(role);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Optional<Role> findOne(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
