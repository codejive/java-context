package org.codejive.context.events;

public interface EventListener<T extends Event> {
    void handleEvent(T event);
}
