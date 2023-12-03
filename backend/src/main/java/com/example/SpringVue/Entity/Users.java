package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Users {

    @Id
    @Column(name="username",nullable = false,length = 50)
    private String userName;

    @Column(name="language",nullable = false,length = 68)
    private String language;

    @Column(name="enabled")
    private boolean enabled;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private NewsPreferences newsPreferences;

}
