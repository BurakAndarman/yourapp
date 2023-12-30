package com.example.SpringVue.Dto.Request;

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
public class SavePlansRequest {

    private String title;

    private String content;

    private List<String> tags;

    private KanbanList kanbanList;

}
