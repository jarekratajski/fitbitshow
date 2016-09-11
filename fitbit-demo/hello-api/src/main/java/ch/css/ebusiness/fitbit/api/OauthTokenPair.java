package ch.css.ebusiness.fitbit.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class OauthTokenPair {
   public final String token;
   public final String refreshToken;

   @JsonCreator
   public OauthTokenPair(String token, String refreshToken) {
      this.token = token;
      this.refreshToken = refreshToken;
   }
}
