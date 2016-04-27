/**
 * This file is part of JATF.
 *
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.parser;

import com.google.common.collect.Lists;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

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
        if (n.getPackage() != null) {
            n.getPackage().accept(this, arg);
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
        tokens.add(n.getName());
    }

    public void visit(QualifiedNameExpr n, Object arg) {
        n.getQualifier().accept(this, arg);
        tokens.add(".");
        tokens.add(n.getName());
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

        tokens.add(n.getName());

        printTypeParameters(n.getTypeParameters(), arg);

        if (n.getExtends() != null) {
            tokens.add("extends");
            for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i.hasNext(); ) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    tokens.add(",");
                }
            }
        }

        if (n.getImplements() != null) {
            tokens.add("implements");
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext(); ) {
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
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
            tokens.add(".");
        }
        tokens.add(n.getName());
        printTypeArgs(n.getTypeArgs(), arg);
    }

    public void visit(TypeParameter n, Object arg) {
        tokens.add(n.getName());
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
        switch (n.getType()) {
            case Boolean:
                tokens.add("boolean");
                break;
            case Byte:
                tokens.add("byte");
                break;
            case Char:
                tokens.add("char");
                break;
            case Double:
                tokens.add("double");
                break;
            case Float:
                tokens.add("float");
                break;
            case Int:
                tokens.add("int");
                break;
            case Long:
                tokens.add("long");
                break;
            case Short:
                tokens.add("short");
                break;
        }
    }

    public void visit(ReferenceType n, Object arg) {
        n.getType().accept(this, arg);
        for (int i = 0; i < n.getArrayCount(); i++) {
            tokens.add("[");
        }
    }

    public void visit(WildcardType n, Object arg) {
        tokens.add("?");
        if (n.getExtends() != null) {
            tokens.add("extends");
            n.getExtends().accept(this, arg);
        }
        if (n.getSuper() != null) {
            tokens.add("super");
            n.getSuper().accept(this, arg);
        }
    }

    public void visit(FieldDeclaration n, Object arg) {
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());
        n.getType().accept(this, arg);

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
        n.getId().accept(this, arg);
        if (n.getInit() != null) {
            tokens.add("=");
            n.getInit().accept(this, arg);
        }
    }

    public void visit(VariableDeclaratorId n, Object arg) {
        tokens.add(n.getName());
        for (int i = 0; i < n.getArrayCount(); i++) {
            tokens.add("[");
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
        n.getType().accept(this, arg);

        if (n.getDimensions() != null) {
            for (Expression dim : n.getDimensions()) {
                tokens.add("[");
                dim.accept(this, arg);
            }
            for (int i = 0; i < n.getArrayCount(); i++) {
                tokens.add("[");
            }
        } else {
            for (int i = 0; i < n.getArrayCount(); i++) {
                tokens.add("[");
            }
            n.getInitializer().accept(this, arg);
        }
    }

    public void visit(AssignExpr n, Object arg) {
        n.getTarget().accept(this, arg);
        tokens.add(" ");
        switch (n.getOperator()) {
            case assign:
                tokens.add("=");
                break;
            case and:
                tokens.add("&=");
                break;
            case or:
                tokens.add("|=");
                break;
            case xor:
                tokens.add("^=");
                break;
            case plus:
                tokens.add("+=");
                break;
            case minus:
                tokens.add("-=");
                break;
            case rem:
                tokens.add("%=");
                break;
            case slash:
                tokens.add("/=");
                break;
            case star:
                tokens.add("*=");
                break;
            case lShift:
                tokens.add("<<=");
                break;
            case rSignedShift:
                tokens.add(">>=");
                break;
            case rUnsignedShift:
                tokens.add(">>>=");
                break;
        }
        n.getValue().accept(this, arg);
    }

    public void visit(BinaryExpr n, Object arg) {
        n.getLeft().accept(this, arg);
        tokens.add(" ");
        switch (n.getOperator()) {
            case or:
                tokens.add("||");
                break;
            case and:
                tokens.add("&&");
                break;
            case binOr:
                tokens.add("|");
                break;
            case binAnd:
                tokens.add("&");
                break;
            case xor:
                tokens.add("^");
                break;
            case equals:
                tokens.add("==");
                break;
            case notEquals:
                tokens.add("!=");
                break;
            case less:
                tokens.add("<");
                break;
            case greater:
                tokens.add(">");
                break;
            case lessEquals:
                tokens.add("<=");
                break;
            case greaterEquals:
                tokens.add(">=");
                break;
            case lShift:
                tokens.add("<<");
                break;
            case rSignedShift:
                tokens.add(">>");
                break;
            case rUnsignedShift:
                tokens.add(">>>");
                break;
            case plus:
                tokens.add("+");
                break;
            case minus:
                tokens.add("-");
                break;
            case times:
                tokens.add("*");
                break;
            case divide:
                tokens.add("/");
                break;
            case remainder:
                tokens.add("%");
                break;
        }
        n.getRight().accept(this, arg);
    }

    public void visit(CastExpr n, Object arg) {
        tokens.add("(");
        n.getType().accept(this, arg);
        n.getExpr().accept(this, arg);
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
        tokens.add(n.getField());
    }

    public void visit(InstanceOfExpr n, Object arg) {
        n.getExpr().accept(this, arg);
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

    public void visit(IntegerLiteralMinValueExpr n, Object arg) {
        tokens.add(n.getValue());
    }

    public void visit(LongLiteralMinValueExpr n, Object arg) {
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
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
            tokens.add(".");
        }
        tokens.add("this");
    }

    public void visit(SuperExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
            tokens.add(".");
        }
        tokens.add("super");
    }

    public void visit(MethodCallExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
            tokens.add(".");
        }
        printTypeArgs(n.getTypeArgs(), arg);
        tokens.add(n.getName());
        printArguments(n.getArgs(), arg);
    }

    public void visit(ObjectCreationExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
            tokens.add(".");
        }
        tokens.add("new");
        printTypeArgs(n.getTypeArgs(), arg);
        n.getType().accept(this, arg);
        printArguments(n.getArgs(), arg);
        if (n.getAnonymousClassBody() != null) {
            printMembers(n.getAnonymousClassBody(), arg);
        }
    }

    public void visit(UnaryExpr n, Object arg) {
        switch (n.getOperator()) {
            case positive:
                tokens.add("+");
                break;
            case negative:
                tokens.add("-");
                break;
            case inverse:
                tokens.add("~");
                break;
            case not:
                tokens.add("!");
                break;
            case preIncrement:
                tokens.add("++");
                break;
            case preDecrement:
                tokens.add("--");
                break;
        }

        n.getExpr().accept(this, arg);

        switch (n.getOperator()) {
            case posIncrement:
                tokens.add("++");
                break;
            case posDecrement:
                tokens.add("--");
                break;
        }
    }

    public void visit(ConstructorDeclaration n, Object arg) {
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        printTypeParameters(n.getTypeParameters(), arg);
        if (n.getTypeParameters() != null) {
            tokens.add(" ");
        }
        tokens.add(n.getName());

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
        if (n.getThrows() != null) {
            tokens.add("throws");
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext(); ) {
                NameExpr name = i.next();
                name.accept(this, arg);
                if (i.hasNext()) {
                    tokens.add(",");
                }
            }
        }
        n.getBlock().accept(this, arg);
    }

    public void visit(MethodDeclaration n, Object arg) {
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());
        printTypeParameters(n.getTypeParameters(), arg);
        n.getType().accept(this, arg);
        tokens.add(n.getName());
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
        for (int i = 0; i < n.getArrayCount(); i++) {
            tokens.add("[");
        }
        if (n.getThrows() != null) {
            tokens.add("throws");
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext(); ) {
                NameExpr name = i.next();
                name.accept(this, arg);
                if (i.hasNext()) {
                    tokens.add(",");
                }
            }
        }
        if (n.getBody() != null) {
            n.getBody().accept(this, arg);
        }
    }

    public void visit(Parameter n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        n.getType().accept(this, arg);
        if (n.isVarArgs()) {
            tokens.add("...");
        }
        n.getId().accept(this, arg);
    }

    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
        if (n.isThis()) {
            printTypeArgs(n.getTypeArgs(), arg);
            tokens.add("this");
        } else {
            if (n.getExpr() != null) {
                n.getExpr().accept(this, arg);
                tokens.add(".");
            }
            printTypeArgs(n.getTypeArgs(), arg);
            tokens.add("super");
        }
        printArguments(n.getArgs(), arg);
    }

    public void visit(VariableDeclarationExpr n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());
        n.getType().accept(this, arg);
        for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext(); ) {
            VariableDeclarator v = i.next();
            v.accept(this, arg);
            if (i.hasNext()) {
                tokens.add(",");
            }
        }
    }

    public void visit(TypeDeclarationStmt n, Object arg) {
        n.getTypeDeclaration().accept(this, arg);
    }

    public void visit(AssertStmt n, Object arg) {
        tokens.add("assert");
        n.getCheck().accept(this, arg);
        if (n.getMessage() != null) {
            tokens.add(":");
            n.getMessage().accept(this, arg);
        }
    }

    public void visit(BlockStmt n, Object arg) {
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
    }

    public void visit(LabeledStmt n, Object arg) {
        tokens.add(n.getLabel());
        tokens.add(":");
        n.getStmt().accept(this, arg);
    }

    public void visit(ExpressionStmt n, Object arg) {
        n.getExpression().accept(this, arg);
    }

    public void visit(SwitchStmt n, Object arg) {
        tokens.add("switch(");
        n.getSelector().accept(this, arg);
        if (n.getEntries() != null) {
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
    }

    public void visit(SwitchEntryStmt n, Object arg) {
        if (n.getLabel() != null) {
            tokens.add("case");
            n.getLabel().accept(this, arg);
            tokens.add(":");
        } else {
            tokens.add("default:");
        }
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
    }

    public void visit(BreakStmt n, Object arg) {
        tokens.add("break");
        if (n.getId() != null) {
            tokens.add(" ");
            tokens.add(n.getId());
        }
    }

    public void visit(ReturnStmt n, Object arg) {
        tokens.add("return");
        if (n.getExpr() != null) {
            tokens.add(" ");
            n.getExpr().accept(this, arg);
        }
    }

    public void visit(EnumDeclaration n, Object arg) {
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        tokens.add("enum");
        tokens.add(n.getName());

        //noinspection Duplicates
        if (n.getImplements() != null) {
            tokens.add("implements");
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext(); ) {
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
        tokens.add(n.getName());
        if (n.getArgs() != null) {
            printArguments(n.getArgs(), arg);
        }
        if (n.getClassBody() != null) {
            printMembers(n.getClassBody(), arg);
        }
    }

    public void visit(InitializerDeclaration n, Object arg) {
        if (n.isStatic()) {
            tokens.add("static");
        }
        n.getBlock().accept(this, arg);
    }

    public void visit(IfStmt n, Object arg) {
        tokens.add("if (");
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt() != null) {
            tokens.add("else");
            n.getElseStmt().accept(this, arg);
        }
    }

    public void visit(WhileStmt n, Object arg) {
        tokens.add("while (");
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    public void visit(ContinueStmt n, Object arg) {
        tokens.add("continue");
        if (n.getId() != null) {
            tokens.add(" ");
            tokens.add(n.getId());
        }
    }

    public void visit(DoStmt n, Object arg) {
        tokens.add("do");
        n.getBody().accept(this, arg);
        tokens.add(" while (");
        n.getCondition().accept(this, arg);
    }

    public void visit(ForeachStmt n, Object arg) {
        tokens.add("for (");
        n.getVariable().accept(this, arg);
        tokens.add(" : ");
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    public void visit(ForStmt n, Object arg) {
        tokens.add("for (");
        if (n.getInit() != null) {
            for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext(); ) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    tokens.add(",");
                }
            }
        }
        if (n.getCompare() != null) {
            n.getCompare().accept(this, arg);
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
        n.getExpr().accept(this, arg);
    }

    public void visit(SynchronizedStmt n, Object arg) {
        tokens.add("synchronized (");
        n.getExpr().accept(this, arg);
        n.getBlock().accept(this, arg);
    }

    public void visit(TryStmt n, Object arg) {
        tokens.add("try");
        n.getTryBlock().accept(this, arg);
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, arg);
            }
        }
        if (n.getFinallyBlock() != null) {
            tokens.add("finally");
            n.getFinallyBlock().accept(this, arg);
        }
    }

    public void visit(CatchClause n, Object arg) {
        tokens.add(" catch (");
        n.getExcept().accept(this, arg);
        n.getCatchBlock().accept(this, arg);

    }

    public void visit(AnnotationDeclaration n, Object arg) {
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());
        tokens.add("@interface");
        tokens.add(n.getName());
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
    }

    public void visit(AnnotationMemberDeclaration n, Object arg) {
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        n.getType().accept(this, arg);
        tokens.add(n.getName());
        tokens.add("(");
        if (n.getDefaultValue() != null) {
            tokens.add("default");
            n.getDefaultValue().accept(this, arg);
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
        tokens.add(n.getName());
        tokens.add("=");
        n.getValue().accept(this, arg);
    }

    private void printMembers(List<BodyDeclaration> members, Object arg) {
        for (BodyDeclaration member : members) {
            member.accept(this, arg);
        }
    }

    private void printMemberAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
            }
        }
    }

    private void printAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
            }
        }
    }

    private void printTypeArgs(List<Type> args, Object arg) {
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

    private void printTypeParameters(List<TypeParameter> args, Object arg) {
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

    private void printArguments(List<Expression> args, Object arg) {
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

    private void printModifiers(int modifiers) {
        if (ModifierSet.isPrivate(modifiers)) {
            tokens.add("private");
        }
        if (ModifierSet.isProtected(modifiers)) {
            tokens.add("protected");
        }
        if (ModifierSet.isPublic(modifiers)) {
            tokens.add("public");
        }
        if (ModifierSet.isAbstract(modifiers)) {
            tokens.add("abstract");
        }
        if (ModifierSet.isStatic(modifiers)) {
            tokens.add("static");
        }
        if (ModifierSet.isFinal(modifiers)) {
            tokens.add("final");
        }
        if (ModifierSet.isNative(modifiers)) {
            tokens.add("native");
        }
        if (ModifierSet.isStrictfp(modifiers)) {
            tokens.add("strictfp");
        }
        if (ModifierSet.isSynchronized(modifiers)) {
            tokens.add("synchronized");
        }
        if (ModifierSet.isTransient(modifiers)) {
            tokens.add("transient");
        }
        if (ModifierSet.isVolatile(modifiers)) {
            tokens.add("volatile");
        }
    }
}
