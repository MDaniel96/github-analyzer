import {animate, state, style, transition, trigger} from '@angular/animations';

export const expandAnimation = [
  trigger('expandCollapse', [
    state('expandCollapseState', style({height: '*'})),
    transition('* => void', [style({height: '*'}), animate(100, style({height: '0'}))]),
    transition('void => *', [style({height: '0'}), animate(100, style({height: '*'}))])
  ])
];

export const fadeInAnimation = [
  trigger('fadeIn', [
    state('in', style({opacity: 1})),
    transition(':enter', [
      style({opacity: 0}),
      animate(300)
    ]),
    transition(':leave',
      animate(300, style({opacity: 0})))
  ])
];
