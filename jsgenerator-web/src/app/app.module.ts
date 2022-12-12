import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ConfigurationComponent} from "./components/configuration.component";
import {PopupComponent} from "./components/popup.component";
import {ReactiveFormsModule} from "@angular/forms";
import {CONFIGURATION} from "./domain/configuration";
import {NavigationComponent} from "./components/navigation.component";

@NgModule({
  declarations: [
    AppComponent,
    PopupComponent,
    NavigationComponent,
    ConfigurationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [
    {
      provide: CONFIGURATION, useValue: {
        targetElementSelector: ':root > body',
        variableNameStrategy: 'TYPE_BASED',
        extension: '.jsgenerator.js',
        variableDeclaration: 'CONST',
        popupOpened: false,
      },
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
