package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Optional<User> findOneByEmailAndPassword(String email, String password);
}
