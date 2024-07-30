package dev.sunday.service;

import dev.sunday.DTO.request.EmailDTO;

public interface EmailSenderService {
    void sendEmailAlert(EmailDTO mailDTO);
}
