import {Component, ViewChild} from '@angular/core';
import {ChartsComponent} from '../charts.component';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {ModificationResponse} from '../../../model/modification-response.model';
import {fadeInAnimation} from '../../../util/animations';
import {ApexChart, ApexNonAxisChartSeries, ApexResponsive, ChartComponent} from 'ng-apexcharts';
import {MatSelectChange} from '@angular/material/select';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any;
};

@Component({
  selector: 'app-modification',
  templateUrl: './modification.component.html',
  styleUrls: ['./modification.component.css'],
  animations: [fadeInAnimation]
})
export class ModificationComponent extends ChartsComponent {
  @ViewChild('chart') chart: ChartComponent;

  chartOptions: Partial<ChartOptions>;

  modificationResponse: ModificationResponse;

  years: Set<number>;
  months: number[];

  selectedYear = 0;
  selectedMonth = 0;

  constructor(public singleRepositoryService: SingleRepositoryService) {
    super();
    this.checkAnalyze(singleRepositoryService);
  }

  getResultCallback = () => {
    this.singleRepositoryService.getModificationResult().subscribe(
      data => {
        if (data !== null) {
          this.stopQueryData();
          this.modificationResponse = data;
          this.preProcessCommits();
        }
      }
    );
  }

  preProcessCommits() {
    this.modificationResponse.modificationsByDate.sort((y1, y2) => (y1 > y2 ? 1 : -1));
    this.years = new Set(this.modificationResponse.modificationsByDate.map(m => m.year));
    this.selectedYear = this.years.values().next().value;
    this.setMonthsBySelectedYear();
  }

  private setMonthsBySelectedYear() {
    this.months = this.modificationResponse.modificationsByDate
      .filter(m => m.year === this.selectedYear)
      .map(m => m.month);
    this.selectedMonth = this.months[0];
    this.drawChart();
  }

  private getAddedLines(): number {
    return this.modificationResponse.modificationsByDate
      .find(m => m.year === this.selectedYear &&
        m.month === this.selectedMonth).addedLines;
  }

  private getRemovedLines(): number {
    return this.modificationResponse.modificationsByDate
      .find(m => m.year === this.selectedYear &&
        m.month === this.selectedMonth).removedLines;
  }

  clearResult() {
    this.modificationResponse = new ModificationResponse();
  }

  drawChart() {
    this.chartOptions = {
      series: [this.getAddedLines(), this.getRemovedLines()],
      chart: {
        width: 480,
        type: 'pie'
      },
      labels: ['Added lines', 'Removed lines'],
      responsive: [
        {
          breakpoint: 520,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }
      ]
    };
  }

  yearSelected(event: MatSelectChange) {
    this.selectedYear = event.value;
    this.setMonthsBySelectedYear();
  }

  monthSelected(event: MatSelectChange) {
    this.selectedMonth = event.value;
    this.drawChart();
  }

  displayMonth(month: number): string {
    const monthList = ['January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'];
    return monthList[month];
  }
}
