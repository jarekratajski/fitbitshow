/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package ch.css.ebusiness.stream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import ch.css.ebusiness.hello.api.HelloService;
import ch.css.ebusiness.stream.api.StreamService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HelloString.
 */
public class StreamServiceImpl implements StreamService {

    private final HelloService helloService;

    @Inject
    public StreamServiceImpl(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> stream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> helloService.hello(name).invoke()));
    }
}
