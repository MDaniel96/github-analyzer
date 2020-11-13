import {Component} from '@angular/core';
import {ChartsComponent} from '../charts.component';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {DistributionResponse} from '../../../model/distribution-response.model';
import {fadeInAnimation} from '../../../util/animations';

@Component({
  selector: 'app-distribution',
  templateUrl: './distribution.component.html',
  styleUrls: ['./distribution.component.css'],
  animations: [fadeInAnimation]
})
export class DistributionComponent extends ChartsComponent {

  distributionResponse: DistributionResponse;

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
          this.drawChart();
        }
      }
    );
  }

  clearResult() {
    this.distributionResponse = new DistributionResponse();
  }

  drawChart() {
    // TODO: Draw distribution diagram from <this.distributionResponse>
  }
}
