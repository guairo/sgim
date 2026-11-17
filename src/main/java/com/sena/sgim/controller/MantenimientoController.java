package com.sena.sgim.controller;

import com.sena.sgim.model.Mantenimiento;
import com.sena.sgim.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mantenimientos")
@RequiredArgsConstructor
public class MantenimientoController {

    private final MantenimientoService service;
    private final BienService bienService;
    private final ProveedorService proveedorService;

    @GetMapping
    public String listar(Model m) { m.addAttribute("mantenimientos", service.listar()); return "mantenimiento/lista"; }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String nuevo(Model m) {
        m.addAttribute("mantenimiento", new Mantenimiento());
        m.addAttribute("tipos", Mantenimiento.Tipo.values());
        m.addAttribute("bienes", bienService.listar(PageRequest.of(0, 1000)).getContent());
        m.addAttribute("proveedores", proveedorService.listarActivos());
        return "mantenimiento/form";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String guardar(@Valid @ModelAttribute Mantenimiento mantenimiento, BindingResult br,
                          @RequestParam Long bienId,
                          @RequestParam(required = false) Long proveedorId,
                          @AuthenticationPrincipal UserDetails user,
                          RedirectAttributes ra) {
        service.programar(bienId, mantenimiento, proveedorId, user.getUsername());
        ra.addFlashAttribute("msg", "Mantenimiento programado");
        return "redirect:/mantenimientos";
    }

    @PostMapping("/iniciar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String iniciar(@PathVariable Long id, @AuthenticationPrincipal UserDetails user,
                          RedirectAttributes ra) {
        service.iniciar(id, user.getUsername());
        ra.addFlashAttribute("msg", "Mantenimiento iniciado");
        return "redirect:/mantenimientos";
    }

    @PostMapping("/finalizar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String finalizar(@PathVariable Long id, @RequestParam(required = false) String observaciones,
                            @AuthenticationPrincipal UserDetails user, RedirectAttributes ra) {
        service.finalizar(id, observaciones, user.getUsername());
        ra.addFlashAttribute("msg", "Mantenimiento finalizado");
        return "redirect:/mantenimientos";
    }
}
