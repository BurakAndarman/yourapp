package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "weather_preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherPreferences {

    @Id
    @Column(name = "username", nullable = false, length = 50)
    private String userName;

    @Column(name = "format", length = 20)
    private String format = "simple";

    @Column(name = "look", length = 20)
    private String look = "cards";

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "username")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "weatherPreferences", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @ToString.Exclude
    private Set<WeatherPreferencesCities> weatherPreferencesCities = new HashSet<>();

    public void addCity(WeatherPreferencesCities weatherPreferencesCities) {
        this.weatherPreferencesCities.add(weatherPreferencesCities);
    }

    public void removeCities(Set<WeatherPreferencesCities> weatherPreferencesCitiesToRemove) {
        for (Iterator<WeatherPreferencesCities> iterator = this.weatherPreferencesCities.iterator(); iterator.hasNext();) {
            WeatherPreferencesCities weatherPreferencesCitiesInEntity = iterator.next();

            if(weatherPreferencesCitiesToRemove.contains(weatherPreferencesCitiesInEntity)) {
                iterator.remove();
            }
        }
    }

    public WeatherPreferences(String userName, User user) {
        this.userName = userName;
        this.user = user;
    }

}
