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

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named
public class SelectManyController implements Serializable {

  @Inject
  private AstroData astroData;

  private List<SolarObject> planets;
  private SolarObject[] selected1 = new SolarObject[0];
  private SolarObject[] selected2 = new SolarObject[0];
  private SolarObject[] selected3 = new SolarObject[0];
  private SolarObject[] selected4 = new SolarObject[0];

  @PostConstruct
  public void init() {
    planets = astroData.getSatellites("Sun");
  }

  public List<SolarObject> getPlanets() {
    return planets;
  }

  public SolarObject[] getSelected1() {
    return selected1;
  }

  public void setSelected1(SolarObject[] selected1) {
    this.selected1 = selected1;
  }

  public SolarObject[] getSelected2() {
    return selected2;
  }

  public void setSelected2(SolarObject[] selected2) {
    this.selected2 = selected2;
  }

  public SolarObject[] getSelected3() {
    return selected3;
  }

  public void setSelected3(SolarObject[] selected3) {
    this.selected3 = selected3;
  }

  public SolarObject[] getSelected4() {
    return selected4;
  }

  public void setSelected4(SolarObject[] selected4) {
    this.selected4 = selected4;
  }
}
