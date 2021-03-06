import {Component, ViewChild} from '@angular/core';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {ContributionResponse} from '../../../model/contribution-response.model';
import {ChartsComponent} from '../charts.component';
import {fadeInAnimation} from '../../../util/animations';
import {ChartComponent} from 'ng-apexcharts';

@Component({
  selector: 'app-contribution',
  templateUrl: './contribution.component.html',
  styleUrls: ['./contribution.component.css'],
  animations: [fadeInAnimation]
})

export class ContributionComponent extends ChartsComponent {

  @ViewChild('chart') chart: ChartComponent;

  contributionResponse: ContributionResponse;

  chartOptions: Partial<any>;

  totalCommits: number;
  developerNames: string[] = [];
  developerCommitNumbersPercent: number[] = [];

  constructor(public singleRepositoryService: SingleRepositoryService) {
    super();
    this.checkAnalyze(singleRepositoryService);
  }

  getResultCallback = () => {
    this.singleRepositoryService.getContributionResult().subscribe(
      data => {
        if (data !== null) {
          this.stopQueryData();
          this.contributionResponse = data;
          this.totalCommits = this.contributionResponse.totalCommits;
          this.preProcessDevelopers();
          this.drawChart();
        }
      }
    );
  }

  preProcessDevelopers() {
    this.contributionResponse.commitsByDevelopers.forEach(element => {
      this.developerNames.push(element.developerName);
      const percent = (element.commits / this.totalCommits * 100).toFixed(2);
      this.developerCommitNumbersPercent.push(Number(percent));
    });
  }

  clearResult() {
    this.contributionResponse = new ContributionResponse();

    this.developerNames = [];
    this.developerCommitNumbersPercent = [];
  }

  drawChart() {
    this.chartOptions = {
      series: [
        {
          name: 'ContributionResponse',
          data: this.developerCommitNumbersPercent
        }
      ],
      chart: {
        height: 350,
        type: 'bar'
      },
      xaxis: {
        categories: this.developerNames
      }
    };
  }
}
