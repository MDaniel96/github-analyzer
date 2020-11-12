import {Component} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {SingleRepositoryService} from '../../service/single-repository.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  animations: [
    trigger('expandCollapse', [
      state('expandCollapseState', style({height: '*'})),
      transition('* => void', [style({height: '*'}), animate(100, style({height: '0'}))]),
      transition('void => *', [style({height: '0'}), animate(100, style({height: '*'}))])
    ])
  ]
})
export class SearchComponent {

  constructor(public singleRepositoryService: SingleRepositoryService) {
  }

  isSearchDisabled() {
    return this.singleRepositoryService.request.repositoryUrl === '' ||
      (this.singleRepositoryService.request.isPrivate && this.singleRepositoryService.request.accessToken === '');
  }
}
