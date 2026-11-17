package com.sena.sgim.controller;

import com.sena.sgim.model.Bien;
import com.sena.sgim.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bienes")
@RequiredArgsConstructor
public class BienController {

    private final BienService bienService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    private final DependenciaService dependenciaService;
    private final FuncionarioService funcionarioService;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(required = false) String q,
                         Model model) {
        Page<Bien> p = bienService.buscar(q, PageRequest.of(page, 10, Sort.by("id").descending()));
        model.addAttribute("bienes", p);
        model.addAttribute("q", q);
        return "bien/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String nuevo(Model model) {
        model.addAttribute("bien", new Bien());
        cargarCombos(model);
        return "bien/form";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("bien", bienService.obtener(id));
        cargarCombos(model);
        return "bien/form";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String guardar(@Valid @ModelAttribute Bien bien, BindingResult br,
                          @RequestParam Long categoriaId,
                          @RequestParam(required = false) Long proveedorId,
                          @RequestParam Long dependenciaId,
                          @AuthenticationPrincipal UserDetails user,
                          Model model,
                          RedirectAttributes ra) {
        if (br.hasErrors()) {
            cargarCombos(model);
            return "bien/form";
        }
        if (bien.getId() == null) {
            bienService.crear(bien, categoriaId, proveedorId, dependenciaId, user.getUsername());
            ra.addFlashAttribute("msg", "Bien registrado con exito");
        } else {
            bienService.actualizar(bien.getId(), bien, categoriaId, proveedorId, dependenciaId, user.getUsername());
            ra.addFlashAttribute("msg", "Bien actualizado");
        }
        return "redirect:/bienes";
    }

    @GetMapping("/asignar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String formAsignar(@PathVariable Long id, Model model) {
        model.addAttribute("bien", bienService.obtener(id));
        model.addAttribute("funcionarios", funcionarioService.listarActivos());
        return "bien/asignar";
    }

    @PostMapping("/asignar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String asignar(@PathVariable Long id, @RequestParam Long funcionarioId,
                          @AuthenticationPrincipal UserDetails user, RedirectAttributes ra) {
        bienService.asignar(id, funcionarioId, user.getUsername());
        ra.addFlashAttribute("msg", "Bien asignado");
        return "redirect:/bienes";
    }

    @PostMapping("/liberar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String liberar(@PathVariable Long id, @AuthenticationPrincipal UserDetails user,
                          RedirectAttributes ra) {
        bienService.liberar(id, user.getUsername());
        ra.addFlashAttribute("msg", "Bien liberado");
        return "redirect:/bienes";
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, @AuthenticationPrincipal UserDetails user,
                           RedirectAttributes ra) {
        bienService.eliminar(id, user.getUsername());
        ra.addFlashAttribute("msg", "Bien eliminado");
        return "redirect:/bienes";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("dependencias", dependenciaService.listarActivas());
        model.addAttribute("estados", Bien.Estado.values());
    }
}
