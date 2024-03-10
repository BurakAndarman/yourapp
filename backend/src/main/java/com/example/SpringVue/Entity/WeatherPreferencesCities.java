package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weather_preferences_cities", uniqueConstraints = { @UniqueConstraint(columnNames = { "username", "city_id" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherPreferencesCities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    @ToString.Exclude
    private WeatherPreferences weatherPreferences;

    @Column(name = "city_id", nullable = false)
    private int cityId;

    @Column(name = "order_no", nullable = false)
    private int orderNo = 0;

    public WeatherPreferencesCities(WeatherPreferences weatherPreferences, int cityId) {
        this.weatherPreferences = weatherPreferences;
        this.cityId = cityId;
    }

    public WeatherPreferencesCities(WeatherPreferences weatherPreferences, int cityId, int orderNo) {
        this.weatherPreferences = weatherPreferences;
        this.cityId = cityId;
        this.orderNo = orderNo;
    }

}
