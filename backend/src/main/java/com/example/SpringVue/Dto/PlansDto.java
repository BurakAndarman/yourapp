package com.example.SpringVue.Dto;

import com.example.SpringVue.Utils.KanbanList;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PlansDto {

    private int id;

    private String title;

    private String content;

    private String image;

    private String kanbanList;

    private List<TagsDto> tags;

    // Fields to be used in client for indicating certain actions

    private boolean created = false;

    private boolean changed = false;

    private boolean deleted = false;

    public PlansDto(int id,String title, String content, String image, String kanbanList, List<TagsDto> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.kanbanList = kanbanList;
        this.tags = tags;
    }

}
