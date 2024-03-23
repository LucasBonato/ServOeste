package com.sev.oeste.Tecnico.Command;

import org.springframework.http.ResponseEntity;

public interface Command<Entity, T> {
    ResponseEntity<T> execute(Entity entity);
}
