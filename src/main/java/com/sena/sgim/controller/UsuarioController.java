package com.sena.sgim.controller;

import com.sena.sgim.model.UsuarioSistema;
import com.sena.sgim.service.UsuarioSistemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioSistemaService service;

    @GetMapping
    public String listar(Model m) { m.addAttribute("usuarios", service.listar()); return "usuario/lista"; }

    @GetMapping("/nuevo")
    public String nuevo(Model m) {
        m.addAttribute("usuario", new UsuarioSistema());
        m.addAttribute("roles", UsuarioSistema.Rol.values());
        return "usuario/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model m) {
        m.addAttribute("usuario", service.obtener(id));
        m.addAttribute("roles", UsuarioSistema.Rol.values());
        return "usuario/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute UsuarioSistema usuario, BindingResult br,
                          @RequestParam(required = false) String nuevoPassword,
                          Model m, RedirectAttributes ra) {
        if (br.hasErrors()) {
            m.addAttribute("roles", UsuarioSistema.Rol.values());
            return "usuario/form";
        }
        if (usuario.getId() == null) service.crear(usuario, nuevoPassword);
        else service.actualizar(usuario.getId(), usuario, nuevoPassword);
        ra.addFlashAttribute("msg", "Usuario guardado");
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        service.eliminar(id);
        ra.addFlashAttribute("msg", "Usuario eliminado");
        return "redirect:/usuarios";
    }
}
