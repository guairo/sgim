package com.sena.sgim.controller;

import com.sena.sgim.model.Categoria;
import com.sena.sgim.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    public String listar(Model m) { m.addAttribute("categorias", service.listar()); return "categoria/lista"; }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String nuevo(Model m) { m.addAttribute("categoria", new Categoria()); return "categoria/form"; }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String editar(@PathVariable Long id, Model m) {
        m.addAttribute("categoria", service.obtener(id));
        return "categoria/form";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String guardar(@Valid @ModelAttribute Categoria categoria, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "categoria/form";
        if (categoria.getId() == null) service.crear(categoria); else service.actualizar(categoria.getId(), categoria);
        ra.addFlashAttribute("msg", "Categoria guardada");
        return "redirect:/categorias";
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        service.eliminar(id);
        ra.addFlashAttribute("msg", "Categoria eliminada");
        return "redirect:/categorias";
    }
}
