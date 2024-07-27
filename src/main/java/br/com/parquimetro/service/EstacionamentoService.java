package br.com.parquimetro.service;

import br.com.parquimetro.dto.*;
import br.com.parquimetro.enumerator.MetodosPagamento;
import br.com.parquimetro.enumerator.ModalidadesEstacionamento;
import br.com.parquimetro.enumerator.StatusEstacionamento;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.EstacionamentoException;
import br.com.parquimetro.error.exception.TransactionException;
import br.com.parquimetro.model.*;
import br.com.parquimetro.repository.EstacionamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Service
public class EstacionamentoService {

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private CondutorService condutorService;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private PrecoService precoService;

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoTransactionManager transactionManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("templatePaymentMessage")
    private SimpleMailMessage paymentMessage;

    public Page<Estacionamento> encontraTodos(Pageable pageable) {
        Sort sort = Sort.by("entrada").descending();
        return estacionamentoRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
    }

    public Estacionamento encontraPorId(String id) {
        Optional<Estacionamento> optionalEstacionamento = estacionamentoRepository.findById(id);
        if (optionalEstacionamento.isEmpty())
            throw new DocumentNotFoundException("estacionamento não existe");
        return optionalEstacionamento.get();
    }

    public List<Estacionamento> encontraPorParteDoNomeCondutor(String termo) {
        Query query = new Query();
        List<Condutor> condutores = condutorService.encontraPorParteDoNome(termo);
        List<String> cpfs = condutores.parallelStream().map(Condutor::getCpf).toList();
        query.addCriteria(Criteria.where("condutor.$id").in(cpfs));
        return mongoTemplate.find(query, Estacionamento.class);
    }

    public List<Estacionamento> encontraPorEmailCondutor(String email) {
        Query query = new Query();
        Condutor condutor = condutorService.encontraPorEmail(email);
        query.addCriteria(Criteria.where("condutor.$id").is(condutor.getCpf()));
        return mongoTemplate.find(query, Estacionamento.class);
    }

    @Transactional
    public EstacionamentoDto registraEntrada(String cpf, String veiculoId, EstacionamentoDto dto) {
        Condutor condutor = condutorService.encontraPorId(cpf);
        Veiculo veiculo = veiculoService.encontraPorId(veiculoId);
        List<Estacionamento> estacionamentos = encontraPorVeiculo(veiculoId);
        Optional<Estacionamento> optional = estacionamentos.parallelStream().filter(estacionamento -> estacionamento.getStatus().equals(StatusEstacionamento.EM_ANDAMENTO.toString())).findFirst();
        if (optional.isPresent())
            throw new EstacionamentoException("veiculo com diária de estacionamento em andamento");

        long plusTime = dto.getModalidade().equals(ModalidadesEstacionamento.TEMPO_FIXO.toString()) ? dto.getTempoDuracao() : 1L;
        Estacionamento estacionamento = modelMapper.map(dto, Estacionamento.class);
        estacionamento.setStatus(StatusEstacionamento.EM_ANDAMENTO.toString());
        estacionamento.setEntrada(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        estacionamento.setSaidaPrevista(estacionamento.getEntrada().plusHours(plusTime));
        estacionamento.setCondutor(condutor);
        estacionamento.setVeiculo(veiculo);
        estacionamento = estacionamentoRepository.save(estacionamento);
        dto = modelMapper.map(estacionamento, EstacionamentoDto.class);
        dto.setCondutor(modelMapper.map(condutor, CondutorDto.class));
        dto.setVeiculo(modelMapper.map(veiculo, VeiculoDto.class));
        return dto;
    }

    @Transactional
    public EstacionamentoDto registraSaida(String id) {
        Estacionamento estacionamento = encontraPorId(id);
        if (!Objects.isNull(estacionamento.getSaida()))
            throw new EstacionamentoException("saída da diária do estacionamento já registrada");
        if (!estacionamento.getStatus().equals(StatusEstacionamento.EM_ANDAMENTO.toString()))
            throw new EstacionamentoException("diaria de estacionamento não está mais em andamento");

        LocalDateTime saida = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        double valor = estacionamento.getModalidade().equals(ModalidadesEstacionamento.TEMPO_FIXO.toString()) ?
                precoService.calculaPrecoPorTempoFixo(estacionamento.getTempoDuracao().longValue(), estacionamento.getEntrada(), saida) :
                precoService.calculaPrecoPorHora(estacionamento.getEntrada(), saida);

        AtomicReference<Estacionamento> atomicEstacionamento = new AtomicReference<>(estacionamento);
        AtomicReference<Estacionamento> atomicResultEstacionamento = new AtomicReference<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                Estacionamento recoverEstacionamento = atomicEstacionamento.get();
                recoverEstacionamento.setStatus(StatusEstacionamento.ENCERRADO_PAGAMENTO_PENDENTE.toString());
                recoverEstacionamento.setSaida(saida);
                recoverEstacionamento.setPagamento(pagamentoService.registra(valor));
                atomicResultEstacionamento.set(estacionamentoRepository.save(estacionamento));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("error ao registrar saída: " + e.getMessage());
            }
            return null;
        });

        EstacionamentoDto dto = modelMapper.map(atomicResultEstacionamento.get(), EstacionamentoDto.class);
        dto.setCondutor(modelMapper.map(estacionamento.getCondutor(), CondutorDto.class));
        dto.setVeiculo(modelMapper.map(estacionamento.getVeiculo(), VeiculoDto.class));
        dto.setPagamento(modelMapper.map(estacionamento.getPagamento(), PagamentoDto.class));
        return dto;
    }

    @Transactional
    public EstacionamentoDto efetivaPagamento(String id, String metodoPagamentoId) {
        Estacionamento estacionamento = encontraPorId(id);
        Optional<MetodoPagamento> optionalMetodoPagamento = estacionamento.getCondutor().getMetodosPagamento().stream().filter(metodo -> metodo.getId().equals(metodoPagamentoId)).findFirst();

        if (estacionamento.getStatus().equals(StatusEstacionamento.ENCERRADO_PAGO.toString()))
            throw new EstacionamentoException("diaria do estacionamento já está paga");
        if (estacionamento.getStatus().equals(StatusEstacionamento.EM_ANDAMENTO.toString()))
            throw new EstacionamentoException("diaria do estacionamento ainda está em andamento");
        if (optionalMetodoPagamento.isEmpty())
            throw new DocumentNotFoundException("metodo de pagamento não encontrado");

        AtomicReference<Estacionamento> atomicEstacionamento = new AtomicReference<>(estacionamento);
        AtomicReference<Estacionamento> atomicResultEstacionamento = new AtomicReference<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                Estacionamento recoverEstacionamento = atomicEstacionamento.get();
                recoverEstacionamento.setPagamento(pagamentoService.efetiva(recoverEstacionamento.getPagamento().getId(), optionalMetodoPagamento.get()));
                recoverEstacionamento.setStatus(StatusEstacionamento.ENCERRADO_PAGO.toString());
                Estacionamento newEstacionamento = estacionamentoRepository.save(recoverEstacionamento);
                atomicResultEstacionamento.set(newEstacionamento);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("error ao efetivar pagamento: " + e.getMessage());
            }
            return null;
        });

        EstacionamentoDto dto = modelMapper.map(atomicResultEstacionamento.get(), EstacionamentoDto.class);
        dto.setCondutor(modelMapper.map(atomicResultEstacionamento.get().getCondutor(), CondutorDto.class));
        dto.setVeiculo(modelMapper.map(atomicResultEstacionamento.get().getVeiculo(), VeiculoDto.class));
        dto.setPagamento(modelMapper.map(atomicResultEstacionamento.get().getPagamento(), PagamentoDto.class));
        dto.getPagamento().setMetodoPagamento(modelMapper.map(atomicResultEstacionamento.get().getPagamento().getMetodoPagamento(), MetodoPagamentoDto.class));
        if (!dto.getPagamento().getMetodoPagamento().getMetodo().equals(MetodosPagamento.PIX.toString()))
            dto.getPagamento().getMetodoPagamento().setDadosCartao(modelMapper.map(atomicResultEstacionamento.get().getPagamento().getMetodoPagamento().getDadosCartao(), DadosCartaoDto.class));

        String texto = String.format(paymentMessage.getText(), atomicResultEstacionamento.get().getPagamento().getValor().toString(),
                atomicResultEstacionamento.get().getEntrada().toString(),
                atomicResultEstacionamento.get().getSaida().toString(),
                atomicResultEstacionamento.get().getCondutor().getNome(),
                atomicResultEstacionamento.get().getVeiculo().getModelo(),
                atomicResultEstacionamento.get().getVeiculo().getPlaca());

        emailService.sendSimpleMessage(paymentMessage, atomicResultEstacionamento.get().getCondutor().getContato().getEmail(), texto);
        return dto;
    }

    @Transactional
    public void verificaStatusEmAndamentoPorModalidade(SimpleMailMessage customMessage, String modalidade) {
        Predicate<Estacionamento> filtroModalidade = estacionamento -> estacionamento.getModalidade().equals(modalidade);
        Predicate<Estacionamento> filtroTempo = estacionamento -> estacionamento.getSaidaPrevista().minusMinutes(15L).isBefore(LocalDateTime.now());
        Page<Estacionamento> estacionamentos = consultaPorStatus(0, StatusEstacionamento.EM_ANDAMENTO.toString());
        int totalPages = estacionamentos.getTotalPages();
        int pageNumber = 1;
        while (pageNumber <= totalPages) {
            estacionamentos.stream().parallel().filter(filtroModalidade.and(filtroTempo)).forEach(estacionamento -> {
                LocalDateTime saidaPrevistaModalidadePorHora = estacionamento.getSaidaPrevista().plusHours(1L);
                LocalDateTime saidaPrevista = estacionamento.getModalidade().equals(ModalidadesEstacionamento.POR_HORA.toString()) ?
                        saidaPrevistaModalidadePorHora :
                        estacionamento.getSaidaPrevista();
                if (estacionamento.getModalidade().equals(ModalidadesEstacionamento.POR_HORA.toString()))
                    atualizaSaidaPrevista(estacionamento, saidaPrevistaModalidadePorHora);
                String texto = String.format(customMessage.getText(), estacionamento.getEntrada().toString(), saidaPrevista.toString());
                emailService.sendSimpleMessage(customMessage, estacionamento.getCondutor().getContato().getEmail(), texto);
            });
            estacionamentos = consultaPorStatus(pageNumber++, StatusEstacionamento.EM_ANDAMENTO.toString());
        }
    }

    private List<Estacionamento> encontraPorVeiculo(String veiculoId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("veiculo.$_id").is(veiculoId));
        return mongoTemplate.find(query, Estacionamento.class);
    }

    private Page<Estacionamento> consultaPorStatus(int pageNumber, String status) {
        Pageable pageable = PageRequest.of(pageNumber, 1000);
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));
        query.with(pageable);
        List<Estacionamento> estacionamentos = mongoTemplate.find(query, Estacionamento.class);
        return PageableExecutionUtils.getPage(estacionamentos, pageable, () -> mongoTemplate.count(query, Estacionamento.class));
    }

    private void atualizaSaidaPrevista(Estacionamento estacionamento, LocalDateTime saidaPrevista) {
        estacionamento.setSaidaPrevista(saidaPrevista);
        mongoTemplate.save(estacionamento);
    }

}
