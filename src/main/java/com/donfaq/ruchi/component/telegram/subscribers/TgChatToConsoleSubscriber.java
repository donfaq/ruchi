package com.donfaq.ruchi.component.telegram.subscribers;

import com.pengrad.telegrambot.model.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.Flow;

@Slf4j
public class TgChatToConsoleSubscriber implements Flow.Subscriber<Message> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.debug("TgChatToConsoleSubscriber subscribed");
        subscription.request(1);
    }

    @Override
    public void onNext(Message message) {
        try {
            log.info("Tg: @{}: {} ",
                    message.chat().username(),
                    Optional.of(message.text()).orElse("<EMPTY>")
            );
        } finally {
            subscription.request(1);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurred while processing telegram message", throwable);
    }

    @Override
    public void onComplete() {
        log.debug("TgChatToConsoleSubscriber completed");
    }
}
