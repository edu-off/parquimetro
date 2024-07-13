package br.com.parquimetro.repository;

import br.com.parquimetro.model.Pagamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends MongoRepository<Pagamento, String> {
}
