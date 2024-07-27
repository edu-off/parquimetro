package br.com.parquimetro.service;

import br.com.parquimetro.dto.VeiculoDto;
import br.com.parquimetro.enumerator.StatusAtivacao;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.StatusException;
import br.com.parquimetro.error.exception.TransactionException;
import br.com.parquimetro.model.Veiculo;
import br.com.parquimetro.repository.VeiculoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private CondutorService condutorService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MongoTransactionManager transactionManager;

    public Page<Veiculo> encontraTodos(Pageable pageable) {
        return veiculoRepository.findAll(pageable);
    }

    public Veiculo encontraPorId(String id) {
        Optional<Veiculo> optionalVeiculo = veiculoRepository.findById(id);
        if (optionalVeiculo.isEmpty())
            throw new DocumentNotFoundException("veiculo não existe");
        return optionalVeiculo.get();
    }

    @Transactional
    public VeiculoDto adiciona(String cpf, VeiculoDto dto) {
        Veiculo veiculo = modelMapper.map(dto, Veiculo.class);
        veiculo.setStatus(StatusAtivacao.ATIVO.toString());
        AtomicReference<VeiculoDto> atomicVeiculoDto = new AtomicReference<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                Veiculo newVeiculo = veiculoRepository.save(veiculo);
                condutorService.vinculaVeiculo(cpf, newVeiculo);
                atomicVeiculoDto.set(modelMapper.map(newVeiculo, VeiculoDto.class));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro ao adicionar veiculo: " + e.getMessage());
            }
            return null;
        });

        return atomicVeiculoDto.get();
    }

    @Transactional
    public VeiculoDto atualiza(String cpf, String id, VeiculoDto dto) {
        Veiculo veiculo = encontraPorId(id);
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
        veiculo.setFabricante(dto.getFabricante());
        veiculo.setCor(dto.getCor());
        AtomicReference<VeiculoDto> atomicVeiculoDto = new AtomicReference<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                Veiculo newVeiculo = veiculoRepository.save(veiculo);
                condutorService.atualizaVeiculo(cpf, newVeiculo);
                atomicVeiculoDto.set(modelMapper.map(newVeiculo, VeiculoDto.class));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro ao atualizar veiculo");
            }
            return null;
        });

        return atomicVeiculoDto.get();
    }

    @Transactional
    public void ativa(String cpf, String id) {
        Veiculo veiculo = encontraPorId(id);
        if (veiculo.getStatus().equalsIgnoreCase(StatusAtivacao.ATIVO.toString()))
            throw new StatusException("veiculo já está ativo");
        veiculo.setStatus(StatusAtivacao.ATIVO.toString());

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                condutorService.atualizaVeiculo(cpf, veiculoRepository.save(veiculo));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro ao ativar veiculo");
            }
            return null;
        });
    }

    @Transactional
    public void inativa(String cpf, String id) {
        Veiculo veiculo = encontraPorId(id);
        if (veiculo.getStatus().equalsIgnoreCase(StatusAtivacao.INATIVO.toString()))
            throw new StatusException("veiculo já está inativo");
        veiculo.setStatus(StatusAtivacao.INATIVO.toString());

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                condutorService.atualizaVeiculo(cpf, veiculoRepository.save(veiculo));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new TransactionException("erro ao inativar veiculo");
            }
            return null;
        });
    }

}
