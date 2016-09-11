package ch.css.ebusiness.fitbit.impl.tocken;

import ch.css.ebusiness.fitbit.api.OauthTokenPair;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;

public interface FitbitTokenEvent extends Jsonable {

   @SuppressWarnings("serial")
   @Immutable
   @JsonDeserialize
   public final class FitbitTokenChanged implements FitbitTokenEvent {
      public final OauthTokenPair token;


      @JsonCreator
      public FitbitTokenChanged(OauthTokenPair token) {
         this.token = token;
      }
   }
}
