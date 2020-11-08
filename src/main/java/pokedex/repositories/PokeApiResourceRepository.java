package pokedex.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pokedex.entities.PokeApiResource;

import java.util.List;

@Repository
public interface PokeApiResourceRepository extends MongoRepository<PokeApiResource, String> {
    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    List<PokeApiResource> findByName(String name);
}
