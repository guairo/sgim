package com.sena.sgim.controller;

import com.sena.sgim.model.Movimiento;
import com.sena.sgim.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService service;
    private final BienService bienService;
    private final DependenciaService dependenciaService;
    private final FuncionarioService funcionarioService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page, Model m) {
        m.addAttribute("movimientos", service.listar(PageRequest.of(page, 15)));
        return "movimiento/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String nuevo(Model m) {
        m.addAttribute("tipos", Movimiento.Tipo.values());
        m.addAttribute("dependencias", dependenciaService.listarActivas());
        m.addAttribute("funcionarios", funcionarioService.listarActivos());
        m.addAttribute("bienes", bienService.listar(PageRequest.of(0, 1000)).getContent());
        return "movimiento/form";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String guardar(@RequestParam Movimiento.Tipo tipo,
                          @RequestParam Long bienId,
                          @RequestParam(required = false) Long dependenciaOrigenId,
                          @RequestParam(required = false) Long dependenciaDestinoId,
                          @RequestParam(required = false) Long funcionarioOrigenId,
                          @RequestParam(required = false) Long funcionarioDestinoId,
                          @RequestParam(required = false) String observaciones,
                          @AuthenticationPrincipal UserDetails user,
                          RedirectAttributes ra) {
        service.registrar(tipo, bienId, dependenciaOrigenId, dependenciaDestinoId,
                funcionarioOrigenId, funcionarioDestinoId, observaciones, user.getUsername());
        ra.addFlashAttribute("msg", "Movimiento registrado");
        return "redirect:/movimientos";
    }
}
