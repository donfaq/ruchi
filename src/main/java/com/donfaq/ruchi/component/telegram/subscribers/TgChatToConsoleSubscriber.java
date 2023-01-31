package com.donfaq.ruchi.component.telegram.subscribers;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Flow;

public class TgChatToConsoleSubscriber implements Flow.Subscriber<Message> {
    private Flow.Subscription subscription;
    private final Logger log = LoggerFactory.getLogger(TgChatCommandSubscriber.class);

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.debug("TgChatToConsoleSubscriber subscribed");
        subscription.request(1);
    }

    @Override
    public void onNext(Message message) {
        try {
            log.info("Tg: @{}: {} ", message.chat().username(), Optional.of(message.text()).orElse("<EMPTY>"));
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
