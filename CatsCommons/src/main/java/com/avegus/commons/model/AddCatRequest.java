package com.avegus.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCatRequest {
    private Long userId;
    private String username;
    private String catName;
    private String fileId;
}
