package com.main.skillexchangeapi.controller.defined;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "ex", produces = "application/json")
public class ExController {
    @GetMapping("/consult")
    public String errorMapping(@RequestParam String param) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, param);
    }
}
