package com.webbats.payment.broker.common.service;

import com.webbats.payment.broker.common.entity.Language;
import com.webbats.payment.broker.common.repo.LanguageRepository;
import com.webbats.payment.broker.common.response.LanguageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    public List<LanguageDTO> getLanguages() {
        List<Language> languageList = languageRepository.findAll();
        return languageList.stream().map(this::convertToDto).toList();
    }

    private LanguageDTO convertToDto(Language language) {
        return LanguageDTO.builder()
                .code(language.getLangCode())
                .name(language.getLangName())
                .build();
    }

}
