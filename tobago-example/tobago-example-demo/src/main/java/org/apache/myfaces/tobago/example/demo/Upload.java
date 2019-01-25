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

import org.apache.myfaces.tobago.internal.util.PartUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;

public class Upload {

  private static final Logger LOG = LoggerFactory.getLogger(Upload.class);

  private Part file1;
  private Part file2;
  private Part[] fileMulti;
  private Part[] fileAjax;
  private Part[] fileDnd;

  private List<UploadItem> list = new ArrayList<UploadItem>();

  public String upload() {
    upload(file1);
    upload(file2);
    upload(fileMulti);
    upload(fileAjax);
    upload(fileDnd);
    file1 = null;
    file2 = null;
    fileMulti = null;
    fileAjax = null;
    fileDnd = null;
    return null;
  }

  public void upload(Part[] files) {
    if (files != null) {
      for (Part file : files) {
        upload(file);
      }
    }
  }

  public void upload(Part part) {
    LOG.info("checking file item");
    if (part == null || part.getSize() == 0) {
      return;
    }
    LOG.info("type='{}'", part.getContentType());
    LOG.info("size={}", part.getSize());
    final String submittedFileName = PartUtils.getSubmittedFileName(part);
    LOG.info("name=" + submittedFileName);
    list.add(new UploadItem(submittedFileName, part.getSize(), part.getContentType()));
  }

  public Part getFile1() {
    return file1;
  }

  public void setFile1(Part file1) {
    this.file1 = file1;
  }

  public Part getFile2() {
    return file2;
  }

  public void setFile2(Part file2) {
    this.file2 = file2;
  }

  public Part[] getFileMulti() {
    return fileMulti;
  }

  public void setFileMulti(Part[] fileMulti) {
    this.fileMulti = fileMulti;
  }

  public Part[] getFileAjax() {
    return fileAjax;
  }

  public void setFileAjax(Part[] fileAjax) {
    this.fileAjax = fileAjax;
  }

  public Part[] getFileDnd() {
    return fileDnd;
  }

  public void setFileDnd(Part[] fileDnd) {
    this.fileDnd = fileDnd;
  }

  public List<UploadItem> getList() {
    return list;
  }

  public String getDropZoneId() {
    return "fileDropArea";
  }
}
