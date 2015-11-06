/*package com.scribe;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class FacebookScribeAuthenticator {
 
  public static final String STATE = "state";
  private String applicationHost;
  private OAuthService oAuthService;
  // Jackson ObjectMapper
  private ObjectMapper objectMapper;
 
  @Autowired
  public FacebookScribeAuthenticator(
      @Value("#{properties['facebook.clientId']}") 
      String clientId,
      @Value("#{properties['facebook.clientSecret']}") 
      String clientSecret,
      @Value("#{properties['application.host']}") 
      String applicationHost) {
    this.applicationHost = applicationHost;
    this.oAuthService = buildOAuthService(clientId, clientSecret);
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new AfterburnerModule());
  }
  
  @RequestMapping("/auth/facebook")
  public RedirectView startAuthentication(HttpSession session) 
      {
    String state = UUID.randomUUID().toString();
    session.setAttribute(STATE, state);
    String authorizationUrl = 
        oAuthService.getAuthorizationUrl(Token.empty()) 
          + "&" + STATE + "=" + state;
    return new RedirectView(authorizationUrl);
  }
}
 
private OAuthService buildOAuthService(String clientId, 
                                       String clientSecret) {
  // The callback must match Site-Url in the Facebook app settings
  return new ServiceBuilder()
      .apiKey(clientId)
      .apiSecret(clientSecret)
      .callback(applicationHost + "/auth/facebook/callback")
      .provider(FacebookApi.class)
      .build();
  }
}
*/