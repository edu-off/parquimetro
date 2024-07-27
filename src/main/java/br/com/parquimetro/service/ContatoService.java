package br.com.parquimetro.service;

import br.com.parquimetro.dto.ContatoDto;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.DuplicateEmailException;
import br.com.parquimetro.model.Contato;
import br.com.parquimetro.repository.ContatoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    protected Contato adiciona(ContatoDto dto) throws Exception {
        Contato contato = modelMapper.map(dto, Contato.class);
        return contatoRepository.save(contato);
    }

    @Transactional
    protected Contato atualiza(ContatoDto dto) throws Exception {
        Optional<Contato> optionalContato = contatoRepository.findById(dto.getEmail());
        if (optionalContato.isEmpty())
            throw new DocumentNotFoundException("email não existe");
        Contato contato = optionalContato.get();
        contato.setTelefones(modelMapper.map(dto, Contato.class).getTelefones());
        return contatoRepository.save(contato);
    }

}
