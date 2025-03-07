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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAccessKey;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAutocomplete;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasHelp;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasPlaceholder;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAutoSpacing;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsPassword;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import jakarta.faces.component.UIInput;

/**
 * Renders a text input field.
 */
@Tag(name = "in")
@BodyContentDescription(anyTagOf = "facestag")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIIn",
    uiComponentFacesClass = "jakarta.faces.component.UIInput",
    componentFamily = UIInput.COMPONENT_FAMILY,
    rendererType = RendererTypes.IN,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.Suggest"
    },
    facets = {
        @Facet(name = Facets.BEFORE,
            description =
                "This facet can contain a part for input groups."),
        @Facet(name = Facets.AFTER,
            description =
                "This facet can contain a part for input groups.")
    },
    behaviors = {
        @Behavior(
            name = ClientBehaviors.CHANGE,
            isDefault = true),
        @Behavior(
            name = ClientBehaviors.INPUT),
        @Behavior(
            name = ClientBehaviors.CLICK),
        @Behavior(
            name = ClientBehaviors.DBLCLICK),
        @Behavior(
            name = ClientBehaviors.FOCUS),
        @Behavior(
          name = ClientBehaviors.KEYDOWN),
        @Behavior(
          name = ClientBehaviors.KEYPRESS),
        @Behavior(
          name = ClientBehaviors.KEYUP),
        @Behavior(
            name = ClientBehaviors.BLUR)
    })
public interface InTagDeclaration
    extends HasIdBindingAndRendered, HasConverter, IsReadonly, IsDisabled, IsRequired, HasHelp, HasTip, IsPassword,
    HasAccessKey, HasValidator, HasValue, HasValueChangeListener, HasTabIndex, IsFocus, IsVisual,
    HasValidatorMessage, HasConverterMessage, HasRequiredMessage, HasLabel, HasLabelLayout,
    HasAutocomplete, HasPlaceholder, HasAutoSpacing {

}
