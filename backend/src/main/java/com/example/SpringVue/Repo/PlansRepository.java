package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.Plans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlansRepository extends JpaRepository<Plans, Integer> {
}
