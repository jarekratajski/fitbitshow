import { Component} from '@angular/core';

@Component({
    selector: 'walk-sample',
    templateUrl: 'templates/walk-sample.html'
})
export class WalkSample {
    public walkAnimSpeed: string = "1.00";
    constructor() {
        console.info('WalkSample Component Mounted Successfully');

    }
}