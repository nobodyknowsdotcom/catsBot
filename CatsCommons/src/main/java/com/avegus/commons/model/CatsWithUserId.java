package com.avegus.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatsWithUserId {
    private Long userId;
    private List<CatDto> cats;
}
