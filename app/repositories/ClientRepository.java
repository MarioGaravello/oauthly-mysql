package repositories;

import models.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.ebean.*;

@Singleton
public class ClientRepository {

    private static final Finder<Long, Client> find = new Finder<>(Client.class);


    public Long findClientId(String client){
      Client c = find.query().where().eq("client", client).findOne();
      return c.getId();
    }

    //return client
    public Client findById(Long id) {
      return find.query().where().eq("id", id).findOne();
    }

    public Client findByClient(String client) {
      return find.query().where().eq("client", client).findOne();
    }

    public void save(Client u){
        Ebean.save(u);
    }

    //Return a list of client
    public List<Client> findByOwnerId(Long ownerId) {
      return find.query().where().eq("owner_id", ownerId).findList();
    }
}
