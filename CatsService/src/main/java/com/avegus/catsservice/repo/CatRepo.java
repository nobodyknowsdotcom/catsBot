package com.avegus.catsservice.repo;

import com.avegus.catsservice.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatRepo extends JpaRepository<Cat, UUID> {

    List<Cat> findAllByCreatorId(Long creatorId);

    List<Cat> findAllByCreatorIdNotAndIdNotIn(Long creatorId, List<UUID> ids2Exclude);
    List<Cat> findAllByCreatorIdNot(Long creatorId);
}
