package com.sena.sgim.controller;

import com.sena.sgim.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auditoria")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService service;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page, Model m) {
        m.addAttribute("registros", service.listar(PageRequest.of(page, 20)));
        return "auditoria/lista";
    }
}
