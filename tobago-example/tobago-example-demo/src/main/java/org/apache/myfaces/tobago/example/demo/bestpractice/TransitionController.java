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

package org.apache.myfaces.tobago.example.demo.bestpractice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.lang.invoke.MethodHandles;

@RequestScoped
@Named
public class TransitionController {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public String sleep5sAndRedirect() throws InterruptedException {
    final int sleep = 5000;
    LOG.info("Waiting " + sleep + " millis.");
    Thread.sleep(sleep);

    return "/content/350-transition/x-transition-after-sleep.xhtml?faces-redirect=true";
  }

  public String sleep5s() throws InterruptedException {
    final int sleep = 5000;
    LOG.info("Waiting " + sleep + " millis.");
    Thread.sleep(sleep);

    return null;
  }
}
