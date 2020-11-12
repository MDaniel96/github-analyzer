import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {SingleRepositoryComponent} from './pages/single-repository/single-repository.component';
import {DoubleRepositoryComponent} from './pages/double-repository/double-repository.component';
import {AppRoutingModule} from './app-routing.module';
import {HeaderComponent} from './components/header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    SingleRepositoryComponent,
    DoubleRepositoryComponent,
    HeaderComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
