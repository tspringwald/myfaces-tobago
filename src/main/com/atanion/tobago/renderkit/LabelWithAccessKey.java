/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 07.09.2004 14:11:53.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public final class LabelWithAccessKey {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(LabelWithAccessKey.class);

// ----------------------------------------------------------------- attributes

  public String text;
  public Character accessKey;
  public int pos = -1;

// --------------------------------------------------------------- constructors

  public LabelWithAccessKey(UIComponent component) {
    accessKey = ComponentUtil.getCharakterAttribute(
        component, TobagoConstants.ATTR_ACCESS_KEY);
    String labelWithAccessKey = (String) component.getAttributes().get(
        TobagoConstants.ATTR_LABEL_WITH_ACCESS_KEY);
    text = (String) component.getAttributes().get(
        TobagoConstants.ATTR_LABEL);

    if (labelWithAccessKey != null && (accessKey != null || text != null)) {
      LOG.warn("labelWithAccessKey overwrites accessKey or label: "
         + "labelWithAccessKey='" + labelWithAccessKey + "', "
         + "accessKey ='" + accessKey + "', "
         + "label='" + text + "'.");
    }
    
    if (labelWithAccessKey != null) {
      pos = labelWithAccessKey.indexOf('&');
      if (pos == -1) {
        LOG.warn("no & in labelWithAccessKey"
            + "labelWithAccessKey='" + labelWithAccessKey + "'.");
        text = labelWithAccessKey;
      } else if (pos == labelWithAccessKey.length() - 1) {
        LOG.warn("& in labelWithAccessKey is last char, this is not allowed"
          + "labelWithAccessKey='" + labelWithAccessKey + "'.");
        text = labelWithAccessKey.substring(0, labelWithAccessKey.length() - 1);
        pos = -1;
      } else {
        text = labelWithAccessKey.substring(0, pos)
            + labelWithAccessKey.substring(pos + 1);
        accessKey = new Character(text.charAt(pos));
      }
    } else {
      if (accessKey != null) {
        pos = text.toLowerCase().indexOf(
            Character.toLowerCase(accessKey.charValue()));
      }
    }
  }

// ------------------------------------------------------------ getter + setter

  public String getText() {
    return text;
  }

  public Character getAccessKey() {
    return accessKey;
  }

  public int getPos() {
    return pos;
  }
}

