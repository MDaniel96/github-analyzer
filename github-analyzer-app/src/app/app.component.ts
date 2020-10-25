import {Component} from '@angular/core';
import {TestService} from './service/test.service';
import {TestMessage} from './model/test-message.model';
import {SingleRepositoryResult} from './model/single-repository-result.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  testMessage: TestMessage;

  constructor(private testService: TestService) {
    const singleRepositoryResult = this.getSingleRepositoryResultTest();
    console.log('Test result', singleRepositoryResult);
  }

  test() {
    this.testService.getTestMessage().subscribe(
      message => {
        this.testMessage = message;
      }
    );
  }

  private getSingleRepositoryResultTest(): SingleRepositoryResult {
    return {
      repositoryName: 'github-analyzer',

      contributionResponse: {
        totalCommits: 40,
        commitsByDevelopers: [
          {
            developerName: 'Dev1',
            commits: 14
          },
          {
            developerName: 'Dev2',
            commits: 18
          },
          {
            developerName: 'Dev3',
            commits: 4
          },
          {
            developerName: 'Dev4',
            commits: 38
          }
        ]
      },

      modificationResponse: {
        modificationsByDate: [
          {
            year: 2019,
            month: 1,
            addedLines: 211,
            removedLines: 94
          },
          {
            year: 2019,
            month: 2,
            addedLines: 91,
            removedLines: 45
          },
          {
            year: 2020,
            month: 1,
            addedLines: 100,
            removedLines: 79
          },
          {
            year: 2020,
            month: 2,
            addedLines: 791,
            removedLines: 123
          }
        ]
      },

      distributionResponse: {
        averageCommitsByMonth: [
          {
            month: 1,
            commits: 41
          },
          {
            month: 2,
            commits: 56
          }
        ],
        averageCommitsByDay: [
          {
            day: 1,
            commits: 2
          },
          {
            day: 2,
            commits: 5
          }
        ],
        averageCommitsByDayPeriods: [
          {
            period: '08-12',
            commits: 0
          },
          {
            period: '12-16',
            commits: 3
          }
        ]
      }
    };
  }
}
