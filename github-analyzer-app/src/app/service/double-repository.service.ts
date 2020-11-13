import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {DoubleRepositoryRequest} from '../model/double-repository-request.model';
import {DevelopmentCompareResponse} from '../model/development-compare-response.model';
import {DeveloperCompareResponse} from '../model/developer-compare-response.model';
import {RepositoryService} from './repository.service';

export const DOUBLE_REPOSITORY_API = environment.apiUrl + '/api/repository/double';

@Injectable()
export class DoubleRepositoryService extends RepositoryService {

  request: DoubleRepositoryRequest = new DoubleRepositoryRequest();

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  getAPI(): string {
    return DOUBLE_REPOSITORY_API;
  }

  getRequest() {
    return this.request;
  }

  getDevelopmentCompareResult() {
    const url = DOUBLE_REPOSITORY_API + '/development';
    return this.httpClient.post<DevelopmentCompareResponse>(url, this.request);
  }

  getDeveloperCompareResult() {
    const url = DOUBLE_REPOSITORY_API + '/developer';
    return this.httpClient.post<DeveloperCompareResponse>(url, this.request);
  }
}
