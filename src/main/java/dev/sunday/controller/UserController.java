package dev.sunday.controller;
import dev.sunday.DTO.request.EmailDTO;
import dev.sunday.service.EmailSenderService;
import dev.sunday.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final EmailSenderService emailSenderService;

    @PostMapping("/email")
    public void sendEmail(@RequestBody @Valid EmailDTO eMailDTO) {
        emailSenderService.sendEmailAlert(eMailDTO);
    }
}
