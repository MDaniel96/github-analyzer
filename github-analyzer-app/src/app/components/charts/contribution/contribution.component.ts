import {Component} from '@angular/core';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {ContributionResponse} from '../../../model/contribution-response.model';
import {ChartsComponent} from '../charts.component';

@Component({
  selector: 'app-contribution',
  templateUrl: './contribution.component.html',
  styleUrls: ['./contribution.component.css']
})
export class ContributionComponent extends ChartsComponent {

  contributionResponse: ContributionResponse;

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
          this.drawChart();
        }
      }
    );
  }

  clearResult() {
    this.contributionResponse = new ContributionResponse();
  }

  drawChart() {
    // TODO: Draw contribution diagram from <this.contributionResponse>
  }
}
