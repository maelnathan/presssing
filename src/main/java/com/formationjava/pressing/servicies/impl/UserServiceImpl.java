package com.formationjava.pressing.servicies.impl;

import com.formationjava.pressing.entities.Role;
import com.formationjava.pressing.entities.User;
import com.formationjava.pressing.repositories.RoleRepository;
import com.formationjava.pressing.repositories.UserRepository;
import com.formationjava.pressing.servicies.UserService;
import com.formationjava.pressing.servicies.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log= LoggerFactory.getLogger(UserServiceImpl.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    /*public User save(UserDto userDto) {
        User user = new User();
        //Role role = new Role();

        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setVersion(1);
        user.setActivated(true);

        if(userDto.getRoles()!=null){
            //Set<Role> roles = userDto.getRoles();
            user.setRoles(userDto.getRoles());
                    *//*.stream()
                    .map(roleRepository::)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());*//*
        }
        userRepository.save(user);
        return user;
    }*/
    public User save(User user) {
        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setVersion(1);
        newUser.setActivated(true);
        if(user.getRoles() != null){
            newUser.setRoles(user.getRoles());
        }
        return userRepository.save(newUser);
    }

    @Override
    public User update(User user) {
        user.setVersion(user.getVersion()+1);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> partialUser(User user) {
        log.debug("Demande de mise à jour partielle du user : {}", user);
        return userRepository
                .findById(user.getId())
                .map(existingUser -> {
                    if (user.getLogin() != null) {
                        existingUser.setLogin(user.getLogin());
                    }
                    if (user.getEmail() != null) {
                        existingUser.setEmail(user.getEmail());
                    }

                    if (user.getPassword() != null) {
                        existingUser.setPassword(user.getPassword());
                    }
                    if (user.getVersion() != null) {
                        existingUser.setVersion(user.getVersion()+1);
                    }
                    return existingUser;
                })
                .map(userRepository::save);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
