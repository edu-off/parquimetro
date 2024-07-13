package br.com.parquimetro.controller;

import br.com.parquimetro.dto.VeiculoDto;
import br.com.parquimetro.model.Veiculo;
import br.com.parquimetro.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/encontra-todos")
    public ResponseEntity<Page<Veiculo>> encontraTodos(Pageable pageable) {
        return ResponseEntity.ok().body(veiculoService.encontraTodos(pageable));
    }

    @GetMapping("/encontra-por-id/{id}")
    public ResponseEntity<Veiculo> encontraPorId(@PathVariable String id) {
        return ResponseEntity.ok().body(veiculoService.encontraPorId(id));
    }

    @PostMapping("/adiciona/{cpf}")
    public ResponseEntity<VeiculoDto> adiciona(@PathVariable String cpf, @Valid @RequestBody VeiculoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoService.adiciona(cpf, dto));
    }

    @PutMapping("/atualiza/{cpf}")
    public ResponseEntity<VeiculoDto> atualiza(@PathVariable String cpf, @RequestParam String id, @Valid @RequestBody VeiculoDto dto) {
        return ResponseEntity.ok().body(veiculoService.atualiza(cpf, id, dto));
    }

    @PatchMapping("/ativa/{cpf}")
    public ResponseEntity<String> ativa(@PathVariable String cpf, @RequestParam String id) {
        veiculoService.ativa(cpf, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/inativa/{cpf}")
    public ResponseEntity<String> inativa(@PathVariable String cpf, @RequestParam String id) {
        veiculoService.inativa(cpf, id);
        return ResponseEntity.ok().build();
    }

}
