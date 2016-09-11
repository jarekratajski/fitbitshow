package ch.css.ebusiness.fitbit.impl.tocken;

import ch.css.ebusiness.fitbit.api.OauthTokenPair;
import com.google.common.base.Optional;
import javaslang.control.Option;

public class FitbitValidTokenState implements FitbitTokenState{
   public final OauthTokenPair token;


   public FitbitValidTokenState(OauthTokenPair token) {
      this.token = token;
   }

   @Override
   public Option<OauthTokenPair> getToken() {
      return Option.of(token);
   }
}
