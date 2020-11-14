import {Component, ViewChild} from '@angular/core';
import {DevelopmentCompareResponse} from '../../../model/development-compare-response.model';
import {DoubleRepositoryService} from '../../../service/double-repository.service';
import {ChartsComponent} from '../charts.component';
import {fadeInAnimation} from '../../../util/animations';
import {ChartComponent} from "ng-apexcharts";
import {ChartOptions} from "../distribution/distribution.component";
import {DeveloperCompareResponse} from "../../../model/developer-compare-response.model";

@Component({
  selector: 'app-development-compare',
  templateUrl: './development-compare.component.html',
  styleUrls: ['./development-compare.component.css'],
  animations: [fadeInAnimation]
})
export class DevelopmentCompareComponent extends ChartsComponent {

  @ViewChild("chart") chart: ChartComponent;
  public chartOptionsMonth: Partial<ChartOptions>;

  months1: number[] = [];
  commitsPerMonth1: number[] = [];

  months2: number[] = [];
  commitsPerMonth2: number[] = [];

  developmentCompareResponse: DevelopmentCompareResponse;

  constructor(public doubleRepositoryService: DoubleRepositoryService) {
    super();
    this.checkAnalyze(doubleRepositoryService);
  }

  getResultCallback = () => {
    this.doubleRepositoryService.getDevelopmentCompareResult().subscribe(
      data => {
        if (data !== null) {
          this.stopQueryData();
          this.developmentCompareResponse = data;
          this.preProcessCommits()
          this.drawChart();
        }
      }
    );
  }

  preProcessCommits(){
    this.developmentCompareResponse.repository1CommitsByMonth.forEach(element => {
      this.months1.push(element.month);
      this.commitsPerMonth1.push(element.commits);
    })

    this.developmentCompareResponse.repository2CommitsByMonth.forEach(element => {
      this.months2.push(element.month);
      this.commitsPerMonth2.push(element.commits);
    })
  }

  clearResult() {
    this.developmentCompareResponse = new DevelopmentCompareResponse();
  }

  drawChart() {
    // TODO: Draw developmentCompare diagram from <this.developmentCompare>
    this.chartOptionsMonth = {
      series: [
        {
          name: "Repository 1 commits",
          data: this.commitsPerMonth1
        },
        {
          name: "Repository 2 commits",
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
