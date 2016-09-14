/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package ch.css.ebusiness.hello.impl;

import ch.css.ebusiness.hello.api.GreetingMessage;
import ch.css.ebusiness.hello.api.HelloService;
import org.junit.Ignore;
import org.junit.Test;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

@Ignore
public class HelloServiceTest {

    @Test
    public void shouldStorePersonalizedGreeting() throws Exception {
        withServer(defaultSetup(), server -> {
            HelloService service = server.client(HelloService.class);

            String msg1 = service.hello("Alice").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Hello, Alice!", msg1); // default greeting

            service.useGreeting("Alice").invoke(new GreetingMessage("Hi")).toCompletableFuture().get(5, SECONDS);
            String msg2 = service.hello("Alice").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Hi, Alice!", msg2);

            String msg3 = service.hello("Bob").invoke().toCompletableFuture().get(5, SECONDS);
            assertEquals("Hello, Bob!", msg3); // default greeting
        });
    }

}
