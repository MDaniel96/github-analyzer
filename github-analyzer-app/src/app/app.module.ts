import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {SingleRepositoryComponent} from './pages/single-repository/single-repository.component';
import {DoubleRepositoryComponent} from './pages/double-repository/double-repository.component';
import {AppRoutingModule} from './app-routing.module';
import {HeaderComponent} from './components/header/header.component';
import {SearchComponent} from './components/search/search.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {FormsModule} from '@angular/forms';
import {SingleRepositoryService} from './service/single-repository.service';
import {ContributionComponent} from './components/charts/contribution/contribution.component';
import {ModificationComponent} from './components/charts/modification/modification.component';
import {DistributionComponent} from './components/charts/distribution/distribution.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatExpansionModule} from '@angular/material/expansion';
import {DevelopmentCompareComponent} from './components/charts/development-compare/development-compare.component';
import {DeveloperCompareComponent} from './components/charts/developer-compare/developer-compare.component';
import {SearchCompareComponent} from './components/search-compare/search-compare.component';
import {DoubleRepositoryService} from './service/double-repository.service';
import {NgApexchartsModule} from 'ng-apexcharts';
import {MatSelectModule} from '@angular/material/select';

@NgModule({
  declarations: [
    AppComponent,
    SingleRepositoryComponent,
    DoubleRepositoryComponent,
    HeaderComponent,
    SearchComponent,
    ContributionComponent,
    ModificationComponent,
    DistributionComponent,
    DevelopmentCompareComponent,
    DeveloperCompareComponent,
    SearchCompareComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatCheckboxModule,
    MatButtonModule,
    FormsModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    NgApexchartsModule,
    MatSelectModule
  ],
  providers: [
    SingleRepositoryService,
    DoubleRepositoryService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
