package ch.css.ebusiness.fitbit.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import javaslang.control.Either;
import javaslang.control.Option;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface FitbitService extends Service {

    ServiceCall<NotUsed, String> after(String code);

    ServiceCall<OauthTokenPair, Done> putToken();

    ServiceCall<NotUsed, Option<OauthTokenPair>> getToken();

    ServiceCall<NotUsed, Either<String, FitbitData>> getData();


    @Override
    default Descriptor descriptor() {
        return named("wtf").withCalls(
                pathCall("/api/after?code", this::after),
                pathCall("/api/token", this::getToken),
                pathCall("/api/token", this::putToken),
                pathCall("/api/data", this::getData)

        ).withAutoAcl(true);
    }
}
