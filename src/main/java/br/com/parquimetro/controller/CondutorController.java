package br.com.parquimetro.controller;

import br.com.parquimetro.dto.CondutorDto;
import br.com.parquimetro.model.Condutor;
import br.com.parquimetro.service.CondutorService;
import br.com.parquimetro.validation.group.AdicaoGroup;
import br.com.parquimetro.validation.group.AtualizacaoGroup;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/condutor")
public class CondutorController {

    @Autowired
    private CondutorService condutorService;

    @GetMapping("/encontra-todos")
    public ResponseEntity<Page<Condutor>> encontraTodos(Pageable pageable) {
        return ResponseEntity.ok().body(condutorService.encontraTodos(pageable));
    }

    @GetMapping("/encontra-por-cpf/{cpf}")
    public ResponseEntity<Condutor> encontraPorId(@PathVariable String cpf) {
        return ResponseEntity.ok().body(condutorService.encontraPorId(cpf));
    }

    @GetMapping("/encontra-por-parte-nome")
    public ResponseEntity<List<Condutor>> encontraPorParteDoNome(@RequestParam String termo) {
        return ResponseEntity.ok().body(condutorService.encontraPorParteDoNome(termo));
    }

    @GetMapping("/encontra-por-email")
    public ResponseEntity<Condutor> encontraPorEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(condutorService.encontraPorEmail(email));
    }

    @PostMapping("/adiciona")
    public ResponseEntity<CondutorDto> adiciona(@Validated(AdicaoGroup.class) @RequestBody CondutorDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(condutorService.adiciona(dto));
    }

    @PutMapping("/atualiza")
    public ResponseEntity<CondutorDto> atualiza(@Validated(AtualizacaoGroup.class) @RequestBody CondutorDto dto) {
        return ResponseEntity.ok().body(condutorService.atualiza(dto));
    }

    @PatchMapping("/ativa/{cpf}")
    public ResponseEntity<String> ativa(@PathVariable String cpf) {
        condutorService.ativa(cpf);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/inativa/{cpf}")
    public ResponseEntity<String> inativa(@PathVariable String cpf) {
        condutorService.inativa(cpf);
        return ResponseEntity.ok().build();
    }

}
