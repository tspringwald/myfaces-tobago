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

  get hiddenSelect(): HTMLScriptElement {
    return this.root.querySelector(`select[name='${this.id}']`);
  }

  get selectField(): HTMLDivElement {
    return this.querySelector(".tobago-filter-wrapper");
  }

  get filterInput(): HTMLInputElement {
    return this.querySelector(".tobago-filter");
  }

  get dropdownMenu(): HTMLDivElement {
    return this.root.querySelector(`.dropdown-menu[name='${this.id}']`);
  }

  get menuStore(): HTMLDivElement {
    return this.root.querySelector(".tobago-page-menuStore");
  }

  get root(): ShadowRoot | Document {
    return this.getRootNode() as ShadowRoot | Document;
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

    this.initList();
  }

  select(event: MouseEvent) {
    const target = <HTMLElement>event.target;
    const row = target.closest("tr");
    row.classList.toggle("table-primary");
    const itemValue = row.dataset.tobagoValue;
    console.info("itemValue", itemValue);
    if (row.classList.contains("table-primary")) {
      const badge = this.getRowTemplate(itemValue, row.innerText);
      console.info("badge", badge);
      this.filterInput.insertAdjacentHTML("beforebegin", badge);
    } else {
      const selectors = `.badge[data-tobago-value="${itemValue}"]`;
      console.info("selectors", selectors);
      this.selectField.querySelector(selectors).remove();
    }
  }

  getRowTemplate(value: string, text: string) : string {
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
