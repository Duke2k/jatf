/*
  This file is part of JATF.
  <p>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class TokenVisitor extends VoidVisitorAdapter<Object> {

  private List<String> tokens = newArrayList();

  public List<String> getTokens() {
    return tokens;
  }

  public List<String> getUniqueTokens() {
    Set<String> uniqueTokens = newHashSet();
    uniqueTokens.addAll(tokens);
    return Lists.newArrayList(uniqueTokens);
  }

  public void visit(CompilationUnit n, Object arg) {
    if (n.getPackageDeclaration().isPresent()) {
      n.getPackageDeclaration().get().accept(this, arg);
    }
    if (n.getImports() != null) {
      for (ImportDeclaration i : n.getImports()) {
        i.accept(this, arg);
      }
    }
    if (n.getTypes() != null) {
      for (TypeDeclaration typeDeclaration : n.getTypes()) {
        typeDeclaration.accept(this, arg);
      }
    }
  }

  public void visit(PackageDeclaration n, Object arg) {
    printAnnotations(n.getAnnotations(), arg);
    tokens.add("package");
    n.getName().accept(this, arg);
  }

  public void visit(NameExpr n, Object arg) {
    tokens.add(n.getName().asString());
  }

  public void visit(ImportDeclaration n, Object arg) {
    tokens.add("import");
    if (n.isStatic()) {
      tokens.add("static");
    }
    n.getName().accept(this, arg);
    if (n.isAsterisk()) {
      tokens.add(".*");
    }
  }

  public void visit(ClassOrInterfaceDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());

    if (n.isInterface()) {
      tokens.add("interface");
    } else {
      tokens.add("class");
    }

    tokens.add(n.getName().asString());

    printTypeParameters(n.getTypeParameters(), arg);

    if (n.getExtendedTypes().isNonEmpty()) {
      tokens.add("extends");
      for (Iterator<ClassOrInterfaceType> i = n.getExtendedTypes().iterator(); i.hasNext(); ) {
        ClassOrInterfaceType c = i.next();
        c.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }

    if (n.getImplementedTypes().isNonEmpty()) {
      tokens.add("implements");
      for (Iterator<ClassOrInterfaceType> i = n.getImplementedTypes().iterator(); i.hasNext(); ) {
        ClassOrInterfaceType c = i.next();
        c.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }

    if (n.getMembers() != null) {
      printMembers(n.getMembers(), arg);
    }
  }

  public void visit(ClassOrInterfaceType n, Object arg) {
    if (n.getScope().isPresent()) {
      n.getScope().get().accept(this, arg);
      tokens.add(".");
    }
    tokens.add(n.getName().asString());
    if (n.getTypeArguments().isPresent()) {
      printTypeArgs(n.getTypeArguments().get(), arg);
    }
  }

  public void visit(TypeParameter n, Object arg) {
    tokens.add(n.getName().asString());
    if (n.getTypeBound() != null) {
      tokens.add("extends");
      for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i.hasNext(); ) {
        ClassOrInterfaceType c = i.next();
        c.accept(this, arg);
        if (i.hasNext()) {
          tokens.add("&");
        }
      }
    }
  }

  public void visit(PrimitiveType n, Object arg) {
    tokens.add(n.getType().name().toLowerCase());
  }

  public void visit(ReferenceType n, Object arg) {
    n.accept(this, arg);
    for (int i = 0; i < n.getArrayLevel(); i++) {
      tokens.add("[");
    }
  }

  public void visit(WildcardType n, Object arg) {
    tokens.add("?");
    if (n.getSuperType().isPresent()) {
      tokens.add("super");
      n.getSuperType().get().accept(this, arg);
    }
    if (n.getExtendedType().isPresent()) {
      tokens.add("extends");
      n.getExtendedType().get().accept(this, arg);
    }
  }

  public void visit(FieldDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());
    n.accept(this, arg);

    tokens.add(" ");
    for (Iterator<VariableDeclarator> i = n.getVariables().iterator(); i.hasNext(); ) {
      VariableDeclarator var = i.next();
      var.accept(this, arg);
      if (i.hasNext()) {
        tokens.add(",");
      }
    }
  }

  public void visit(VariableDeclarator n, Object arg) {
    n.accept(this, arg);
    if (n.getInitializer().isPresent()) {
      tokens.add("=");
      n.getInitializer().get().accept(this, arg);
    }
  }

  public void visit(ArrayInitializerExpr n, Object arg) {
    tokens.add("{");
    if (n.getValues() != null) {
      for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext(); ) {
        Expression expr = i.next();
        expr.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
  }

  public void visit(VoidType n, Object arg) {
    tokens.add("void");
  }

  public void visit(ArrayAccessExpr n, Object arg) {
    n.getName().accept(this, arg);
    tokens.add("[");
    n.getIndex().accept(this, arg);
  }

  public void visit(ArrayCreationExpr n, Object arg) {
    tokens.add("new");
    n.accept(this, arg);
    n.getLevels().forEach(level -> {
      tokens.add("[");
      level.accept(this, arg);
    });
    if (n.getInitializer().isPresent()) {
      n.getInitializer().get().accept(this, arg);
    }
  }

  public void visit(AssignExpr n, Object arg) {
    n.getTarget().accept(this, arg);
    tokens.add(" ");
    tokens.add(n.getOperator().asString());
    n.getValue().accept(this, arg);
  }

  public void visit(BinaryExpr n, Object arg) {
    n.getLeft().accept(this, arg);
    tokens.add(" ");
    tokens.add(n.getOperator().asString());
    n.getRight().accept(this, arg);
  }

  public void visit(CastExpr n, Object arg) {
    tokens.add("(");
    n.getType().accept(this, arg);
    n.getExpression().accept(this, arg);
  }

  public void visit(ClassExpr n, Object arg) {
    n.getType().accept(this, arg);
    tokens.add(".class");
  }

  public void visit(ConditionalExpr n, Object arg) {
    n.getCondition().accept(this, arg);
    tokens.add("?");
    n.getThenExpr().accept(this, arg);
    tokens.add(":");
    n.getElseExpr().accept(this, arg);
  }

  public void visit(EnclosedExpr n, Object arg) {
    tokens.add("(");
    n.getInner().accept(this, arg);
  }

  public void visit(FieldAccessExpr n, Object arg) {
    n.getScope().accept(this, arg);
    tokens.add(".");
    tokens.add(n.getNameAsString());
  }

  public void visit(InstanceOfExpr n, Object arg) {
    n.getExpression().accept(this, arg);
    tokens.add("instanceof");
    n.getType().accept(this, arg);
  }

  public void visit(CharLiteralExpr n, Object arg) {
    tokens.add("'");
    tokens.add(n.getValue());
  }

  public void visit(DoubleLiteralExpr n, Object arg) {
    tokens.add(n.getValue());
  }

  public void visit(IntegerLiteralExpr n, Object arg) {
    tokens.add(n.getValue());
  }

  public void visit(LongLiteralExpr n, Object arg) {
    tokens.add(n.getValue());
  }

  public void visit(StringLiteralExpr n, Object arg) {
    tokens.add("\"");
    tokens.add(n.getValue());
  }

  public void visit(BooleanLiteralExpr n, Object arg) {
    tokens.add(String.valueOf(n.getValue()));
  }

  public void visit(NullLiteralExpr n, Object arg) {
    tokens.add("null");
  }

  public void visit(ThisExpr n, Object arg) {
    if (n.getTypeName().isPresent()) {
      n.getTypeName().get().accept(this, arg);
      tokens.add(".");
    }
    tokens.add("this");
  }

  public void visit(SuperExpr n, Object arg) {
    if (n.getTypeName().isPresent()) {
      n.getTypeName().get().accept(this, arg);
      tokens.add(".");
    }
    tokens.add("super");
  }

  public void visit(MethodCallExpr n, Object arg) {
    if (n.getScope().isPresent()) {
      n.getScope().get().accept(this, arg);
      tokens.add(".");
    }
    if (n.getTypeArguments().isPresent()) {
      printTypeArgs(n.getTypeArguments().get(), arg);
    }
    tokens.add(n.getName().asString());
    printArguments(n.getArguments(), arg);
  }

  public void visit(ObjectCreationExpr n, Object arg) {
    if (n.getScope().isPresent()) {
      n.getScope().get().accept(this, arg);
      tokens.add(".");
    }
    tokens.add("new");
    if (n.getTypeArguments().isPresent()) {
      printTypeArgs(n.getTypeArguments().get(), arg);
    }
    n.getType().accept(this, arg);
    printArguments(n.getArguments(), arg);
    if (n.getAnonymousClassBody().isPresent()) {
      printMembers(n.getAnonymousClassBody().get(), arg);
    }
  }

  public void visit(UnaryExpr n, Object arg) {
    if (n.isPrefix()) {
      tokens.add(n.getOperator().asString());
    }
    n.getExpression().accept(this, arg);
    if (n.isPostfix()) {
      tokens.add(n.getOperator().asString());
    }
  }

  public void visit(ConstructorDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());

    printTypeParameters(n.getTypeParameters(), arg);
    if (n.getTypeParameters() != null) {
      tokens.add(" ");
    }
    tokens.add(n.getNameAsString());

    tokens.add("(");
    if (n.getParameters() != null) {
      for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext(); ) {
        Parameter p = i.next();
        p.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(", ");
        }
      }
    }
    if (n.getThrownExceptions().isNonEmpty()) {
      tokens.add("throws");
      for (Iterator<ReferenceType> i = n.getThrownExceptions().iterator(); i.hasNext(); ) {
        ReferenceType referenceType = i.next();
        referenceType.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
    n.getBody().accept(this, arg);
  }

  public void visit(MethodDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());
    printTypeParameters(n.getTypeParameters(), arg);
    n.getType().accept(this, arg);
    tokens.add(n.getName().asString());
    tokens.add("(");
    if (n.getParameters() != null) {
      for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext(); ) {
        Parameter p = i.next();
        p.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(", ");
        }
      }
    }
    if (n.getThrownExceptions().isNonEmpty()) {
      tokens.add("throws");
      for (Iterator<ReferenceType> i = n.getThrownExceptions().iterator(); i.hasNext(); ) {
        ReferenceType referenceType = i.next();
        referenceType.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
    if (n.getBody().isPresent()) {
      n.getBody().get().accept(this, arg);
    }
  }

  public void visit(Parameter n, Object arg) {
    printAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());
    n.getType().accept(this, arg);
    if (n.isVarArgs()) {
      tokens.add("...");
    }
    n.accept(this, arg);
  }

  public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
    if (n.isThis()) {
      if (n.getTypeArguments().isPresent()) {
        printTypeArgs(n.getTypeArguments().get(), arg);
      }
      tokens.add("this");
    } else {
      if (n.getExpression().isPresent()) {
        n.getExpression().get().accept(this, arg);
        tokens.add(".");
      }
      if (n.getTypeArguments().isPresent()) {
        printTypeArgs(n.getTypeArguments().get(), arg);
      }
      tokens.add("super");
    }
    printArguments(n.getArguments(), arg);
  }

  public void visit(VariableDeclarationExpr n, Object arg) {
    printAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());
    boolean typeDefProcessed = false;
    for (Iterator<VariableDeclarator> i = n.getVariables().iterator(); i.hasNext(); ) {
      VariableDeclarator v = i.next();
      if (!typeDefProcessed) {
        v.getType().accept(this, arg);
        typeDefProcessed = true;
      }
      v.accept(this, arg);
      if (i.hasNext()) {
        tokens.add(",");
      }
    }
  }

  public void visit(AssertStmt n, Object arg) {
    tokens.add("assert");
    n.getCheck().accept(this, arg);
    if (n.getMessage().isPresent()) {
      tokens.add(":");
      n.getMessage().get().accept(this, arg);
    }
  }

  public void visit(BlockStmt n, Object arg) {
    n.getStatements().forEach(s -> s.accept(this, arg));
  }

  public void visit(LabeledStmt n, Object arg) {
    tokens.add(n.getLabel().asString());
    tokens.add(":");
    n.getStatement().accept(this, arg);
  }

  public void visit(ExpressionStmt n, Object arg) {
    n.getExpression().accept(this, arg);
  }

  public void visit(SwitchStmt n, Object arg) {
    tokens.add("switch(");
    n.getSelector().accept(this, arg);
    n.getEntries().forEach(e -> e.accept(this, arg));
  }

  public void visit(ReturnStmt n, Object arg) {
    tokens.add("return");
    if (n.getExpression().isPresent()) {
      tokens.add(" ");
      n.getExpression().get().accept(this, arg);
    }
  }

  public void visit(EnumDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());

    tokens.add("enum");
    tokens.add(n.getName().asString());

    //noinspection Duplicates
    if (n.getImplementedTypes().isNonEmpty()) {
      tokens.add("implements");
      for (Iterator<ClassOrInterfaceType> i = n.getImplementedTypes().iterator(); i.hasNext(); ) {
        ClassOrInterfaceType c = i.next();
        c.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
    if (n.getEntries() != null) {
      for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext(); ) {
        EnumConstantDeclaration e = i.next();
        e.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
    if (n.getMembers() != null) {
      printMembers(n.getMembers(), arg);
    }
  }

  public void visit(EnumConstantDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    tokens.add(n.getName().asString());
    if (n.getArguments() != null) {
      printArguments(n.getArguments(), arg);
    }
    if (n.getClassBody() != null) {
      printMembers(n.getClassBody(), arg);
    }
  }

  public void visit(InitializerDeclaration n, Object arg) {
    if (n.isStatic()) {
      tokens.add("static");
    }
    n.getBody().accept(this, arg);
  }

  public void visit(IfStmt n, Object arg) {
    tokens.add("if (");
    n.getCondition().accept(this, arg);
    n.getThenStmt().accept(this, arg);
    if (n.getElseStmt().isPresent()) {
      tokens.add("else");
      n.getElseStmt().get().accept(this, arg);
    }
  }

  public void visit(WhileStmt n, Object arg) {
    tokens.add("while (");
    n.getCondition().accept(this, arg);
    n.getBody().accept(this, arg);
  }

  public void visit(ContinueStmt n, Object arg) {
    tokens.add("continue");
    if (n.getLabel().isPresent()) {
      tokens.add(" ");
      tokens.add(n.getLabel().get().getIdentifier());
    }
  }

  public void visit(DoStmt n, Object arg) {
    tokens.add("do");
    n.getBody().accept(this, arg);
    tokens.add(" while (");
    n.getCondition().accept(this, arg);
  }

  public void visit(ForStmt n, Object arg) {
    tokens.add("for (");
    if (n.getInitialization().isNonEmpty()) {
      for (Iterator<Expression> i = n.getInitialization().iterator(); i.hasNext(); ) {
        Expression e = i.next();
        e.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
    if (n.getCompare().isPresent()) {
      n.getCompare().get().accept(this, arg);
    }
    if (n.getUpdate() != null) {
      for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext(); ) {
        Expression e = i.next();
        e.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
    n.getBody().accept(this, arg);
  }

  public void visit(ThrowStmt n, Object arg) {
    tokens.add("throw");
    n.getExpression().accept(this, arg);
  }

  public void visit(SynchronizedStmt n, Object arg) {
    tokens.add("synchronized (");
    n.getExpression().accept(this, arg);
    n.getBody().accept(this, arg);
  }

  public void visit(TryStmt n, Object arg) {
    tokens.add("try");
    n.getTryBlock().accept(this, arg);
    if (n.getCatchClauses() != null) {
      for (CatchClause c : n.getCatchClauses()) {
        c.accept(this, arg);
      }
    }
    if (n.getFinallyBlock().isPresent()) {
      tokens.add("finally");
      n.getFinallyBlock().get().accept(this, arg);
    }
  }

  public void visit(CatchClause n, Object arg) {
    tokens.add(" catch (");
    n.getParameter().accept(this, arg);
    n.getBody().accept(this, arg);

  }

  public void visit(AnnotationDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());
    tokens.add("@interface");
    tokens.add(n.getName().asString());
    if (n.getMembers() != null) {
      printMembers(n.getMembers(), arg);
    }
  }

  public void visit(AnnotationMemberDeclaration n, Object arg) {
    printMemberAnnotations(n.getAnnotations(), arg);
    printModifiers(n.getModifiers());

    n.getType().accept(this, arg);
    tokens.add(n.getName().asString());
    tokens.add("(");
    if (n.getDefaultValue().isPresent()) {
      tokens.add("default");
      n.getDefaultValue().get().accept(this, arg);
    }
  }

  public void visit(MarkerAnnotationExpr n, Object arg) {
    tokens.add("@");
    n.getName().accept(this, arg);
  }

  public void visit(SingleMemberAnnotationExpr n, Object arg) {
    tokens.add("@");
    n.getName().accept(this, arg);
    tokens.add("(");
    n.getMemberValue().accept(this, arg);
  }

  public void visit(NormalAnnotationExpr n, Object arg) {
    tokens.add("@");
    n.getName().accept(this, arg);
    tokens.add("(");
    if (n.getPairs() != null) {
      for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i.hasNext(); ) {
        MemberValuePair m = i.next();
        m.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
  }

  public void visit(MemberValuePair n, Object arg) {
    tokens.add(n.getName().asString());
    tokens.add("=");
    n.getValue().accept(this, arg);
  }

  private void printMembers(NodeList<BodyDeclaration<?>> members, Object arg) {
    for (BodyDeclaration member : members) {
      member.accept(this, arg);
    }
  }

  private void printMemberAnnotations(NodeList<AnnotationExpr> annotations, Object arg) {
    if (annotations != null) {
      for (AnnotationExpr a : annotations) {
        a.accept(this, arg);
      }
    }
  }

  private void printAnnotations(NodeList<AnnotationExpr> annotations, Object arg) {
    if (annotations != null) {
      for (AnnotationExpr a : annotations) {
        a.accept(this, arg);
      }
    }
  }

  private void printTypeArgs(NodeList<Type> args, Object arg) {
    if (args != null) {
      tokens.add("<");
      for (Iterator<Type> i = args.iterator(); i.hasNext(); ) {
        Type t = i.next();
        t.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
  }

  private void printTypeParameters(NodeList<TypeParameter> args, Object arg) {
    if (args != null) {
      tokens.add("<");
      for (Iterator<TypeParameter> i = args.iterator(); i.hasNext(); ) {
        TypeParameter t = i.next();
        t.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
  }

  private void printArguments(NodeList<Expression> args, Object arg) {
    tokens.add("(");
    if (args != null) {
      for (Iterator<Expression> i = args.iterator(); i.hasNext(); ) {
        Expression e = i.next();
        e.accept(this, arg);
        if (i.hasNext()) {
          tokens.add(",");
        }
      }
    }
  }

  private void printModifiers(NodeList<Modifier> modifiers) {
    modifiers.forEach(this::accept);
  }

  private void accept(Modifier m) {
    tokens.add(m.toString().toLowerCase());
  }
}
