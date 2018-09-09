package models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import models.Scope;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.validation.Constraints;

import io.ebean.*;

@Entity
public class Allow extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    public Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getUserId() {
        return user.getId();
    }


    public void setUserId(User user) {
        this.user = user;
    }

    public Long getClientId() {
        return client.getId();
    }


    public void setClientId(Client client) {
        this.client = client;
    }
}
