package pokedex.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pokedex.entities.Type;

import java.util.Optional;

@Repository
public interface TypeRepository extends MongoRepository<Type, String> {
    Optional<Type> findByName(String name);
}
