package pokedex.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pokedex.entities.Ability;

import java.util.Optional;

@Repository
public interface AbilityRepository extends MongoRepository<Ability, String> {
    Optional<Ability> findByName(String name);
}
