import {Component} from 'angular2/core';
import {bootstrap} from 'angular2/platform/browser';
import {FitbitDemo} from './fitbit-demo';
import {OrderCtrl} from './app/components/order';
import {PizzaboyCtrl} from './app/components/pizzaboy';
import {BossCtrl} from "./app/components/boss";

@Component({
  selector: 'main',
  directives: [FitbitDemo, OrderCtrl, PizzaboyCtrl, BossCtrl],
  templateUrl: 'templates/main.html'
})

class Main {

}

bootstrap(Main);
