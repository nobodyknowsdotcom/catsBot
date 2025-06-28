package com.avegus.catsservice.model;

import com.avegus.catsservice.model.id.CatId2UserId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cat_view", schema = "cats")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CatView {

    @EmbeddedId
    private CatId2UserId id;

    @Column(name = "is_like", nullable = false)
    private Boolean isLike;

    @Column(name = "viewed_times", nullable = false)
    private Long viewedTimes;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt = LocalDateTime.now();

    public CatView(Long userId, UUID catId, Boolean isLike, Long viewedTimes) {
        this.id = new CatId2UserId(userId, catId);
        this.isLike = isLike;
        this.viewedTimes = viewedTimes;
    }
}
