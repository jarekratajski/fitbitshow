import {NgModule, Component}      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule }    from '@angular/http';

import { ChartsModule } from 'ng2-charts/ng2-charts';

import { FitbitDemo }   from './components/fitbit-demo';
import { WalkSample }   from './components/walk-sample';

@NgModule({
    imports:      [ BrowserModule,
        HttpModule,ChartsModule],
    declarations: [ FitbitDemo,WalkSample ],
    bootstrap:    [ FitbitDemo ]
})
export class AppModule { }
