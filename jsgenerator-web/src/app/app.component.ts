import {Component} from '@angular/core';

@Component({
  selector: 'jsgenerator-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
})
export class AppComponent {
  code = `<div class="highlight">
    Lorem ipsum dolor sit amet...
</div>`;
}
