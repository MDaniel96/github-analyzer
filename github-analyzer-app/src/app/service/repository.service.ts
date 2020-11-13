import {EventEmitter} from '@angular/core';
import {HttpClient} from '@angular/common/http';

export abstract class RepositoryService {

  analyzeStarted = new EventEmitter<void>();
  analyzeFailed = new EventEmitter<void>();

  httpClient: HttpClient;

  protected constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  abstract getAPI(): string;

  abstract getRequest();

  analyze() {
    this.analyzeStarted.emit();
    const url = this.getAPI() + '/analyze';
    return this.httpClient.post(url, this.getRequest()).subscribe(
      () => {
      },
      () => {
        alert('Error occurred: Invalid repository url or access token provided.');
        this.analyzeFailed.emit();
      }
    );
  }
}
