import {BehaviorSubject, filter, Observable} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable()
export class MonacoEditorService {
  #loading = new BehaviorSubject<boolean>(false);
  #loaded = this.#loading.pipe(filter(_ => _));

  public load(): Observable<true> {
    if (!this.#loading.value) {
      const BASE_URL = `./assets/monaco-editor/min/vs`;
      const target = window as { monaco?: {}, require?: Function & { config?: Function } };

      if ('object' === typeof target.monaco) {
        this.#loading.next(true);
      } else if (target.require?.config) {
        target.require.config({paths: {vs: `${BASE_URL}`}});
        this.#loading.next(true);
      } else {
        const script = document.createElement('script');
        script.addEventListener('load', () => {
          target.require?.config!({paths: {vs: `${BASE_URL}`}});
          target.require!(
            [`vs/editor/editor.main`],
            () => this.#loading.next(true));
        });
        script.src = `${BASE_URL}/loader.js`;
        script.type = 'text/javascript';
        document.body.appendChild(script);
      }
    }

    return this.#loaded as unknown as Observable<true>;
  }
}
