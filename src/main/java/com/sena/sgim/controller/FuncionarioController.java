package com.sena.sgim.controller;

import com.sena.sgim.model.Funcionario;
import com.sena.sgim.service.DependenciaService;
import com.sena.sgim.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService service;
    private final DependenciaService dependenciaService;

    @GetMapping
    public String listar(Model m) { m.addAttribute("funcionarios", service.listar()); return "funcionario/lista"; }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String nuevo(Model m) {
        m.addAttribute("funcionario", new Funcionario());
        m.addAttribute("dependencias", dependenciaService.listarActivas());
        return "funcionario/form";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String editar(@PathVariable Long id, Model m) {
        m.addAttribute("funcionario", service.obtener(id));
        m.addAttribute("dependencias", dependenciaService.listarActivas());
        return "funcionario/form";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    public String guardar(@Valid @ModelAttribute Funcionario funcionario, BindingResult br,
                          @RequestParam Long dependenciaId, Model m, RedirectAttributes ra) {
        if (br.hasErrors()) {
            m.addAttribute("dependencias", dependenciaService.listarActivas());
            return "funcionario/form";
        }
        if (funcionario.getId() == null) service.crear(funcionario, dependenciaId);
        else service.actualizar(funcionario.getId(), funcionario, dependenciaId);
        ra.addFlashAttribute("msg", "Funcionario guardado");
        return "redirect:/funcionarios";
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        service.eliminar(id);
        ra.addFlashAttribute("msg", "Funcionario eliminado");
        return "redirect:/funcionarios";
    }
}
