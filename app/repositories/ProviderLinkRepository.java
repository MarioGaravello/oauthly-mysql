package repositories;

import models.ProviderLink;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProviderLinkRepository {

    private MongoCollection collection;

    @Inject
    public ProviderLinkRepository(PlayJongo playJongo) {
        this.collection = playJongo.jongo().getCollection("providerLink");
//        collection.ensureIndex(); //providerKey & remoteUserId, unique
    }

    public ProviderLink findById(String id) {
        return collection.findOne("{_id:#}", id).as(ProviderLink.class);
    }

    public ProviderLink findByProvider(String providerKey, String remoteUserId) {
        return collection.findOne("{providerKey:#, remoteUserId:#}", providerKey, remoteUserId).as(ProviderLink.class);
    }

    public void save(ProviderLink u){
        collection.save(u);
    }
}