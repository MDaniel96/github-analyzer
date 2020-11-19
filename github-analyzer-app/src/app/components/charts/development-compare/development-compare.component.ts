import {Component, ViewChild} from '@angular/core';
import {DevelopmentCompareResponse} from '../../../model/development-compare-response.model';
import {DoubleRepositoryService} from '../../../service/double-repository.service';
import {ChartsComponent} from '../charts.component';
import {fadeInAnimation} from '../../../util/animations';
import {ChartComponent} from 'ng-apexcharts';
import {ChartOptions} from '../distribution/distribution.component';
import {CommitsByMonth} from '../../../model/commits-by-month.model';

@Component({
  selector: 'app-development-compare',
  templateUrl: './development-compare.component.html',
  styleUrls: ['./development-compare.component.css'],
  animations: [fadeInAnimation]
})
export class DevelopmentCompareComponent extends ChartsComponent {

  @ViewChild('chart') chart: ChartComponent;

  public chartOptions: Partial<ChartOptions>;

  months1: string[] = [];
  commitsPerMonth1: number[] = [];

  months2: string[] = [];
  commitsPerMonth2: number[] = [];

  repository1Name = '';
  repository2Name = '';

  repository1Year: number;
  repository2Year: number;

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
          this.repository1Name = this.doubleRepositoryService.request.repository1Url.split('/')[4];
          this.repository2Name = this.doubleRepositoryService.request.repository2Url.split('/')[4];
          this.repository1Year = this.developmentCompareResponse.repository1CommitsByMonth[0].year;
          this.repository2Year = this.developmentCompareResponse.repository2CommitsByMonth[0].year;
          this.preProcessCommits();
          this.drawChart();
        }
      }
    );
  }

  preProcessCommits() {
    this.processCommitsByMonth(this.developmentCompareResponse.repository1CommitsByMonth, this.months1, this.commitsPerMonth1);
    this.processCommitsByMonth(this.developmentCompareResponse.repository2CommitsByMonth, this.months2, this.commitsPerMonth2);
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
    this.developmentCompareResponse = new DevelopmentCompareResponse();

    this.months1 = [];
    this.commitsPerMonth1 = [];

    this.months2 = [];
    this.commitsPerMonth2 = [];
  }

  drawChart() {
    this.chartOptions = {
      series: [
        {
          name: `${this.repository1Name} (${this.repository1Year})`,
          data: this.commitsPerMonth1
        },
        {
          name: `${this.repository2Name} (${this.repository2Year})`,
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
