package com.example.SpringVue.Entity;

import com.example.SpringVue.Utils.KanbanList;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "username", nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "plans", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<PlansTags> plansTags;

    public Plans(int id, String title, String content, KanbanList kanbanList, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.kanbanList = kanbanList;
        this.user = user;
    }

    public Plans(String title, String content, KanbanList kanbanList, User user) {
        this.title = title;
        this.content = content;
        this.kanbanList = kanbanList;
        this.user = user;
    }

}

