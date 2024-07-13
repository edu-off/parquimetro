package br.com.parquimetro.service;

import br.com.parquimetro.dto.VeiculoDto;
import br.com.parquimetro.enumerator.StatusAtivacao;
import br.com.parquimetro.error.exception.DocumentNotFoundException;
import br.com.parquimetro.error.exception.StatusException;
import br.com.parquimetro.model.Veiculo;
import br.com.parquimetro.repository.VeiculoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private CondutorService condutorService;

    @Autowired
    private ModelMapper modelMapper;

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
        veiculo = veiculoRepository.save(veiculo);
        condutorService.vinculaVeiculo(cpf, veiculo);
        return modelMapper.map(veiculo, VeiculoDto.class);
    }

    @Transactional
    public VeiculoDto atualiza(String cpf, String id, VeiculoDto dto) {
        Veiculo veiculo = encontraPorId(id);
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
        veiculo.setFabricante(dto.getFabricante());
        veiculo.setCor(dto.getCor());
        veiculo = veiculoRepository.save(veiculo);
        condutorService.atualizaVeiculo(cpf, veiculo);
        return modelMapper.map(veiculo, VeiculoDto.class);
    }

    @Transactional
    public void ativa(String cpf, String id) {
        Veiculo veiculo = encontraPorId(id);
        if (veiculo.getStatus().equalsIgnoreCase(StatusAtivacao.ATIVO.toString()))
            throw new StatusException("veiculo já está ativo");
        veiculo.setStatus(StatusAtivacao.ATIVO.toString());
        condutorService.atualizaVeiculo(cpf, veiculoRepository.save(veiculo));
    }

    @Transactional
    public void inativa(String cpf, String id) {
        Veiculo veiculo = encontraPorId(id);
        if (veiculo.getStatus().equalsIgnoreCase(StatusAtivacao.INATIVO.toString()))
            throw new StatusException("veiculo já está inativo");
        veiculo.setStatus(StatusAtivacao.INATIVO.toString());
        condutorService.atualizaVeiculo(cpf, veiculoRepository.save(veiculo));
    }

}
