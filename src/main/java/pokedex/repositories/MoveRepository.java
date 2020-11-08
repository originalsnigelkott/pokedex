package pokedex.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pokedex.entities.Move;

import java.util.Optional;

@Repository
public interface MoveRepository extends MongoRepository<Move, String> {
    Optional<Move> findByName(String name);
}
