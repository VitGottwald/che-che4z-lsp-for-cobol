/*
 * Copyright (c) 2022 DAF Trucks NV.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * DAF Trucks NV – implementation of DaCo COBOL statements
 * and DAF development standards
 */
package org.eclipse.lsp.cobol.usecases.daco;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.eclipse.lsp.cobol.common.error.ErrorSource;
import org.eclipse.lsp.cobol.usecases.DialectConfigs;
import org.eclipse.lsp.cobol.usecases.engine.UseCaseEngine;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;
import org.junit.jupiter.api.Test;

/**
 * Tests the DaCo ROW SAVE statement
 */
class TestDaCoTableRowSaveStatement {

    private static final String TEXT =
            "        IDENTIFICATION DIVISION. \r\n"
                    + "        PROGRAM-ID. test1. \r\n"
                    + "        ENVIRONMENT DIVISION.\n"
                    + "        IDMS-CONTROL SECTION.\n"
                    + "            PROTOCOL. MODE ABC.\n"
                    + "            IDMS-RECORDS MANUAL\n"
                    + "        DATA DIVISION. \r\n"
                    + "        WORKING-STORAGE SECTION. \r\n"
                    + "        01 {$*WS-AREA}. \r\n"
                    + "           03 {$*AREA-XW1}. \r\n"
                    + "             05 {$*TBLPRO-XL1}. \r\n"
                    + "               07 FILLER               PIC X(5)    VALUE 'REMBD'. \r\n"
                    + "             05 {$*TBFPRO-XL1}. \r\n"
                    + "               07 FILLER               PIC X(5)    VALUE 'REMBD'. \r\n"
                    + "             05 {$*DSAPRO-XL1}. \r\n"
                    + "               07 FILLER               PIC X(5)    VALUE 'REMBD'. \r\n"
                    + "        PROCEDURE DIVISION. \r\n"
                    + "            ROW SAVE {$TBLPRO-XL1} IN 2. \r\n"
                    + "            ROW SAVE {$TBLPRO-XL1} IN 'ABC'. \r\n"
                    + "            ROW SAVE {$TBLPRO-XL1} IN {$DSAPRO-XL1}. \r\n"
                    // Negative tests
                    + "            ROW SAVE {$DSAPRO-XL1|1} IN 2. \r\n"
                    + "            ROW SAVE {GBR4|1|2} IN 2. \r\n"
                    + "            ROW SAVE {$TBLPRO-XL1} IN {GBR4|2}. \r\n";

    @Test
    void test() {

        UseCaseEngine.runTest(
                TEXT,
                ImmutableList.of(),
                ImmutableMap.of(
                        "1",
                        new Diagnostic(
                                new Range(),
                                "String must starts with TBL or TBF values",
                                DiagnosticSeverity.Error,
                                ErrorSource.DIALECT.getText()),
                        "2",
                        new Diagnostic(
                                new Range(),
                                "Variable GBR4 is not defined",
                                DiagnosticSeverity.Error,
                                ErrorSource.PARSING.getText())),
                ImmutableList.of(),
                DialectConfigs.getDaCoAnalysisConfig());
    }
}
