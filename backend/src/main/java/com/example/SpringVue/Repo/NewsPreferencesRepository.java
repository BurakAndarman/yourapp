package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.NewsPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsPreferencesRepository extends JpaRepository<NewsPreferences, String> {
}