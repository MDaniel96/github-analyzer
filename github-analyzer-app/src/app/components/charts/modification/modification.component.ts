import { Component, ViewChild } from "@angular/core";
import {ChartsComponent} from '../charts.component';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {ModificationResponse} from '../../../model/modification-response.model';
import {fadeInAnimation} from '../../../util/animations';
import { ChartComponent } from "ng-apexcharts";

import {
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexChart
} from "ng-apexcharts";

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
  @ViewChild("chart") chart: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  modificationResponse: ModificationResponse;

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
          this.drawChart();
        }
      }
    );
  }

  clearResult() {
    this.modificationResponse = new ModificationResponse();
  }

  drawChart() {
    // TODO: Draw modification diagram from <this.modificationResponse>
    this.chartOptions = {
      series: [7503, 2955],
      chart: {
        width: 380,
        type: "pie"
      },
      labels: ["Addition", "Remove"],
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: "bottom"
            }
          }
        }
      ]
    };
  }
}
