package com.sena.sgim.controller;

import com.sena.sgim.model.Dependencia;
import com.sena.sgim.service.DependenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dependencias")
@RequiredArgsConstructor
public class DependenciaController {

    private final DependenciaService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("dependencias", service.listar());
        return "dependencia/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String nuevo(Model model) {
        model.addAttribute("dependencia", new Dependencia());
        return "dependencia/form";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("dependencia", service.obtener(id));
        return "dependencia/form";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String guardar(@Valid @ModelAttribute Dependencia dependencia, BindingResult br,
                          @AuthenticationPrincipal UserDetails user, RedirectAttributes ra) {
        if (br.hasErrors()) return "dependencia/form";
        if (dependencia.getId() == null) service.crear(dependencia, user.getUsername());
        else service.actualizar(dependencia.getId(), dependencia, user.getUsername());
        ra.addFlashAttribute("msg", "Dependencia guardada");
        return "redirect:/dependencias";
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, @AuthenticationPrincipal UserDetails user,
                           RedirectAttributes ra) {
        service.eliminar(id, user.getUsername());
        ra.addFlashAttribute("msg", "Dependencia eliminada");
        return "redirect:/dependencias";
    }
}
