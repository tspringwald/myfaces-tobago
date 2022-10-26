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

import org.apache.myfaces.tobago.internal.component.AbstractUISelectMany;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectManyRenderer<T extends AbstractUISelectMany> extends SelectManyRendererBase<T> {
  @Override
  public HtmlElements getComponentTag() {
    return null;
  }

  @Override
  protected String getFieldId(FacesContext facesContext, T component) {
    return null;
  }

  @Override
  protected void encodeBeginField(FacesContext facesContext, T component) throws IOException {

  }

  @Override
  protected void encodeEndField(FacesContext facesContext, T component) throws IOException {

  }
}
