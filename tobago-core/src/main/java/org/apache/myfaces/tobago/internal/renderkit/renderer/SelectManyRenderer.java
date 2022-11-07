/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectMany;
import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectManyRenderer<T extends AbstractUISelectMany> extends SelectManyRendererBase<T> {
  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_MANY;
  }

  @Override
  protected CssItem[] getComponentCss(final FacesContext facesContext, final T component) {
    final boolean inline = component.isInline();
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || component.isReadonly();

    List<CssItem> cssItems = new ArrayList<>();
    if (disabled) {
      cssItems.add(TobagoClass.DISABLED);
    }
    if (inline) {
      cssItems.add(BootstrapClass.LIST_GROUP);
    } else {
      cssItems.add(BootstrapClass.DROPDOWN);
    }
    return cssItems.toArray(new CssItem[0]);
  }

  @Override
  protected String getFieldId(FacesContext facesContext, T component) {
    return component.getFieldId(facesContext);
  }

  @Override
  protected void encodeBeginField(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final String filterId = clientId + ComponentUtils.SUB_SEPARATOR + "filter";
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || component.isReadonly();
    final String filter = component.getFilter();
    final boolean inline = component.isInline();
    final Markup markup = component.getMarkup();
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);

    renderHiddenSelect(facesContext, component);
    renderSelectField(facesContext, component);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
      TobagoClass.OPTIONS,
      inline ? BootstrapClass.LIST_GROUP_ITEM : BootstrapClass.DROPDOWN_MENU);
    writer.writeNameAttribute(clientId);

    writer.startElement(HtmlElements.TABLE);
    writer.writeClassAttribute(BootstrapClass.TABLE, BootstrapClass.TABLE_HOVER, BootstrapClass.TABLE_SM);
    writer.startElement(HtmlElements.TBODY);

    final Object[] values = component.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(component);
    for (SelectItem item : items) {
      Object itemValue = item.getValue();
      // when using selectItem tag with a literal value: use the converted value
      if (itemValue instanceof String && values != null && values.length > 0 && !(values[0] instanceof String)) {
        itemValue = ComponentUtils.getConvertedValue(facesContext, component, (String) itemValue);
      }
      final String formattedValue = getFormattedValue(facesContext, (T) component, itemValue);
      final boolean contains;
      if (submittedValues == null) {
        contains = ArrayUtils.contains(values, itemValue);
      } else {
        contains = ArrayUtils.contains(submittedValues, formattedValue);
      }
      writer.startElement(HtmlElements.TR);
      writer.writeAttribute(DataAttributes.VALUE, formattedValue, true);
      writer.writeClassAttribute(
        contains ? BootstrapClass.TABLE_PRIMARY : null,
        disabled || item.isDisabled() ? TobagoClass.DISABLED : null);

      writer.startElement(HtmlElements.TD);
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
      writer.writeText(item.getLabel());
      writer.endElement(HtmlElements.TD);
      writer.endElement(HtmlElements.TR);
    }

    writer.endElement(HtmlElements.TBODY);
    writer.endElement(HtmlElements.TABLE);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected void writeAdditionalAttributes(FacesContext facesContext, TobagoResponseWriter writer, T input)
    throws IOException {
    super.writeAdditionalAttributes(facesContext, writer, input);
    writer.writeAttribute(CustomAttributes.FILTER, input.getFilter(), true);
  }

  private void renderHiddenSelect(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String selectedId = clientId + ComponentUtils.SUB_SEPARATOR + "selected";
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || component.isReadonly();

    writer.startElement(HtmlElements.SELECT);
    writer.writeIdAttribute(selectedId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    writer.writeClassAttribute(BootstrapClass.D_NONE);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);

    final Object[] values = component.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(component);
    renderSelectItems(component, null, items, values, submittedValues, writer, facesContext);
    writer.endElement(HtmlElements.SELECT);
  }

  private void renderSelectField(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final String filterId = clientId + ComponentUtils.SUB_SEPARATOR + "filter";
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || component.isReadonly();
    final String filter = component.getFilter();
    final boolean inline = component.isInline();
    final Markup markup = component.getMarkup();
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(fieldId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeClassAttribute(
      inline ? BootstrapClass.FORM_CONTROL : BootstrapClass.FORM_SELECT,
      TobagoClass.SELECT__FIELD,
      inline ? BootstrapClass.LIST_GROUP_ITEM : BootstrapClass.DROPDOWN_TOGGLE,
      BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)),
      component.getCustomClass());
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    if (!inline) {
      writer.writeAttribute(DataAttributes.BS_TOGGLE, "dropdown", false);
    }
    writer.writeAttribute(Arias.EXPANDED, Boolean.FALSE.toString(), false);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT);
    writer.writeIdAttribute(filterId);
    writer.writeClassAttribute(TobagoClass.FILTER, BootstrapClass.FORM_CONTROL);
    writer.writeAttribute(HtmlAttributes.AUTOCOMPLETE, "off", false);
    writer.writeAttribute(HtmlAttributes.READONLY, filter == null);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    writer.endElement(HtmlElements.INPUT);

    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected void encodeEndField(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    encodeBehavior(writer, facesContext, component);
  }
}
