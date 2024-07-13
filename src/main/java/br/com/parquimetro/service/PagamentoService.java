package br.com.parquimetro.service;

import br.com.parquimetro.enumerator.StatusPagamento;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.model.MetodoPagamento;
import br.com.parquimetro.model.Pagamento;
import br.com.parquimetro.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Transactional
    protected Pagamento registra(Double valor) {
        Pagamento pagamento = Pagamento.builder().status(StatusPagamento.PENDENTE.toString()).valor(valor).build();
        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    protected Pagamento efetiva(String pagamentoId, MetodoPagamento metodoPagamento) {
        Pagamento pagamento = encontraPorId(pagamentoId);
        pagamento.setStatus(StatusPagamento.PAGO.toString());
        pagamento.setMetodoPagamento(metodoPagamento);
        return pagamentoRepository.save(pagamento);
    }

    private Pagamento encontraPorId(String id) {
        Optional<Pagamento> optionalPagamento = pagamentoRepository.findById(id);
        if (optionalPagamento.isEmpty())
            throw new DocumentNotFoundException("pagamento não existe");
        return optionalPagamento.get();
    }

}
