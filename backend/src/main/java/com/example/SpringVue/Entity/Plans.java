package com.example.SpringVue.Entity;

import com.example.SpringVue.Utils.KanbanList;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @Column(name="content")
    private String content = "";

    @Enumerated(EnumType.STRING)
    @Column(name="kanban_list",nullable = false)
    private KanbanList kanbanList = KanbanList.TODO;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "plans", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @ToString.Exclude
    private Set<PlansTags> plansTags = new HashSet<>();

    public Plans(String title, String content, KanbanList kanbanList, User user) {
        this.title = title;
        this.content = content;
        this.kanbanList = kanbanList;
        this.user = user;
    }

}

