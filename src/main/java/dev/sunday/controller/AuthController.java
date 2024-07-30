package dev.sunday.controller;

import dev.sunday.DTO.request.EmailDTO;
import dev.sunday.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final EmailSenderService emailSenderService;


}
