package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @Column(name="username",nullable = false,length = 50)
    private String userName;

    @Column(name="password",nullable = false,length = 68)
    private String password;

    @Column(name="enabled")
    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private NewsPreferences newsPreferences;

}
