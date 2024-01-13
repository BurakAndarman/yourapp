package com.example.SpringVue.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plans_tags", uniqueConstraints = { @UniqueConstraint(columnNames = { "plan_id", "tag_id" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlansTags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Plans plans;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id", referencedColumnName = "name", nullable = false)
    @ToString.Exclude
    private Tags tags;

}
