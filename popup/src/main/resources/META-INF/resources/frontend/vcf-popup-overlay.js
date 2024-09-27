/*
 * Copyright 2000-2020 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import '@vaadin/polymer-legacy-adapter/template-renderer.js';
import '@vaadin/overlay';
import { Overlay } from '@vaadin/overlay/src/vaadin-overlay';
import { PositionMixin } from '@vaadin/overlay/src/vaadin-overlay-position-mixin';
import { css, registerStyles } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin';

registerStyles(
  'vcf-popup-overlay',
  css`
    [part='header'],
    [part='header-content'],
    [part='footer'] {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      flex: none;
      pointer-events: none;
      z-index: 1;
    }

    [part='header'] {
      flex-wrap: nowrap;
    }

    ::slotted([slot='header-content']),
    ::slotted([slot='title']),
    ::slotted([slot='footer']) {
      display: contents;
      pointer-events: auto;
    }

    ::slotted([slot='title']) {
      font: inherit !important;
      overflow-wrap: anywhere;
    }

    [part='header-content'] {
      flex: 1;
    }

    :host([has-title]) [part='header-content'],
    [part='footer'] {
      justify-content: flex-end;
    }

    :host(:not([has-title]):not([has-header])) [part='header'],
    :host(:not([has-header])) [part='header-content'],
    :host(:not([has-title])) [part='title'],
    :host(:not([has-footer])) [part='footer'] {
      display: none !important;
    }

    :host(:is([has-title], [has-header], [has-footer])) [part='content'] {
      height: auto;
    }

    .resizer-container {
      max-height: 100%;
      display: flex;
      flex-direction: column;
    }

    @media (min-height: 320px) {
      :host(:is([has-title], [has-header], [has-footer])) .resizer-container {
        overflow: hidden;
      }

      :host(:is([has-title], [has-header], [has-footer])) [part='content'] {
        flex: 1;
        overflow: auto;
      }
    }

    [part='content'] {
      min-width: 5em;
    }

    :host([has-bounds-set]) [part='overlay'] {
      max-width: none;
    }
  `,
  { moduleId: 'vcf-popup-overlay-styles' }
);

let memoizedTemplate;

class PopupOverlayElement extends PositionMixin(Overlay) {
  static get is() {
    return 'vcf-popup-overlay';
  }

  static get template() {
    if (!memoizedTemplate) {
      memoizedTemplate = super.template.cloneNode(true);
      const contentPart = memoizedTemplate.content.querySelector('[part="content"]');
      const overlayPart = memoizedTemplate.content.querySelector('[part="overlay"]');
      const resizerContainer = document.createElement('section');
      resizerContainer.id = 'resizerContainer';
      resizerContainer.classList.add('resizer-container');
      resizerContainer.appendChild(contentPart);
      overlayPart.appendChild(resizerContainer);

      const pointerArrow = document.createElement('div');
      pointerArrow.setAttribute('part', 'pointer-arrow');
      overlayPart.appendChild(pointerArrow);

      const headerContainer = document.createElement('header');
      headerContainer.setAttribute('part', 'header');
      resizerContainer.insertBefore(headerContainer, contentPart);

      const titleContainer = document.createElement('div');
      titleContainer.setAttribute('part', 'title');
      headerContainer.appendChild(titleContainer);

      const titleSlot = document.createElement('slot');
      titleSlot.setAttribute('name', 'title');
      titleContainer.appendChild(titleSlot);

      const headerContentContainer = document.createElement('div');
      headerContentContainer.setAttribute('part', 'header-content');
      headerContainer.appendChild(headerContentContainer);

      const headerSlot = document.createElement('slot');
      headerSlot.setAttribute('name', 'header-content');
      headerContentContainer.appendChild(headerSlot);

      const footerContainer = document.createElement('footer');
      footerContainer.setAttribute('part', 'footer');
      resizerContainer.appendChild(footerContainer);

      const footerSlot = document.createElement('slot');
      footerSlot.setAttribute('name', 'footer');
      footerContainer.appendChild(footerSlot);
    }
    return memoizedTemplate;
  }

  static get properties() {
    return {
      closeOnScroll: {
        type: Boolean,
        value: false,
        reflectToAttribute: true
      },

      headerTitle: String,

      headerRenderer: Function,

      footerRenderer: Function,

      /**
       * Position of the popup overlay with respect to its target.
       * Supported values:
       * `bottom` - under the target element
       * `end` - in LTR environment to the right of the target element, in RTL environment to the left
       */
      preferredPosition: {
        type: String,
        value: 'bottom',
        reflectToAttribute: true,
        observer: '__preferredPositionChanged'
      },

      /**
       * Alignment of the popup with respect to its target.
       * Supported values:
       * `center` - the popup will be aligned to the center of the target element
       * By default alignment is not set
       */
      preferredAlignment: {
        type: String,
        reflectToAttribute: true,
        observer: '__preferredAlignmentChanged'
      }
    };
  }

  static get observers() {
    return [
      '_headerFooterRendererChange(headerRenderer, footerRenderer, opened)',
      '_headerTitleChanged(headerTitle, opened)'
    ];
  }

  constructor() {
    super();

    this.__onScroll = this.__onScroll.bind(this);
  }

  connectedCallback() {
    super.connectedCallback();

    if (this.opened) {
      this.__addUpdatePositionEventListeners();
    }
  }

  /** @protected */
  disconnectedCallback() {
    super.disconnectedCallback();
    this.__removeUpdatePositionEventListeners();
  }

  /** @protected */
  ready() {
    super.ready();

    this.setAttribute('role', 'dialog');

    // Update overflow attribute on resize
    this.__resizeObserver = new ResizeObserver(() => {
      this.__updateOverflow();
    });
    this.__resizeObserver.observe(this.$.resizerContainer);

    // Update overflow attribute on scroll
    this.$.content.addEventListener('scroll', () => {
      this.__updateOverflow();
    });

    this._pointerArrow = this.shadowRoot.querySelector('[part="pointer-arrow"]');
  }

  __preferredPositionChanged(position) {
    if (position === 'bottom') {
      this.noHorizontalOverlap = false;
      this.noVerticalOverlap = true;
    } else if (position === 'end') {
      this.noHorizontalOverlap = true;
      this.noVerticalOverlap = false;
    }
  }

  __preferredAlignmentChanged() {
    this._updatePosition();
  }

  /** @private */
  __createContainer(slot) {
    const container = document.createElement('div');
    container.setAttribute('slot', slot);
    return container;
  }

  /** @private */
  __clearContainer(container) {
    container.innerHTML = '';
    // Whenever a Lit-based renderer is used, it assigns a Lit part to the node it was rendered into.
    // When clearing the rendered content, this part needs to be manually disposed of.
    // Otherwise, using a Lit-based renderer on the same node will throw an exception or render nothing afterward.
    delete container._$litPart$;
  }

  /** @private */
  __initContainer(container, slot) {
    if (container) {
      // Reset existing container in case if a new renderer is set.
      this.__clearContainer(container);
    } else {
      // Create the container, but wait to append it until requestContentUpdate is called.
      container = this.__createContainer(slot);
    }
    return container;
  }

  /** @private */
  _headerFooterRendererChange(headerRenderer, footerRenderer, opened) {
    const headerRendererChanged = this.__oldHeaderRenderer !== headerRenderer;
    this.__oldHeaderRenderer = headerRenderer;

    const footerRendererChanged = this.__oldFooterRenderer !== footerRenderer;
    this.__oldFooterRenderer = footerRenderer;

    const openedChanged = this._oldOpenedFooterHeader !== opened;
    this._oldOpenedFooterHeader = opened;

    // Set attributes here to update styles before detecting content overflow
    this.toggleAttribute('has-header', !!headerRenderer);
    this.toggleAttribute('has-footer', !!footerRenderer);

    if (headerRendererChanged) {
      if (headerRenderer) {
        this.headerContainer = this.__initContainer(this.headerContainer, 'header-content');
      } else if (this.headerContainer) {
        this.headerContainer.remove();
        this.headerContainer = null;
        this.__updateOverflow();
      }
    }

    if (footerRendererChanged) {
      if (footerRenderer) {
        this.footerContainer = this.__initContainer(this.footerContainer, 'footer');
      } else if (this.footerContainer) {
        this.footerContainer.remove();
        this.footerContainer = null;
        this.__updateOverflow();
      }
    }

    if (
      (headerRenderer && (headerRendererChanged || openedChanged)) ||
      (footerRenderer && (footerRendererChanged || openedChanged))
    ) {
      if (opened) {
        this.requestContentUpdate();
      }
    }
  }

  /** @private */
  _headerTitleChanged(headerTitle, opened) {
    this.toggleAttribute('has-title', !!headerTitle);

    if (opened && (headerTitle || this._oldHeaderTitle)) {
      this.requestContentUpdate();
    }
    this._oldHeaderTitle = headerTitle;
  }

  /** @private */
  _headerTitleRenderer() {
    if (this.headerTitle) {
      if (!this.headerTitleElement) {
        this.headerTitleElement = document.createElement('h3');
        this.headerTitleElement.setAttribute('slot', 'title');
        this.headerTitleElement.classList.add('draggable');
      }
      this.appendChild(this.headerTitleElement);
      this.headerTitleElement.textContent = this.headerTitle;
    } else if (this.headerTitleElement) {
      this.headerTitleElement.remove();
      this.headerTitleElement = null;
    }
  }

  requestContentUpdate() {
    super.requestContentUpdate();

    if (this.headerContainer) {
      // If a new renderer has been set, make sure to reattach the container
      if (!this.headerContainer.parentElement) {
        this.appendChild(this.headerContainer);
      }

      if (this.headerRenderer) {
        // Only call header renderer after the container has been initialized
        this.headerRenderer.call(this.owner, this.headerContainer, this.owner);
      }
    }

    if (this.footerContainer) {
      // If a new renderer has been set, make sure to reattach the container
      if (!this.footerContainer.parentElement) {
        this.appendChild(this.footerContainer);
      }

      if (this.footerRenderer) {
        // Only call header renderer after the container has been initialized
        this.footerRenderer.call(this.owner, this.footerContainer, this.owner);
      }
    }

    this._headerTitleRenderer();

    this.__updateOverflow();
  }

  /**
   * Safari 13 renders overflowing elements incorrectly.
   * This forces it to recalculate height.
   * @private
   */
  __forceSafariReflow() {
    const scrollPosition = this.$.resizerContainer.scrollTop;
    const overlay = this.$.overlay;
    overlay.style.display = 'block';

    requestAnimationFrame(() => {
      overlay.style.display = '';
      this.$.resizerContainer.scrollTop = scrollPosition;
    });
  }

  /** @private */
  __updateOverflow() {
    let overflow = '';

    // Only set "overflow" attribute if the popup has a header, title or footer.
    // Check for state attributes as extending components might not use renderers.
    if (this.hasAttribute('has-header') || this.hasAttribute('has-footer') || this.headerTitle) {
      const content = this.$.content;

      if (content.scrollTop > 0) {
        overflow += ' top';
      }

      if (content.scrollTop < content.scrollHeight - content.clientHeight) {
        overflow += ' bottom';
      }
    }

    const value = overflow.trim();
    if (value.length > 0 && this.getAttribute('overflow') !== value) {
      this.setAttribute('overflow', value);
    } else if (value.length === 0 && this.hasAttribute('overflow')) {
      this.removeAttribute('overflow');
    }
  }

  __onScroll(e) {
    // If the scroll event occurred inside the overlay, ignore it.
    if (!this.contains(e.target)) {
      if (this.closeOnScroll) {
        this.opened = false;
      } else {
        this._updatePosition();
      }
    }
  }

  /**
   * @protected
   * @override
   */
  _updatePosition() {
    super._updatePosition();

    if (!this.positionTarget) {
      return;
    }

    if (this.preferredPosition === 'end' && this.preferredAlignment === 'center') {
      this._centerVertically();
    }

    if (this.hasAttribute('highlight-target')) {
      this.highlightTargetInBackdrop();
    } else {
      this.$.backdrop.style.clipPath = null;
    }

    if (this._theme && this._theme.includes('pointer-arrow')) {
      this._updatePointerArrowPosition();
    }
  }

  highlightTargetInBackdrop() {
    const ENLARGE_TARGET_HOLE_BY_PIXELS = 5;
    let targetRect = this.positionTarget.getBoundingClientRect();

    targetRect = this._addPixelsAroundRect(targetRect, ENLARGE_TARGET_HOLE_BY_PIXELS);
    this._makeHoleInBackdrop(targetRect);
    this._repositionPopupToPointToBackropHole(ENLARGE_TARGET_HOLE_BY_PIXELS);
  }

  _repositionPopupToPointToBackropHole(pixelsToMove) {
    let pxShift = pixelsToMove;
    if (this.preferredPosition === 'end') {
      pxShift = -pxShift;
    }

    if (this.style.bottom) {
      this.style.bottom = parseFloat(this.style.bottom) + pxShift + 'px';
    }
    if (this.style.left) {
      this.style.left = parseFloat(this.style.left) - pxShift + 'px';
    }
    if (this.style.top) {
      this.style.top = parseFloat(this.style.top) + pxShift + 'px';
    }
    if (this.style.right) {
      this.style.right = parseFloat(this.style.right) - pxShift + 'px';
    }
  }

  _makeHoleInBackdrop(targetRect) {
    const topLeft = targetRect.x + 'px ' + targetRect.y + 'px';
    const topRight = targetRect.right + 'px ' + targetRect.y + 'px';
    const bottomRight = targetRect.right + 'px ' + targetRect.bottom + 'px';
    const bottomLeft = targetRect.x + 'px ' + targetRect.bottom + 'px';
    this.$.backdrop.style.clipPath = `polygon(0px 0px,0 100%,100% 100%,100% 0px,0px 0px, ${topLeft}, ${topRight},${bottomRight},${bottomLeft},${topLeft})`;
  }

  _centerVertically() {
    const targetRect = this.positionTarget.getBoundingClientRect();
    const offset = targetRect.height / 2 - this._getNormalizedOverlayHeight() / 2;

    if (this.style.top) {
      const currentValue = parseFloat(this.style.top);
      this.style.top = Math.max(currentValue + offset, 15) + 'px';
    }
    if (this.style.bottom) {
      const currentValue = parseFloat(this.style.bottom);
      this.style.bottom = Math.max(currentValue + offset, 15) + 'px';
    }
  }

  _updatePointerArrowPosition() {
    new PopupPointerArrowPositionUpdater(this).updatePosition();
  }

  _getNormalizedOverlayHeight() {
    // Using previous size to fix a case where window resize may cause the overlay to be squeezed
    // smaller than its current space before the fit-calculations. Taken from PositionMixin#__shouldAlignStartHorizontally().
    return this.requiredVerticalSpace || Math.max(this.__oldContentHeight || 0, this.$.overlay.offsetHeight);
  }

  _getNormalizedOverlayWidth() {
    // Using previous size to fix a case where window resize may cause the overlay to be squeezed
    // smaller than its current space before the fit-calculations. Taken from PositionMixin#__shouldAlignStartVertically().
    return Math.max(this.__oldContentWidth || 0, this.$.overlay.offsetWidth);
  }

  _addPixelsAroundRect(targetRect, pixels) {
    targetRect.x = targetRect.x - pixels;
    targetRect.y = targetRect.y - pixels;
    targetRect.width = targetRect.width + 2 * pixels;
    targetRect.height = targetRect.height + 2 * pixels;
    return targetRect;
  }
}

class PopupPointerArrowPositionUpdater {
  constructor(popupOverlay) {
    this._pointerArrow = popupOverlay._pointerArrow;
    this._preferredPosition = popupOverlay.preferredPosition;
    this._isPopupStartAligned = popupOverlay.hasAttribute('start-aligned');
    this._isPopupTopAligned = popupOverlay.hasAttribute('top-aligned');

    this._targetRect = popupOverlay.positionTarget.getBoundingClientRect();
    this._pointerArrowRect = this._pointerArrow.getBoundingClientRect();
    this._overlayRect = popupOverlay.$.overlay.getBoundingClientRect();
    this._overlayWidth = popupOverlay._getNormalizedOverlayWidth();
    this._overlayHeight = popupOverlay._getNormalizedOverlayHeight();
  }

  updatePosition() {
    this._clearPositionProperties();

    if (this._preferredPosition === 'bottom') {
      this._positionArrowHorizontally();
    }

    if (this._preferredPosition === 'end') {
      this._positionArrowVertically();
    }
  }

  _clearPositionProperties() {
    this._pointerArrow.style.top = null;
    this._pointerArrow.style.bottom = null;
    this._pointerArrow.style.left = null;
    this._pointerArrow.style.right = null;
  }

  _positionArrowHorizontally() {
    if (this._isTargetWiderThanOverlay()) {
      this._alignToHorizontalCenterOfOverlay();
    } else {
      this._alignToHorizontalCenterOfTarget();
    }
  }

  _positionArrowVertically() {
    if (this._isTargetTallerThanOverlay()) {
      this._alignToVerticalCenterOfOverlay();
    } else {
      this._alignToVerticalCenterOfTarget();
    }
  }

  _isTargetWiderThanOverlay() {
    return this._targetRect.width > this._overlayWidth;
  }

  _isTargetTallerThanOverlay() {
    return this._targetRect.height > this._overlayHeight;
  }

  _alignToHorizontalCenterOfTarget() {
    const offset = this._targetRect.width / 2 - this._pointerArrowRect.width / 2;
    if (this._isPopupStartAligned) {
      this._pointerArrow.style.left = offset + 'px';
    } else {
      this._pointerArrow.style.right = offset + 'px';
    }
  }

  _alignToHorizontalCenterOfOverlay() {
    const offset = this._overlayWidth / 2 - this._pointerArrowRect.width / 2;
    this._pointerArrow.style.left = offset + 'px';
  }

  _alignToVerticalCenterOfOverlay() {
    const offset = this._overlayHeight / 2 - this._pointerArrowRect.height / 2;
    this._pointerArrow.style.top = offset + 'px';
  }

  _alignToVerticalCenterOfTarget() {
    let offset = this._targetRect.height / 2 - this._pointerArrowRect.height / 2;
    if (this._isPopupTopAligned) {
      offset = offset + (this._targetRect.y - this._overlayRect.y);
      offset = Math.max(offset, 3); // do not display pointer arrow at the very corner of the popup, but slightly below it
      this._pointerArrow.style.top = offset + 'px';
    } else {
      offset = offset + (this._overlayRect.bottom - this._targetRect.bottom);
      offset = Math.max(offset, 3); // do not display  pointer arrow at the very corner of the popup, but slightly above it
      this._pointerArrow.style.bottom = offset + 'px';
    }
  }
}

customElements.define(PopupOverlayElement.is, PopupOverlayElement);
