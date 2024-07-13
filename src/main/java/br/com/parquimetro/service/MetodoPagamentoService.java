package br.com.parquimetro.service;

import br.com.parquimetro.dto.DadosCartaoDto;
import br.com.parquimetro.dto.MetodoPagamentoDto;
import br.com.parquimetro.enumerator.MetodosPagamento;
import br.com.parquimetro.enumerator.StatusAtivacao;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.MetodoPagamentoException;
import br.com.parquimetro.error.exception.StatusException;
import br.com.parquimetro.model.Condutor;
import br.com.parquimetro.model.DadosCartao;
import br.com.parquimetro.model.MetodoPagamento;
import br.com.parquimetro.repository.MetodoPagamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

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

    public MetodoPagamento encontraPorId(String id) {
        Optional<MetodoPagamento> optionalMetodoPagamento = metodoPagamentoRepository.findById(id);
        if (optionalMetodoPagamento.isEmpty())
            throw new DocumentNotFoundException("metodo pagamento não existe");
        return optionalMetodoPagamento.get();
    }

    @Transactional
    public MetodoPagamentoDto adiciona(String cpf, MetodoPagamentoDto dto) {
        Condutor condutor = condutorService.encontraPorId(cpf);
        Optional<MetodoPagamento> optionalCondutor = condutor.getMetodosPagamento().stream().filter(metodo -> metodo.getMetodo().equals(MetodosPagamento.PIX.toString())).findFirst();
        if (dto.getMetodo().equals(MetodosPagamento.PIX.toString()) && optionalCondutor.isPresent())
            throw new MetodoPagamentoException("PIX já incluido");

        MetodoPagamento metodoPagamento = modelMapper.map(dto, MetodoPagamento.class);
        metodoPagamento.setStatus(StatusAtivacao.ATIVO.toString());
        DadosCartao dadosCartao = null;
        if (!Objects.isNull(dto.getDadosCartao())) {
            dadosCartao = dadosCartaoService.adiciona(dto.getDadosCartao());
            metodoPagamento.setDadosCartao(dadosCartao);
        }

        metodoPagamento = metodoPagamentoRepository.save(metodoPagamento);
        condutorService.vinculaMetodoPagamento(cpf, metodoPagamento);
        dto = modelMapper.map(metodoPagamento, MetodoPagamentoDto.class);
        if (!Objects.isNull(dto.getDadosCartao()))
            dto.setDadosCartao(modelMapper.map(dadosCartao, DadosCartaoDto.class));
        return dto;
    }

    @Transactional
    public MetodoPagamentoDto atualiza(String cpf, String id, MetodoPagamentoDto dto) {
        MetodoPagamento metodoPagamento = encontraPorId(id);
        DadosCartao dadosCartao = null;
        if (!Objects.isNull(dto.getDadosCartao())) {
            dadosCartao = dadosCartaoService.atualiza(metodoPagamento.getDadosCartao().getId(), dto.getDadosCartao());
            metodoPagamento.setDadosCartao(dadosCartao);
        }

        metodoPagamento = metodoPagamentoRepository.save(metodoPagamento);
        condutorService.atualizaMetodoPagamento(cpf, metodoPagamento);
        dto = modelMapper.map(metodoPagamento, MetodoPagamentoDto.class);
        if (!Objects.isNull(dto.getDadosCartao()))
            dto.setDadosCartao(modelMapper.map(dadosCartao, DadosCartaoDto.class));
        return dto;
    }

    @Transactional
    public void ativa(String cpf, String id) {
        MetodoPagamento metodoPagamento = encontraPorId(id);
        if (metodoPagamento.getStatus().equalsIgnoreCase(StatusAtivacao.ATIVO.toString()))
            throw new StatusException("metodo pagamento já está ativo");
        metodoPagamento.setStatus(StatusAtivacao.ATIVO.toString());
        condutorService.atualizaMetodoPagamento(cpf, metodoPagamentoRepository.save(metodoPagamento));
    }

    @Transactional
    public void inativa(String cpf, String id) {
        MetodoPagamento metodoPagamento = encontraPorId(id);
        if (metodoPagamento.getStatus().equalsIgnoreCase(StatusAtivacao.INATIVO.toString()))
            throw new StatusException("etodo pagamento já está inativo");
        metodoPagamento.setStatus(StatusAtivacao.INATIVO.toString());
        condutorService.atualizaMetodoPagamento(cpf, metodoPagamentoRepository.save(metodoPagamento));
    }

}
