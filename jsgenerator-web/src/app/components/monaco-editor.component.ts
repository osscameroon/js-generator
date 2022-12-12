import {MonacoEditorService} from "./monaco-editor.service";
import {Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild, ViewContainerRef} from "@angular/core";
import type * as Monaco from "monaco-editor";
import {editor} from "monaco-editor";
import {first, map, Observable, Subject, Subscription} from "rxjs";
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;
import IDimension = editor.IDimension;

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

  @ViewChild('monaco')
  monacoRef?: ElementRef<HTMLDivElement>;

  editor?: IStandaloneCodeEditor;

  constructor(monacoEditorService: MonacoEditorService,
              viewContainerRef: ViewContainerRef) {
    this.#monacoEditorService = monacoEditorService;
    this.#viewContainerRef = viewContainerRef;
  }

  ngOnInit() {
    this.#dimensionSubscription = this.#dimension
      .pipe(throttle<IDimension>(500))
      .subscribe(console.log);
    const subscription = this.#monacoEditorService.load().pipe(first()).subscribe(() => {
      this.editor = monaco.editor.create(this.monacoRef?.nativeElement!, {
        autoClosingBrackets: 'always',
        automaticLayout: true,
        codeLens: true,
        dimension: {
          width: 251,
          height: 397,
        },
        language: 'html',
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
