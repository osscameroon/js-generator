import {Component, EventEmitter, Input, Output, ViewChild} from "@angular/core";
import {PopupComponent} from "./popup.component";

@Component({
  selector: 'jsgenerator-configuration',
  templateUrl: './configuration.component.html'
})
export class ConfigurationComponent {
  #popupComponent?: PopupComponent = undefined;
  #opened = false;

  @Output()
  openedChange = new EventEmitter<boolean>();

  @Input()
  set opened(value: boolean) {
    if (value !== this.#opened) {
      this.#opened = value;
      this.openedChange.emit(value);
    }
  }

  get opened(): boolean {
    return this.#opened;
  }

  toggle() {
    this.opened = !this.opened;
  }

  @ViewChild(PopupComponent)
  set popupComponent(value: PopupComponent) {
    this.#popupComponent = value;
  }
}
