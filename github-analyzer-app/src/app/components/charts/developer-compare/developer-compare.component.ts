import {Component, ViewChild} from '@angular/core';
import {ChartsComponent} from '../charts.component';
import {DoubleRepositoryService} from '../../../service/double-repository.service';
import {DeveloperCompareResponse} from '../../../model/developer-compare-response.model';
import {fadeInAnimation} from '../../../util/animations';
import {ChartComponent} from 'ng-apexcharts';
import {ChartOptions} from '../distribution/distribution.component';
import {CommitsByMonth} from '../../../model/commits-by-month.model';

@Component({
  selector: 'app-developer-compare',
  templateUrl: './developer-compare.component.html',
  styleUrls: ['./developer-compare.component.css'],
  animations: [fadeInAnimation]
})
export class DeveloperCompareComponent extends ChartsComponent {

  @ViewChild('chart') chart: ChartComponent;

  chartOptions: Partial<ChartOptions>;

  developerCompareResponse: DeveloperCompareResponse;

  months1: string[] = [];
  commitsPerMonth1: number[] = [];

  months2: string[] = [];
  commitsPerMonth2: number[] = [];

  repository1Year: number;
  repository2Year: number;

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
          this.repository1Year = this.developerCompareResponse.developer1CommitsByMonth[0].year;
          this.repository2Year = this.developerCompareResponse.developer2CommitsByMonth[0].year;
          this.preProcessCommits();
          this.drawChart();
        }
      }
    );
  }

  preProcessCommits() {
    this.processCommitsByMonth(this.developerCompareResponse.developer1CommitsByMonth, this.months1, this.commitsPerMonth1);
    this.processCommitsByMonth(this.developerCompareResponse.developer2CommitsByMonth, this.months2, this.commitsPerMonth2);
  }

  private processCommitsByMonth(commitsByMonth: CommitsByMonth[], months: string[], commitsPerMonth: number[]) {
    const monthList = ['January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'];

    commitsByMonth.forEach(commitByMonth => {
      months.push(monthList[commitByMonth.month]);
      commitsPerMonth.push(commitByMonth.commits);
    });
  }

  clearResult() {
    this.developerCompareResponse = new DeveloperCompareResponse();

    this.months1 = [];
    this.commitsPerMonth1 = [];

    this.months2 = [];
    this.commitsPerMonth2 = [];
  }

  drawChart() {
    this.chartOptions = {
      series: [
        {
          name: `${this.developerCompareResponse.developer1Name} (${this.repository1Year})`,
          data: this.commitsPerMonth1
        },
        {
          name: `${this.developerCompareResponse.developer2Name} (${this.repository2Year})`,
          data: this.commitsPerMonth2
        }
      ],
      chart: {
        height: 350,
        type: 'line',
        zoom: {
          enabled: false
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: 'straight'
      },
      grid: {
        row: {
          colors: ['#f3f3f3', 'transparent'],
          opacity: 0.5
        }
      },
      xaxis: {
        categories: this.months1
      }
    };
  }
}
