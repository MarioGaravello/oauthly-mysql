package controllers;

import config.AuthorizationServerSecure;
import config.JwtUtils;
import dtos.Token;
import dtos.TokenStatus;
import config.Utils;
import models.Client;
import models.Allow;
import models.Scope;
import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Result;
import repositories.ClientRepository;
import repositories.AllowRepository;
import repositories.ScopeRepository;
import repositories.UserRepository;
import scala.Tuple2;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>This controller implements oauth2 authorization server semantics with password grant type.<br>
 * Supports refresh tokens.<br>
 * Works with request params in token post method for the requirement, it is best to modify it for generic usage.<br>
 * It uses JWT to issue tokens, so tokens are self contained and there is no token store.<br>
 * It works with ResourceServerFilter to protect resources.<br>
 * Currently it works with a single user and client id, but it is easy to extend for multiple users.</p>
 *
 * Created by Selim Eren Bekçe on 2016-08-25.
 */
public class OAuthAuthorizationServerController extends play.mvc.Controller {
    private final FormFactory formFactory;
    private final JwtUtils jwtUtils;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final AllowRepository allowRepository;
    private final ScopeRepository scopeRepository;

    @Inject
    public OAuthAuthorizationServerController(FormFactory formFactory, JwtUtils jwtUtils, ClientRepository clientRepository, UserRepository userRepository, AllowRepository allowRepository, ScopeRepository scopeRepository) {
        this.formFactory = formFactory;
        this.jwtUtils = jwtUtils;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.allowRepository = allowRepository;
        this.scopeRepository = scopeRepository;
    }

    /**
     * Issues new tokens.
     *
     * client_id id of the client (required)
     * client_secret secret of the client (required)
     * grant_type supported grant types: 'client_credentials', 'authorization_code', 'password' or 'refresh_token' (required)
     * username the resource owner username when grant_type is 'password'
     * password the resource owner password when grant_type is 'password'
     * refresh_token the previously retrieved refresh_token when grant_type is 'refresh_token'
     * redirect_uri redirect_uri parameter while retrieving authorization code from /authorize, when grant_type is 'authorization_code'
     * code retrieved code when grant_type is 'authorization_code'
     * scope requested scope when grant_type is 'password'
     * @return token
     */
    public Result token() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String grant_type = form.get("grant_type");
        if(grant_type == null){
            return badRequest(Json.newObject().put("message", "missing grant_type"));
        }
        String client_id = form.get("client_id");
        String client_secret = form.get("client_secret");
        Long id_client = clientRepository.findClientId(client_id);
        Client client = clientRepository.findByClient(client_id);
        Scope s = scopeRepository.findByClient(id_client);
        if(client == null || !Objects.equals(client.getSecret(), client_secret)){
            return badRequest(Json.newObject().put("message", "either client_id or client_secret is missing or invalid"));
        }
        String username = form.get("username");
        String password = form.get("password");
        String refresh_token = form.get("refresh_token");
        String redirect_uri = form.get("redirect_uri");
        String code = form.get("code");
        String scope = form.get("scope");

        switch (grant_type) {
            case "client_credentials": {
                /* In this mode, we use client_id as user_id in Grant */
                Allow grant = allowRepository.findByClient(id_client);
                if(grant == null){
                    grant = new Allow();
                    grant.setClientId(client);
                    allowRepository.save(grant);
                }
                if(s == null){
                    s = new Scope();
                    if(scope == null){
                        Set<String> scp = new HashSet<String>();
                        scp.add(" ");
                        s.setScopes(scp);
                    }else{
                        Set<String> scp = new HashSet<String>(Arrays.asList(scope.split(" ")));
                        s.setScopes(scp);
                    }
                    s.setClientId(client);
                    s.save();
                }
                Token token = jwtUtils.prepareToken(client_id, client_secret, grant.getId(), s.getScopes());
                return ok(Json.toJson(token));
            }
            case "authorization_code": {
                Allow grant = jwtUtils.validateAuthorizationCode(code, redirect_uri);
                if(grant == null){
                    return badRequest(Json.newObject().put("message", "invalid code or redirect_uri"));
                }
                Token token = jwtUtils.prepareToken(client_id, client_secret, grant.getId(), s.getScopes());
                System.out.println("token"+ token.toString());
                return ok(Json.toJson(token));
            }
            case "password": {
                if(!client.isTrusted()){
                    return badRequest(Json.newObject().put("message", "client not trusted for password type grant"));
                }
                User user = userRepository.findByUsernameNormalized(Utils.normalizeUsername(username));
                if(user == null){
                    user = userRepository.findByEmail(username);
                }
                if(user == null || !user.checkPassword(password)){
                    return badRequest(Json.newObject().put("message", "invalid username or password"));
                }
                Allow grant = allowRepository.findByClientAndUser(id_client, user.getId());
                if(s == null){
                    s = new Scope();
                }
                if(grant == null){
                    // create new grant automatically because this is a trusted app
                    grant = new Allow();
                    grant.setUserId(user);
                    grant.setClientId(client);
                    if(scope != null){
                        s.setScopes(new HashSet<>(Arrays.asList(scope.split(" "))));
                    }
                    allowRepository.save(grant);
                } else if (scope != null) {
                    s.getScopes().addAll(Arrays.asList(scope.split(" ")));
                    allowRepository.save(grant);
                }
                Token token = jwtUtils.prepareToken(client_id, client_secret, grant.getId(), s.getScopes());
                return ok(Json.toJson(token));
            }
            case "refresh_token": {

                Tuple2<Allow, TokenStatus> tokenStatus = jwtUtils.getTokenStatus(refresh_token);
                if (tokenStatus._2() != TokenStatus.VALID_REFRESH) {
                    return badRequest(Json.newObject().put("message", "invalid_refresh_token"));
                }
                Allow grant = tokenStatus._1();
                if(s == null) {
                    s = new Scope();
                    if(scope == null){
                        Set<String> scp = new HashSet<String>();
                        scp.add(" ");
                        s.setScopes(scp);
                    }else{
                        Set<String> scp = new HashSet<String>(Arrays.asList(scope.split(" ")));
                        s.setScopes(scp);
                    }
                    s.setClientId(client);
                    s.save();
                }
                Token token = jwtUtils.prepareToken(client_id, client_secret, grant.getId(), s.getScopes());
                return ok(Json.toJson(token));
            }
            default:
                return badRequest(Json.newObject().put("message", "unsupported_grant_type"));
        }
    }

    @AuthorizationServerSecure
    public Result authorize(String client_id, String response_type, String redirect_uri, String scope, String state) {

        Long id_client = clientRepository.findClientId(client_id);
        Client client = clientRepository.findByClient(client_id);

        if(client == null){
            return badRequest(Json.newObject().put("message", "invalid client_id"));
        }
        if(!redirect_uri.startsWith(client.getRedirectUri())){
            return badRequest(Json.newObject().put("message", "invalid redirect_uri"));
        }
        if(!"code".equals(response_type)){
            return badRequest(Json.newObject().put("message", "invalid response_type"));
        }

        User user = request().attrs().get(AuthorizationServerSecure.USER);
        if(!user.isEmailVerified()) {
            return redirect(routes.ProfileController.changeEmailPage(ctx().request().uri()));
        }

        Allow grant = allowRepository.findByClientAndUser(id_client, user.getId());
        Scope s = scopeRepository.findByClient(id_client);
        if(client.isTrusted()) {
            if(grant == null){
                grant = new Allow();
                grant.setUserId(user);
                grant.setClientId(client);
            }
            if(scope != null){
                Set<String> scp = new HashSet<String>(Arrays.asList(scope.split(" ")));
                if(scp!=null && s!=null){
                    s.setScopes(scp);
                }else if(scp!=null && s==null){
                    s = new Scope();
                    s.setScopes(scp);
                    s.setClientId(client);
                    s.save();
                }
            }
            allowRepository.save(grant);
        }


        if(grant != null){
            boolean scopeOK = true;
            if(scope != null){
                List<String> scopes = Arrays.asList(scope.split(" "));
                for (String str : scopes) {
                    if(!s.getScopes().contains(str)){
                        scopeOK = false;
                        break;
                    }
                }
            }
            if(scopeOK){
                String code = jwtUtils.prepareAuthorizationCode(client.getClient(), client.getSecret(), grant.getId(), redirect_uri);
                String uri = String.format("%s%scode=%s", redirect_uri, redirect_uri.contains("?") ? "&" : "?", code);
                if(state != null){
                    uri += "&state="+state;
                }
                return redirect(uri);
            }
        }

        return ok(views.html.authorize.render(client.getName(), client_id, response_type, redirect_uri, scope, state));
    }

    @AuthorizationServerSecure
    public Result authorizeDo(String client_id, String response_type, String redirect_uri, String scope, String state) {

        Long id_client = clientRepository.findClientId(client_id);
        Client client = clientRepository.findByClient(client_id);
        Scope s = scopeRepository.findByClient(id_client);
        if(client == null){
            return badRequest(Json.newObject().put("message", "invalid client_id"));
        }
        if(!redirect_uri.startsWith(client.getRedirectUri())){
            return badRequest(Json.newObject().put("message", "invalid redirect_uri"));
        }
        if(!"code".equals(response_type)){
            return badRequest(Json.newObject().put("message", "invalid response_type"));
        }

        User user = request().attrs().get(AuthorizationServerSecure.USER);

        Allow grant = allowRepository.findByClientAndUser(id_client, user.getId());
        if(grant == null){
            grant = new Allow();
            grant.setUserId(user);
            grant.setClientId(client);
        }
        if(scope != null){
            Set<String> scp = new HashSet<String>(Arrays.asList(scope.split(" ")));
            s.setScopes(scp);
        }
        allowRepository.save(grant);

        String code = jwtUtils.prepareAuthorizationCode(client.getClient(), client.getSecret(), grant.getId(), redirect_uri);
        String uri = String.format("%s%scode=%s", redirect_uri, redirect_uri.contains("?") ? "&" : "?", code);
        if(state != null){
            uri += "&state="+state;
        }
        return redirect(uri);
    }
}
