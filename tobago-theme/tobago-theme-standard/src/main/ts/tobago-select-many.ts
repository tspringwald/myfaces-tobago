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

  get filterInput(): HTMLInputElement {
    return this.querySelector(".tobago-filter");
  }

  get dropdownMenu(): HTMLDivElement {
    return this.querySelector(".dropdown-menu");
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

    this.selectField.addEventListener("click", this.showDropdownMenu.bind(this));

    // todo: later
    // this.hiddenSelect.addEventListener("change", ((event: InputEvent) => {
    //   const select = <HTMLSelectElement>event.currentTarget;
    //   select.querySelectorAll("option").forEach((option =>
    //     this.sync(option)).bind(this));
    //   }).bind(this));

    // this.hiddenSelect.classList.remove("d-none");

    this.initList();
  }

  select(event: MouseEvent) {
    const target = <HTMLElement>event.target;
    const row = target.closest("tr");
    const itemValue = row.dataset.tobagoValue;
    console.info("itemValue", itemValue);
    const select = this.hiddenSelect;
    const option: HTMLOptionElement = select.querySelector(`[value="${itemValue}"]`);
    option.selected = !option.selected;
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
      // highlight list row
      row.classList.add("table-active");
    } else {
      // remove badge
      const selectField1 = this.selectField;
      console.log("selectField1", selectField1);
      selectField1.querySelector(`[data-tobago-value="${itemValue}"]`).remove();
      // remove highlight list row
    }
  }

  getRowTemplate(value: string, text: string): string {
    return `<span class="badge text-bg-primary" data-tobago-value="${value}">${text}</span>`;
  }

  private showDropdownMenu(event: MouseEvent): void {
    this.dropdownMenu.classList.add(this.CssClass.SHOW);
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
