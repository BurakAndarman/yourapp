package com.example.SpringVue.Entity;

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

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "plan_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Plans plans;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Tags tags;

    public PlansTags(Plans plans, Tags tags) {
        this.plans = plans;
        this.tags = tags;
    }

}
