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

package org.eclipse.lsp.cobol.dialects.idms;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.lsp.cobol.common.ResultWithErrors;
import org.eclipse.lsp.cobol.common.copybook.CopybookConfig;
import org.eclipse.lsp.cobol.common.copybook.CopybookModel;
import org.eclipse.lsp.cobol.common.copybook.CopybookName;
import org.eclipse.lsp.cobol.common.error.SyntaxError;
import org.eclipse.lsp.cobol.common.message.MessageService;
import org.eclipse.lsp.cobol.common.model.tree.CopyDefinition;
import org.eclipse.lsp.cobol.common.model.tree.CopyNode;
import org.eclipse.lsp.cobol.common.model.Locality;
import org.eclipse.lsp.cobol.common.model.tree.Node;
import org.eclipse.lsp.cobol.common.utils.ThreadInterruptionUtil;
import org.eclipse.lsp.cobol.common.copybook.CopybookService;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Class implements idms copybook processing
 */
@RequiredArgsConstructor
class IdmsCopybookService {

  private final String programDocumentUri;
  private final CopybookService copybookService;
  private final CopybookConfig copybookConfig;
  private final ParseTreeListener treeListener;
  private final MessageService messageService;
  private final Set<CopybookName> processedCopybooks;

  /**
   * Process idms copybook
   * @param copybookModel - copybook model
   * @param parentLevel - copy statement parent level
   * @param locality - copybook statement locality
   * @return - a list of generated nodes
   */
  public ResultWithErrors<List<Node>> processCopybook(CopybookModel copybookModel, int parentLevel, Locality locality) {
    CopybookName copybookName = copybookModel.getCopybookName();

    if (copybookModel.getContent() == null) {
      List<SyntaxError> errors = new LinkedList<>();
      errors.add(ErrorHelper.missingCopybooks(messageService, locality, copybookName.getQualifiedName()));
      return new ResultWithErrors<>(ImmutableList.of(), errors);
    }

    if (processedCopybooks.contains(copybookName)) {
      List<SyntaxError> errors = new LinkedList<>();
      errors.add(ErrorHelper.circularDependency(messageService, locality, copybookName.getQualifiedName()));
      return new ResultWithErrors<>(ImmutableList.of(), errors);
    }

    processedCopybooks.add(copybookName);

    CopyNode node = new CopyNode(locality, copybookName.getDisplayName(), IdmsDialect.NAME);

    Location location = new Location();
    location.setUri(copybookModel.getUri());
    location.setRange(new Range(new Position(), new Position()));

    node.setDefinition(new CopyDefinition(location, copybookModel.getUri()));

    List<SyntaxError> errors = new LinkedList<>();
    processNodes(copybookModel, parentLevel).unwrap(errors::addAll)
        .forEach(node::addChild);
    return new ResultWithErrors<>(ImmutableList.of(node), errors);
  }

  private ResultWithErrors<List<Node>> processNodes(CopybookModel copybookModel, int parentLevel) {
    IdmsCopyLexer lexer = new IdmsCopyLexer(CharStreams.fromString(copybookModel.getContent()));
    lexer.removeErrorListeners();

    CommonTokenStream tokens = new CommonTokenStream(lexer);
    ParserListener listener = new ParserListener();
    lexer.addErrorListener(listener);

    IdmsCopyParser parser = getCobolParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(listener);
    parser.setErrorHandler(new CobolErrorStrategy(messageService));
    parser.addParseListener(treeListener);

    IdmsCopybookVisitor visitor = new IdmsCopybookVisitor(copybookService, copybookConfig, treeListener, messageService,
        programDocumentUri, copybookModel.getUri(), parentLevel, processedCopybooks);

    ParserRuleContext node = parser.startRule();
    List<Node> nodes = visitor.visit(node);

    List<SyntaxError> errors = new LinkedList<>(listener.getErrors());
    errors.addAll(visitor.getErrors());
    return new ResultWithErrors<>(nodes, errors);
  }

  private IdmsCopyParser getCobolParser(CommonTokenStream tokens) {
    ThreadInterruptionUtil.checkThreadInterrupted();
    return new IdmsCopyParser(tokens);
  }

}
