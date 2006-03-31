package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 10.02.2006
 * Time: 22:20:01
 * To change this template use File | Settings | File Templates.
 */

/**
 * Renders a layout cell.
 * A panel with ability to span over more than one layout cells.
 */
@Tag(name = "cell")
@UIComponentTag(ComponentType = "org.apache.myfaces.tobago.Cell",
    UIComponent = "org.apache.myfaces.tobago.component.UICell",
    RendererType = "Panel")
public interface CellTagDeclaration extends TobagoBodyTagDeclaration, 
    HasIdBindingAndRendered {

  /**
   * Count of layout column's to span over.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = { "java.lang.Integer" },
      defaultValue = "1")
  void setSpanX(String spanX);

  /**
   * Count of layout row's to span over.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = { "java.lang.Integer" },
      defaultValue = "1")
  void setSpanY(String spanY);

  /**
   * possible values are:
   * 'false' : no scrollbars should rendered
   * 'true'  : scrollbars should always rendered
   * 'auto'  : scrollbars should rendered when needed
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "false",
      allowedValues = {"false", "true", "auto"})
  void setScrollbars(String scrollbars);
}
