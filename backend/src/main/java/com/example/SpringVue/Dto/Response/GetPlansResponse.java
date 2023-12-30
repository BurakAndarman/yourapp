package com.example.SpringVue.Dto.Response;

import com.example.SpringVue.Dto.PlansDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class GetPlansResponse {

    private List<PlansDto> todo;

    private List<PlansDto> thisWeek;

    private List<PlansDto> today;

    private List<PlansDto> done;

}
