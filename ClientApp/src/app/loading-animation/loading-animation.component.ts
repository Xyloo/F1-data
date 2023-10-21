import { trigger, style, animate, state, transition } from '@angular/animations';
import { Component } from '@angular/core';

@Component({
  selector: 'app-loading-animation',
  templateUrl: './loading-animation.component.html',
  styleUrls: ['./loading-animation.component.css'],
  animations: [
    trigger('spinAnimation', [
      state('spin', style({ transform: 'rotate(360deg)' })),
      transition('* => spin', animate('2s'))
    ])
  ]
})
export class LoadingAnimationComponent {
}
