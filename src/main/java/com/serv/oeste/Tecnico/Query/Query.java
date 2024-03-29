package com.serv.oeste.Tecnico.Query;

import org.springframework.http.ResponseEntity;

public interface Query<Input, Output> {
    ResponseEntity<Output> execute(Input input);
}
