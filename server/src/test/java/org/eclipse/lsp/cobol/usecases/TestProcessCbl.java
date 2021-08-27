/*
 * Copyright (c) 2021 Broadcom.
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
package org.eclipse.lsp.cobol.usecases;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.eclipse.lsp.cobol.service.delegates.validations.SourceInfoLevels;
import org.eclipse.lsp.cobol.usecases.engine.UseCaseEngine;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/** UseCase test example without errors */
class TestProcessCbl {
  private static final String TEXT =
      "PROCESS AWO\n"
          + "CBL AWO\n"
          + "       CBL NOAWO, AWO\n"
          + "             PROCESS NOAWO, AWO\n"
          + "       Identification Division.\n"
          + "       Program-id. DEMO.";

  private static final String PREFIX = "PROCESS ";
  private static final String SUFFIX = "\n       Identification Division.\n       Program-id. DEMO.";

  @Test
  void test() {
    UseCaseEngine.runTest(TEXT, ImmutableList.of(), ImmutableMap.of());
  }

  private static Stream<String> getOptions() {
    return Stream.of(
        "NOADATA",
        "ADATA",

        "ADV",
        "NOADV",

        "AFP(NOVOLATILE)",
        "AFP(VOLATILE)",

        "QUOTE",
        "APOST",
        "Q",

        "ARCH(8)",
        "ARCH(10)",
        "ARCH(13)",

        "ARITH(COMPAT)",
        "ARITH(EXTEND)",
        "AR(C)",
        "AR(E)",

        "NOAWO",
        "AWO",

        "NOBLOCK0",
        "BLOCK0",

        "BUFSIZE(4096)",
        "BUFSIZE(256)",
        "BUFSIZE(10000)",
        "BUFSIZE(128K)",
        "BUF(256)",

        "NOCICS",
        "CICS",
        "CICS(\"string1\")",

        "CODEPAGE(1140)",
        "CP(37)",

        "NOCOMPILE(S)",
        "NOCOMPILE(E)",
        "NOCOMPILE(W)",
        "COMPILE",
        "NOCOMPILE",
        "C",
        "NOC",

        "NOCOPYLOC",
        "COPYLOC(MYLIB,DSN(USERID.COBOL.COPYLIB1))",
        "COPYLOC(MYLIB,PATH('/home/userid/copylib1'))",
        "CPLC(DSN(USER.COBOL))",
        "COPYLOC(SYSLIB,PATH('/tmp'))",
        "NOCPLC",

        "NOCOPYRIGHT",
        "COPYRIGHT('copyright string')",
        "NOCPYR",

        "NOCURRENCY",
        "CURRENCY($)",
        "CURR(€)",
        "NOCURR",

        "DATA(31)",
        "DATA(24)",

        "DBCS",
        "NODBCS",

        "NODECK",
        "DECK",
        "D",
        "NOD",

        "NODEFINE",
        "DEFINE(foo)",
        "DEFINE(foo=bar)",
        "DEF(foo,bar)",
        "NODEF",

        "NODIAGTRUNC",
        "DIAGTRUNC",
        "DTR",
        "NODTR",

        "DISPSIGN(COMPAT)",
        "DISPSIGN(SEP)",
        "DS(C)",
        "DS(S)",

        "DLL",
        "NODLL",

        "DUMP",
        "NODUMP",
        "DU",
        "NODU",

        "NODYNAM",
        "DYNAM",
        "DYN",
        "NODYN",

        "NOEXIT",
        "EXIT()",
        "EXIT(INEXIT(mod1))",
        "EXIT(INEXIT(str1,mod1))",
        "EXIT(NOINEXIT)",
        "EX(INX(mod1) NOINX)",
        "EXIT(LIBEXIT(str1,mod1) NOLIBEXIT)",
        "EX(LIBX(mod2) NOLIBX)",
        "EXIT(PRTEXIT(str1,mod1) NOPRTEXIT)",
        "EX(PRTX(mod2) NOPRTX)",
        "EXIT(ADEXIT(str1,mod1) NOADEXIT)",
        "EX(ADX(mod2) NOADX)",
        "EXIT(MSGEXIT(str1,mod1) NOMSGEXIT)",
        "EX(MSGX(mod2) NOMSGX)",
        "NOEX",

        "NOEXPORTALL",
        "EXPORTALL",
        "EXP",
        "NOEXP",

        "NOFASTSRT",
        "FASTSRT",
        "FSRT",
        "NOFSRT",

        "FLAG(I,I)",
        "NOFLAG",
        "FLAG(W)",
        "F(E)",
        "F(S,U)",
        "NOF",

        "NOFLAGSTD",
        "FLAGSTD(M)",
        "FLAGSTD(I D)",
        "FLAGSTD(H NS, O)",
        "FLAGSTD(M SD)",

        "HGPR(PRESERVE)",
        "HGPR(NOPRESERVE)",

        "NOINITCHECK",
        "INITCHECK",
        "INITCHECK()",
        "INITCHECK(LAX)",
        "IC(STRICT)",
        "NOIC",

        "NOINITIAL",
        "INITIAL",

        "INLINE",
        "NOINLINE",
        "INL",
        "NOINL",

        "INTDATE(ANSI)",
        "INTDATE(LILIAN)",

        "INVDATA",
        "INVDATA(NOFORCENUMCMP,CLEANSIGN)",
        "INVDATA(NOCLEANSIGN)",
        "INVDATA(FORCENUMCMP)",
        "INVD(CS,NOCS)",
        "INVD(FNC,NOFNC)",
        "NOINVDATA",
        "NOINVD",

        "LANGUAGE(ENGLISH)",
        "LANG(EN)",
        "LANGUAGE(JAPANESE)",
        "LANG(JA)",
        "LANG(JP)",
        "LANGUAGE(UENGLISH)",
        "LANG(UE)",

        "LINECOUNT(60)",
        "LC(10)",
        "LC(0)",
        "LC(255)",

        "NOLIST",
        "LIST",

        "LP(32)",
        "LP(64)",

        "NOMAP",
        "MAP",
        "MAP(HEX)",
        "MAP(DEC)",

        "MAXPCF(100000)",
        "MAXPCF(0)",
        "MAXPCF(999999)",

        "NOMDECK",
        "MDECK",
        "MDECK(COMPILE)",
        "MDECK(NOCOMPILE)",
        "MD",
        "MD(C)",
        "MD(NOC)",
        "NOMD",

        "NONAME",
        "NAME",
        "NAME(NOALIAS)",
        "NAME(ALIAS)",

        "NSYMBOL(NATIONAL)",
        "NSYMBOL(DBCS)",
        "NS(NAT)",

        "NONUMBER",
        "NUMBER",
        "NUM",
        "NONUM",

        "NUMCHECK",
        "NONUMCHECK",
        "NC",
        "NONC",
        "NUMCHECK(ZON)",
        "NUMCHECK(ZON(ALPHNUM))",
        "NUMCHECK(ZON(NOALPHNUM))",
        "NUMCHECK(ZON(LAX))",
        "NUMCHECK(ZON(STRICT))",
        "NUMCHECK(ZON(ALPHNUM, STRICT))",
        "NUMCHECK(NOZON, PAC)",
        "NUMCHECK(NOPAC)",
        "NUMCHECK(BIN)",
        "NUMCHECK(BIN(TRUNCBIN))",
        "NUMCHECK(BIN(NOTRUNCBIN))",
        "NUMCHECK(NOBIN)",
        "NUMCHECK(MSG, ABD)",
        "NUMCHECK(ZON(ALPHNUM,STRICT),PAC,BIN(TRUNCBIN),MSG)",

        "NUMPROC(NOPFD)",
        "NUMPROC(PFD)",

        "OBJECT",
        "NOOBJECT",
        "OBJ",
        "NOOBJ",

        "NOOFFSET",
        "OFFSET",
        "OFF",
        "NOOFF",

        "OPTFILE",

        "OPTIMIZE(0)",
        "OPT(1)",
        "OPT(2)",

        "OUTDD(SYSOUT)",
        "OUT(SOME.DD.NAME)",

        "NOPARMCHECK",
        "PARMCHECK",
        // in case (MSG,100) without space between ',' and '1' `,100` will be a NUMERICLITERAL
        "PARMCHECK(MSG, 100)",
        "PC(ABD)",
        "PC(MSG, 5000)",
        "NOPC",

        "PGMNAME(COMPAT)",
        "PGMN(CO)",
        "PGMNAME(LONGMIXED)",
        "PGMN(MIXED)",
        "PGMN(LM)",
        "PGMN(M)",
        "PGMNAME(LONGUPPER)",
        "PGMN(UPPER)",
        "PGMN(LU)",
        "PGMN(U)",

        "QUALIFY(COMPAT)",
        "QUALIFY(EXTEND)",
        "QUA(C)",
        "QUA(E)",

        "RENT",
        "NORENT",

        "RMODE(AUTO)",
        "RMODE(24)",
        "RMODE(ANY)",

        "NORULES",
        "RULES(ENDPERIOD, ENDP)",
        "RULES(NOENDPERIOD)",
        "RULES(EVENPACK, EVENP)",
        "RULES(NOEVENPACK)",
        "RULES(LAXPERF, LXPRF)",
        "RULES(NOLAXPERF)",
        "RULES(SLACKBYTES, SLCKB)",
        "RULES(NOSLACKBYTES)",
        "RULES(OMITODOMIN, OOM)",
        "RULES(NOOMITODOMIN)",
        "RULES(UNREF)",
        "RULES(NOUNREFALL, NOUNRA)",
        "RULES(NOUNREFSOURCE, NOUNRS)",
        "RULES(LAXREDEF, LXRDF)",
        "RULES(NOLAXREDEF)",

        "SEQUENCE",
        "NOSEQUENCE",
        "SEQ",
        "NOSEQ",

        "NOSERVICE",
        "SERVICE('service string')",
        "NOSERV",

        "SOURCE",
        "SOURCE(DEC)",
        "SOURCE(HEX)",
        "NOSOURCE",
        "S",
        "NOS",

        "SPACE(1)",
        "SPACE(2)",
        "SPACE(3)",

        "NOSQL",
        "SQL",
        "SQL(\"DB2-suboption-string\")",

        "SQLCCSID",
        "NOSQLCCSID",
        "SQLC",
        "NOSQLC",

        "NOSQLIMS",
        "SQLIMS",
        "SQLIMS(\"IMS-suboption-string\")",

        "NOSSRANGE",
        "SSRANGE()",
        "SSRANGE(ZLEN)",
        "SSRANGE(NOZLEN,ABD)",
        "SSR(MSG)",
        "NOSSR",

        "NOSTGOPT",
        "STGOPT",
        "SO",
        "NOSO",

        "SUPPRESS",
        "NOSUPPRESS",
        "SUPP",
        "NOSUPP",

        "NOTERMINAL",
        "TERMINAL",
        "TERM",
        "NOTERM",

        "NOTEST",
        "TEST",
        "TEST()",
        "NOTEST(NODWARF, NOSOURCE, NOSEPARATE)",
        "TEST(DWARF)",
        "TEST(EJPD, NOEJPD)",
        "TEST(SEPARATE, SEPARATE(DSNAME), SEPARATE(NODSNAME))",
        "TEST(SOURCE, NOSOURCE)",
        "TEST(NOSO, SO, NOSEP, SEP)",

        "NOTHREAD",
        "THREAD",

        "TRUNC(STD)",
        "TRUNC(OPT)",
        "TRUNC(BIN)",

        "TUNE(8)",
        "TUNE(11)",
        "TUNE(13)",

        "NOVBREF",
        "VBREF",

        "VLR(STANDARD)",
        "VLR(COMPAT)",
        "VLR(C)",
        "VLR(S)",

        "VSAMOPENFS(COMPAT)",
        "VSAMOPENFS(SUCC)",
        "VS(C)",
        "VS(S)",

        "NOWORD",
        "WORD(ABCD)",
        "WD(1234)",
        "NOWD",

        "XMLPARSE(XMLSS)",
        "XMLPARSE(COMPAT)",
        "XP(X)",
        "XP(C)",

        "XREF",
        "XREF(FULL)",
        "XREF(SHORT)",
        "X",
        "NOXREF",
        "NOX",

        "NOZONECHECK",
        "ZONECHECK(MSG)",
        "ZONECHECK(ABD)",
        "ZC(ABD)",
        "NOZC",

        "ZONEDATA(PFD)",
        "ZONEDATA(NOPFD)",
        "ZONEDATA(MIG)",
        "ZD(PFD)",

        "ZWB",
        "NOZWB"
    );
  }

  @ParameterizedTest
  @MethodSource("getOptions")
  void testOption(String cblOption) {
    UseCaseEngine.runTest(PREFIX + cblOption + SUFFIX, ImmutableList.of(), ImmutableMap.of());
  }

  @Test
  void negativeCaseForCurrency() {
    UseCaseEngine.runTest(PREFIX + "CURR({foo|1})" + SUFFIX, ImmutableList.of(), ImmutableMap.of(
        "1",
        new Diagnostic(null,
            "Syntax error on 'foo' expected 'Currency symbol'",
            DiagnosticSeverity.Error,
            SourceInfoLevels.ERROR.getText())
    ));
  }

  @Test
  void negativeCaseForBuff() {
    UseCaseEngine.runTest(PREFIX + "BUFSIZE({256Kb|1})" + SUFFIX, ImmutableList.of(), ImmutableMap.of(
        "1",
        new Diagnostic(null,
            "Syntax error on '256Kb' expected {'01-49', '66', '77', '88', INTEGERLITERAL}",
            DiagnosticSeverity.Error,
            SourceInfoLevels.ERROR.getText())
    ));
  }
}
