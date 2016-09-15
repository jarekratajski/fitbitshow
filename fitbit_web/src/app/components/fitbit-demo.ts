import {Component, ViewChild} from '@angular/core';
import { Headers, Http } from '@angular/http'
import { ChartsModule } from 'ng2-charts/ng2-charts';
import { WalkSample }   from './walk-sample';
@Component({
    host: {
        '(document:keydown)': 'onKey($event)'
    },
  selector: 'fitbit-demo',
  templateUrl: 'templates/fitbit-demo.html'
})

export class FitbitDemo {
   tokenRegistered : boolean = false;
    hbAnim = "";
    hr: number = 0;
    pulse: Array<number> = [];
    animSpeed: string = "1.00";
    totalSteps: number = 15;
    fitbitData : any;

    addPulse : Array<number>=[0];
    addSteps : Array<number>=[0];

    @ViewChild(WalkSample)
    private walkAnimation : WalkSample;

  constructor(private http: Http ) {
    console.info('FitbitDemo Component Mounted Successfully');
    this.checkToken();
    setInterval(() => this.getData(), 120000);

      setInterval(() => {
          this.addPulse.unshift(this.addPulse[0]);
          this.addSteps.unshift(0);
          this.recalcScreen();

      },60000);

      setInterval(() => {
          this.addPulse[0] = Math.round(this.addPulse[0]*0.95);
          this.addSteps.unshift(0);
          this.recalcScreen();
      },5000);
  }

  private checkToken() {
    this.http.get("/services/token").subscribe(
        res => {
            const incomingJson = res.json();
            if ( incomingJson != null) {
                this.tokenRegistered = (res.json()['token'] != null);
                this.getData();
            }
        }
    );
  }

  private sum(a: Array<number>, b: Array<number> ) : Array<number> {
     return  a.map(  (elem, idx) => {
          if ( idx <  b.length) {
              return elem + b[idx];
          } else {
              return elem;
          }
      } );
  }

  private recalcScreen() {
      const fitbit = this.fitbitData;

      this.pulse = this.sum( fitbit.pulse.map( a=>a).reverse(), this.addPulse).reverse();

      this.hr = this.pulse.map( a=>a).reverse()[0];
      this.animSpeed = (60.0 / this.hr).toFixed(2);
      this.hbAnim = 'heartbeat '+this.animSpeed+'s infinite';
      this.lineChartLabels = this.pulse.map(a=>"");
      this.lineChartData = [ {data:this.pulse, label : "pulse"}];
      const steps =    this.sum( fitbit.steps.reverse(), this.addSteps).reverse();
      this.totalSteps = steps.reduce( (a,b)=>a+b, 0);

      const lastSteps = steps.slice(-30).reduce( (a,b)=>a+b, 0);
      if ( lastSteps > 0 ) {
          //1750 <= 50 steps
          this.walkAnimation.walkAnimSpeed = ''+ (1750 * ( 560 / lastSteps))+'ms';
      } else {
          this.walkAnimation.walkAnimSpeed = '18750ms';
      }
  }

  private getData() {
      if ( this.tokenRegistered) {
          this.http.get("/services/data").subscribe(
              res => {
                  console.log(res);

                  const data = res.json();
                  if  (data[0] === "right") {
                      this.fitbitData = data[1];
                      this.recalcScreen();
                  }
              }
          );
      }
  }

  public onKey(event: any)  {
      if ( event.keyCode == 34) {
          event.preventDefault();
          this.addSteps[0] = this.addSteps[0]+5;
          return false;
      }
      if ( event.keyCode == 33) {
          this.addPulse[0] = this.addPulse[0]+1;
          event.preventDefault();
          return false;
      }
      return true;
  }

 //--------charts
    // lineChart
    public lineChartData:Array<any> = [
        {data: [7, 10, 12, 15, 22, 33, 44]}

    ];
    public lineChartLabels:Array<any> = [];
    public lineChartOptions:any = {
        animation: false,
        responsive: true
    };
    public lineChartColors:Array<any> = [
        { // redish

            borderColor: 'rgba(248,159,177,1)',
            pointBackgroundColor: 'rgba(148,159,177,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(148,159,177,0.8)'
        }
    ];
    public lineChartLegend:boolean = true;
    public lineChartType:string = 'line';



}
