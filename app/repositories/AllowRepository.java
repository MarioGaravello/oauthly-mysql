package repositories;

import models.Allow;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.ebean.*;

@Singleton
public class AllowRepository {

    private static final Finder<Long, Allow> find = new Finder<>(Allow.class);

    public Allow findById(Long id) {
      return find.query().where().eq("id", id).findOne();
    }

    public void save(Allow u){
        Ebean.save(u);
    }


    public Allow findByClientAndUser(Long clientId, Long userId) {
      return find.query().where().and(Expr.eq("client_id", clientId), Expr.eq("user_id", userId)).findOne();
    }

    public Allow findByClient(Long clientId) {
        return find.query().where().eq("client_id", clientId).findOne();
    }

}
