export class ContributionResponse {
  totalCommits: number;
  commitsByDevelopers: CommitsByDeveloper[];
}

export class CommitsByDeveloper {
  developerName: string;
  commits: number;
}
