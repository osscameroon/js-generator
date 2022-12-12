import {MonacoEditorService} from "./monaco-editor.service";
import {
  Component,
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
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;
import IDimension = editor.IDimension;
import IStandaloneEditorConstructionOptions = editor.IStandaloneEditorConstructionOptions;

declare const monaco: typeof Monaco;

@Component({
  selector: 'jsgenerator-monaco-editor',
  templateUrl: './monaco-editor.component.html',
})
export class MonacoEditorComponent implements OnInit, OnDestroy {
  #monacoEditorService: MonacoEditorService;
  #dimension = new Subject<IDimension>();
  #dimensionSubscription?: Subscription;
  #viewContainerRef: ViewContainerRef;

  @Input()
  options: IStandaloneEditorConstructionOptions = {
    autoClosingBrackets: 'always',
    automaticLayout: true,
    codeLens: true,
    language: 'html',
  };

  @ViewChild('outlet', {read: ViewContainerRef})
  outletRef?: ViewContainerRef;
  @ViewChild('inlet', {read: TemplateRef})
  inletRef?: TemplateRef<any>;

  editor?: IStandaloneCodeEditor;

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
    setTimeout(() => this.editor = monaco.editor.create(root, {
      ...this.options, dimension,
    }), 1_500);
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

/*

<!DOCTYPE html>
<html>
    <head>
        <title>TITLE</title>
    </head>
    <body>
        lorem ipsum
    </body>
</html>

 */
