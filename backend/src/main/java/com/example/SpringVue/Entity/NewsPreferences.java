package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "news_preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewsPreferences {

    @Id
    @Column(name = "username",nullable = false,length = 50)
    private String userName;

    @Column(name="language")
    @Size(max = 2, min = 2)
    private String language = "en";

    @Column(name="interested_topics",length = 80)
    private String interestedTopics = "";

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "username")
    private User user;

    public NewsPreferences(String userName, User user) {
        this.userName = userName;
        this.user = user;
    }

    public NewsPreferences(String userName, String language, String interestedTopics) {
        this.userName = userName;
        this.language = language;
        this.interestedTopics = interestedTopics;
    }
}
