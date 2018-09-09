# oauthly-mysql

[![Build Status](https://travis-ci.org/bekce/oauthly.svg?branch=master)](https://travis-ci.org/bekce/oauthly)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/18e70942adcf440e8c85d3e186c0e916)](https://www.codacy.com/app/seb_4/oauthly)

Oauthly-mysql is a fork of [oauthly](https://github.com/bekce/oauthly) but oauthly use MongoDB while oauthly-mysql use Mysql.  

OAuth 2.0 Compliant Authorization and Resource Server in Java with Play Framework. Suitable for being the Authorization Server for your software platform, with SSO and social provider login support.

There are a lot of authorization server examples on many platforms, such as Spring Boot, Play Framework, etc,
but none works as a full-fledged authorization server. This one does, for free. The functionality is comparable to
auth0, will be better in the future (with your PRs, of course).

This project was started as a Spring Boot project (on branch `spring-boot`) after, it was fully converted it to Play Framework by Selim Eren Bekçe. The reason for this change is the superior features of Play Framework.
After I have fork oauthly that it use mongoDB and I have created oauthly-mysql that use mysql db.

Oauthly-mysql supports the following grant type:
- authorization_code
- client_credentials
- password
- refresh_token


## Features

- Uses Play Framework (Java) 2.6.x 
- Fully supported OAuth2 grant types: client credentials, authorization code, resource owner password, refresh token
- Login, register, profile, user management, client management and authorize client views with Bootstrap
- Supports logging in with social providers and advanced account linking features
- Supports sending email confirmation links
- Utilizes JWT for tokens, authorization codes and cookies
- Completely stateless server side logic
- Logged-in users are remembered with long-term safe cookies
- Multiple client id and secret pairs are supported, managed by a view
- Customizable expiry times for generated tokens (see `application.conf` file)
- Google reCAPTCHA support on endpoints (with `@RecaptchaProtected` annotation)
- OAuth2 scopes support
- Mysql database
- Mailgun API integration for sending emails
- Uses bcrypt for user passwords, [twirl](https://playframework.com/documentation/2.6.x/JavaTemplates) for templating

## Instructions for local mode

0. Have a running Mysql db instance and `sbt` installed.
1. `sbt run`
2. Go to <http://localhost:9000>, register a new account for yourself.
All accounts are required to confirm email addresses. To receive emails, you need to get your own mailbox api key. For demo purposes, the confirmation link is logged on console after you register. Copy and paste the link on your browser to finalize account creation. 
First account will be given admin access.
3. You can also enable login with Facebook, Google or any other OAuth 2.0 Authorization Server.
    1. For Facebook, go to <https://developers.facebook.com/apps/> and create yourself an app with redirect uri `http://localhost:9000/oauth/client/facebook/callback`, then put its client id and secret to `application.conf`. You may need some additional settings on your Facebook app. Consult their documentation. 
    2. For Google, go to <https://console.developers.google.com/apis/credentials> and create an OAuth Client ID with redirect url `http://localhost:9000/oauth/client/google/callback`, then put its client id and secret to `application.conf`. You will also need to enable Google People API. Consult their documentation. 
    3. For any other OAuth 2.0 Authorization Server, see [this](#custom-oauth2-provider)
3. By now, you have authenticated yourself as an admin on OAuthly-mysql platform. Now you will configure your applications and services (OAuth 2.0 Clients) to connect to OAuthly-mysql (OAuth 2.0 Authorization Server). Go to <http://localhost:9000/client> to create one client, by setting its `name` and `redirect_uri`. 
4. Set generated Client ID and Client Secret and following endpoint addresses on your OAuth 2.0 Client Application:

- Authorize endpoint: http://localhost:9000/oauth/authorize

    Example: `curl -v 'http://localhost:9000/oauth/authorize?client_id=RcC53b7oEDfNQ4rJnEM4&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A9001%2Flogin'`
    
- Token endpoint: http://localhost:9000/oauth/token (Use POST with FORM parameters)

    Example: `curl -v -X POST -d 'grant_type=authorization_code&code=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJoIjoxMDI1MDQ5NzEzLCJyIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNTE1MDg0OTM3LCJ2dCI6MywiZyI6IjExbU9tMHVVMEQwOHMxSXo5S3RMIn0.wtLx54iK1kEWhXAVU5gb6AnyPQnN1Qb2r4L-s20TADk&client_id=RcC53b7oEDfNQ4rJnEM4&client_secret=BZVtYr0giScsJRuXDkdmuRHCZuKypSrp&redirect_uri=http%3A%2F%2Flocalhost%3A9001%2Flogin' 'http://localhost:9000/oauth/token'`

- User info endpoint: http://localhost:9000/api/me (Use `Authorization: Bearer token` header)

    Example: `curl -v -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJoIjoxMDI1MDQ5NzEzLCJleHAiOjE1MTUwODUxNTYsImdyYW50IjoiMTFtT20wdVUwRDA4czFJejlLdEwiLCJ2dCI6MX0.sl4F-ik9Tstw38JxOSfHYUCi1cN4NxYmqgNCoA0hIbA" http://localhost:9000/api/me`
    
## Instructions for production mode

Please first follow the instructions for local mode AND run it on your local machine before attempting this. If you don't have that much time, AT LEAST read them once.

0. OAuthly-mysql must run on a seperate domain. If your domain is `example.com`, running on `oauthly.example.com` is recommended.
1. `sbt stage` in root folder to prepare distribution. 
2. Move `target/universal/stage/oauthly-1.0-SNAPSHOT.zip` to your server and extract it to some place.
3. Set your smtp configurations for `play.mailer` and set `mock=false` or create account on <http://mailgun.com> and get a (free) API key for a valid domain of yours.
4. Create account on <https://www.google.com/recaptcha/admin> and get `recaptcha.siteKey` and `recaptcha.secret`. 
5. (Optional) If you want to allow Login with Facebook and/or Google, get your keys as specified above.
6. Prepare `conf/prod.conf` file from following template. Put random values for `jwt.secret` and `play.http.secret.key`. 

```
include "application.conf"

http.port=9000
play.filters.enabled=[
  play.filters.csrf.CSRFFilter,
  play.filters.headers.SecurityHeadersFilter,
  play.filters.hosts.AllowedHostsFilter,
]
play.filters.hosts.allowed = ["oauthly.example.com"]
ebean.default = ["models.*"]

db.default.driver=com.mysql.jdbc.Driver
db.default.url="jdbc:mysql://localhost:3306/bekce_mysql?autoReconnect=true&useSSL=false"
db.default.username=root
db.default.password="root"

play {
  evolutions {
      # Db specific configuration. Should be a map of db names to configuration in the same format as this.
      db {
          default {
              autoApply = true
              autoApplyDowns=true
          }
      }
      # Whether evolutions are enabled
      enabled = true
      # Whether evolutions should be automatically applied.  In prod mode, this will only apply ups, in dev mode, it will
      # cause both ups and downs to be automatically applied.
      autoApply = false
  }
}
play.http.secret.key=
jwt.secret=
recaptcha.enabled=true
recaptcha.siteKey=
recaptcha.secret=
use.secure.session.cookie=true
mail.mailgun.key=key-123
mail.mailgun.domain=example.com
mail.mailgun.from="OAuthly <noreply@example.com>"
brand.name="My Brand"
tos.text="""
Terms of Service content
"""

oauth.providers=[
  {
    key=facebook
    displayName=Facebook
    clientId=
    clientSecret=
    tokenUrl="https://graph.facebook.com/v2.11/oauth/access_token"
    authorizeUrl="https://www.facebook.com/v2.11/dialog/oauth"
    scopes="public_profile email"
    userInfoUrl="https://graph.facebook.com/me?fields=id,name,email"
  }
]
```

7. `bin/oauthly -Dconfig.resource=prod.conf` to start the server. 
8. TLS is a must! You can follow <https://www.playframework.com/documentation/2.6.x/ConfiguringHttps> for configuring it but I recommend setting up TLS on a reverse proxy like nginx. 

## Custom OAuth2 Provider

Currently we implement Facebook and Google OAuth2 clients. Every vendor may implement the token and user info retrieval part differently. Follow below steps for a new OAuth2 client implementation.

1. Put relevant config block in `application.conf`.
2. Set `tokenRetriever` and `currentUserIdentifier` in `AuthorizationServerManager` class. Only `id` is necessary for identifying users in remote system. If you map email addresses in remote system to OAuthly-mysql, the confirmation step will be bypassed resulting in a smoother sign-up process. 
3. We use [bootstrap-social](https://lipis.github.io/bootstrap-social/) for provider login buttons, fork it if necessary.


## Screenshots 

![login](https://i.imgur.com/nF8ZRCE.png)
![register](https://i.imgur.com/qMTFShT.png)
![reset-password](https://i.imgur.com/mhB9fpQ.png)
![profile](https://i.imgur.com/UukRhWl.png)
![client](https://i.imgur.com/9fXZMfn.png)

