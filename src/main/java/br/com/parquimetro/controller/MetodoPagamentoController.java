package br.com.parquimetro.controller;

import br.com.parquimetro.dto.MetodoPagamentoDto;
import br.com.parquimetro.model.MetodoPagamento;
import br.com.parquimetro.service.MetodoPagamentoService;
import br.com.parquimetro.validation.group.DadosCartaoNotRequiredGroup;
import br.com.parquimetro.validation.group.DadosCartaoRequiredGroup;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metodo-pagamento")
public class MetodoPagamentoController {

    @Autowired
    private MetodoPagamentoService metodoPagamentoService;

    @GetMapping("/encontra-por-id/{id}")
    public ResponseEntity<MetodoPagamento> encontraPorId(@PathVariable String id) {
        return ResponseEntity.ok().body(metodoPagamentoService.encontraPorId(id));
    }

    @PostMapping("/adiciona-sem-dados-cartao/{cpf}")
    public ResponseEntity<MetodoPagamentoDto> adicionaSemDadosCartao(@PathVariable String cpf, @Validated(DadosCartaoNotRequiredGroup.class) @RequestBody MetodoPagamentoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagamentoService.adiciona(cpf, dto));
    }

    @PostMapping("/adiciona-com-dados-cartao/{cpf}")
    public ResponseEntity<MetodoPagamentoDto> adicionaComDadosCartao(@PathVariable String cpf, @Validated(DadosCartaoRequiredGroup.class) @RequestBody MetodoPagamentoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(metodoPagamentoService.adiciona(cpf, dto));
    }

    @PutMapping("/atualiza-com-dados-cartao/{cpf}")
    public ResponseEntity<MetodoPagamentoDto> atualizaComDadosCartao(@PathVariable String cpf, @RequestParam String id, @Validated(DadosCartaoRequiredGroup.class) @RequestBody MetodoPagamentoDto dto) {
        return ResponseEntity.ok().body(metodoPagamentoService.atualiza(cpf, id, dto));
    }

    @PatchMapping("/ativa/{cpf}")
    public ResponseEntity<String> ativa(@PathVariable String cpf, @RequestParam String id) {
        metodoPagamentoService.ativa(cpf, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/inativa/{cpf}")
    public ResponseEntity<String> inativa(@PathVariable String cpf, @RequestParam String id) {
        metodoPagamentoService.inativa(cpf, id);
        return ResponseEntity.ok().build();
    }

}
