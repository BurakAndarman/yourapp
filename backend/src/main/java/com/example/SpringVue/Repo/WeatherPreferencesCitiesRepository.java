package com.example.SpringVue.Repo;

import com.example.SpringVue.Entity.WeatherPreferencesCities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPreferencesCitiesRepository extends JpaRepository<WeatherPreferencesCities, Integer> {
}
