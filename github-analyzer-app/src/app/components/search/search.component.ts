import {Component} from '@angular/core';
import {SingleRepositoryService} from '../../service/single-repository.service';
import {expandAnimation} from '../../util/animations';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  animations: [expandAnimation]
})
export class SearchComponent {

  constructor(public singleRepositoryService: SingleRepositoryService) {
  }

  isSearchDisabled() {
    return this.singleRepositoryService.request.repositoryUrl === '' ||
      (this.singleRepositoryService.request.isPrivate && this.singleRepositoryService.request.accessToken === '');
  }
}
