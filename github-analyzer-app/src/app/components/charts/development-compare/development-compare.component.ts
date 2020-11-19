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

  private processCommitsByMonth(commitsByMonth: CommitsByMonth[], months: number[], commitsPerMonth: number[]) {
    commitsByMonth.forEach(commitByMonth => {
      months.push(commitByMonth.month);
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
          name: 'Repository 1 commits',
          data: this.commitsPerMonth1
        },
        {
          name: 'Repository 2 commits',
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
