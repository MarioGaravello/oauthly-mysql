package models;

import dtos.Token;


/**
 * Models an authorization from an oauth provider (facebook, twitter, etc)
 * to one of oauthly users.
 * Created by Selim Eren Bekçe on 15.08.2017.
 */
import javax.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.validation.Constraints;

import io.ebean.*;

@Entity
public class ProviderLink {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    /**
     * belonging user, could be null while setting up
     */
//    @Indexed
    private Long userId;
    /**
     * e.g. 'facebook' or 'twitter'
     */
    private String providerKey;
    /**
     * last retrieved token
     */
    private Token token;
    /**
     * External user id (e.g. on facebook)
     */
    private Long remoteUserId;
    private String remoteUserEmail;
    private String remoteUserName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Long getRemoteUserId() {
        return remoteUserId;
    }

    public void setRemoteUserId(Long remoteUserId) {
        this.remoteUserId = remoteUserId;
    }

    public String getRemoteUserEmail() {
        return remoteUserEmail;
    }

    public void setRemoteUserEmail(String remoteUserEmail) {
        this.remoteUserEmail = remoteUserEmail;
    }

    public String getRemoteUserName() {
        return remoteUserName;
    }

    public void setRemoteUserName(String remoteUserName) {
        this.remoteUserName = remoteUserName;
    }
}
