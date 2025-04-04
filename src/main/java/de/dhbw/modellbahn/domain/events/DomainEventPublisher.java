package de.dhbw.modellbahn.domain.events;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DomainEventPublisher {
    private static final Logger logger = Logger.getLogger(DomainEventPublisher.class.getName());
    private static final DomainEventPublisher instance = new DomainEventPublisher();
    private final Map<Class<? extends DomainEvent>, List<EventSubscriber>> subscribers = new ConcurrentHashMap<>();

    public static DomainEventPublisher getInstance() {
        return instance;
    }

    public <T extends DomainEvent> void publish(T event) {
        List<EventSubscriber> eventSubscribers = subscribers.getOrDefault(event.getClass(), Collections.emptyList());
        for (EventSubscriber subscriber : eventSubscribers) {
            try {
                subscriber.handleEvent(event);
            } catch (Exception e) {
                logger.warning("Error delivering event: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public <T extends DomainEvent> void subscribe(Class<T> eventType, Consumer<T> subscriber) {
        TypedEventSubscriber<T> wrapper = new TypedEventSubscriber<>(eventType, subscriber);
        subscribers.computeIfAbsent(eventType, _ -> new CopyOnWriteArrayList<>()).add(wrapper);
    }

    public <T extends DomainEvent> void unsubscribe(Class<T> eventType, Consumer<T> subscriber) {
        List<EventSubscriber> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers != null) {
            eventSubscribers.removeIf(wrapper -> wrapper.matches(subscriber));
        }
    }

    // Interface to handle events with type erasure
    private interface EventSubscriber {
        void handleEvent(DomainEvent event);

        boolean matches(Object subscriber);
    }

    private record TypedEventSubscriber<T extends DomainEvent>(Class<T> eventType,
                                                               Consumer<T> subscriber) implements EventSubscriber {
        @Override
        public void handleEvent(DomainEvent event) {
            if (eventType.isInstance(event)) {
                // Safe cast because we've verified the type
                T typedEvent = eventType.cast(event);
                subscriber.accept(typedEvent);
            }
        }

        @Override
        public boolean matches(Object other) {
            return other == subscriber;
        }
    }
}