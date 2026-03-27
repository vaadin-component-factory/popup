import '@vaadin/vaadin-lumo-styles/spacing.js';
import { overlay } from '@vaadin/vaadin-lumo-styles/mixins/overlay.js';
import { css, registerStyles } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';

const popupOverlay = css`
  /* Pointer arrow theme */

  :host([theme~='pointer-arrow'][top-aligned][preferred-position='bottom']:not([phone])) {
    padding-top: 0.5rem;
  }

  :host([theme~='pointer-arrow'][top-aligned][preferred-position='bottom']:not([phone])) [part='pointer-arrow'] {
    position: absolute;
    border-left: 0.5rem solid transparent;
    border-right: 0.5rem solid transparent;
    border-bottom: 0.5rem solid var(--lumo-base-color);
    top: 0;
    height: 0;
    width: 0;

    filter: drop-shadow(0px -2px 1px var(--lumo-shade-10pct));
  }

  :host([theme~='pointer-arrow'][bottom-aligned][preferred-position='bottom']:not([phone])) {
    padding-bottom: 0.5rem;
  }

  /* the following will color the pointer arrow to the same color as the popup footer */
  :host([theme~='pointer-arrow'][has-footer][bottom-aligned][preferred-position='bottom']:not([phone]))
    [part='pointer-arrow']:after {
    content: '';
    display: block;
    position: absolute;
    border-left: 0.5rem solid transparent;
    border-right: 0.5rem solid transparent;
    border-top: 0.5rem solid var(--lumo-contrast-5pct);
    bottom: 0;
    left: -0.5rem;
    height: 0;
    width: 0;
  }

  :host([theme~='pointer-arrow'][bottom-aligned][preferred-position='bottom']:not([phone])) [part='pointer-arrow'] {
    position: absolute;
    border-left: 0.5rem solid transparent;
    border-right: 0.5rem solid transparent;
    border-top: 0.5rem solid var(--lumo-base-color);
    bottom: 0;
    height: 0;
    width: 0;

    filter: drop-shadow(0px 2px 1px var(--lumo-shade-10pct));
  }

  :host([theme~='pointer-arrow'][start-aligned][preferred-position='end']:not([phone])) {
    padding-inline-start: 0.5rem;
  }

  :host([theme~='pointer-arrow'][start-aligned][preferred-position='end']:not([phone])) [part='pointer-arrow'] {
    position: absolute;
    border-top: 0.5rem solid transparent;
    border-bottom: 0.5rem solid transparent;
    border-right: 0.5rem solid var(--lumo-base-color);
    left: 0;
    height: 0;
    width: 0;

    filter: drop-shadow(-2px 0 1px var(--lumo-shade-10pct));
  }

  :host([theme~='pointer-arrow'][end-aligned][preferred-position='end']:not([phone])) {
    padding-inline-end: 0.5rem;
  }

  :host([theme~='pointer-arrow'][end-aligned][preferred-position='end']:not([phone])) [part='pointer-arrow'] {
    position: absolute;
    border-top: 0.5rem solid transparent;
    border-bottom: 0.5rem solid transparent;
    border-left: 0.5rem solid var(--lumo-base-color);
    right: 0;
    height: 0;
    width: 0;

    filter: drop-shadow(2px 0 1px var(--lumo-shade-10pct));
  }
`;

registerStyles('vcf-popup-overlay', [overlay, popupOverlay], { moduleId: 'lumo-vcf-popup-overlay' });