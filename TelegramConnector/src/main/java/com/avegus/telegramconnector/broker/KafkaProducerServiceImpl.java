package com.avegus.telegramconnector.broker;

import com.avegus.commons.model.AddCatRequest;
import com.avegus.commons.model.CatIdDto;
import com.avegus.commons.model.CatIdWithUserId;
import com.avegus.commons.model.UserIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topics.cat.add}")
    private String addCatTopic;

    @Value("${topics.cat.like}")
    private String likeCatTopic;

    @Value("${topics.cat.dislike}")
    private String dislikeCatTopic;

    @Value("${topics.cat.delete}")
    private String deleteCatTopic;

    @Value("${topics.cat.request-by-id}")
    private String requestByIdCatTopic;

    @Value("${topics.cat.request-by-user}")
    private String requestAllByUserCatTopic;

    @Value("${topics.cat.request-random}")
    private String requestRandomCatTopic;

    @Override
    public void sendLikeEvent(CatIdWithUserId catIdWithUserId) {
        log.info("Sending like event for {}", catIdWithUserId.getCatId());
        kafkaTemplate.send(likeCatTopic, catIdWithUserId);
    }

    @Override
    public void sendDislikeEvent(CatIdWithUserId catIdWithUserId) {
        log.info("Sending dislike event for {}", catIdWithUserId.getCatId());
        kafkaTemplate.send(dislikeCatTopic, catIdWithUserId);
    }

    @Override
    public void requestRandomCat(UserIdDto userId) {
        log.info("Sending random cat event for {}", userId.getId());
        kafkaTemplate.send(requestRandomCatTopic, userId);
    }

    @Override
    public void requestUserCats(UserIdDto userId) {
        log.info("Sending all user cats event for {}", userId.getId());
        kafkaTemplate.send(requestAllByUserCatTopic, userId);
    }

    @Override
    public void requestUserCat(CatIdWithUserId catWithUserId) {
        log.info("Sending request user cat event for {}", catWithUserId.getUserId());
        kafkaTemplate.send(requestByIdCatTopic, catWithUserId);
    }

    @Override
    public void sendDeleteCatRequest(CatIdWithUserId catWithUserId) {
        log.info("Sending delete event for {}", catWithUserId.getUserId());
        kafkaTemplate.send(deleteCatTopic, catWithUserId);
    }

    @Override
    public void sendAddCatRequest(AddCatRequest request) {
        log.info("Sending add event for {}", request.getUserId());
        kafkaTemplate.send(addCatTopic, request);
    }
}
