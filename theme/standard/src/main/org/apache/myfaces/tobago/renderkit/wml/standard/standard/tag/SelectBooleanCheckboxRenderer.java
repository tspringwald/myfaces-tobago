/*
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectBooleanCheckboxRenderer extends RendererBase {

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    boolean value = ComponentUtil.getBooleanAttribute(component, ATTR_VALUE);

    writer.startElement("select", component);
    writer.writeAttribute("name", component.getClientId(facesContext), null);
    writer.writeAttribute("id", component.getClientId(facesContext), null);
    writer.writeAttribute("multiple", true);
    writer.startElement("option", null);
    writer.writeAttribute("value", value ? "on" : "off", null);

    UIComponent label = component.getFacet(FACET_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    }

    writer.endElement("option");
    writer.endElement("select");
  }
}
