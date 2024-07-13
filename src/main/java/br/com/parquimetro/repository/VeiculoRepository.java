package br.com.parquimetro.repository;

import br.com.parquimetro.model.Veiculo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends MongoRepository<Veiculo, String> {
}
