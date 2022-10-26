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
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public class SelectManyRenderer<T extends AbstractUISelectMany> extends SelectManyRendererBase<T> {
  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_MANY;
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
    final boolean readonly = component.isReadonly();
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || readonly;
    final String filter = component.getFilter();
    final Markup markup = component.getMarkup();
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);

    renderHiddenSelect(facesContext, component);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(fieldId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeClassAttribute(
      BootstrapClass.FORM_SELECT,
      BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)),
      component.getCustomClass());
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);

    final Object[] values = component.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(component);

    renderBadges(facesContext, values, submittedValues);

    /*renderSelectItems(
      component, TobagoClass.SELECT_MANY_LISTBOX__OPTION, items, values, submittedValues, writer, facesContext);*/

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.DROPDOWN_MENU, BootstrapClass.SHOW);

    if (filter != null) {
      writer.startElement(HtmlElements.DIV);
      writer.writeClassAttribute(TobagoClass.FILTER__WRAPPER);
      writer.startElement(HtmlElements.INPUT);
      writer.writeIdAttribute(filterId);
      writer.writeClassAttribute(BootstrapClass.FORM_CONTROL);
      writer.endElement(HtmlElements.INPUT);
      writer.endElement(HtmlElements.DIV);

      writer.startElement(HtmlElements.HR);
      writer.writeClassAttribute(BootstrapClass.DROPDOWN_DIVIDER);
      writer.endElement(HtmlElements.HR);
    }

    writer.startElement(HtmlElements.TABLE);
    writer.writeClassAttribute(BootstrapClass.TABLE, BootstrapClass.TABLE_HOVER);
    writer.startElement(HtmlElements.TBODY);

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
      writer.writeClassAttribute(
        contains ? BootstrapClass.TABLE_ACTIVE : null,
        item.isDisabled() ? BootstrapClass.TEXT_MUTED : null);

      writer.startElement(HtmlElements.TD);
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);

      writer.writeText(item.getLabel());
      writer.endElement(HtmlElements.TD);
      writer.startElement(HtmlElements.TD);
      writer.writeText("dummy column");
      writer.endElement(HtmlElements.TD);
      writer.endElement(HtmlElements.TR);
    }

    writer.endElement(HtmlElements.TBODY);
    writer.endElement(HtmlElements.TABLE);
    writer.endElement(HtmlElements.DIV);
  }

  private void renderHiddenSelect(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String clientId = component.getClientId(facesContext);
    final String selectedId = clientId + ComponentUtils.SUB_SEPARATOR + "selected";
    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);
    final boolean readonly = component.isReadonly();
    final boolean disabled = !items.iterator().hasNext() || component.isDisabled() || readonly;

    writer.startElement(HtmlElements.SELECT);
    writer.writeIdAttribute(selectedId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    writer.writeClassAttribute(BootstrapClass.D_NONE);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, true);

    final Object[] values = component.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(component);
    renderSelectItems(component, null, items, values, submittedValues, writer, facesContext);
    writer.endElement(HtmlElements.SELECT);
  }

  private void renderBadges(FacesContext facesContext, Object[] values, String[] submittedValues) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    if (submittedValues != null) {
      for (String submittedValue : submittedValues) {
        writer.startElement(HtmlElements.SPAN);
        writer.writeClassAttribute(BootstrapClass.BADGE, BootstrapClass.TEXT_BG_PRIMARY);
        writer.writeText(submittedValue);
        writer.endElement(HtmlElements.SPAN);
      }
    } else if (values != null) {
      for (Object value : values) {
        writer.startElement(HtmlElements.SPAN);
        writer.writeClassAttribute(BootstrapClass.BADGE, BootstrapClass.TEXT_BG_PRIMARY);
        writer.writeText(value.toString());
        writer.endElement(HtmlElements.SPAN);
      }
    }
  }

  @Override
  protected void encodeEndField(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.endElement(HtmlElements.DIV);

    encodeBehavior(writer, facesContext, component);
  }
}
