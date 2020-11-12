import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SingleRepositoryComponent} from './pages/single-repository/single-repository.component';
import {DoubleRepositoryComponent} from './pages/double-repository/double-repository.component';

const routes: Routes = [
  {
    path: '', redirectTo: 'single-repository', pathMatch: 'full'
  },
  {
    path: 'single-repository', component: SingleRepositoryComponent, pathMatch: 'full',
  },
  {
    path: 'double-repository', component: DoubleRepositoryComponent, pathMatch: 'full',
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
