package controllers;

import config.ResourceServerSecure;
import dtos.MeDto;
import models.Allow;
import models.ProviderLink;
import models.Scope;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ProviderLinkRepository;
import repositories.ScopeRepository;
import repositories.UserRepository;
import scala.Tuple2;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class MeController extends Controller {

    @Inject
    private UserRepository userRepository;
    @Inject
    private ProviderLinkRepository providerLinkRepository;
    @Inject
    private ScopeRepository scopeRepository;

    @ResourceServerSecure(scope = "profile")
    public Result get(){
        Allow grant = request().attrs().get(ResourceServerSecure.GRANT);
        Long client_id = grant.getClientId();
        User user = userRepository.findById(grant.getUserId());
        Scope s = scopeRepository.findByClient(client_id);
        MeDto dto = new MeDto();
        if(s.getScopes().contains("user_social_links")){
            dto.setSocialLinks(providerLinkRepository.findByUserId(user.getId()).stream().collect(
                    Collectors.toMap(ProviderLink::getProviderKey, c-> Tuple2.apply(c.getRemoteUserId().toString(), c.getToken()))
            ));
        }
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        return ok(Json.toJson(dto));
    }
}
