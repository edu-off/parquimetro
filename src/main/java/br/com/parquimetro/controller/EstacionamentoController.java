package br.com.parquimetro.controller;

import br.com.parquimetro.dto.EstacionamentoDto;
import br.com.parquimetro.model.Estacionamento;
import br.com.parquimetro.service.EstacionamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estacionamento")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;

    @GetMapping("/encontra-todos")
    public ResponseEntity<Page<Estacionamento>> encontraTodos(Pageable pageable) {
        return ResponseEntity.ok().body(estacionamentoService.encontraTodos(pageable));
    }

    @GetMapping("/encontra-por-id/{id}")
    public ResponseEntity<Estacionamento> encontraPorId(@PathVariable String id) {
        return ResponseEntity.ok().body(estacionamentoService.encontraPorId(id));
    }

    @GetMapping("/encontra-por-parte-nome-condutor")
    public ResponseEntity<List<Estacionamento>> encontraPorParteDoNomeCondutor(@RequestParam String termo) {
        return ResponseEntity.ok().body(estacionamentoService.encontraPorParteDoNomeCondutor(termo));
    }

    @GetMapping("/encontra-por-email-condutor")
    public ResponseEntity<List<Estacionamento>> encontraPorEmailCondutor(@RequestParam String email) {
        return ResponseEntity.ok().body(estacionamentoService.encontraPorEmailCondutor(email));
    }

    @PostMapping("/registra-entrada/{cpf}")
    public ResponseEntity<EstacionamentoDto> registraEntrada(@PathVariable String cpf, @RequestParam String veiculoId, @Valid @RequestBody EstacionamentoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estacionamentoService.registraEntrada(cpf, veiculoId, dto));
    }

    @PutMapping("/registra-saida/{id}")
    public ResponseEntity<EstacionamentoDto> registraSaida(@PathVariable String id) {
        return ResponseEntity.ok().body(estacionamentoService.registraSaida(id));
    }

    @PutMapping("/efetiva-pagamento/{id}")
    public ResponseEntity<EstacionamentoDto> efetivaPagamento(@PathVariable String id, @RequestParam String metodoPagamentoId) {
        return ResponseEntity.ok().body(estacionamentoService.efetivaPagamento(id, metodoPagamentoId));
    }

}
