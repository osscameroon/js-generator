import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";

@Component({
  selector: 'jsgenerator-popup',
  templateUrl: './popup.component.html'
})
export class PopupComponent implements OnInit {
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

  ngOnInit() {
    addEventListener('keyup', event => {
      if ([event.key, event.code].includes('Escape') && this.opened) {
        this.opened = false;
      }
    })
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
