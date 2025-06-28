package com.avegus.catsservice.service;

import com.avegus.catsservice.model.Cat;
import com.avegus.catsservice.model.CatView;
import com.avegus.catsservice.model.id.CatId2UserId;
import com.avegus.catsservice.repo.CatRepo;
import com.avegus.catsservice.repo.CatViewRepo;
import com.avegus.commons.model.CatIdWithUserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CatServiceImpl implements CatService {

    private final CatRepo catRepo;
    private final CatViewRepo catViewRepo;

    @Override
    public void addCat(String name, String fileId, String creatorUsername, Long creatorId) {
        if (!name.isEmpty() && !fileId.isEmpty()) {
            var cat = Cat.create(name, fileId, creatorUsername, creatorId);
            log.info("Added cat: {}", cat);
            catRepo.save(cat);
        } else {
            log.error("Empty name/file id");
        }
    }

    @Override
    public List<Cat> listUserCats(Long creatorId) {
        return catRepo.findAllByCreatorId(creatorId);
    }

    @Override
    public Optional<Cat> getCat(UUID catId) {
        return catRepo.findById(catId);
    }

    @Override
    public void deleteCat(UUID catId, Long whoRequestedId) {
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    if (cat.getCreatorId().equals(whoRequestedId)) {
                        catRepo.deleteById(catId);
                    } else {
                        log.warn("User {} tried to delete cat {} without his ownership!", whoRequestedId, catId);
                    }
                });
    }

    @Transactional
    @Override
    public void likeCat(CatIdWithUserId catIdWithUserId) {
        var catId = UUID.fromString(catIdWithUserId.getCatId());
        // Set like, unset dislike if was set earlier
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    manage4Like(cat, catIdWithUserId.getUserId());
                });
    }

    private void manage4Like(Cat cat, Long viewer) {
        var maybeViewInfo = catViewRepo.findById(new CatId2UserId(viewer, cat.getId()));

        maybeViewInfo.ifPresentOrElse(viewInfo -> {

            if (!viewInfo.getIsLike()) {
                cat.setDislikesCount(cat.getDislikesCount() - 1);
                cat.setLikesCount(cat.getLikesCount() + 1);
            }

            viewInfo.setIsLike(true);
            actualizeAndSave(viewInfo);
        }, () -> {
            var newViewInfo = new CatView(viewer, cat.getId(), true, 1L);
            catViewRepo.save(newViewInfo);
        });

        catRepo.save(cat);
    }

    @Transactional
    @Override
    public void dislikeCat(CatIdWithUserId catIdWithUserId) {
        var catId = UUID.fromString(catIdWithUserId.getCatId());
        // Set dislike, unset like if was set earlier
        catRepo.findById(catId)
                .ifPresent(cat -> {
                    manage4Dislike(cat, catIdWithUserId.getUserId());
                });
    }

    private void manage4Dislike(Cat cat, Long viewer) {
        var maybeViewInfo = catViewRepo.findById(new CatId2UserId(viewer, cat.getId()));

        maybeViewInfo.ifPresentOrElse(viewInfo -> {

            if (viewInfo.getIsLike()) {
                cat.setDislikesCount(cat.getDislikesCount() + 1);
                cat.setLikesCount(cat.getLikesCount() - 1);
            }

            viewInfo.setIsLike(false);
            actualizeAndSave(viewInfo);
        }, () -> {
            var newViewInfo = new CatView(viewer, cat.getId(), false, 1L);
            catViewRepo.save(newViewInfo);
        });

        catRepo.save(cat);
    }

    @Override
    public Optional<Cat> randomCatFor(Long whoRequested) {
        var viewedCatId2ViewInfo = catViewRepo.findAllById_UserId(whoRequested)
                .stream()
                .collect(Collectors.toMap(catView -> catView.getId().getCatId(), Function.identity()));

        // Empty viewedCatsIds on findAllByCreatorIdNotAndIdNotIn gives always empty resultSet .-.
        List<Cat> cats;
        if (!viewedCatId2ViewInfo.isEmpty()) {
            cats = catRepo.findAllByCreatorIdNotAndIdNotIn(whoRequested, viewedCatId2ViewInfo.keySet().stream().toList());
        } else {
            cats = catRepo.findAllByCreatorIdNot(whoRequested);
        }

        // If there is any non-viewed cats
        if (!cats.isEmpty()) {
            return cats.stream().findFirst();
        }

        // Get cat with min view count and return, if present
        // Do not forget to upd counter
        return viewedCatId2ViewInfo.values().stream()
                .min(Comparator.comparing(CatView::getViewedTimes))
                .flatMap(catInfo -> catRepo.findById(catInfo.getId().getCatId()))
                .map(foundRareCat -> {
                    var viewInfo2Update = viewedCatId2ViewInfo.get(foundRareCat.getId());
                    actualizeAndSave(viewInfo2Update);

                    return foundRareCat;
                });
    }

    private void actualizeAndSave(CatView viewInfo) {
        viewInfo.setViewedAt(LocalDateTime.now());
        viewInfo.setViewedTimes(viewInfo.getViewedTimes() + 1);
        catViewRepo.save(viewInfo);
    }
}
