package br.com.parquimetro.repository;

import br.com.parquimetro.model.MetodoPagamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagamentoRepository extends MongoRepository<MetodoPagamento, String> {
}
