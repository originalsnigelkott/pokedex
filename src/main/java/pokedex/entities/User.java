package pokedex.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotBlank(message = "Username must be present.")
    @Size(min = 3, max = 32, message = "Username must be at least 3 characters and no more than 32 characters.")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password must be present.")
    private String password;
    private List<String> roles = Arrays.asList("MEMBER");

    public User() {
    }

    public User(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
