package com.formationjava.pressing.servicies;

import com.formationjava.pressing.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleService {
    Role save(Role role);
    Role update(Role role);
    Page<Role> findAll(Pageable pageable);
    Optional<Role> findOne(Long id);
    void delete(Long id);
}
