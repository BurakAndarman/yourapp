package com.example.SpringVue.Entity;

import com.example.SpringVue.Utils.KanbanList;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name="tags")
    private String tags = "";

    @Enumerated(EnumType.STRING)
    @Column(name="kanban_list",nullable = false)
    private KanbanList kanbanList = KanbanList.TODO;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", nullable = false)
    @ToString.Exclude
    private User user;

    public Plans(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

}

