import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: 'jsgenerator-popup',
  templateUrl: './popup.component.html'
})
export class PopupComponent {
  #opened = false;

  @Output()
  openedChange = new EventEmitter<boolean>();

  @Input()
  title = 'Call for Action';

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

  open() {
    this.opened = true;
  }

  close() {
    this.opened = false;
  }

  toggle() {
    this.opened = !this.#opened;
  }
}
