package com.sena.sgim.controller;

import com.sena.sgim.model.Proveedor;
import com.sena.sgim.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService service;

    @GetMapping
    public String listar(Model m) { m.addAttribute("proveedores", service.listar()); return "proveedor/lista"; }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String nuevo(Model m) { m.addAttribute("proveedor", new Proveedor()); return "proveedor/form"; }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String editar(@PathVariable Long id, Model m) {
        m.addAttribute("proveedor", service.obtener(id));
        return "proveedor/form";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String guardar(@Valid @ModelAttribute Proveedor proveedor, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) return "proveedor/form";
        if (proveedor.getId() == null) service.crear(proveedor); else service.actualizar(proveedor.getId(), proveedor);
        ra.addFlashAttribute("msg", "Proveedor guardado");
        return "redirect:/proveedores";
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        service.eliminar(id);
        ra.addFlashAttribute("msg", "Proveedor eliminado");
        return "redirect:/proveedores";
    }
}
