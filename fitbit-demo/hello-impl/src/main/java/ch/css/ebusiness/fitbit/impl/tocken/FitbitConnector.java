package ch.css.ebusiness.fitbit.impl.tocken;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.services.Base64Encoder;
import com.google.gson.*;
import javaslang.control.Either;
import javaslang.control.Option;

import java.io.IOException;
import java.nio.charset.Charset;


public class FitbitConnector {
   public static final String API_KEY = "227S5Y";
   public static final String API_SECRET = "eacaba96e3b491e5540150fe8075b52d";
   OAuth20Service service;
   private final Fitbit20 api = new Fitbit20();

   public FitbitConnector() {
      service = new ServiceBuilder()
         .apiKey(API_KEY)
         .apiSecret(API_SECRET)
         .debug()
         .build(api);
   }


   public Either<String, OAuth2AccessToken> getToken(String code) {
      try {

         OAuthRequest request = new OAuthRequest(
            Verb.POST, this.service.getApi().getAccessTokenEndpoint(), service);
         request.addParameter("grant_type","authorization_code");
         request.addParameter("client_id", API_KEY);
         request.addParameter("code", code);
         request.addParameter("redirect_uri", "http://localhost:8088/services/after");

         this.signAppRequest(request);
         final Response response = request.send();
         OAuth2AccessToken extracted = api.getAccessTokenExtractor().extract(response);

         return Either.right(extracted);
      } catch (IOException e) {
         return Either.left(e.getMessage());
      }
   }

   public Option<Integer> getSteps(String token) {
      final OAuthRequest req = new OAuthRequest(Verb.GET, api.HOST +
         "/1/user/-/activities/heart/date/today/1d/1sec.json",
         service);
      final OAuth2AccessToken aToken = new OAuth2AccessToken(token);
      signUserRequest(req, aToken.getAccessToken());
      final Response response = req.send();

      System.out.println(response.getCode());
      try {
         final String result = response.getBody();
         System.out.println(result);
         JsonParser parser = new JsonParser();
         JsonElement json = parser.parse(result);
         JsonObject activities =  json.getAsJsonObject().getAsJsonObject("activities-heart-intraday");
         JsonArray array = activities.getAsJsonArray("dataset");
         int size = array.size();
         if ( size > 0 ) {
            JsonElement elem = array.get(size - 1);
            JsonObject lastElem =  elem.getAsJsonObject();
            int hr = lastElem.getAsJsonPrimitive("value").getAsInt();

            return Option.of(hr);
         } else {
            return Option.none();
         }

      } catch (IOException e) {
         return Option.none();
      }
   }


   private void signAppRequest(OAuthRequest request) {
      if(API_KEY != null && API_SECRET != null) {
         request.addHeader("Authorization", "Basic " + Base64Encoder.getInstance().encode(String.format("%s:%s", new
            Object[]{API_KEY, API_SECRET}).getBytes(Charset.forName("UTF-8"))));
      }
   }

   private void signUserRequest(OAuthRequest request, String token) {

      request.addHeader("Authorization", "Bearer "+token);

   }

   private static final class Fitbit20 extends DefaultApi20 {

      public static final String HOST = "https://api.fitbit.com";

      @Override
      public String getAccessTokenEndpoint() {
         return HOST + "/oauth2/token";
      }

      @Override
      protected String getAuthorizationBaseUrl() {
         return null;
      }
   }
}
