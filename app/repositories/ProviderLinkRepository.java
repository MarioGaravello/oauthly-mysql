package repositories;

import models.ProviderLink;
import models.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.ebean.*;

@Singleton
public class ProviderLinkRepository {

    private static final Finder<Long, ProviderLink> find = new Finder<>(ProviderLink.class);

    public ProviderLink findById(Long id) {
      return find.query().where().eq("id:", id).findOne();
    }

    public ProviderLink findByProvider(String providerKey, Long remoteUserId) {
      return find.query().where().and(Expr.eq("providerKey", providerKey), Expr.eq("remoteUserId", remoteUserId)).findOne();
    }

    public List<ProviderLink> findByUserId(Long userId) {
      return StreamSupport.stream(find.query().where().eq("userId", userId).findList().spliterator(), false).collect(Collectors.toList());
    }

    public java.util.Map<String, Long> findMapByUserId(Long userId) {
      return StreamSupport.stream(find.query().where().eq("userId", userId).findList().spliterator(), false).collect(Collectors.toMap(i -> i.getProviderKey(), i -> i.getId()));
    }


    public void save(ProviderLink u){
        Ebean.save(u);
    }

    public void delete(Long id){
      Ebean.delete(ProviderLink.class, id);
    }
}
