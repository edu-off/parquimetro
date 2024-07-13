package br.com.parquimetro.service;

import br.com.parquimetro.dto.DadosCartaoDto;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.model.DadosCartao;
import br.com.parquimetro.repository.DadosCartaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DadosCartaoService {

    @Autowired
    private DadosCartaoRepository dadosCartaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    protected DadosCartao adiciona(DadosCartaoDto dto) {
        DadosCartao dadosCartao = modelMapper.map(dto, DadosCartao.class);
        return dadosCartaoRepository.save(dadosCartao);
    }

    @Transactional
    protected DadosCartao atualiza(String id, DadosCartaoDto dto) {
        Optional<DadosCartao> optionalDadosCartao = dadosCartaoRepository.findById(id);
        if (optionalDadosCartao.isEmpty())
            throw new DocumentNotFoundException("cartão não existe");
        DadosCartao dadosCartao = optionalDadosCartao.get();
        dadosCartao.setNumero(dto.getNumero());
        dadosCartao.setNomeCompleto(dto.getNomeCompleto());
        dadosCartao.setDataVencimento(dto.getDataVencimento());
        dadosCartao.setCodigoSeguranca(dto.getCodigoSeguranca());
        return dadosCartaoRepository.save(dadosCartao);
    }

}
