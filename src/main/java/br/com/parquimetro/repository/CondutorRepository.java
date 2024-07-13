package br.com.parquimetro.repository;

import br.com.parquimetro.model.Condutor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CondutorRepository extends MongoRepository<Condutor, String> {

    Boolean existsByEmail(String email);

}
