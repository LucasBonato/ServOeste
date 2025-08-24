package com.serv.oeste.application.dtos.requests;

import com.serv.oeste.domain.enums.Roles;

public record AuthRegisterRequest(String username, String password, Roles role) { }
