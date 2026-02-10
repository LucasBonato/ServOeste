package com.serv.oeste.presentation.swagger;

import java.util.List;
import java.util.Map;

public record DummyResponse(
        String type,
        String title,
        Integer status,
        String detail,
        Map<String, List<String>> error
) { }
