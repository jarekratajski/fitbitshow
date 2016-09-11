/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package ch.css.ebusiness.hello.impl;

import ch.css.ebusiness.fitbit.api.FitbitService;
import ch.css.ebusiness.fitbit.impl.FitbitServiceImpl;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import ch.css.ebusiness.hello.api.HelloService;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class HelloModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {

    bindServices(
       serviceBinding(HelloService.class, HelloServiceImpl.class),
       serviceBinding(FitbitService.class, FitbitServiceImpl.class)
       );
  }
}
