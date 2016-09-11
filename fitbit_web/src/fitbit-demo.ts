import {Component} from 'angular2/core';

@Component({
  selector: 'fitbit-demo',
  templateUrl: 'fitbit-demo.html'
})


export class FitbitDemo {

  constructor() {
    console.info('FitbitDemo Component Mounted Successfully');
  }

}
