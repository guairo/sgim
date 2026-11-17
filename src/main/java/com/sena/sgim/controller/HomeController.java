package com.sena.sgim.controller;

import com.sena.sgim.model.Bien;
import com.sena.sgim.service.BienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BienService bienService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("disponibles", bienService.contarPorEstado(Bien.Estado.DISPONIBLE));
        model.addAttribute("asignados", bienService.contarPorEstado(Bien.Estado.ASIGNADO));
        model.addAttribute("mantenimiento", bienService.contarPorEstado(Bien.Estado.EN_MANTENIMIENTO));
        model.addAttribute("bajas", bienService.contarPorEstado(Bien.Estado.BAJA));
        return "dashboard";
    }
}
