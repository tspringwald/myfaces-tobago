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
package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import static org.apache.myfaces.tobago.TobagoConstants.*;

/*
  * Created 29.07.2003 at 15:09:53.
  * $Id$
  */

/**
 * Renders a layout cell.
 * A panel with ability to span over more than one layout cells.
 */
@Tag(name="cell")
public class CellTag extends TobagoBodyTag implements HasIdBindingAndRendered {

  // ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CellTag.class);

// ///////////////////////////////////////////// attribute

  private String spanX = "1";

  private String spanY = "1";

  private String scrollbars;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  public int doStartTag() throws JspException {
    return super.doStartTag();
  }

  public String getRendererType() {
    return RENDERER_TYPE_PANEL;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

   ComponentUtil.setIntegerProperty(component, ATTR_SPAN_X, spanX, getIterationHelper());
   ComponentUtil.setIntegerProperty(component, ATTR_SPAN_Y, spanY, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_SCROLLBARS, scrollbars, getIterationHelper());

    component.getAttributes().put(ATTR_LAYOUT_DIRECTIVE, Boolean.TRUE);

    if (LOG.isDebugEnabled()) {
      LOG.debug("spanX=" + spanX + " spanY=" + spanY);
      LOG.debug("spanX=" +
          component.getAttributes().get(ATTR_SPAN_X)
          + " spanY=" +
          component.getAttributes().get(ATTR_SPAN_Y));
      LOG.debug("component = " + getComponentInstance());
    }
  }

  public void release() {
    super.release();
    spanX = "1";
    spanY = "1";
    scrollbars = null;
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getSpanX() {
    return spanX;
  }

    /**
   *
   *   Count of layout column's to span over.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"java.lang.Integer"}, defaultValue="1")
  public void setSpanX(String spanX) {
    this.spanX = spanX;
  }

  public String getSpanY() {
    return spanY;
  }

  /**
   *
   *   Count of layout row's to span over.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"java.lang.Integer"}, defaultValue="1")
  public void setSpanY(String spanY) {
    this.spanY = spanY;
  }

  public String getScrollbars() {
    return scrollbars;
  }


  /**
   *
   *  possible values are:
   *    'false' : no scrollbars should rendered
   *    'true'  : scrollbars should always rendered
   *    'auto'  : scrollbars should rendered when needed
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue="false")
  public void setScrollbars(String scrollbars) {
    this.scrollbars = scrollbars;
  }
}
