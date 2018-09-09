package repositories;

import io.ebean.Ebean;
import io.ebean.Expr;
import io.ebean.Finder;
import models.Scope;

import javax.inject.Singleton;


@Singleton
public class ScopeRepository {

    private static final Finder<Long, Scope> find = new Finder<>(Scope.class);

    public Scope findById(Long id) {
      return find.query().where().eq("id", id).findOne();
    }

    public void save(Scope u){
        Ebean.save(u);
    }

    public Scope findByClient(Long client_id) {
      return find.query().where().eq("client_id", client_id).findOne();
    }
}
