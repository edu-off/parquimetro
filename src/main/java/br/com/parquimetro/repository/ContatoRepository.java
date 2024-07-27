package br.com.parquimetro.repository;

import br.com.parquimetro.model.Contato;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends MongoRepository<Contato, String> {

    Boolean existsByEmail(String email);

}
