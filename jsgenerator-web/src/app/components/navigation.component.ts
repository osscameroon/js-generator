import {Component, Inject} from "@angular/core";
import {Configuration, CONFIGURATION} from "../domain/configuration";

@Component({
  selector: 'jsgenerator-navigation',
  templateUrl: './navigation.component.html',
})
export class NavigationComponent {
  constructor(@Inject(CONFIGURATION) public configuration: Configuration) {
  }
}
