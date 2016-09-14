import {Component, ViewChild} from '@angular/core';
import { Headers, Http } from '@angular/http'
import { ChartsModule } from 'ng2-charts/ng2-charts';
import { WalkSample }   from './walk-sample';
@Component({
  selector: 'fitbit-demo',
  templateUrl: 'templates/fitbit-demo.html'
})

export class FitbitDemo {
   tokenRegistered : boolean = false;
    hbAnim = "";
    hr: number = 0;
    pulse: Array<number> = [];
    animSpeed: string = "1.00";

    @ViewChild(WalkSample)
    private walkAnimation : WalkSample;

  constructor(private http: Http ) {
    console.info('FitbitDemo Component Mounted Successfully');
    this.checkToken();
    setInterval(() => this.getData(), 120000);


  }

  private checkToken() {
    this.http.get("/services/token").subscribe(
        res => {
            console.log(res);

            this.tokenRegistered = (res.json()['token'] != null);
            this.getData();
        }
    );
  }

  private getData() {
      if ( this.tokenRegistered) {
          this.http.get("/services/data").subscribe(
              res => {
                  console.log(res);

                  const data = res.json();
                  if  (data[0] === "right") {
                      const fitbit = data[1];
                      this.pulse = fitbit.pulse;
                      this.hr = this.pulse.reverse()[0];
                      this.animSpeed = (60.0 / this.hr).toFixed(2);
                      this.hbAnim = 'heartbeat '+this.animSpeed+'s infinite';
                      this.lineChartData = [ {data:this.pulse, label :""}];

                      const lastSteps = fitbit.steps.reverse()[0];
                      if ( lastSteps > 0 ) {
                          //1750 <= 50 steps
                          this.walkAnimation.walkAnimSpeed = ''+ (1750 * ( lastSteps / 50))+'ms';
                      } else {
                          this.walkAnimation.walkAnimSpeed = '1750ms';
                      }


                  }

              }
          );
      }
  }
 //--------charts
    // lineChart
    public lineChartData:Array<any> = [
        {data: [7, 10, 12, 15, 22, 33, 44]}

    ];
    public lineChartLabels:Array<any> = ['Before', 'Now'];
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

    public randomize():void {
        let _lineChartData:Array<any> = new Array(this.lineChartData.length);
        for (let i = 0; i < this.lineChartData.length; i++) {
            _lineChartData[i] = {data: new Array(this.lineChartData[i].data.length), label: this.lineChartData[i].label};
            for (let j = 0; j < this.lineChartData[i].data.length; j++) {
                _lineChartData[i].data[j] = Math.floor((Math.random() * 100) + 1);
            }
        }
        this.lineChartData = _lineChartData;
    }

    // events
    public chartClicked(e:any):void {
        console.log(e);
    }

    public chartHovered(e:any):void {
        console.log(e);
    }

}