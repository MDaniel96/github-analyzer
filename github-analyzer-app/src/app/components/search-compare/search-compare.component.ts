import {Component} from '@angular/core';
import {DoubleRepositoryService} from '../../service/double-repository.service';
import {expandAnimation} from '../../util/animations';

@Component({
  selector: 'app-search-compare',
  templateUrl: './search-compare.component.html',
  styleUrls: ['./search-compare.component.css'],
  animations: [expandAnimation]
})
export class SearchCompareComponent {

  constructor(public doubleRepositoryService: DoubleRepositoryService) {
  }

  isSearchDisabled() {
    return this.doubleRepositoryService.request.repository1Url === '' || this.doubleRepositoryService.request.repository2Url === '' ||
      (this.doubleRepositoryService.request.isRepository1Private && this.doubleRepositoryService.request.access1Token === '') ||
      (this.doubleRepositoryService.request.isRepository2Private && this.doubleRepositoryService.request.access2Token === '');
  }
}
