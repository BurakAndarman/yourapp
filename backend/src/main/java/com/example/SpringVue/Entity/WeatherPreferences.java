package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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
        this.weatherPreferencesCities.removeAll(weatherPreferencesCitiesToRemove);
    }

    public void removeCity(WeatherPreferencesCities weatherPreferencesCities) {
        this.weatherPreferencesCities.remove(weatherPreferencesCities);
    }

    public WeatherPreferencesCities getFirstCityByOrderNo(int orderNo) {
        return this.weatherPreferencesCities.stream()
                .filter(weatherPreferencesCities -> weatherPreferencesCities.getOrderNo() == orderNo)
                .findFirst()
                .orElse(null);
    }

    public WeatherPreferencesCities getFirstCityByCityId(int cityId) {
        return this.weatherPreferencesCities.stream()
                .filter(weatherPreferencesCities -> weatherPreferencesCities.getCityId() == cityId)
                .findFirst()
                .orElse(null);
    }

    public WeatherPreferences(String userName, User user) {
        this.userName = userName;
        this.user = user;
    }

}
