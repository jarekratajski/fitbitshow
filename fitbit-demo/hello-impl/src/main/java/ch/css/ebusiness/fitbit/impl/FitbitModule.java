/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package ch.css.ebusiness.fitbit.impl;

import ch.css.ebusiness.fitbit.api.FitbitService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 *
 */
public class FitbitModule extends AbstractModule implements ServiceGuiceSupport {


    @Override
    protected void configure() {
        bindServices(serviceBinding(FitbitService.class, FitbitServiceImpl.class));
    }
}
