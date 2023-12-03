package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "news_preferences", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewsPreferences {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users.username")
    private Users user;

    @Column(name="language")
    @Size(max = 2, min = 2)
    private String language = "en";

    @Column(name="interested_topics",length = 80)
    private String interestedTopics = "";

}
