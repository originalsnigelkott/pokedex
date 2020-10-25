package pokedex.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pokedex.entities.Pokemon;

import java.util.List;

@Repository
public interface PokemonRepository extends MongoRepository<Pokemon, String> {
    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    List<Pokemon> findByName(String name);
}
