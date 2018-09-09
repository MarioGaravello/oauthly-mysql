package models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.validation.Constraints;

import io.ebean.*;

/**
 * Client, a.k.a. App
 * Created by Selim Eren Bekçe on 15.08.2017.
 */
 @Entity
public class Client extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String client;
    private String secret;
    private String name;
    private String logo_url;
    /**
     * If true, then all grants shall automatically be given without user consent
     */
    private boolean trusted;
    /**
     * Origin of redirect uri for each request shall match this.
     * e.g. 'http://localhost:8080'
     */
    private String redirect_uri;
    /**
     * The id of the managing user of this client
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    public User user;

    /**
     * Contains the allowed origin for using the JS authentication. Must match incoming Origin header 1-1 else the request will not be allowed.
     */
    private String allowed_origin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logo_url;
    }

    public void setLogoUrl(String logoUrl) {
        this.logo_url = logoUrl;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public String getRedirectUri() {
        return redirect_uri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirect_uri = redirectUri;
    }

    public Long getOwnerId() {
        return user.getId();
    }

    public void setOwnerId(Long ownerId) {
        user.setId(ownerId);
    }

    public String getAllowedOrigin() {
        return allowed_origin;
    }

    public void setAllowedOrigin(String allowedOrigin) {
        this.allowed_origin = allowedOrigin;
    }

    public Client(User u) {
        this.user = u;
    }
}
