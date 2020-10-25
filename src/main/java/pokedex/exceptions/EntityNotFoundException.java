package pokedex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotFoundException extends ResponseStatusException {
    public EntityNotFoundException(String entityName, String propertyName) {
        super(HttpStatus.NOT_FOUND, String.format("Could not find %s with that %s", entityName, propertyName));
    }
}
