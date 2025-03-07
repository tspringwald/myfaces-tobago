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

package org.apache.myfaces.tobago.example.demo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SessionScoped
@Named
public class SuggestQuotMarkController implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private List<String> suggestions = new ArrayList<>(8);
  private String query;

  public SuggestQuotMarkController() {
    suggestions.add("Mercury");
    suggestions.add("Venus");
    suggestions.add("Earth");
    suggestions.add("Mars");
    suggestions.add("Jupiter");
    suggestions.add("Saturn");
    suggestions.add("Uranus");
    suggestions.add("Quotation\"Mark");
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(final String query) {
    this.query = query;
  }

  public List<String> getSuggestions() {
    final String substring = query != null ? query : "";
    LOG.info("Creating items for substring: '" + substring + "'");
    return suggestions.stream().filter(s -> StringUtils.containsIgnoreCase(s, substring)).collect(Collectors.toList());
  }
}
