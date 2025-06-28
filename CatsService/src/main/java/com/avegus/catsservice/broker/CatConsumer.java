package com.avegus.catsservice.broker;

import com.avegus.commons.model.*;

public interface CatConsumer {
    void onCatAddRequest(AddCatRequest request);
    void onCatLikeRequest(CatIdWithUserId catId);
    void onCatDislikeRequest(CatIdWithUserId catIdWithUserId);
    void onCatDeleteRequest(CatIdWithUserId catIdWithUserId);
    void onUserCatByCatIdRequest(CatIdWithUserId request);
    void onCatByUserIdRequest(UserIdDto userId);
    void onAllUserCatsRequest(UserIdDto userId);
}
