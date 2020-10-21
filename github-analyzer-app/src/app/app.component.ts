import {Component} from '@angular/core';
import {TestService} from './service/test.service';
import {TestMessage} from './model/test-message.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  testMessage: TestMessage;

  constructor(private testService: TestService) {
  }

  test() {
    this.testService.getTestMessage().subscribe(
      message => {
        this.testMessage = message;
      }
    );
  }
}
