package com.avegus.catsservice.repo;

import com.avegus.catsservice.model.CatView;
import com.avegus.catsservice.model.id.CatId2UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatViewRepo extends JpaRepository<CatView, CatId2UserId> {

    List<CatView> findAllById_UserId(Long userId);
}
