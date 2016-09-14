package ch.css.ebusiness.fitbit.impl.tocken;

import ch.css.ebusiness.fitbit.api.OauthTokenPair;
import javaslang.control.Option;

public class FitbitEmptyTokenState implements FitbitTokenState {
    @Override
    public Option<OauthTokenPair> getToken() {
        return Option.none();
    }
}
