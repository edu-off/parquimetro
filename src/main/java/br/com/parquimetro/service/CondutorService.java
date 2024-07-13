package br.com.parquimetro.service;

import br.com.parquimetro.dto.CondutorDto;
import br.com.parquimetro.dto.EnderecoDto;
import br.com.parquimetro.enumerator.StatusAtivacao;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.DuplicateEmailException;
import br.com.parquimetro.error.exception.StatusException;
import br.com.parquimetro.model.Condutor;
import br.com.parquimetro.model.MetodoPagamento;
import br.com.parquimetro.model.Veiculo;
import br.com.parquimetro.repository.CondutorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ModelMapper modelMapper;

    public Page<Condutor> encontraTodos(Pageable pageable) {
        return condutorRepository.findAll(pageable);
    }

    public Condutor encontraPorId(String cpf) {
        Optional<Condutor> optionalCondutor = condutorRepository.findById(cpf);
        if (optionalCondutor.isEmpty())
            throw new DocumentNotFoundException("condutor não existe");
        return optionalCondutor.get();
    }

    public List<Condutor> encontraPorParteDoNome(String termo) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(termo);
        Query query = TextQuery.queryText(criteria).sortByScore();
        List<Condutor> condutores = mongoTemplate.find(query, Condutor.class);
        if (condutores.isEmpty())
            throw new DocumentNotFoundException("não existem condutores para este termo");
        return condutores;
    }

    public Condutor encontraPorEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Condutor condutor = mongoTemplate.findOne(query, Condutor.class);
        if (Objects.isNull(condutor))
            throw new DocumentNotFoundException("não existe condutor com este e-mail");
        return condutor;
    }

    @Transactional
    public CondutorDto adiciona(CondutorDto dto) {
        Condutor condutor = modelMapper.map(dto, Condutor.class);
        condutor.setStatus(StatusAtivacao.ATIVO.toString());
        condutor.setEndereco(enderecoService.adiciona(dto.getEndereco()));
        condutor = condutorRepository.save(condutor);
        dto = modelMapper.map(condutor, CondutorDto.class);
        dto.setEndereco(modelMapper.map(condutor.getEndereco(), EnderecoDto.class));
        return dto;
    }

    @Transactional
    public CondutorDto atualiza(CondutorDto dto) {
        Condutor condutor = encontraPorId(dto.getCpf());
        if (!dto.getEmail().equals(condutor.getEmail())) {
            if (condutorRepository.existsByEmail(dto.getEmail()))
                throw new DuplicateEmailException("email inserido já existe");
        }
        condutor.setNome(dto.getNome());
        condutor.setEmail(dto.getEmail());
        condutor.setDdd(dto.getDdd());
        condutor.setTelefone(dto.getTelefone());
        condutor.setEndereco(enderecoService.atualiza(condutor.getEndereco().getId(), dto.getEndereco()));
        condutor = condutorRepository.save(condutor);
        dto = modelMapper.map(condutor, CondutorDto.class);
        dto.setEndereco(modelMapper.map(condutor.getEndereco(), EnderecoDto.class));
        return dto;
    }

    @Transactional
    public void ativa(String cpf) {
        Condutor condutor = encontraPorId(cpf);
        if (condutor.getStatus().equalsIgnoreCase(StatusAtivacao.ATIVO.toString()))
            throw new StatusException("condutor já está ativo");
        condutor.setStatus(StatusAtivacao.ATIVO.toString());
        condutorRepository.save(condutor);
    }

    @Transactional
    public void inativa(String cpf) {
        Condutor condutor = encontraPorId(cpf);
        if (condutor.getStatus().equalsIgnoreCase(StatusAtivacao.INATIVO.toString()))
            throw new StatusException("condutor já está inativo");
        condutor.setStatus(StatusAtivacao.INATIVO.toString());
        condutorRepository.save(condutor);
    }

    @Transactional
    protected void vinculaVeiculo(String cpf, Veiculo veiculo) {
        Condutor condutor = encontraPorId(cpf);
        condutor.getVeiculos().add(veiculo);
        condutorRepository.save(condutor);
    }

    @Transactional
    protected void atualizaVeiculo(String cpf, Veiculo veiculo) {
        Condutor condutor = encontraPorId(cpf);
        AtomicBoolean isFind = new AtomicBoolean(false);
        condutor.getVeiculos().stream().filter(item -> item.getId().equals(veiculo.getId())).forEach(item -> {
            item.setStatus(veiculo.getStatus());
            item.setPlaca(veiculo.getPlaca());
            item.setModelo(veiculo.getModelo());
            item.setAno(veiculo.getAno());
            item.setFabricante(veiculo.getFabricante());
            item.setCor(veiculo.getCor());
            isFind.set(true);
        });
        if (!isFind.get())
            condutor.getVeiculos().add(veiculo);
        condutorRepository.save(condutor);
    }

    @Transactional
    protected void vinculaMetodoPagamento(String cpf, MetodoPagamento metodoPagamento) {
        Condutor condutor = encontraPorId(cpf);
        condutor.getMetodosPagamento().add(metodoPagamento);
        condutorRepository.save(condutor);
    }

    @Transactional
    protected void atualizaMetodoPagamento(String cpf, MetodoPagamento metodoPagamento) {
        Condutor condutor = encontraPorId(cpf);
        AtomicBoolean isFind = new AtomicBoolean(false);
        condutor.getMetodosPagamento().stream().filter(item -> item.getId().equals(metodoPagamento.getId())).forEach(item -> {
            item.setStatus(metodoPagamento.getStatus());
            item.setMetodo(metodoPagamento.getMetodo());
            item.getDadosCartao().setNumero(metodoPagamento.getDadosCartao().getNumero());
            item.getDadosCartao().setNomeCompleto(metodoPagamento.getDadosCartao().getNomeCompleto());
            item.getDadosCartao().setDataVencimento(metodoPagamento.getDadosCartao().getDataVencimento());
            item.getDadosCartao().setCodigoSeguranca(metodoPagamento.getDadosCartao().getCodigoSeguranca());
            isFind.set(true);
        });
        if (!isFind.get())
            condutor.getMetodosPagamento().add(metodoPagamento);
        condutorRepository.save(condutor);
    }

}
