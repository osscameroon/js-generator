import {Component, Inject} from '@angular/core';
import {CONFIGURATION, Configuration} from "./domain/configuration";

@Component({
  selector: 'jsgenerator-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  constructor(@Inject(CONFIGURATION) public configuration: Configuration) {
  }
}
