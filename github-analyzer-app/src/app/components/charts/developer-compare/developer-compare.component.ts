import {Component} from '@angular/core';
import {ChartsComponent} from '../charts.component';
import {DoubleRepositoryService} from '../../../service/double-repository.service';
import {DeveloperCompareResponse} from '../../../model/developer-compare-response.model';
import {fadeInAnimation} from '../../../util/animations';

@Component({
  selector: 'app-developer-compare',
  templateUrl: './developer-compare.component.html',
  styleUrls: ['./developer-compare.component.css'],
  animations: [fadeInAnimation]
})
export class DeveloperCompareComponent extends ChartsComponent {

  developerCompareResponse: DeveloperCompareResponse;

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
          this.drawChart();
        }
      }
    );
  }

  clearResult() {
    this.developerCompareResponse = new DeveloperCompareResponse();
  }

  drawChart() {
    // TODO: Draw developerCompare diagram from <this.developerCompareResult>
  }
}
