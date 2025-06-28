package com.avegus.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CatDto {
    private UUID id;
    private String name;
    private String fileId;
    private Long creatorId;
    private String creatorUsername;
    private Long likesCount;
    private Long dislikesCount;

    public String generateDescription() {
        return String.format("%s\n@%s", name, creatorUsername);
    }

    public String generateDescriptionWithLikes(String likes, String dislikes) {
        return String.format("%s\n@%s\n \uD83D\uDC4D (%s) \t \uD83D\uDC4E (%s)", name, creatorUsername, likes, dislikes);
    }
}
