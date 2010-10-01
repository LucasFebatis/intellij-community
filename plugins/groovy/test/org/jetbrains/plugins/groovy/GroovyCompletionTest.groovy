/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.jetbrains.plugins.groovy;


import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.groovy.util.TestUtils

/**
 * @author Maxim.Medvedev
 */
public class GroovyCompletionTest extends GroovyCompletionTestBase {
  @Override
  protected String getBasePath() {
    return TestUtils.getTestDataPath() + "groovy/completion/";
  }

  public void testFinishMethodWithLParen() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "getBar", "getClass", "getFoo");
    myFixture.type('(');
    myFixture.checkResultByFile(getTestName(false) + "_after.groovy");
  }

  public void testSmartCompletionAfterNewInDeclaration() throws Throwable {
    myFixture.configureByFile(getTestName(false) + ".groovy");
    myFixture.complete(CompletionType.SMART);
    assertOrderedEquals(myFixture.getLookupElementStrings(), "Bar", "Foo");
  }

  public void testSmartCompletionAfterNewInDeclarationWithInterface() throws Throwable {
    doSmartTest();
  }

  public void testCaretAfterSmartCompletionAfterNewInDeclaration() throws Throwable {
    doSmartTest();
  }

  public void testSmartCompletionAfterNewInDeclarationWithAbstractClass() throws Throwable {
    doSmartTest();
  }

  public void testSmartCompletionAfterNewInDeclarationWithArray() throws Throwable {
    doSmartTest();
  }

  public void testSmartCompletionAfterNewInDeclarationWithIntArray() throws Throwable {
    doSmartTest();
  }

  public void testShortenNamesInSmartCompletionAfterNewInDeclaration() throws Throwable {
    doSmartTest();
  }

  public void testNamedParametersForApplication() throws Throwable {
    doVariantableTest("abx", "aby");
  }

  public void testNamedParametersForMethodCall() throws Throwable {
    doVariantableTest("abx", "aby");
  }

  public void testNamedParametersForNotMap() throws Throwable {
    doBasicTest();
  }

  public void testNamedParametersForConstructorCall() throws Throwable {
    doVariantableTest("hahaha", "hohoho");
  }

  public void testInstanceofHelpsDetermineType() throws Throwable {
    doBasicTest();
  }

  public void testNotInstanceofDoesntHelpDetermineType() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + ".groovy");
  }

  public void testNotInstanceofDoesntHelpDetermineType2() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + ".groovy");
  }

  public void testTypeParameterCompletion() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "put", "putAll");
  }

  public void testCatchClauseParameter() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "getStackTrace", "getStackTraceDepth", "getStackTraceElement");
  }

  public void testFieldSuggestedOnce1() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + ".groovy");
    assertNull(myFixture.getLookupElements());
  }

  public void testFieldSuggestedOnce2() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + ".groovy");
    assertNull(myFixture.getLookupElements());
  }

  public void testFieldSuggestedOnce3() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + ".groovy");
    assertNull(myFixture.getLookupElements());
  }

  public void testFieldSuggestedOnce4() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + ".groovy");
    assertNull(myFixture.getLookupElements());
  }

  public void testFieldSuggestedOnce5() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + ".groovy");
    assertNull(myFixture.getLookupElements());
  }

  public void testFieldSuggestedInMethodCall() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + "_after.groovy");
  }

  public void testMethodParameterNoSpace() throws Throwable {
    myFixture.testCompletion(getTestName(false) + ".groovy", getTestName(false) + "_after.groovy");
  }

  public void testGroovyDocParameter() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "xx", "xy");
  }

  public void testInnerClassExtendsImplementsCompletion() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "extends", "implements");
  }

  public void testInnerClassCompletion() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "Inner1", "Inner2");
  }

  public void testInnerClassInStaticMethodCompletion() throws Throwable {
    doSmartTest();
  }


  public void testQualifiedThisCompletion() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "foo1", "foo2");
  }

  public void testQualifiedSuperCompletion() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "foo1", "foo2");
  }

  public void testThisKeywordCompletionAfterClassName1() throws Throwable {
    doBasicTest();
  }

  public void testThisKeywordCompletionAfterClassName2() throws Throwable {
    doBasicTest();
  }

  public void testCompletionInParameterListInClosableBlock() throws Throwable {
    doBasicTest();
  }

  public void testCompletionInParameterListInClosableBlock2() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "aDouble");
  }

  public void testStaticMemberFromInstanceContext() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "var1", "var2");
  }

  public void testInstanceMemberFromStaticContext() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "var3", "var4");
  }

  public void testTypeCompletionInVariableDeclaration1() throws Throwable {
    doBasicTest();
  }

  public void testTypeCompletionInVariableDeclaration2() throws Throwable {
    doBasicTest();
  }

  public void testTypeCompletionInParameter() throws Throwable {
    doBasicTest();
  }

  public void testGStringConcatenationCompletion() throws Throwable {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "substring", "substring", "subSequence");
  }

  public void testPropertyWithSecondUpperLetter() throws Exception {
    myFixture.testCompletionVariants(getTestName(false) + ".groovy", "geteMail", "getePost");
  }

  public void testSmartCompletionInAssignmentExpression() throws Throwable {
    doSmartTest();
  }

  public void testSimpleMethodParameter() throws Throwable {
    doSmartCompletion("d1", "d2");
  }

  public void testReturnStatement() throws Exception {
    doSmartCompletion("b", "b1", "b2", "foo");
  }

  public void testIncSmartCompletion() throws Exception {
    doSmartCompletion("a", "b");
  }

  public void testInheritConstructorsAnnotation() throws Throwable {
    myFixture.addFileToProject("groovy/transform/InheritConstructors.java", "package groovy.transform;\n" +
                                                                            "\n" +
                                                                            "import java.lang.annotation.ElementType;\n" +
                                                                            "import java.lang.annotation.Retention;\n" +
                                                                            "import java.lang.annotation.RetentionPolicy;\n" +
                                                                            "import java.lang.annotation.Target;@Retention(RetentionPolicy.SOURCE)\n" +
                                                                            "@Target({ElementType.TYPE})\n" +
                                                                            "public @interface InheritConstructors {\n" +
                                                                            "}");
    doSmartTest();
  }

  public void testIntCompletionInPlusMethod() {doBasicTest();}
  public void testIntCompletionInGenericParameter() {doBasicTest();}

  public void testSmartCastCompletion() {doSmartTest();}
  public void testSmartCastCompletionWithoutRParenth() {doSmartTest();}
  public void testSmartCastCompletionWithRParenth() {doSmartTest();}

  public void testWhenSiblingIsStaticallyImported_Method() throws Exception {
    myFixture.addFileToProject "foo/Foo.groovy", """package foo
      class Foo {
        static def abcMethod() {}
        static def defMethod() {}
      }
    """

    myFixture.configureByText("a.groovy", """
      import static foo.Foo.abcMethod

      abcMethod()
      defM<caret>
    """)
    myFixture.completeBasic()
    myFixture.checkResult """
      import static foo.Foo.abcMethod
      import static foo.Foo.defMethod

      abcMethod()
      defMethod()<caret>
    """
  }

  public void testWhenSiblingIsStaticallyImported_Field() throws Exception {
    myFixture.addFileToProject "foo/Foo.groovy", """package foo
      class Foo {
        static def abcField = 4
        static def defField = 2
      }
    """

    myFixture.configureByText("a.groovy", """
      import static foo.Foo.abcField

      println abcField
      defF<caret>
    """)
    myFixture.completeBasic()
    myFixture.checkResult """
      import static foo.Foo.abcField
      import static foo.Foo.defField

      println abcField
      defField<caret>
    """
  }

  public void testCompletionNamedArgument1() {
    def file = myFixture.configureByText(GroovyFileType.GROOVY_FILE_TYPE, """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m(arg111: 1, arg<caret>: 2,   arg333: 3) }
}
""")
    LookupElement[] lookupElements = myFixture.completeBasic()
    assertNull(lookupElements)

    myFixture.type('!')
    assertEquals """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m(arg111: 1, arg222: !2,   arg333: 3) }
}
""", getFileText(file)
  }

  public void testCompletionNamedArgument2() {
    def file = myFixture.configureByText(GroovyFileType.GROOVY_FILE_TYPE, """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m arg111: 1, arg<caret>: 2,   arg333: 3 }
}
""")
    LookupElement[] lookupElements = myFixture.completeBasic()
    assertNull(lookupElements)

    myFixture.type('!')
    assertEquals """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m arg111: 1, arg222: !2,   arg333: 3 }
}
""", getFileText(file)
  }

  public void testCompletionNamedArgument3() {
    def file = myFixture.configureByText(GroovyFileType.GROOVY_FILE_TYPE, """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m arg1<caret> }
}
""")
    LookupElement[] lookupElements = myFixture.completeBasic()
    assertNull(lookupElements)

    myFixture.type('!')
    assertEquals """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m arg111: ! }
}
""", getFileText(file)
  }

  public void testCompletionNamedArgument4() {
    def file = myFixture.configureByText(GroovyFileType.GROOVY_FILE_TYPE, """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m (arg1<caret> zzz) }
}
""")
    LookupElement[] lookupElements = myFixture.completeBasic()
    assertNull(lookupElements)

    myFixture.type('!')
    assertEquals """
class A {
 public int m(arg) { return arg.arg111 + arg.arg222 + arg.arg333; }
 { m (arg111: !, zzz) }
}
""", getFileText(file)
  }

  public void testSpreadOperator() {
    doVariantableTest("foo1", "foo2")
  }

  def getFileText(PsiFile file) {
    return PsiDocumentManager.getInstance(project).getDocument(file).text
  }
}