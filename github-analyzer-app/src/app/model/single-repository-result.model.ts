import {ContributionResponse} from './contribution-response.model';
import {ModificationResponse} from './modification-response.model';
import {DistributionResponse} from './distribution-response.model';

export class SingleRepositoryResult {
  repositoryName: string;

  contributionResponse: ContributionResponse;
  modificationResponse: ModificationResponse;
  distributionResponse: DistributionResponse;
}
