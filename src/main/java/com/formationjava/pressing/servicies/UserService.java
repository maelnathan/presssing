package com.formationjava.pressing.servicies;

import com.formationjava.pressing.entities.User;
import com.formationjava.pressing.servicies.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    User save(User user);
    /*User save(UserDto userDto);*/
    User update(User user);
    Optional<User> partialUser(User user);
    Page<User> findAll(Pageable pageable);
    Optional<User> findOne(Long id);
    void delete(Long id);
}
