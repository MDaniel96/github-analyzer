import {Component, ViewChild} from '@angular/core';
import {ChartsComponent} from '../charts.component';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {DistributionResponse} from '../../../model/distribution-response.model';
import {fadeInAnimation} from '../../../util/animations';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexGrid,
  ApexStroke,
  ApexTitleSubtitle,
  ApexXAxis,
  ChartComponent
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
};

@Component({
  selector: 'app-distribution',
  templateUrl: './distribution.component.html',
  styleUrls: ['./distribution.component.css'],
  animations: [fadeInAnimation]
})
export class DistributionComponent extends ChartsComponent {

  @ViewChild('chart') chart: ChartComponent;

  public chartOptionsMonth: Partial<ChartOptions>;
  public chartOptionsDay: Partial<ChartOptions>;
  public chartOptionsPeriod: Partial<ChartOptions>;

  distributionResponse: DistributionResponse;

  months: string[] = [];
  commitsPerMonth: number[] = [];

  days: string[] = [];
  commitsPerDay: number[] = [];

  periods: string[] = [];
  commitsPerPeriods: number[] = [];

  constructor(public singleRepositoryService: SingleRepositoryService) {
    super();
    this.checkAnalyze(singleRepositoryService);
  }

  getResultCallback = () => {
    this.singleRepositoryService.getDistributionResult().subscribe(
      data => {
        if (data !== null) {
          this.stopQueryData();
          this.distributionResponse = data;
          this.preProcessCommits();
          this.drawChart();
        }
      }
    );
  }

  preProcessCommits() {
    const months = ['January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'];
    const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

    this.distributionResponse.averageCommitsByMonth.forEach(element => {
      this.months.push(months[element.month]);
      this.commitsPerMonth.push(element.commits);
    });

    this.distributionResponse.averageCommitsByDay.forEach(element => {
      this.days.push(days[element.day]);
      this.commitsPerDay.push(element.commits);
    });

    this.distributionResponse.averageCommitsByDayPeriods.forEach(element => {
      this.periods.push(element.period);
      this.commitsPerPeriods.push(Math.round(element.commits * 100) / 100);
    });
  }

  clearResult() {
    this.distributionResponse = new DistributionResponse();

    this.months = [];
    this.commitsPerMonth = [];
    this.days = [];
    this.commitsPerDay = [];
    this.periods = [];
    this.commitsPerPeriods = [];
  }

  drawChart() {
    this.chartOptionsMonth = {
      series: [
        {
          name: 'Average commits',
          data: this.commitsPerMonth
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
        categories: this.months
      }
    };
    this.chartOptionsDay = {
      series: [
        {
          name: 'Average commits',
          data: this.commitsPerDay
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
        categories: this.days
      }
    };
    this.chartOptionsPeriod = {
      series: [
        {
          name: 'Average commits',
          data: this.commitsPerPeriods
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
        categories: this.periods
      }
    };
  }
}
