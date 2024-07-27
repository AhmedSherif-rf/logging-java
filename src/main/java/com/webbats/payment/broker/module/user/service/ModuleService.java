package com.webbats.payment.broker.module.user.service;

import com.webbats.payment.broker.module.user.response.ModuleDTO;

import java.util.List;

public interface ModuleService {
    List<ModuleDTO> getAllModules();
}
