package com.example.SpringVue.Entity;

import com.example.SpringVue.Utils.Tags;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;

@Entity
@Table(name = "plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Plans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private int id;

    @Column(name="title",nullable = false,length = 50)
    private String title;

    @Column(name="content",nullable = false)
    private String content;

    @Enumerated
    @Column(name="tags")
    private Tags tags = Tags.valueOf("TODO");

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    public Plans(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}

