package com.webbats.payment.broker.module.common;


import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.common.response.LanguageDTO;
import com.webbats.payment.broker.common.service.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConstants.API_COMMON)
@RequiredArgsConstructor
@Tag(name = "Common Public APIs Controller", description = "Endpoints providing apis for frontend")
public class CommonApiController {

    private final LanguageService languageService;

    @GetMapping("/languages")
    @Operation(summary = "Endpoint for getting languages supported")
    public List<LanguageDTO> getLanguages() {
        return languageService.getLanguages();
    }
}