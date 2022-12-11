import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'jsgenerator-configuration',
  templateUrl: './configuration.component.html'
})
export class ConfigurationComponent implements OnInit {
  #fb: FormBuilder;
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

  form?: FormGroup;

  constructor(fb: FormBuilder) {
    this.#fb = fb;
  }

  ngOnInit() {
    this.form = this.#fb.group({
      extension: ['.jsgenerator.js', Validators.required],
      targetElementSelector: [':root > body', Validators.required],
      variableDeclaration: ['CONST', Validators.required],
      variableNameStrategy: ['TYPE_BASED', Validators.required],
    });

    this.form.valueChanges.subscribe(console.log);
  }

  toggle() {
    this.opened = !this.opened;
  }
}
