package com.formationjava.pressing.repositories;

import com.formationjava.pressing.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
