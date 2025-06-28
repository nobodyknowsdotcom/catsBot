package com.avegus.catsservice.broker;

import com.avegus.catsservice.model.Cat;
import com.avegus.commons.model.*;
import com.avegus.catsservice.service.CatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatConsumerImpl implements CatConsumer {

    private final CatService catService;
    private final CatProducer catProducer;

    @KafkaListener(topics = "${topics.cat.add}", groupId = "cat-service")
    @Override
    public void onCatAddRequest(AddCatRequest request) {
        log.info("Received cat add request: {}", request);
        catService.addCat(request.getCatName(), request.getFileId(), request.getUsername(), request.getUserId());
    }

    @KafkaListener(topics = "${topics.cat.like}", groupId = "cat-service")
    @Override
    public void onCatLikeRequest(CatIdWithUserId catIdWithUserId) {
        log.info("Received like request for catId: {}", catIdWithUserId.getCatId());
        catService.likeCat(catIdWithUserId);
        sendRandomCat(catIdWithUserId.getUserId());
    }

    @KafkaListener(topics = "${topics.cat.dislike}", groupId = "cat-service")
    @Override
    public void onCatDislikeRequest(CatIdWithUserId catIdWithUserId) {
        log.info("Received dislike request for catId: {}", catIdWithUserId.getCatId());
        catService.dislikeCat(catIdWithUserId);
        sendRandomCat(catIdWithUserId.getUserId());
    }

    @KafkaListener(topics = "${topics.cat.delete}", groupId = "cat-service")
    @Override
    public void onCatDeleteRequest(CatIdWithUserId request) {
        log.info("Received delete request: {}", request);
        catService.deleteCat(UUID.fromString(request.getCatId()), request.getUserId());
    }

    @KafkaListener(topics = "${topics.cat.request-by-id}", groupId = "cat-service")
    @Override
    public void onUserCatByCatIdRequest(CatIdWithUserId request) {
        log.info("Received get-by-id request: {}", request);
        catService.getCat(UUID.fromString(request.getCatId()))
                .ifPresent(cat -> {
                    var toSend = new CatWithUserId(request.getUserId(), cat.toDto());
                    catProducer.produceUserCat(toSend);
                });
    }

    @KafkaListener(topics = "${topics.cat.request-by-user}", groupId = "cat-service")
    @Override
    public void onCatByUserIdRequest(UserIdDto userId) {
        log.info("Received cats by user request: {}", userId);
        var userCats = catService.listUserCats(userId.getId())
                .stream()
                .map(Cat::toDto)
                .collect(Collectors.toList());
        catProducer.produceUserCats(new CatsWithUserId(userId.getId(), userCats));
    }

    @KafkaListener(topics = "${topics.cat.request-random}", groupId = "cat-service")
    @Override
    public void onAllUserCatsRequest(UserIdDto userId) {
        log.info("Received random cat request: {}", userId);
        sendRandomCat(userId.getId());
    }

    private void sendRandomCat(Long userId) {
        catService.randomCatFor(userId)
                .ifPresentOrElse(randomCat -> {
                    var toSend = new CatWithUserId(userId, randomCat.toDto());
                    catProducer.produceRandomCat(toSend);
                }, () -> catProducer.produceOutOfRandomCats(new UserIdDto(userId)));
    }
}
