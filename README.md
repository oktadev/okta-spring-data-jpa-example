# Spring Data JPA Resource Server Example

This example shows you how to build an OAuth 2.0 resource server using Spring Data JPA. Please read [Build a Secure Spring Data JPA Resource Server](https://developer.okta.com/blog/2020/11/20/spring-data-jpa) to see how this app was created.

## Prerequisites

Before running this sample, you will need the following:

* [Java 11+](https://sdkman.io/jdks)
* [The Okta CLI Tool](https://github.com/okta/okta-cli/#installation)
* An Okta Developer Account, create one using `okta register`, or configure an existing one with `okta login`

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example application, run the following commands:

```bash
git clone https://github.com/oktadeveloper/okta-spring-data-jpa-example.git
cd okta-spring-data-jpa-example
```

This will get a copy of the project installed locally. 

### Spring Boot Configuration

To configure your app to work with Okta, run `okta apps create`. Type **1** for Web and **1** for Okta Spring Boot starter.

```
Type of Application
(The Okta CLI only supports a subset of application types and properties):
> 1: Web
> 2: Single Page App
> 3: Native App (mobile)
> 4: Service (Machine-to-Machine)
Enter your choice [Web]: 1
Type of Application
> 1: Okta Spring Boot Starter
> 2: Spring Boot
> 3: JHipster
> 4: Other
```

You can also create a new OIDC app using the Okta Developer console.

1. Log in to your developer account, navigate to **Applications**, and click on **Add Application**.
2. Select **Web** and click **Next**. 
3. Give the application a name and add `http://localhost:8080/login/oauth2/code/okta` as a login redirect URI. 
4. Click **Done**.

Add your settings to `src/main/resources/application.properties`:

```properties
okta.oauth2.issuer=https://{yourOktaDomain}/oauth2/default
okta.oauth2.client-id={yourClientId}
okta.oauth2.client-secret={yourClientSecret}
```

**NOTE:** The value of `{yourOktaDomain}` should be something like `dev-123456.okta.com`. Make sure you don't include `-admin` in the value!

Start your Spring Boot app.

```bash
mvn spring-boot:run
```

## Generate an Access Token JWT

To generate a JWT, you can use the [OIDC Debugger](https://oidcdebugger.com/).

However, before you do that, you need to add a login redirect URI to your OIDC application. Navigate to the developer dashboard at [https://developer.okta.com](https://developer.okta.com). If this is your first time logging in, you may need to click the **Admin** button.

Click on **Applications** in the top menu.

In the panel, select the **okta-spring-boot-sample** application (the one that the CLI generated for you).

Select the **General** tab. Click **Edit**.

Add a new entry under **Login redirect URIs**: `https://oidcdebugger.com/debug`

Click **Save**.

Take note of the OIDC applications **Client ID** and **Client Secret** at the bottom. You'll need both of those values in just a moment.  

Open the OIDC Debugger at [https://oidcdebugger.com](https://oidcdebugger.com/).

Fill in the following values:

 - **Authorization URI**: `https://{yourOktaDomain}/oauth2/default/v1/authorize` (update {yourOktaDomain} with your actual Okta domain, something like `dev-1234567.okta.com`
 - **Redirect URI**: `https://oidcdebugger.com/debug` (default value)
- **Client ID**: the client ID from your OIDC application above
- **Scope**: `openid` (default value)
- **State**: any value (in production this is used to protect against cross-site request forgery)
- **Response type**: `code`

Click **SEND REQUEST** at the bottom of the form.

You should see an authorization code.

You need to exchange this code for a token. You can use HTTPie. Don't forget to fill in the values in brackets: the **authorization code**, your **Okta domain**, your OIDC app **client ID**, and your OIDC app **client secret**.

```bash
http -f https://{yourOktaDomain}/oauth2/default/v1/token \
  grant_type=authorization_code \
  code={yourAuthCode} \
  client_id={clientId} \
  client_secret={clientSecret} \
  redirect_uri=https://oidcdebugger.com/debug
```

You should get a JSON response that includes an access token and an ID token.

```bash
HTTP/1.1 200 OK
Cache-Control: no-cache, no-store
Connection: keep-alive
...

{
    "access_token": "eyJraWQiOiJycGZWTzd4R2hDWmlvUXdrWWha...",
    "expires_in": 3600,
    "id_token": "eyJraWQiOiJycGZWTzd4R2hDWmlvUXdrWWhaSkph...",
    "scope": "openid",
    "token_type": "Bearer"
}
```

## Access Your API with an OAuth 2.0 Bearer Token

Copy the resulting `access_token` value to the clipboard, and in the terminal where you are running your HTTPie commands, save the token value to a shell variable, like this:

```shell
TOKEN=eyJraWQiOiJxMm5rZmtwUDRhMlJLV2REU2JfQ...
```

Now you can use the token in a request.

```bash
http :8080/dinosaurs "Authorization: Bearer $TOKEN"
```

You should see your list of dinosaurs. 🦖

## Links

This example uses the following open source libraries from Okta:

* [Okta Spring Boot Starter](https://github.com/okta/okta-spring-boot)
* [Okta CLI](https://github.com/okta/okta-cli)

## Help

Please post any questions as comments on the [blog post](https://developer.okta.com/blog/2020/11/20/spring-data-jpa), or visit our [Okta Developer Forums](https://devforum.okta.com/).

For more details on how to build an application with Okta and Spring Boot / Spring Security you can read [this blog post](https://developer.okta.com/blog/2019/05/15/spring-boot-login-options).

[Okta Spring Boot Library]: https://github.com/okta/okta-spring-boot
[OIDC Web Application Setup Instructions]: https://developer.okta.com/authentication-guide/implementing-authentication/auth-code#1-setting-up-your-application
[Authorization Code Flow]: https://developer.okta.com/authentication-guide/implementing-authentication/auth-code

## License

Apache 2.0, see [LICENSE](LICENSE).
