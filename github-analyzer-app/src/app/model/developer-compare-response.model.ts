import {CommitsByMonth} from './commits-by-month.model';

export class DeveloperCompareResponse {
  developer1Name: string;
  developer2Name: string;

  developer1CommitsByMonth: CommitsByMonth[];
  developer2CommitsByMonth: CommitsByMonth[];
}
