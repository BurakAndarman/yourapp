package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.WeatherPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPreferencesRepository extends JpaRepository<WeatherPreferences, String> {
}
