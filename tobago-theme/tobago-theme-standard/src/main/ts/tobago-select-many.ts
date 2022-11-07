/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {BootstrapEvents} from "./BootstrapEvents";
import {TobagoFilterRegistry} from "./tobago-filter-registry";

class SelectMany extends HTMLElement {

  private readonly CssClass = {
    DROPDOWN_MENU: "dropdown-menu",
    SHOW: "show",
    TABLE_ACTIVE: "table-active",
    TABLE_PRIMARY: "table-primary",
    TOBAGO_DISABLED: "tobago-disabled",
    TOBAGO_FOCUS: "tobago-focus",
    TOBAGO_OPTIONS: "tobago-options"
  };

  private readonly Key = {
    ESCAPE: "Escape"
  };

  constructor() {
    super();
  }

  get hiddenSelect(): HTMLSelectElement {
    return this.querySelector("select");
  }

  get selectField(): HTMLDivElement {
    return this.querySelector(".tobago-select-field");
  }

  get badgeCloseButtons(): NodeListOf<HTMLButtonElement> {
    return this.selectField.querySelectorAll("button.btn.badge");
  }

  get filter(): string {
    return this.getAttribute("filter");
  }

  get filterInput(): HTMLInputElement {
    return this.querySelector(".tobago-filter");
  }

  get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.dropdown-menu[name='${this.id}']`);
  }

  get menuStore(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
  }

  get tbody(): HTMLElement {
    return this.querySelector("tbody");
  }

  connectedCallback(): void {
    if (this.dropdownMenu) {
      window.addEventListener("resize", this.resizeEvent.bind(this));
      this.addEventListener(BootstrapEvents.DROPDOWN_SHOW, this.showDropdown.bind(this));
      this.addEventListener(BootstrapEvents.DROPDOWN_SHOWN, this.shownDropdown.bind(this));
      this.addEventListener(BootstrapEvents.DROPDOWN_HIDE, this.preventBootstrapHide.bind(this));
      this.addEventListener(BootstrapEvents.DROPDOWN_HIDDEN, this.hiddenDropdown.bind(this));
    }
    document.addEventListener("click", this.clickEvent.bind(this));
    this.filterInput.addEventListener("focus", this.focusEvent.bind(this));
    this.filterInput.addEventListener("blur", this.blurEvent.bind(this));
    this.addEventListener("keydown", this.keydownEvent.bind(this));

    // init badges
    this.querySelectorAll("option:checked").forEach(
      option => this.sync(<HTMLOptionElement>option)
    );

    this.initList();

    // init filter
    if (this.filter != null) {
      const input = this.filterInput;
      input.addEventListener("keyup", this.filterEvent.bind(this));
    }
  }

  select(event: MouseEvent): void {
    const target = <HTMLElement>event.target;
    const row = target.closest("tr");
    const itemValue = row.dataset.tobagoValue;
    console.info("itemValue", itemValue);
    const select = this.hiddenSelect;
    const option: HTMLOptionElement = select.querySelector(`[value="${itemValue}"]`);
    option.selected = !option.selected;
    this.sync(option);
  }

  removeBadge(event: MouseEvent): void {
    const target = <HTMLElement>event.target;
    const group: HTMLElement = target.closest(".btn-group");
    const itemValue = group.dataset.tobagoValue;
    const select = this.hiddenSelect;
    const option: HTMLOptionElement = select.querySelector(`[value="${itemValue}"]`);
    option.selected = false;
    this.sync(option);
  }

  sync(option: HTMLOptionElement) {
    const itemValue = option.value;
    const row: HTMLTableRowElement = this.tbody.querySelector(`[data-tobago-value="${itemValue}"]`);
    if (option.selected) {
      // create badge
      this.filterInput.insertAdjacentHTML("beforebegin",
        this.getRowTemplate(itemValue, row.innerText, option.disabled || this.hiddenSelect.disabled));

      // todo: nicer adding the @click with lit-html
      const closeButton = this.selectField
        .querySelector(".btn-group[data-tobago-value='" + itemValue + "'] button.btn.badge");
      closeButton?.addEventListener("click", this.removeBadge.bind(this));
      closeButton?.addEventListener("focus", this.focusEvent.bind(this));
      closeButton?.addEventListener("blur", this.blurEvent.bind(this));

      // highlight list row
      row.classList.add(this.CssClass.TABLE_PRIMARY);
    } else {
      // remove badge
      const badge = this.selectField.querySelector(`[data-tobago-value="${itemValue}"]`);
      const previousBadge = badge.previousElementSibling;
      const nextBadge = badge.nextElementSibling.tagName === "SPAN" ? badge.nextElementSibling : null;
      badge.remove();
      if (previousBadge) {
        previousBadge.querySelector<HTMLButtonElement>("button.btn.badge").focus();
      } else if (nextBadge) {
        nextBadge.querySelector<HTMLButtonElement>("button.btn.badge").focus();
      } else {
        this.filterInput.disabled = false;
        this.filterInput.focus();
      }

      // remove highlight list row
      row.classList.remove(this.CssClass.TABLE_PRIMARY);
    }

    if (!this.classList.contains(this.CssClass.TOBAGO_DISABLED) && this.filter === null) {
      // disable input field to prevent focus.
      this.filterInput.disabled = this.badgeCloseButtons.length > 0;
    }
  }

  getRowTemplate(value: string, text: string, disabled: boolean): string {
    return `
<span class="btn-group" role="group" data-tobago-value="${value}">
  <tobago-badge class="badge text-bg-primary btn
  ${disabled ? "disabled" : ""}">${text}</tobago-badge>
  ${disabled ? ""
      : "<button type='button' class='tobago-button btn btn-secondary badge'><i class='bi-x-lg'></i></button>"}
</span>`;
  }

  filterEvent(event: Event): void {
    const input = event.currentTarget as HTMLInputElement;
    const searchString = input.value;
    console.info("searchString", searchString);
    const filterFunction = TobagoFilterRegistry.get(this.filter);
    // XXX todo: if filterFunction not found?
    if (filterFunction != null) {
      this.querySelectorAll("tr").forEach(row => {
        const itemValue = row.dataset.tobagoValue;
        if (filterFunction(itemValue.toLowerCase(), searchString)) {
          row.classList.remove("d-none");
        } else {
          row.classList.add("d-none");
        }
      });
    }
  }

  private showDropdown(event: Event): void {
    // console.log("### showDropdown");
  }

  private shownDropdown(event: Event): void {
    this.setDropdownMenuWidth();
  }

  private preventBootstrapHide(event: CustomEvent): void {
    event.stopPropagation();
    event.preventDefault();
  }

  private clickEvent(event: MouseEvent): void {
    if (this.isDeleted(event.target as Element)) {
      // do nothing, this is probably a removed badge
    } else if (this.isPartOfComponent(event.target as Element)) {

      if (!this.filterInput.disabled) {
        this.filterInput.focus();
      } else if (this.badgeCloseButtons.length > 0) {
        this.badgeCloseButtons[0].focus();
      }

    } else {
      this.hideDropdown();
      this.setFocus(false);
    }
  }

  private keydownEvent(event: KeyboardEvent) {
    if (event.key === this.Key.ESCAPE) {
      this.hideDropdown();
    }
  }

  private isPartOfComponent(element: Element): boolean {
    if (element) {
      if (this.id === element.id
        || (element.classList?.contains(this.CssClass.DROPDOWN_MENU)
          && this.id === element.getAttribute("name"))) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfComponent(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  private isDeleted(element: Element): boolean {
    return element.closest("html") === null;
  }

  private hideDropdown(): void {
    if (this.dropdownMenu?.classList.contains(this.CssClass.SHOW)) {
      this.selectField.classList.remove(this.CssClass.SHOW);
      this.selectField.ariaExpanded = "false";
      this.dropdownMenu.classList.remove(this.CssClass.SHOW);
      console.log("### hideDropdown");
    }
  }

  private hiddenDropdown(event: Event): void {
    // console.log("### hiddenDropdown");
  }

  private resizeEvent(event: UIEvent): void {
    this.setDropdownMenuWidth();
  }

  private setDropdownMenuWidth(): void {
    if (this.dropdownMenu) {
      this.dropdownMenu.style.width = `${this.offsetWidth}px`;
    }
  }

  private focusEvent(event: FocusEvent): void {
    if (!this.hiddenSelect.disabled) {
      this.setFocus(true);
    }
  }

  private blurEvent(event: FocusEvent): void {
    if (event.relatedTarget !== null) {
      //relatedTarget is the new focused element; null indicate a mouseclick or an inactive browser window
      if (!this.isPartOfComponent(event.relatedTarget as Element)) {
        this.setFocus(false);
        this.hideDropdown();
      }
    }
  }

  private setFocus(focus: boolean): void {
    if (focus) {
      this.classList.add(this.CssClass.TOBAGO_FOCUS);
    } else {
      this.classList.remove(this.CssClass.TOBAGO_FOCUS);
    }
  }

  private initList() {
    const tbody = this.tbody;
    tbody.addEventListener("click", this.select.bind(this));
    tbody.querySelectorAll("tr").forEach((row: HTMLTableRowElement) => {
      // row stuff
    });
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-many") == null) {
    window.customElements.define("tobago-select-many", SelectMany);
  }
});
