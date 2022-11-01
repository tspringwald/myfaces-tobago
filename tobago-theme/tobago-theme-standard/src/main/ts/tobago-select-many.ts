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
    SHOW: "show"
  };

  constructor() {
    super();
  }

  get hiddenSelect(): HTMLSelectElement {
    return this.querySelector("select");
  }

  get selectField(): HTMLDivElement {
    return this.querySelector(".tobago-filter-wrapper");
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
    // todo: implement open/close dropdown-menu
    // todo: position/size of dropdown-menu
    // todo: implement select/deselect options
    // todo: implement remove badge

    window.addEventListener("resize", this.resizeEvent.bind(this));
    this.addEventListener(BootstrapEvents.DROPDOWN_SHOW, this.showDropdown.bind(this));
    this.addEventListener(BootstrapEvents.DROPDOWN_SHOWN, this.shownDropdown.bind(this));
    this.addEventListener(BootstrapEvents.DROPDOWN_HIDE, this.hideDropdown.bind(this));
    this.addEventListener(BootstrapEvents.DROPDOWN_HIDDEN, this.HiddenDropdown.bind(this));

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
    console.log("this", this);
    console.log("option", option);
    console.log("ds", option.value);
    const itemValue = option.value;
    console.log("itemValue", itemValue);
    const row: HTMLTableRowElement = this.tbody.querySelector(`[data-tobago-value="${itemValue}"]`);
    console.log("row", row);
    if (option.selected) {
      // create badge
      this.filterInput.insertAdjacentHTML("beforebegin", this.getRowTemplate(itemValue, row.innerText));

      // todo: nicer adding the @click with lit-html
      const current = this.filterInput.parentElement.querySelector(".btn-group[data-tobago-value='"+ itemValue +"']");
      current.addEventListener("click", this.removeBadge.bind(this));

      // highlight list row
      row.classList.add("table-active");
    } else {
      // remove badge
      const selectField1 = this.selectField;
      console.log("selectField1", selectField1);
      selectField1.querySelector(`[data-tobago-value="${itemValue}"]`).remove();

      // remove highlight list row
      row.classList.remove("table-active");
    }
  }

  getRowTemplate(value: string, text: string): string {
    return `
<span class="btn-group" role="group" data-tobago-value="${value}">
  <span class="btn badge text-bg-primary">${text}</span>
  <button type="button" class="btn badge btn-secondary"><i class="bi-x-lg"></i></button>
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

  private showDropdownMenu(event: MouseEvent): void {
    this.dropdownMenu?.classList.add(this.CssClass.SHOW);
  }

  private showDropdown(event: Event): void {
    // console.log("### showDropdown");
  }

  private shownDropdown(event: Event): void {
    this.setDropdownMenuWidth();
    // console.log("### shownDropdown");
  }

  private hideDropdown(event: Event): void {
    // console.log("### hideDropdown");
  }

  private HiddenDropdown(event: Event): void {
    // console.log("### HiddenDropdown");
  }

  private resizeEvent(event: UIEvent): void {
    this.setDropdownMenuWidth();
  }

  private setDropdownMenuWidth(): void {
    if (this.dropdownMenu) {
      this.dropdownMenu.style.width = `${this.offsetWidth}px`;
    }
  }

  private focusFilter(event: MouseEvent): void {
    // console.log("### focusFilter");
  }

  private blurFilter(event: MouseEvent): void {
    // console.log("### blurFilter");
  }

  private blurSelect(event: MouseEvent): void {
    // console.log("### blurSelect");
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
