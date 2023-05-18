package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    //findBy규칙 -> Username 문법
    //select * from user where = 1?
    User findByUsername(String username);
}
