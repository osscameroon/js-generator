import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";

@Component({
  selector: 'jsgenerator-popup',
  templateUrl: './popup.component.html'
})
export class PopupComponent implements OnInit {
  private static listenerRegistered = false;

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
    if (!PopupComponent.listenerRegistered) {
      addEventListener('keyup', event => {
        const IS_ESCAPE_KEY = [event.key, event.code].includes('Escape');
        const MAY_BE_SHORTCUT = event.altKey || event.ctrlKey || event.metaKey || event.shiftKey || event.isComposing;

        if (IS_ESCAPE_KEY && !MAY_BE_SHORTCUT && this.opened) {
          this.opened = false;
        }
      });
      PopupComponent.listenerRegistered = true;
    }
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
