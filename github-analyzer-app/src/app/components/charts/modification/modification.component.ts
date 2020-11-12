import {Component} from '@angular/core';
import {ChartsComponent} from '../charts.component';
import {SingleRepositoryService} from '../../../service/single-repository.service';
import {ModificationResponse} from '../../../model/modification-response.model';

@Component({
  selector: 'app-modification',
  templateUrl: './modification.component.html',
  styleUrls: ['./modification.component.css']
})
export class ModificationComponent extends ChartsComponent {

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
  }
}
