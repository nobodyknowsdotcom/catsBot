package com.avegus.catsservice.model.id;

import com.avegus.catsservice.model.Cat;
import com.avegus.commons.model.CatIdWithUserId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class CatId2UserId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "cat_id", nullable = false)
    private UUID catId;

    public static CatId2UserId from(CatIdWithUserId catIdWithUserId) {
        return new CatId2UserId(
                catIdWithUserId.getUserId(),
                UUID.fromString(catIdWithUserId.getCatId())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatId2UserId that = (CatId2UserId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(catId, that.catId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, catId);
    }
}
