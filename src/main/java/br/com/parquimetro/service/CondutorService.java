package br.com.parquimetro.service;

import br.com.parquimetro.dto.CondutorDto;
import br.com.parquimetro.dto.ContatoDto;
import br.com.parquimetro.dto.EnderecoDto;
import br.com.parquimetro.enumerator.StatusAtivacao;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.DuplicateEmailException;
import br.com.parquimetro.error.exception.StatusException;
import br.com.parquimetro.error.exception.TransactionException;
import br.com.parquimetro.model.Condutor;
import br.com.parquimetro.model.MetodoPagamento;
import br.com.parquimetro.model.Veiculo;
import br.com.parquimetro.repository.CondutorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ContatoService contatoService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MongoTransactionManager transactionManager;

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
        return mongoTemplate.find(query, Condutor.class);
    }

    public Condutor encontraPorEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("contato.$id").is(email));
        Condutor condutor = mongoTemplate.findOne(query, Condutor.class);
        if (Objects.isNull(condutor))
            throw new DocumentNotFoundException("não existe condutor com este e-mail");
        return condutor;
    }

    @Transactional
    public CondutorDto adiciona(CondutorDto dto) {
        AtomicReference<CondutorDto> atomicCondutorDto = new AtomicReference<>();
        EnderecoDto enderecoDto = dto.getEndereco();
        ContatoDto contatoDto = dto.getContato();
        Condutor condutor = modelMapper.map(dto, Condutor.class);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                condutor.setStatus(StatusAtivacao.ATIVO.toString());
                condutor.setEndereco(enderecoService.adiciona(enderecoDto));
                condutor.setContato(contatoService.adiciona(contatoDto));
                atomicCondutorDto.set(getCondutorDtoFromSavingCollection(condutor));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro na adição do condutor: " + e.getMessage());
            }
            return null;
        });

        return atomicCondutorDto.get();

    }

    @Transactional
    public CondutorDto atualiza(CondutorDto dto) {
        EnderecoDto enderecoDto = dto.getEndereco();
        ContatoDto contatoDto = dto.getContato();
        AtomicReference<CondutorDto> atomicCondutorDto = new AtomicReference<>();
        Condutor condutor = encontraPorId(dto.getCpf());
        condutor.setNome(dto.getNome());
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                condutor.setEndereco(enderecoService.atualiza(condutor.getEndereco().getId(), enderecoDto));
                condutor.setContato(contatoService.atualiza(contatoDto));
                atomicCondutorDto.set(getCondutorDtoFromSavingCollection(condutor));
            } catch(Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro na atualização do condutor: " + e.getMessage());
            }
            return null;
        });

        return atomicCondutorDto.get();
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
    protected void vinculaVeiculo(String cpf, Veiculo veiculo) throws Exception {
        Condutor condutor = encontraPorId(cpf);
        condutor.getVeiculos().add(veiculo);
        condutorRepository.save(condutor);
    }

    @Transactional
    protected void atualizaVeiculo(String cpf, Veiculo veiculo) throws Exception {
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
    protected void vinculaMetodoPagamento(String cpf, MetodoPagamento metodoPagamento) throws Exception {
        Condutor condutor = encontraPorId(cpf);
        condutor.getMetodosPagamento().add(metodoPagamento);
        condutorRepository.save(condutor);
    }

    @Transactional
    protected void atualizaMetodoPagamento(String cpf, MetodoPagamento metodoPagamento) throws Exception {
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

    private CondutorDto getCondutorDtoFromSavingCollection(Condutor condutor) {
        Condutor newCondutor = condutorRepository.save(condutor);
        CondutorDto newCondutorDto = modelMapper.map(newCondutor, CondutorDto.class);
        newCondutorDto.setEndereco(modelMapper.map(newCondutor.getEndereco(), EnderecoDto.class));
        newCondutorDto.setContato(modelMapper.map(newCondutor.getContato(), ContatoDto.class));
        return newCondutorDto;
    }

}
