package models;

/**
 * Scope record for clients
 * Created by Selim Eren Bekçe on 15.08.2017.
 */
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.validation.Constraints;

import io.ebean.*;

@Entity
public class Scope extends Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    /**
     * belonging client
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    public Client client;
    /**
     * e.g. 'email'
     */
    private String scope;

    /**
     * e.g. 'Read your email address'
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return client.getId();
    }

    public void setClientId(Client client) {
        this.client = client;
    }

    public Set<String> getScopes() {
        Set<String> scp = new HashSet<String>(Arrays.asList(scope.split(" ")));
        return scp;
    }

    public void setScopes(Set<String> scopes) {
        String joined = String.join(",", scopes);
        this.scope = joined;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
