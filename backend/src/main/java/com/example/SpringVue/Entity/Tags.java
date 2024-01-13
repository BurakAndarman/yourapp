package com.example.SpringVue.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tags {

    @Id
    @Column(name = "name",length = 15,nullable = false)
    private String name;

    @Column(name = "color", length = 10, nullable = false)
    private String color;

    @OneToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<PlansTags> plansTags;

}
