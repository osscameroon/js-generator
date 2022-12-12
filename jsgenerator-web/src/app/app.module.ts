import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ConfigurationComponent} from "./components/configuration.component";
import {PopupComponent} from "./components/popup.component";
import {ReactiveFormsModule} from "@angular/forms";
import {CONFIGURATION} from "./domain/configuration";
import {NavigationComponent} from "./components/navigation.component";
import {FooterComponent} from "./components/footer.component";
import {MonacoEditorService} from "./components/monaco-editor.service";

@NgModule({
  declarations: [
    AppComponent,
    PopupComponent,
    FooterComponent,
    NavigationComponent,
    ConfigurationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [
    MonacoEditorService,
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
