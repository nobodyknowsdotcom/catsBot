package com.avegus.telegramconnector.service.state;

import com.avegus.telegramconnector.model.User;
import com.avegus.telegramconnector.repo.UserRepo;
import com.avegus.telegramconnector.model.enums.BotState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotStateServiceImpl implements BotStateService {

    private final UserRepo userRepo;

    @Override
    public void updateState(String username, Long userId, BotState botState) {
        var upsatedUser = userRepo.findById(userId)
                .map(foundUser -> {
                    foundUser.setState(botState.name());
                    return foundUser;
                })
                .orElse(User.from(userId, username, botState.name()));
        userRepo.save(upsatedUser);
    }

    @Override
    public Optional<BotState> getCurrentState(Long userId) {
        try {
            return userRepo.findById(userId).map(user -> BotState.valueOf(user.getState()));
        } catch (Exception e) {
            log.error("Cannot fetch status of user {}, ", userId, e);
            return Optional.empty();
        }
    }
}
