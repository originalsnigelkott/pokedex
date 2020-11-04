package pokedex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PokeApiException extends ResponseStatusException {
    public PokeApiException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "PokeApi not responding.");
    }
}
