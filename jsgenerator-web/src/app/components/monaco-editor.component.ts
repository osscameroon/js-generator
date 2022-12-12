import {MonacoEditorService} from "./monaco-editor.service";
import {Component} from "@angular/core";

@Component({
  selector: 'jsgenerator-monaco-editor',
  templateUrl: './monaco-editor.component.html',
})
export class MonacoEditorComponent {
  constructor(private readonly monacoEditorService: MonacoEditorService) {
    monacoEditorService.load().subscribe(console.log);
  }
}
