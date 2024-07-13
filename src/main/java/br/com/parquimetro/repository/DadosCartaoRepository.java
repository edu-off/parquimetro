package br.com.parquimetro.repository;

import br.com.parquimetro.model.DadosCartao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DadosCartaoRepository extends MongoRepository<DadosCartao, String> {
}
