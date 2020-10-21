import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {TestMessage} from '../test-message.model';

export const TEST_API = environment.apiUrl + '/api/test';

@Injectable()
export class TestService {

  constructor(private httpClient: HttpClient) {
  }

  getTestMessage() {
    return this.httpClient.get<TestMessage>(TEST_API);
  }
}
