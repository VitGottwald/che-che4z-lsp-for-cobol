/*
 * Copyright (c) 2022 Broadcom.
 * The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Broadcom, Inc. - initial API and implementation
 *
 */
package org.eclipse.lsp.cobol.service;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.eclipse.lsp.cobol.core.model.tree.EmbeddedCodeNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.eclipse.lsp.cobol.service.utils.SettingsParametersEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/** Test to check CachingConfigurationServiceTest */
class CachingConfigurationServiceTest {

  @Test
  void testInitialLoading() {
    SettingsService settingsService = spy(SettingsService.class);
    CachingConfigurationService configuration = new CachingConfigurationService(settingsService);

    assertEquals(
        new AnalysisConfig(
            new CopybookConfig(CopybookProcessingMode.ENABLED, SQLBackend.DB2_SERVER),
            ImmutableList.of(),
            ImmutableList.of()),
        configuration.getConfig(CopybookProcessingMode.ENABLED));
  }

  @Test
  void testUpdatingConfiguration() {
    SettingsService settingsService = spy(SettingsService.class);
    JsonArray featuresArray = new JsonArray();
    featuresArray.add("SQL");

    JsonArray dialectSettings = new JsonArray();
    dialectSettings.add("Dialect");

    List<Object> clientConfig =
        Arrays.asList(new JsonPrimitive("DATACOM_SERVER"), featuresArray, dialectSettings);

    when(settingsService.getConfigurations(
            Arrays.asList(TARGET_SQL_BACKEND.label, ANALYSIS_FEATURES.label, DIALECTS.label)))
        .thenReturn(supplyAsync(() -> clientConfig));

    CachingConfigurationService configuration = new CachingConfigurationService(settingsService);
    configuration.updateConfigurationFromSettings();

    assertEquals(
        new AnalysisConfig(
            new CopybookConfig(CopybookProcessingMode.DISABLED, SQLBackend.DATACOM_SERVER),
            ImmutableList.of(EmbeddedCodeNode.Language.SQL),
            ImmutableList.of("Dialect")),
        configuration.getConfig(CopybookProcessingMode.DISABLED));
  }
}