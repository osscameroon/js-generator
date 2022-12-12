import {MonacoEditorService} from "./monaco-editor.service";
import {Component, ElementRef, HostBinding, OnInit, ViewChild} from "@angular/core";
import type * as Monaco from "monaco-editor";
import {first} from "rxjs";
import {editor} from "monaco-editor";
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;

declare const monaco: typeof Monaco;

@Component({
  selector: 'jsgenerator-monaco-editor',
  templateUrl: './monaco-editor.component.html',
})
export class MonacoEditorComponent implements OnInit {
  #monacoEditorService: MonacoEditorService;
  @ViewChild('monaco')
  monacoRef?: ElementRef<HTMLDivElement>;
  editor?: IStandaloneCodeEditor;

  constructor(monacoEditorService: MonacoEditorService) {
    this.#monacoEditorService = monacoEditorService;
  }

  ngOnInit() {
    const subscription = this.#monacoEditorService.load().pipe(first()).subscribe(() => {
      this.editor = monaco.editor.create(this.monacoRef?.nativeElement!, {
        autoClosingBrackets: 'always',
        automaticLayout: true,
        codeLens: true,
        dimension: {
          width: 200,
          height: 300,
        },
        language: 'html',
      });
      subscription.unsubscribe();
    });
  }
}
