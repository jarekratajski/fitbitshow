package ch.css.ebusiness.fitbit.impl.tocken;


import ch.css.ebusiness.fitbit.api.OauthTokenPair;
import com.google.common.base.Optional;
import javaslang.control.Option;

public interface FitbitTokenState {

   Option<OauthTokenPair> getToken();

}
