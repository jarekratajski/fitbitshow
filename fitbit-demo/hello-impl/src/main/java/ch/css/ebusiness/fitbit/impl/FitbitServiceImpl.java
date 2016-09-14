/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package ch.css.ebusiness.fitbit.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import ch.css.ebusiness.fitbit.api.FitbitData;
import ch.css.ebusiness.fitbit.api.FitbitService;
import ch.css.ebusiness.fitbit.api.OauthTokenPair;
import ch.css.ebusiness.fitbit.impl.tocken.FitbitConnector;
import ch.css.ebusiness.fitbit.impl.tocken.FitbitTokenCommand;
import ch.css.ebusiness.fitbit.impl.tocken.FitbitTokenEntity;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import javaslang.control.Either;
import javaslang.control.Option;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HelloService.
 */
public class FitbitServiceImpl implements FitbitService {

    private final PersistentEntityRegistry persistentEntityRegistry;


    private final FitbitConnector fitbit = new FitbitConnector();

    @Inject
    public FitbitServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;

        persistentEntityRegistry.register(FitbitTokenEntity.class);

        System.setProperty("https.proxyHost", "vip1-proxy.css.ch");
        System.setProperty("https.proxyPort", "8080");


    }


    @Override
    public ServiceCall<OauthTokenPair, Done> putToken() {
        return request -> {

            PersistentEntityRef<FitbitTokenCommand> ref = persistentEntityRegistry.refFor(FitbitTokenEntity.class, "default");
            // Ask the entity the Hello command.
            return ref.ask(new FitbitTokenCommand.TokenChanged(request));
        };
    }

    @Override
    public ServiceCall<NotUsed, Option<OauthTokenPair>> getToken() {
        return request -> {

            PersistentEntityRef<FitbitTokenCommand> ref = persistentEntityRegistry.refFor(FitbitTokenEntity.class, "default");
            // Ask the entity the Hello command.
            return ref.ask(new FitbitTokenCommand.GetToken())
                    .thenApply(msg -> msg);

        };
    }

    @Override
    public ServerServiceCall<NotUsed, String> after(String code) {
        return HeaderServiceCall.of(
                (requestHeader, notUsed) -> {
                    System.out.println("code is " + code);
                    ResponseHeader responseHeader = ResponseHeader.OK
                            .withStatus(200)
                            .withHeader("Location", "/");
                    final Either<String, OAuth2AccessToken> token = fitbit.getToken(code);
                    System.out.println("token:" + token);
                    token.forEach(pair -> {
                        System.out.println("making pair" + pair);
                        PersistentEntityRef<FitbitTokenCommand> ref = persistentEntityRegistry.refFor(FitbitTokenEntity.class, "default");
                        ref.ask(new FitbitTokenCommand.TokenChanged(new OauthTokenPair(pair.getAccessToken(), pair
                                .getRefreshToken())));
                    });
                    return completedFuture(Pair.create(responseHeader, "kuku"));
                }
        );


    }


    @Override
    public ServiceCall<NotUsed, Either<String, FitbitData>> getData() {
        return request -> {

            PersistentEntityRef<FitbitTokenCommand> ref = persistentEntityRegistry.refFor(FitbitTokenEntity.class, "default");

            return ref.ask(new FitbitTokenCommand.GetData())
                    .thenApply(msg -> msg);

        };
    }
}
