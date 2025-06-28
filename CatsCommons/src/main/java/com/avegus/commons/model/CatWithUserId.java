package com.avegus.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatWithUserId {
    private Long userId;
    private CatDto cat;
}
