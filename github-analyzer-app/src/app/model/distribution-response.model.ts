export class DistributionResponse {
  averageCommitsByMonth: AverageCommitsByMonth[];
  averageCommitsByDay: AverageCommitsByDay[];
  averageCommitsByDayPeriods: AverageCommitsByDayPeriods[];
}

export class AverageCommitsByMonth {
  month: number;
  commits: number;
}

export class AverageCommitsByDay {
  day: number;
  commits: number;
}

export class AverageCommitsByDayPeriods {
  period: string;
  commits: number;
}
