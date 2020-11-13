import {Component} from '@angular/core';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {ContributionResponse} from '../../../model/contribution-response.model';
import {ChartsComponent} from '../charts.component';
import {fadeInAnimation} from '../../../util/animations';

@Component({
  selector: 'app-contribution',
  templateUrl: './contribution.component.html',
  styleUrls: ['./contribution.component.css'],
  animations: [fadeInAnimation]
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
