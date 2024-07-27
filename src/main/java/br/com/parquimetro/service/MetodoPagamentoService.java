package br.com.parquimetro.service;

import br.com.parquimetro.dto.DadosCartaoDto;
import br.com.parquimetro.dto.MetodoPagamentoDto;
import br.com.parquimetro.enumerator.MetodosPagamento;
import br.com.parquimetro.enumerator.StatusAtivacao;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.MetodoPagamentoException;
import br.com.parquimetro.error.exception.StatusException;
import br.com.parquimetro.error.exception.TransactionException;
import br.com.parquimetro.model.Condutor;
import br.com.parquimetro.model.DadosCartao;
import br.com.parquimetro.model.MetodoPagamento;
import br.com.parquimetro.repository.MetodoPagamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MetodoPagamentoService {

    @Autowired
    private MetodoPagamentoRepository metodoPagamentoRepository;

    @Autowired
    private CondutorService condutorService;

    @Autowired
    private DadosCartaoService dadosCartaoService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MongoTransactionManager transactionManager;

    public MetodoPagamento encontraPorId(String id) {
        Optional<MetodoPagamento> optionalMetodoPagamento = metodoPagamentoRepository.findById(id);
        if (optionalMetodoPagamento.isEmpty())
            throw new DocumentNotFoundException("metodo pagamento não existe");
        return optionalMetodoPagamento.get();
    }

    @Transactional
    public MetodoPagamentoDto adiciona(String cpf, MetodoPagamentoDto dto) {
        Condutor condutor = condutorService.encontraPorId(cpf);
        Optional<MetodoPagamento> optionalCondutor = condutor.getMetodosPagamento().stream()
                .filter(metodo -> metodo.getMetodo().equals(MetodosPagamento.PIX.toString()))
                .findFirst();
        if (dto.getMetodo().equals(MetodosPagamento.PIX.toString()) && optionalCondutor.isPresent())
            throw new MetodoPagamentoException("PIX já incluido");

        AtomicReference<MetodoPagamentoDto> metodoPagamentoDto = new AtomicReference<>(dto);
        AtomicReference<MetodoPagamentoDto> resultMetodoPagamentoDto = new AtomicReference<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                MetodoPagamento metodoPagamento = modelMapper.map(metodoPagamentoDto.get(), MetodoPagamento.class);
                metodoPagamento.setStatus(StatusAtivacao.ATIVO.toString());
                DadosCartao dadosCartao = null;
                if (!Objects.isNull(metodoPagamentoDto.get().getDadosCartao())) {
                    dadosCartao = dadosCartaoService.adiciona(metodoPagamentoDto.get().getDadosCartao());
                    metodoPagamento.setDadosCartao(dadosCartao);
                }

                MetodoPagamento newMetodoPagamento = metodoPagamentoRepository.save(metodoPagamento);
                condutorService.vinculaMetodoPagamento(cpf, newMetodoPagamento);
                MetodoPagamentoDto newMetodoPagamentoDto = modelMapper.map(newMetodoPagamento, MetodoPagamentoDto.class);
                if (!Objects.isNull(newMetodoPagamentoDto.getDadosCartao()))
                    newMetodoPagamentoDto.setDadosCartao(modelMapper.map(dadosCartao, DadosCartaoDto.class));
                resultMetodoPagamentoDto.set(newMetodoPagamentoDto);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro na adição de método de pagamento: " + e.getMessage());
            }
            return null;
        });
        return resultMetodoPagamentoDto.get();
    }

    @Transactional
    public MetodoPagamentoDto atualiza(String cpf, String id, MetodoPagamentoDto dto) {
        AtomicReference<MetodoPagamentoDto> metodoPagamentoDto = new AtomicReference<>(dto);
        AtomicReference<MetodoPagamentoDto> resultMetodoPagamentoDto = new AtomicReference<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                MetodoPagamento metodoPagamento = encontraPorId(id);
                DadosCartao dadosCartao = null;
                if (!Objects.isNull(metodoPagamentoDto.get().getDadosCartao())) {
                    dadosCartao = dadosCartaoService.atualiza(metodoPagamento.getDadosCartao().getId(), metodoPagamentoDto.get().getDadosCartao());
                    metodoPagamento.setDadosCartao(dadosCartao);
                }

                MetodoPagamento newMetodoPagamento = metodoPagamentoRepository.save(metodoPagamento);
                condutorService.atualizaMetodoPagamento(cpf, newMetodoPagamento);
                MetodoPagamentoDto newMetodoPagamentoDto = modelMapper.map(newMetodoPagamento, MetodoPagamentoDto.class);
                if (!Objects.isNull(newMetodoPagamentoDto.getDadosCartao()))
                    newMetodoPagamentoDto.setDadosCartao(modelMapper.map(dadosCartao, DadosCartaoDto.class));
                resultMetodoPagamentoDto.set(newMetodoPagamentoDto);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro ao atualizar método de pagamento: " + e.getMessage());
            }
            return null;
        });
        return resultMetodoPagamentoDto.get();
    }

    @Transactional
    public void ativa(String cpf, String id) {
        MetodoPagamento metodoPagamento = encontraPorId(id);
        if (metodoPagamento.getStatus().equalsIgnoreCase(StatusAtivacao.ATIVO.toString()))
            throw new StatusException("metodo pagamento já está ativo");

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                metodoPagamento.setStatus(StatusAtivacao.ATIVO.toString());
                condutorService.atualizaMetodoPagamento(cpf, metodoPagamentoRepository.save(metodoPagamento));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro ao ativar método de pagamento: " + e.getMessage());
            }
            return null;
        });
    }

    @Transactional
    public void inativa(String cpf, String id) {
        MetodoPagamento metodoPagamento = encontraPorId(id);
        if (metodoPagamento.getStatus().equalsIgnoreCase(StatusAtivacao.INATIVO.toString()))
            throw new StatusException("etodo pagamento já está inativo");
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                metodoPagamento.setStatus(StatusAtivacao.INATIVO.toString());
                condutorService.atualizaMetodoPagamento(cpf, metodoPagamentoRepository.save(metodoPagamento));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro ao ativar método de pagamento: " + e.getMessage());
            }
            return null;
        });
    }

}
