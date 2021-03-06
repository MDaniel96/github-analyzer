import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {SingleRepositoryRequest} from '../model/single-repository-request.model';
import {ContributionResponse} from '../model/contribution-response.model';
import {DistributionResponse} from '../model/distribution-response.model';
import {ModificationResponse} from '../model/modification-response.model';
import {RepositoryService} from './repository.service';

export const SINGLE_REPOSITORY_API = environment.apiUrl + '/api/repository/single';

@Injectable()
export class SingleRepositoryService extends RepositoryService {

  request: SingleRepositoryRequest = new SingleRepositoryRequest();

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  getAPI(): string {
    return SINGLE_REPOSITORY_API;
  }

  getRequest() {
    return this.request;
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
