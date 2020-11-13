import {CommitsByMonth} from './commits-by-month.model';

export class DevelopmentCompareResponse {
  repository1Developers: number;
  repository2Developers: number;

  repository1CommitsByMonth: CommitsByMonth[];
  repository2CommitsByMonth: CommitsByMonth[];
}
