import {Component, DoCheck, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";

@Component({
  selector: 'jsgenerator-popup',
  templateUrl: './popup.component.html',
})
export class PopupComponent implements OnInit, DoCheck {
  private static listenerRegistered = false;
  #previouslyOpened = false;

  @Output()
  openedChange = new EventEmitter<boolean>();
  @Input()
  opened = false;
  @Input()
  title = 'Call for Action';

  @ViewChild('box')
  boxRef?: ElementRef;

  constructor() {
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
    }

    this.#previouslyOpened = this.opened;
  }

  ngDoCheck() {
    if (this.#previouslyOpened !== this.opened) {
      setTimeout(() =>
        this.openedChange.emit(this.opened));
      this.#previouslyOpened = this.opened;
    }
  }

  onContainerClick(event: MouseEvent) {
    const {nativeElement: box} = this.boxRef!;

    if (box !== event.target && !box?.contains(event.target)) {
      this.close();
    }
  }

  open() {
    this.opened = true;
  }

  close() {
    this.opened = false;
  }

  toggle() {
    this.opened = !this.opened;
  }
}
