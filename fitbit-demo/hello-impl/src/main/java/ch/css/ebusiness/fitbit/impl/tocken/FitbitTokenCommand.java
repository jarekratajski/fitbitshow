package ch.css.ebusiness.fitbit.impl.tocken;

import akka.Done;
import ch.css.ebusiness.fitbit.api.FitbitData;
import ch.css.ebusiness.fitbit.api.OauthTokenPair;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import javaslang.control.Either;
import javaslang.control.Option;

import javax.annotation.concurrent.Immutable;

public interface FitbitTokenCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    public final class TokenChanged implements FitbitTokenCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        public final OauthTokenPair token;

        @JsonCreator
        public TokenChanged(OauthTokenPair token) {
            this.token = token;
        }
    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    public final class GetToken implements FitbitTokenCommand, CompressedJsonable, PersistentEntity
            .ReplyType<Option<OauthTokenPair>> {

    }


    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    public final class GetData implements FitbitTokenCommand, CompressedJsonable, PersistentEntity
            .ReplyType<Either<String, FitbitData>> {

    }
}
