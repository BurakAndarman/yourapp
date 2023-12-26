package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.Plans;
import com.example.SpringVue.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlansRepository extends JpaRepository<Plans, Integer> {

    List<Plans> findAllByUser(User user);

}
