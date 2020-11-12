import {SingleRepositoryService} from '../../service/single-repository.service';

export abstract class ChartsComponent {

  visible = false;
  intervalId = null;

  abstract getResultCallback: () => void;

  abstract drawChart();

  abstract clearResult();

  checkAnalyze(singleRepositoryService: SingleRepositoryService) {
    singleRepositoryService.analyzeStarted.subscribe(
      () => {
        this.startQueryData();
        this.clearResult();
      }
    );
    singleRepositoryService.analyzeFailed.subscribe(
      () => {
        this.stopQueryData();
      }
    );
  }

  startQueryData() {
    this.visible = true;
    this.intervalId = setInterval(this.getResultCallback, 300);
  }

  stopQueryData() {
    clearInterval(this.intervalId);
    this.intervalId = null;
  }

  toString(object: any): string {
    return JSON.stringify(object, null, ' ');
  }
}
