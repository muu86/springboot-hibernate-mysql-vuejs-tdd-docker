package com.mj.taskagile.domain.common.event;

public interface DomainEventPublisher {
    
    void publish(DomainEvent event);
}
