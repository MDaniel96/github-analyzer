import {RepositoryService} from '../../service/repository.service';

export abstract class ChartsComponent {

  visible = false;
  intervalId = null;
  panelOpen = true;

  abstract getResultCallback: () => void;

  abstract drawChart();

  abstract clearResult();

  checkAnalyze(service: RepositoryService) {
    service.analyzeStarted.subscribe(
      () => {
        this.startQueryData();
        this.clearResult();
      }
    );
    service.analyzeFailed.subscribe(
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
