import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {SingleRepositoryRequest} from '../model/single-repository-request.model';
import {ContributionResponse} from '../model/contribution-response.model';
import {DistributionResponse} from '../model/distribution-response.model';
import {ModificationResponse} from '../model/modification-response.model';

export const SINGLE_REPOSITORY_API = environment.apiUrl + '/api/repository/single';

@Injectable()
export class SingleRepositoryService {

  request: SingleRepositoryRequest = new SingleRepositoryRequest();

  analyzeStarted = new EventEmitter<void>();
  analyzeFailed = new EventEmitter<void>();

  constructor(private httpClient: HttpClient) {
  }

  analyze() {
    this.analyzeStarted.emit();
    const url = SINGLE_REPOSITORY_API + '/analyze';
    return this.httpClient.post(url, this.request).subscribe(
      () => {
      },
      () => {
        alert('Error occurred: Invalid repository url or access token provided.');
        this.analyzeFailed.emit();
      }
    );
  }

  getContributionResult() {
    const url = SINGLE_REPOSITORY_API + '/contribution';
    return this.httpClient.post<ContributionResponse>(url, this.request);
  }

  getDistributionResult() {
    const url = SINGLE_REPOSITORY_API + '/distribution';
    return this.httpClient.post<DistributionResponse>(url, this.request);
  }

  getModificationResult() {
    const url = SINGLE_REPOSITORY_API + '/modification';
    return this.httpClient.post<ModificationResponse>(url, this.request);
  }
}
