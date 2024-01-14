package com.example.SpringVue.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class TagsDto {

    private int id;

    private String name;

    private String color;

    private boolean created = false;

    public TagsDto(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

}
