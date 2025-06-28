package com.avegus.telegramconnector.broker;

import com.avegus.commons.model.*;

public interface KafkaProducerService {
    void sendLikeEvent(CatIdWithUserId catIdWithUserId);
    void sendDislikeEvent(CatIdWithUserId catIdWithUserId);
    void requestRandomCat(UserIdDto userId);
    void requestUserCats(UserIdDto userId);
    void requestUserCat(CatIdWithUserId catsWithUserId);
    void sendDeleteCatRequest(CatIdWithUserId catsWithUserId);
    void sendAddCatRequest(AddCatRequest request);
}
