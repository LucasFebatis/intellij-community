/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.openapi.editor.impl.softwrap.mapping;

import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.ex.SoftWrapModelEx;
import com.intellij.openapi.editor.impl.SoftWrapModelImpl;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;

import java.awt.*;
import java.io.IOException;

/**
 * @author Denis Zhdanov
 * @since 09/16/2010
 */
public class SoftWrapApplianceOnDocumentModificationTest extends LightPlatformCodeInsightTestCase {

  //private static final String PATH = "/codeInsight/softwrap/";

  @Override
  protected void tearDown() throws Exception {
    myEditor.getSettings().setUseSoftWraps(false);
    super.tearDown();
  }

  public void testSoftWrapAdditionOnTyping() throws Exception {
    String text =
      "this is a test string that is expected to end just before right margin<caret>";
    init(800, text);

    int offset = myEditor.getDocument().getTextLength() + 1;
    assertTrue(getSoftWrapModel().getRegisteredSoftWraps().isEmpty());
    type(" thisisalongtokenthatisnotexpectedtobebrokenintopartsduringsoftwrapping");
    assertNotNull(myEditor.getSoftWrapModel().getSoftWrap(offset));
  }

  public void testLongLineOfIdSymbolsIsNotSoftWrapped() throws Exception {
    String text =
      "abcdefghijklmnopqrstuvwxyz<caret>\n" +
      "123\n" +
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    init(100, text);
    assertTrue(getSoftWrapModel().getRegisteredSoftWraps().isEmpty());
    type('1');
    assertTrue(getSoftWrapModel().getRegisteredSoftWraps().isEmpty());

    int offset = myEditor.getDocument().getText().indexOf("\n");
    type(" test");
    assertEquals(1, getSoftWrapModel().getRegisteredSoftWraps().size());
    assertNotNull(getSoftWrapModel().getSoftWrap(offset));
  }

  public void testFoldRegionCollapsing() throws Exception {
    String text =
      "class Test {\n" +
      "    public void foo() {\n" +
      "        System.out.println(\"test\");\n" +
      "    }\n" +
      "}";

    init(300, text);
    final FoldingModel foldingModel = myEditor.getFoldingModel();
    assertEmpty(foldingModel.getAllFoldRegions());

    final int startOffset = text.indexOf('{');
    final int endOffset = text.indexOf('}') + 1;

    VisualPosition foldStartPosition = myEditor.offsetToVisualPosition(startOffset);

    addFoldRegion(startOffset, endOffset, "...");

    final FoldRegion foldRegion = foldingModel.getAllFoldRegions()[0];
    assertNotNull(foldRegion);
    assertTrue(foldRegion.isExpanded());
    toggleFoldRegionState(myEditor.getFoldingModel().getAllFoldRegions()[0], false);

    // Expecting that all offsets that belong to collapsed fold region point to the region's start.
    assertEquals(foldStartPosition, myEditor.offsetToVisualPosition(startOffset + 5));
  }

  public void testTypingEnterAtDocumentEnd() throws IOException {
    String text =
      "class Test {\n" +
      "    public void foo() {\n" +
      "        System.out.println(\"test\");\n" +
      "    }\n" +
      "}<caret>";

    init(300, text);
    type('\n');
    VisualPosition position = myEditor.getCaretModel().getVisualPosition();
    assertEquals(new VisualPosition(5, 0), position);
  }

  public void testDeleteDocumentTail() throws IOException {
    String text =
      "class Test {\n" +
      "    public void foo() {\n" +
      "        System.out.println(\"test\");\n" +
      "    }\n" +
      "}\n" +
      "abcde";

    init(300, text);
    int offset = text.indexOf("abcde");
    myEditor.getSelectionModel().setSelection(offset, text.length());
    delete();
    assertEquals(new VisualPosition(5, 0), myEditor.getCaretModel().getVisualPosition());
  }

  public void testTypingTabOnLastEmptyLine() throws IOException {
    String text =
      "class Test {\n" +
      "}\n" +
      "<caret>";

    init(300, text);
    type('\t');
    assertEquals(new VisualPosition(2, 4), myEditor.getCaretModel().getVisualPosition());
  }

  public void testTrailingFoldRegionRemoval() throws IOException {
    String text =
      "public class BrokenAlignment {\n" +
      "    @SuppressWarnings({ \"SomeInspectionIWantToIgnore\" })\n" +
      "    public void doSomething(int x, int y) {\n" +
      "    }\n" +
      "}";

    init(700, text);

    int startFoldOffset = text.indexOf('@');
    int endFoldOffset = text.indexOf(')');
    addFoldRegion(startFoldOffset, endFoldOffset, "/SomeInspectionIWantToIgnore/");
    toggleFoldRegionState(myEditor.getFoldingModel().getAllFoldRegions()[0], false);

    int endSelectionOffset = text.lastIndexOf("}\n") + 1;
    myEditor.getSelectionModel().setSelection(startFoldOffset, endSelectionOffset);

    delete();
    // Don't expect any exceptions here.
  }

  public void testTypeNewLastLineAndSymbolOnIt() throws IOException {
    // Inspired by IDEA-59439
    String text = 
      "This is a test document\n" +
      "line1\n" +
      "line2\n" +
      "line3\n" +
      "line4\n" +
      "line5\n" +
      "line6<caret>";
    
    init(700, text);
    type("\nq");
    assertEquals(new VisualPosition(7, 1), myEditor.offsetToVisualPosition(myEditor.getDocument().getTextLength()));
  }
  
  //private void init(final int visibleWidth) throws Exception {
  //  configureByFile(PATH + getFileName());
  //  initCommon(visibleWidth);
  //}

  private void init(int visibleWidth, String fileText) throws IOException {
    configureFromFileText(getFileName(), fileText);
    initCommon(visibleWidth);
  }

  private String getFileName() {
    return getTestName(false) + ".txt";
  }

  private static void addFoldRegion(final int startOffset, final int endOffset, final String placeholder) {
    myEditor.getFoldingModel().runBatchFoldingOperation(new Runnable() {
      @Override
      public void run() {
        myEditor.getFoldingModel().addFoldRegion(startOffset, endOffset, placeholder);
      }
    });
  }

  private static void toggleFoldRegionState(final FoldRegion foldRegion, final boolean expanded) {
    myEditor.getFoldingModel().runBatchFoldingOperation(new Runnable() {
      @Override
      public void run() {
        foldRegion.setExpanded(expanded);
      }
    });
  }

  private static void initCommon(final int visibleWidth) {
    myEditor.getSettings().setUseSoftWraps(true);
    SoftWrapModelImpl model = (SoftWrapModelImpl)myEditor.getSoftWrapModel();
    model.refreshSettings();

    SoftWrapApplianceManager applianceManager = model.getApplianceManager();
    applianceManager.setWidthProvider(new SoftWrapApplianceManager.VisibleAreaWidthProvider() {
      @Override
      public int getVisibleAreaWidth() {
        return visibleWidth;
      }
    });
    applianceManager.registerSoftWrapIfNecessary(new Rectangle(visibleWidth, visibleWidth * 2), 0);
  }

  private static SoftWrapModelEx getSoftWrapModel() {
    return (SoftWrapModelEx)myEditor.getSoftWrapModel();
  }
}
