package com.example.SpringVue.Dto;

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

    private String title;

    private String content;

    private List<String> tags;

    private KanbanList kanbanList;

    // Fields to be used in client for indicating certain actions

    private boolean created = false;

    private boolean changed = false;

    private boolean deleted = false;

    public PlansDto(String title, String content, List<String> tags, KanbanList kanbanList) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.kanbanList = kanbanList;
    }

}
