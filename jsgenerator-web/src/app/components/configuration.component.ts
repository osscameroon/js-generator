import {Component, HostBinding, Inject, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Configuration, CONFIGURATION} from "../domain/configuration";

@Component({
  selector: 'jsgenerator-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.sass'],
})
export class ConfigurationComponent implements OnInit {
  #fb: FormBuilder;
  form?: FormGroup;

  @HostBinding('class.none')
  get closed() {
    return !this.configuration.popupOpened;
  }

  constructor(@Inject(CONFIGURATION) public configuration: Configuration, fb: FormBuilder) {
    this.#fb = fb;
  }

  ngOnInit() {
    this.form = this.#fb.group({
      extension: [this.configuration.extension, Validators.required],
      variableDeclaration: [this.configuration.variableDeclaration, Validators.required],
      variableNameStrategy: [this.configuration.variableNameStrategy, Validators.required],
      targetElementSelector: [this.configuration.targetElementSelector, Validators.required],
    });

    this.form.valueChanges.subscribe(value => this.configuration = {
      ...this.configuration,
      ...value,
    });
  }
}
