import {Component} from '@angular/core';
import {DevelopmentCompareResponse} from '../../../model/development-compare-response.model';
import {DoubleRepositoryService} from '../../../service/double-repository.service';
import {ChartsComponent} from '../charts.component';
import {fadeInAnimation} from '../../../util/animations';

@Component({
  selector: 'app-development-compare',
  templateUrl: './development-compare.component.html',
  styleUrls: ['./development-compare.component.css'],
  animations: [fadeInAnimation]
})
export class DevelopmentCompareComponent extends ChartsComponent {

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
          this.drawChart();
        }
      }
    );
  }

  clearResult() {
    this.developmentCompareResponse = new DevelopmentCompareResponse();
  }

  drawChart() {
    // TODO: Draw developmentCompare diagram from <this.developmentCompare>
  }
}
