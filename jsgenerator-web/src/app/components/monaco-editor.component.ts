import {MonacoEditorService} from "./monaco-editor.service";
import {
  Component,
  DoCheck,
  HostListener,
  Input,
  OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
  ViewContainerRef
} from "@angular/core";
import type * as Monaco from "monaco-editor";
import {editor} from "monaco-editor";
import {filter, first, Observable, Subject, Subscription} from "rxjs";
import {
  AbstractControl,
  ControlValueAccessor,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator
} from "@angular/forms";
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;
import IDimension = editor.IDimension;
import IStandaloneEditorConstructionOptions = editor.IStandaloneEditorConstructionOptions;
import {fn} from "@angular/compiler/src/output/output_ast";

declare const monaco: typeof Monaco;

@Component({
  host: {
    '(blur)': 'onTouched?.()',
  },
  selector: 'jsgenerator-monaco-editor',
  templateUrl: './monaco-editor.component.html',
  providers: [
    {
      multi: true,
      provide: NG_VALIDATORS,
      useExisting: MonacoEditorComponent,
    },
    {
      multi: true,
      provide: NG_VALUE_ACCESSOR,
      useExisting: MonacoEditorComponent,
    },
  ],
})
export class MonacoEditorComponent implements OnInit, OnDestroy, DoCheck, Validator, ControlValueAccessor {
  #defaultOptions: IStandaloneEditorConstructionOptions = {
    autoClosingBrackets: 'always',
    automaticLayout: true,
    codeLens: true,
    language: 'html',
  };
  #monacoEditorService: MonacoEditorService;
  #dimension = new Subject<IDimension>();
  #dimensionSubscription?: Subscription;
  #viewContainerRef: ViewContainerRef;
  #previousValue?: string = undefined;
  #value?: string = undefined;
  #everRendered = false;

  @Input()
  options?: IStandaloneEditorConstructionOptions;
  @ViewChild('outlet', {read: ViewContainerRef})
  outletRef?: ViewContainerRef;
  @ViewChild('inlet', {read: TemplateRef})
  inletRef?: TemplateRef<any>;

  onChanged?: (value: string) => any;
  editor?: IStandaloneCodeEditor;
  onTouched?: () => any;

  constructor(monacoEditorService: MonacoEditorService,
              viewContainerRef: ViewContainerRef) {
    this.#monacoEditorService = monacoEditorService;
    this.#viewContainerRef = viewContainerRef;
  }

  ngOnInit() {
    this.#dimensionSubscription = this.#dimension
      .pipe(throttle<IDimension>(750))
      .pipe(filter(() => !!this.editor))
      .subscribe(size => this.#reRender(size));
    const subscription = this.#monacoEditorService.load().pipe(first()).subscribe(() => {
      const style = getComputedStyle(this.#viewContainerRef.element.nativeElement);
      this.#reRender({
        height: parseInt(style.height),
        width: parseInt(style.width),
      });
      subscription.unsubscribe();
    });
  }

  ngOnDestroy() {
    this.#dimensionSubscription?.unsubscribe();
    this.#dimension.complete();
  }

  ngDoCheck() {
    if (this.#everRendered) {
      const value = this.editor?.getModel()?.getValue();

      if (value !== this.#value) {
        this.#previousValue = this.#value;
        this.#value = value;
        setTimeout(() => this.onChanged?.(value!));
      }
    }
  }

  registerOnTouched(fn: () => void) {
    this.onTouched = fn;
  }

  registerOnChange(fn: (_: string) => void) {
    this.onChanged = fn;
  }

  writeValue(value: string) {
    this.#previousValue = this.#value;
    this.#value = value;
  }

  validate(control: AbstractControl): ValidationErrors | null {
    return null;
  }

  @HostListener('window:resize')
  onResizeEventHandler() {
    const {width: w, height: h} = getComputedStyle(this.#viewContainerRef.element.nativeElement);
    this.#dimension.next({width: parseInt(w), height: parseInt(h)});
  }

  #reRender(dimension: IDimension) {
    this.editor?.dispose();
    this.outletRef?.clear();
    this.outletRef?.createEmbeddedView(this.inletRef!);
    const root = this.#viewContainerRef.element.nativeElement.querySelector('div');
    setTimeout(() => {
      this.#everRendered = true;
      this.editor = monaco.editor.create(root, {
        ...this.#defaultOptions,
        ...this.options ?? {},
        dimension,
      });
      this.editor?.getModel?.()?.setValue?.(this.#value ?? '');
    }, 1_500);
  }
}

const throttle = <T>(blackoutMilliseconds: number) => (source: Observable<T>): Observable<T> => {
  const onTimeout = () => {
    handle = setTimeout(onTimeout, blackoutMilliseconds) as any as number;

    if (!collected) {
      subject.next(cachedValue);
      collected = true;
    }
  };
  const subscription = source.subscribe({
    error: reason => subject.error(reason),
    next: value => {
      cachedValue = value;
      collected = false;

      if (handle < 0) {
        onTimeout();
      }
    },
    complete: () => {
      subscription.unsubscribe();
      clearTimeout(handle);
      subject.complete();
    },
  });
  const subject = new Subject<T>();
  let collected = false;
  let cachedValue: T;
  let handle = -1;

  return subject;
};
