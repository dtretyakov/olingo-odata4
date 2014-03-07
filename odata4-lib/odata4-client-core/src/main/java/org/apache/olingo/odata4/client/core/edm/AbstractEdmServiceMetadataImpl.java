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
package org.apache.olingo.odata4.client.core.edm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.olingo.odata4.client.api.edm.xml.EntityContainer;
import org.apache.olingo.odata4.client.api.edm.xml.EntitySet;
import org.apache.olingo.odata4.client.api.edm.xml.Schema;
import org.apache.olingo.odata4.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.odata4.commons.api.edm.EdmActionImportInfo;
import org.apache.olingo.odata4.commons.api.edm.EdmEntitySetInfo;
import org.apache.olingo.odata4.commons.api.edm.EdmFunctionImportInfo;
import org.apache.olingo.odata4.commons.api.edm.EdmServiceMetadata;
import org.apache.olingo.odata4.commons.core.edm.EdmEntitySetInfoImpl;

public abstract class AbstractEdmServiceMetadataImpl implements EdmServiceMetadata {

  protected final XMLMetadata xmlMetadata;

  private List<EdmEntitySetInfo> entitySetInfos;

  protected List<EdmFunctionImportInfo> functionImportInfos;

  protected List<EdmActionImportInfo> actionImportInfos;

  public static EdmServiceMetadata getInstance(final XMLMetadata xmlMetadata) {
    return xmlMetadata instanceof org.apache.olingo.odata4.client.core.edm.xml.v3.XMLMetadataImpl
            ? new org.apache.olingo.odata4.client.core.edm.v3.EdmServiceMetadataImpl(
                    (org.apache.olingo.odata4.client.core.edm.xml.v3.XMLMetadataImpl) xmlMetadata)
            : new org.apache.olingo.odata4.client.core.edm.v4.EdmServiceMetadataImpl(
                    (org.apache.olingo.odata4.client.core.edm.xml.v4.XMLMetadataImpl) xmlMetadata);

  }

  public AbstractEdmServiceMetadataImpl(final XMLMetadata xmlMetadata) {
    this.xmlMetadata = xmlMetadata;
  }

  @Override
  public InputStream getMetadata() {
    throw new UnsupportedOperationException("Not supported in client code.");
  }

  @Override
  public List<EdmEntitySetInfo> getEntitySetInfos() {
    synchronized (this) {
      if (entitySetInfos == null) {
        entitySetInfos = new ArrayList<EdmEntitySetInfo>();
        for (Schema schema : xmlMetadata.getSchemas()) {
          for (EntityContainer entityContainer : schema.getEntityContainers()) {
            for (EntitySet entitySet : entityContainer.getEntitySets()) {
              entitySetInfos.add(
                      new EdmEntitySetInfoImpl(entityContainer.getName(), entitySet.getName()));
            }
          }
        }
      }
      return entitySetInfos;
    }
  }

  @Override
  public boolean equals(final Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}