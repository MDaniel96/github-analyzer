import {Component, ViewChild} from '@angular/core';
import {ChartsComponent} from '../charts.component';
import {DoubleRepositoryService} from '../../../service/double-repository.service';
import {DeveloperCompareResponse} from '../../../model/developer-compare-response.model';
import {fadeInAnimation} from '../../../util/animations';
import {ChartComponent} from "ng-apexcharts";
import {ChartOptions} from "../distribution/distribution.component";

@Component({
  selector: 'app-developer-compare',
  templateUrl: './developer-compare.component.html',
  styleUrls: ['./developer-compare.component.css'],
  animations: [fadeInAnimation]
})
export class DeveloperCompareComponent extends ChartsComponent {

  @ViewChild("chart") chart: ChartComponent;
  public chartOptionsMonth: Partial<ChartOptions>;
  developerCompareResponse: DeveloperCompareResponse;

  months1: number[] = [];
  commitsPerMonth1: number[] = [];

  months2: number[] = [];
  commitsPerMonth2: number[] = [];

  constructor(public doubleRepositoryService: DoubleRepositoryService) {
    super();
    this.checkAnalyze(doubleRepositoryService);
  }

  getResultCallback = () => {
    this.doubleRepositoryService.getDeveloperCompareResult().subscribe(
      data => {
        if (data !== null) {
          this.stopQueryData();
          this.developerCompareResponse = data;
          this.preProcessCommits()
          this.drawChart();
        }
      }
    );
  }

  preProcessCommits(){
    this.developerCompareResponse.developer1CommitsByMonth.forEach(element => {
      this.months1.push(element.month);
      this.commitsPerMonth1.push(element.commits);
    })

    this.developerCompareResponse.developer2CommitsByMonth.forEach(element => {
      this.months2.push(element.month);
      this.commitsPerMonth2.push(element.commits);
    })
  }

  clearResult() {
    this.developerCompareResponse = new DeveloperCompareResponse();
  }

  drawChart() {
    // TODO: Draw developerCompare diagram from <this.developerCompareResult>
    this.chartOptionsMonth = {
      series: [
        {
          name: "Repository 1 top developer",
          data: this.commitsPerMonth1
        },
        {
          name: "Repository 2 top developer",
          data: this.commitsPerMonth2
        }
      ],
      chart: {
        height: 350,
        type: "line",
        zoom: {
          enabled: false
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: "straight"
      },
      grid: {
        row: {
          colors: ["#f3f3f3", "transparent"], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      xaxis: {
        categories: this.months1
      }
    };
  }
}
