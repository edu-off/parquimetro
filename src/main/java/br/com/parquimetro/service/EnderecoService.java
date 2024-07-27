package br.com.parquimetro.service;

import br.com.parquimetro.dto.EnderecoDto;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.model.Endereco;
import br.com.parquimetro.repository.EnderecoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    protected Endereco adiciona(EnderecoDto dto) throws Exception {
        Endereco endereco = modelMapper.map(dto, Endereco.class);
        return enderecoRepository.save(endereco);
    }

    @Transactional
    protected Endereco atualiza(String id, EnderecoDto dto) throws Exception {
        Optional<Endereco> optionalEndereco = enderecoRepository.findById(id);
        if (optionalEndereco.isEmpty())
            throw new DocumentNotFoundException("endereço não existe");
        Endereco endereco = optionalEndereco.get();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        endereco.setCep(dto.getCep());
        return enderecoRepository.save(endereco);
    }

}
