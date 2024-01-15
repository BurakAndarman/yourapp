package com.example.SpringVue.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tags", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "color" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private int id;

    @Column(name = "name",length = 15,nullable = false)
    private String name;

    @Column(name = "color", length = 10, nullable = false)
    private String color;

    @OneToMany(mappedBy = "tags",cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private Set<PlansTags> plansTags;

    public Tags(String name, String color) {
        this.name = name;
        this.color = color;
    }

}
