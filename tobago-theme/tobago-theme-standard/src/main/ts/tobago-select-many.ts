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

  connectedCallback(): void {
    // todo: implement open/close dropdown-menu
    // todo: position/size of dropdown-menu
    // todo: implement select/deselect options
    // todo: implement remove badge

    this.selectField.addEventListener("click", this.showDropdownMenu.bind(this));
  }

  private showDropdownMenu(event: MouseEvent): void {
    this.dropdownMenu.classList.add(this.CssClass.SHOW);
  }

  get hiddenSelect(): HTMLScriptElement {
    return this.root.querySelector(`select[name='${this.id}']`);
  }

  get selectField(): HTMLInputElement {
    return this.root.querySelector(`.tobago-filter-wrapper[name='${this.id}']`);
  }

  get filterInput(): HTMLInputElement {
    return this.root.querySelector(".tobago-filter");
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
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-many") == null) {
    window.customElements.define("tobago-select-many", SelectMany);
  }
});
