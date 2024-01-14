package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepository  extends JpaRepository<Tags, Integer> {

    Optional<Tags> findFirstByNameAndColor(String name, String color);

}
