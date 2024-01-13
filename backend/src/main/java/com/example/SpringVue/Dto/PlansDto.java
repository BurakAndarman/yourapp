package com.example.SpringVue.Dto;

import com.example.SpringVue.Entity.Tags;
import com.example.SpringVue.Utils.KanbanList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class PlansDto {

    private int id;

    private String title;

    private String content;

    private KanbanList kanbanList;

    private List<Tags> tags;

    // Fields to be used in client for indicating certain actions

    private boolean created = false;

    private boolean changed = false;

    private boolean deleted = false;

    public PlansDto(int id,String title, String content, KanbanList kanbanList, List<Tags> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.kanbanList = kanbanList;
        this.tags = tags;
    }

}
