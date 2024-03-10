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

    @OneToMany(mappedBy = "weatherPreferences", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private Set<WeatherPreferencesCities> weatherPreferencesCities = new HashSet<>();

    public WeatherPreferences(String userName, User user) {
        this.userName = userName;
        this.user = user;
    }

}
