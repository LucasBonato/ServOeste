package com.serv.oeste.Tecnico.Command;

import org.springframework.http.ResponseEntity;

public interface Command<Entity, T> {
    ResponseEntity<T> execute(Entity entity);
}
