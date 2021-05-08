package  refactoringminer.python.typeinference.core;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.core.dom.util.DOMASTUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PyASTMatcher extends ASTMatcher {
    private static Logger log = Logger.getLogger(PyASTMatcher.class);
    public boolean DEBUG=true;
    public boolean match(AnnotationTypeDeclaration node, Object other) {
        boolean match;

        if (other instanceof AnnotationTypeDeclaration){
            ((AnnotationTypeDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((AnnotationTypeDeclaration) other).setPyLength(node.getPyLength());
            ((AnnotationTypeDeclaration) other).setPyLine(node.getPyLine());
            ((AnnotationTypeDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }

        if (!(other instanceof AnnotationTypeDeclaration)) {
            match = false;
        } else {
            AnnotationTypeDeclaration o = (AnnotationTypeDeclaration)other;
            match = this.safeSubtreeMatch(node.getJavadoc(), o.getJavadoc()) & this.safePythonSubtreeListMatch(node.modifiers(), o.modifiers()) & this.safeSubtreeMatch(node.getName(), o.getName()) & this.safeSubtreeListMatch(node.bodyDeclarations(), o.bodyDeclarations());
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (DEBUG ) log.info("Unmatched "+node.getClass());

        return match;
    }

    public boolean match(AnnotationTypeMemberDeclaration node, Object other) {
        boolean match;

        if (other instanceof AnnotationTypeMemberDeclaration){
            ((AnnotationTypeMemberDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((AnnotationTypeMemberDeclaration) other).setPyLength(node.getPyLength());
            ((AnnotationTypeMemberDeclaration) other).setPyLine(node.getPyLine());
            ((AnnotationTypeMemberDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }

        if (!(other instanceof AnnotationTypeMemberDeclaration)) {
            match = false;
        } else {
            AnnotationTypeMemberDeclaration o = (AnnotationTypeMemberDeclaration)other;
            match = this.safeSubtreeMatch(node.getJavadoc(), o.getJavadoc()) & this.safePythonSubtreeListMatch(node.modifiers(), o.modifiers()) & this.safeSubtreeMatch(node.getType(), o.getType()) & this.safeSubtreeMatch(node.getName(), o.getName()) & this.safeSubtreeMatch(node.getDefault(), o.getDefault());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());

        return match;
    }

    public boolean match(AnonymousClassDeclaration node, Object other) {
        boolean match;
        if (other instanceof AnonymousClassDeclaration){
            ((AnonymousClassDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((AnonymousClassDeclaration) other).setPyLength(node.getPyLength());
            ((AnonymousClassDeclaration) other).setPyLine(node.getPyLine());
            ((AnonymousClassDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        if (!(other instanceof AnonymousClassDeclaration)) {
            match = false;
        } else {
            AnonymousClassDeclaration o = (AnonymousClassDeclaration)other;
            match = this.safePythonSubtreeListMatch(node.bodyDeclarations(), o.bodyDeclarations());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());

        return match;
    }

    public boolean match(ArrayAccess node, Object other) {
        boolean match;
        if ( other instanceof ArrayAccess){
            ((ArrayAccess) other).setPyStartPosition(node.getPyStartPosition());
            ((ArrayAccess) other).setPyLength(node.getPyLength());
            ((ArrayAccess) other).setPyLine(node.getPyLine());
            ((ArrayAccess) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        if (!(other instanceof ArrayAccess)) {
            match = false;
        } else {
            ArrayAccess o = (ArrayAccess)other;
            match = this.safeSubtreeMatch(node.getArray(), o.getArray()) & this.safeSubtreeMatch(node.getIndex(), o.getIndex());
        }
        if (DEBUG & !match ) log.info("Unmatched "+node.getClass());

        return match;
    }

    public boolean match(ArrayCreation node, Object other) {
        boolean match;
        if (!(other instanceof ArrayCreation)) {
            match = false;
        } else {
            ArrayCreation o = (ArrayCreation)other;
            match = this.safeSubtreeMatch(node.getType(), o.getType()) & this.safePythonSubtreeListMatch(node.dimensions(), o.dimensions()) & this.safeSubtreeMatch(node.getInitializer(), o.getInitializer());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ArrayCreation){
            ((ArrayCreation) other).setPyStartPosition(node.getPyStartPosition());
            ((ArrayCreation) other).setPyLength(node.getPyLength());
            ((ArrayCreation) other).setPyLine(node.getPyLine());
            ((ArrayCreation) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ArrayInitializer node, Object other) {
        boolean match;
        if (!(other instanceof ArrayInitializer)) {
            match = false;
        } else {
            ArrayInitializer o = (ArrayInitializer)other;
            match = this.safePythonSubtreeListMatch(node.expressions(), o.expressions());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ArrayInitializer){
            ((ArrayInitializer) other).setPyStartPosition(node.getPyStartPosition());
            ((ArrayInitializer) other).setPyLength(node.getPyLength());
            ((ArrayInitializer) other).setPyLine(node.getPyLine());
            ((ArrayInitializer) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ArrayType node, Object other) {
        boolean match ;
        if (!(other instanceof ArrayType)) {
            return false;
        } else {
            ArrayType o = (ArrayType)other;
            match =     this.safeSubtreeMatch(node.getElementType(), o.getElementType());

        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());

        ((ArrayType) other).setPyStartPosition(node.getPyStartPosition());
        ((ArrayType) other).setPyLength(node.getPyLength());
        ((ArrayType) other).setPyLine(node.getPyLine());
        ((ArrayType) other).setPyColumnOffSet(node.getPyColumnOffSet());


        return match;
    }

    public boolean match(AssertStatement node, Object other) {
        boolean match;
        if (!(other instanceof AssertStatement)) {
            match= false;
        } else {
            AssertStatement o = (AssertStatement)other;
            match = this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getMessage(), o.getMessage());
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof AssertStatement){
            ((AssertStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((AssertStatement) other).setPyLength(node.getPyLength());
            ((AssertStatement) other).setPyLine(node.getPyLine());
            ((AssertStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Assignment node, Object other) {
        boolean match;

        if (!(other instanceof Assignment)) {
            match= false;
        } else {
            Assignment o = (Assignment)other;
            match= node.getOperator().toString().equals(o.getOperator().toString() ) & this.safeSubtreeMatch(node.getLeftHandSide(), o.getLeftHandSide()) & this.safeSubtreeMatch(node.getRightHandSide(), o.getRightHandSide());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof Assignment){
            ((Assignment) other).setPyStartPosition(node.getPyStartPosition());
            ((Assignment) other).setPyLength(node.getPyLength());
            ((Assignment) other).setPyLine(node.getPyLine());
            ((Assignment) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean safePythonSubtreeListMatch(List list1, List list2) {
        boolean match=true;
        int size1 = list1.size();
        int size2 = list2.size();
        if (size1 != size2) {
            return false;
        } else {
            Iterator it1 = list1.iterator();
            Iterator it2 = list2.iterator();

            while(it1.hasNext()) {
                ASTNode n1 = (ASTNode)it1.next();
                ASTNode n2 = (ASTNode)it2.next();
                if (!n1.subtreeMatch(this, n2)) {
                    match = false;
                }
            }

            return match;
        }
    }

    public boolean match(Block node, Object other) {
        boolean match;

        if (!(other instanceof Block)) {
            match = false;
        } else {
            Block o = (Block)other;
            match = this.safePythonSubtreeListMatch(node.statements(), o.statements());
        }

        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if ( other instanceof Block){
            ((Block) other).setPyStartPosition(node.getPyStartPosition());
            ((Block) other).setPyLength(node.getPyLength());
            ((Block) other).setPyLine(node.getPyLine());
            ((Block) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(BlockComment node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());

        if ( other instanceof BlockComment){
            ((BlockComment) other).setPyStartPosition(node.getPyStartPosition());
            ((BlockComment) other).setPyLength(node.getPyLength());
            ((BlockComment) other).setPyLine(node.getPyLine());
            ((BlockComment) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(BooleanLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (other instanceof BooleanLiteral){
            ((BooleanLiteral) other).setPyStartPosition(node.getPyStartPosition());
            ((BooleanLiteral) other).setPyLength(node.getPyLength());
            ((BooleanLiteral) other).setPyLine(node.getPyLine());
            ((BooleanLiteral) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(BreakStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof BreakStatement){
            ((BreakStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((BreakStatement) other).setPyLength(node.getPyLength());
            ((BreakStatement) other).setPyLine(node.getPyLine());
            ((BreakStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CastExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof CastExpression){
            ((CastExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((CastExpression) other).setPyLength(node.getPyLength());
            ((CastExpression) other).setPyLine(node.getPyLine());
            ((CastExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CatchClause node, Object other) {
        boolean match;

        if (!(other instanceof CatchClause)) {
            match = false;
        } else {
            CatchClause o = (CatchClause)other;
            match = this.safeSubtreeMatch(node.getException(), o.getException()) & this.safeSubtreeMatch(node.getBody(), o.getBody());
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof CatchClause){
            ((CatchClause) other).setPyStartPosition(node.getPyStartPosition());
            ((CatchClause) other).setPyLength(node.getPyLength());
            ((CatchClause) other).setPyLine(node.getPyLine());
            ((CatchClause) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CharacterLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof CharacterLiteral){
            ((CharacterLiteral) other).setPyStartPosition(node.getPyStartPosition());
            ((CharacterLiteral) other).setPyLength(node.getPyLength());
            ((CharacterLiteral) other).setPyLine(node.getPyLine());
            ((CharacterLiteral) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ClassInstanceCreation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ClassInstanceCreation){
            ((ClassInstanceCreation) other).setPyStartPosition(node.getPyStartPosition());
            ((ClassInstanceCreation) other).setPyLength(node.getPyLength());
            ((ClassInstanceCreation) other).setPyLine(node.getPyLine());
            ((ClassInstanceCreation) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CompilationUnit node, Object other) {
        boolean match;
        if (!(other instanceof CompilationUnit)) {
            match= false;
        } else {
            CompilationUnit o = (CompilationUnit)other;
            match= this.safeSubtreeMatch(node.getPackage(), o.getPackage()) && this.safePythonSubtreeListMatch(node.imports(), o.imports()) && this.safePythonSubtreeListMatch(node.types(), o.types());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof CompilationUnit){
            ((CompilationUnit) other).setPyStartPosition(node.getPyStartPosition());
            ((CompilationUnit) other).setPyLength(node.getPyLength());
            ((CompilationUnit) other).setPyLine(node.getPyLine());
            ((CompilationUnit) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ConditionalExpression node, Object other) {
        boolean match;
        if (!(other instanceof ConditionalExpression)) {
            match = false;
        } else {
            ConditionalExpression o = (ConditionalExpression)other;
            match = this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getThenExpression(), o.getThenExpression()) & this.safeSubtreeMatch(node.getElseExpression(), o.getElseExpression());
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ConditionalExpression){
            ((ConditionalExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((ConditionalExpression) other).setPyLength(node.getPyLength());
            ((ConditionalExpression) other).setPyLine(node.getPyLine());
            ((ConditionalExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ConstructorInvocation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ConstructorInvocation){
            ((ConstructorInvocation) other).setPyStartPosition(node.getPyStartPosition());
            ((ConstructorInvocation) other).setPyLength(node.getPyLength());
            ((ConstructorInvocation) other).setPyLine(node.getPyLine());
            ((ConstructorInvocation) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ContinueStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ContinueStatement){
            ((ContinueStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((ContinueStatement) other).setPyLength(node.getPyLength());
            ((ContinueStatement) other).setPyLine(node.getPyLine());
            ((ContinueStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(CreationReference node, Object other) {
        boolean match;
        if (!(other instanceof CreationReference)) {
            match = false;
        } else {
            CreationReference o = (CreationReference)other;
            match = this.safeSubtreeMatch(node.getType(), o.getType()) & this.safePythonSubtreeListMatch(node.typeArguments(), o.typeArguments());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof CreationReference){
            ((CreationReference) other).setPyStartPosition(node.getPyStartPosition());
            ((CreationReference) other).setPyLength(node.getPyLength());
            ((CreationReference) other).setPyLine(node.getPyLine());
            ((CreationReference) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Dimension node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof Dimension){
            ((Dimension) other).setPyStartPosition(node.getPyStartPosition());
            ((Dimension) other).setPyLength(node.getPyLength());
            ((Dimension) other).setPyLine(node.getPyLine());
            ((Dimension) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(DoStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof DoStatement){
            ((DoStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((DoStatement) other).setPyLength(node.getPyLength());
            ((DoStatement) other).setPyLine(node.getPyLine());
            ((DoStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EmptyStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof EmptyStatement){
            ((EmptyStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((EmptyStatement) other).setPyLength(node.getPyLength());
            ((EmptyStatement) other).setPyLine(node.getPyLine());
            ((EmptyStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EnhancedForStatement node, Object other) {
        boolean match;
        if (!(other instanceof EnhancedForStatement || other instanceof EnhancedForStatementWithElse)) {
            match = false;
        }
        else if (other instanceof EnhancedForStatementWithElse){
            EnhancedForStatementWithElse o = (EnhancedForStatementWithElse)other;
            List nodeM = new ArrayList();
            List nodeN = new ArrayList();
            if (!node.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeM.add(node.getParameter());
            }
            if (!o.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeN.add(o.getParameter());
            }

            for (Object parameter : node.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeM.add(para);
                }

            }
            for (Object parameter : o.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeN.add(para);
                }
            }
            match= this.safePythonSubtreeListMatch(nodeM, nodeN) & this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getBody(), o.getBody()) & this.safeSubtreeMatch(node.getElseBody(), o.getElseBody());

        }
        else {
            EnhancedForStatement o = (EnhancedForStatement)other;
            List nodeM = new ArrayList();
            List nodeN = new ArrayList();
            if (!node.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeM.add(node.getParameter());
            }
            if (!o.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeN.add(o.getParameter());
            }

            for (Object parameter : node.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeM.add(para);
                }

            }
            for (Object parameter : o.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeN.add(para);
                }
            }
            match= this.safePythonSubtreeListMatch(nodeM, nodeN) & this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getBody(), o.getBody());
        }



        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof EnhancedForStatement){
            ((EnhancedForStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((EnhancedForStatement) other).setPyLength(node.getPyLength());
            ((EnhancedForStatement) other).setPyLine(node.getPyLine());
            ((EnhancedForStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        else if(other instanceof EnhancedForStatementWithElse){
            ((EnhancedForStatementWithElse) other).setPyStartPosition(node.getPyStartPosition());
            ((EnhancedForStatementWithElse) other).setPyLength(node.getPyLength());
            ((EnhancedForStatementWithElse) other).setPyLine(node.getPyLine());
            ((EnhancedForStatementWithElse) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EnhancedForStatementWithElse node, Object other) {
        boolean match;
        if (other instanceof EnhancedForStatement){
            EnhancedForStatement o = (EnhancedForStatement)other;
            List nodeM = new ArrayList();
            List nodeN = new ArrayList();
            if (!node.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeM.add(node.getParameter());
            }
            if (!o.getParameter().getName().getIdentifier().equals("DummyTerminalNode")){
                nodeN.add(node.getParameter());
            }
            for (Object parameter : node.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeM.add(para);
                }
            }
            for (Object parameter : o.Parameters()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeN.add(para);
                }
            }

            match =  this.safePythonSubtreeListMatch(nodeM, nodeN) & this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getBody(), o.getBody()) & this.safeSubtreeMatch(node.getElseBody(), o.getElseBody());

        }
        else if (!(other instanceof EnhancedForStatementWithElse)) {
            match = false;
        } else {
            EnhancedForStatementWithElse o = (EnhancedForStatementWithElse)other;
            match =  this.safeSubtreeMatch(node.getParameter(), o.getParameter()) & this.safePythonSubtreeListMatch(node.Parameters(), o.Parameters()) & this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getBody(), o.getBody()) & this.safeSubtreeMatch(node.getElseBody(), o.getElseBody());
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());

        if (other instanceof EnhancedForStatement){
                ((EnhancedForStatement) other).setPyStartPosition(node.getPyStartPosition());
                ((EnhancedForStatement) other).setPyLength(node.getPyLength());
                ((EnhancedForStatement) other).setPyLine(node.getPyLine());
                ((EnhancedForStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        else if (other instanceof EnhancedForStatementWithElse){
                ((EnhancedForStatementWithElse) other).setPyStartPosition(node.getPyStartPosition());
                ((EnhancedForStatementWithElse) other).setPyLength(node.getPyLength());
                ((EnhancedForStatementWithElse) other).setPyLine(node.getPyLine());
                ((EnhancedForStatementWithElse) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(EnumConstantDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof EnumConstantDeclaration){
            ((EnumConstantDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((EnumConstantDeclaration) other).setPyLength(node.getPyLength());
            ((EnumConstantDeclaration) other).setPyLine(node.getPyLine());
            ((EnumConstantDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(EnumDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof EnumDeclaration){
            ((EnumDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((EnumDeclaration) other).setPyLength(node.getPyLength());
            ((EnumDeclaration) other).setPyLine(node.getPyLine());
            ((EnumDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ExportsDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ExportsDirective){
            ((ExportsDirective) other).setPyStartPosition(node.getPyStartPosition());
            ((ExportsDirective) other).setPyLength(node.getPyLength());
            ((ExportsDirective) other).setPyLine(node.getPyLine());
            ((ExportsDirective) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ExpressionMethodReference node, Object other) {
        boolean match ;
        if (!(other instanceof ExpressionMethodReference)) {
            match= false;
        } else {
            ExpressionMethodReference o = (ExpressionMethodReference)other;
            match= this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeListMatch(node.typeArguments(), o.typeArguments()) & this.safeSubtreeMatch(node.getName(), o.getName());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ExpressionMethodReference){
            ((ExpressionMethodReference) other).setPyStartPosition(node.getPyStartPosition());
            ((ExpressionMethodReference) other).setPyLength(node.getPyLength());
            ((ExpressionMethodReference) other).setPyLine(node.getPyLine());
            ((ExpressionMethodReference) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ExpressionStatement node, Object other) {
        boolean match ;
       if( node.getExpression() instanceof PyErrorExpression && other instanceof EmptyStatement){
           match = true;
       }else{
           match = super.match(node, other);
       }


        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof ExpressionStatement){
            ((ExpressionStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((ExpressionStatement) other).setPyLength(node.getPyLength());
            ((ExpressionStatement) other).setPyLine(node.getPyLine());
            ((ExpressionStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());

        }
        return match;
    }

    public boolean match(FieldAccess node, Object other) {
        boolean match;
        if (!(other instanceof FieldAccess)) {
            match= false;
        } else {
            FieldAccess o = (FieldAccess)other;
            match= this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getName(), o.getName());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof FieldAccess){
            ((FieldAccess) other).setPyStartPosition(node.getPyStartPosition());
            ((FieldAccess) other).setPyLength(node.getPyLength());
            ((FieldAccess) other).setPyLine(node.getPyLine());
            ((FieldAccess) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(FieldDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof FieldDeclaration){
            ((FieldDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((FieldDeclaration) other).setPyLength(node.getPyLength());
            ((FieldDeclaration) other).setPyLine(node.getPyLine());
            ((FieldDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ForStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ForStatement){
            ((ForStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((ForStatement) other).setPyLength(node.getPyLength());
            ((ForStatement) other).setPyLine(node.getPyLine());
            ((ForStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(IfStatement node, Object other) {
        boolean match;
        if (!(other instanceof IfStatement)) {
            match = false;
        } else {
            IfStatement o = (IfStatement)other;
            match = this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getThenStatement(), o.getThenStatement()) & this.safeSubtreeMatch(node.getElseStatement(), o.getElseStatement());
        }




        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof IfStatement){
            ((IfStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((IfStatement) other).setPyLength(node.getPyLength());
            ((IfStatement) other).setPyLine(node.getPyLine());
            ((IfStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ImportDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ImportDeclaration){
            ((ImportDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((ImportDeclaration) other).setPyLength(node.getPyLength());
            ((ImportDeclaration) other).setPyLine(node.getPyLine());
            ((ImportDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(InfixExpression node, Object other) {
        boolean match;
        if (!(other instanceof InfixExpression)) {
            match = false;
        } else {
            InfixExpression o = (InfixExpression)other;
            if (node.hasExtendedOperands() & o.hasExtendedOperands() & !this.safePythonSubtreeListMatch(node.extendedOperands(), o.extendedOperands())) {
                match = false;
            } else if (node.hasExtendedOperands() != o.hasExtendedOperands()) {
                if (node.hasExtendedOperands()){
                    match = isNodeMatch(node, o);
//                    InfixExpression created_infix = isMatch(node, o);
//                    if(created_infix!=null)
//                    {
//                        match=true;
//                    }
//                    else{
//                        match=false;
//                    }
                }
                else if (o.hasExtendedOperands()){
                    match = isNodeMatch(o,node);
//                    InfixExpression created_infix = isMatch(o,node);
//                    if(created_infix!=null)
//                    {
//                        match=true;
//                    }
//                    else{
//                        match=false;
//                    }
                }
                else{
                    match = false;
                }
            } else {
                match = node.getOperator().toString().equals(o.getOperator().toString()) & this.safeSubtreeMatch(node.getLeftOperand(), o.getLeftOperand()) & this.safeSubtreeMatch(node.getRightOperand(), o.getRightOperand());
            }
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ((other instanceof InfixExpression)) {
            ((InfixExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((InfixExpression) other).setPyLength(node.getPyLength());
            ((InfixExpression) other).setPyLine(node.getPyLine());
            ((InfixExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    private boolean isNodeMatch(InfixExpression node1, InfixExpression node2){
//        node1 = nodes that have extended expressions
        //        node2 = nodes not have that have extended expressions
        boolean match=true;
        List<ASTNode> leftSide = new ArrayList<>();
        ASTNode node = node2.getLeftOperand();
        leftSide.add(node2.getRightOperand());
        ASTNode originalNode = node2;
        while (node instanceof InfixExpression){
            leftSide.add(((InfixExpression) node).getRightOperand());
            node = ((InfixExpression) node).getLeftOperand();
        }
        leftSide.add(node);

        if (!leftSide.get(leftSide.size()-1).subtreeMatch(new PyASTMatcher(),node1.getLeftOperand())&
        !leftSide.get(leftSide.size()-2).subtreeMatch(new PyASTMatcher(),node1.getRightOperand())){
            match= false;
        }
        int i = 1;
        if (node1.extendedOperands().size()==leftSide.size()-2){
            for (Object operand : node1.extendedOperands()) {
                if (!leftSide.get(leftSide.size()-2-i).subtreeMatch(new PyASTMatcher(),operand)){
                    match= false;
                }
                i=i+1;
            }
        }
        else
        {
            match = false;
        }

//        if (rightSide.size()==leftSide.size())
//            for (int i = 0; i<rightSide.size(); i++){
//                if (!rightSide.get(i).subtreeMatch(new PyASTMatcher(),leftSide.get(rightSide.size()-1-i)))
//                    match =false;
//            }
//        else{
//            match = false;
//        }
        return match;
    }



    private InfixExpression isMatch(InfixExpression node, InfixExpression o) {
        boolean match;
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST ast = new AST(options);

        InfixExpression infix  = ast.newInfixExpression();
        infix.setOperator(node.getOperator());
        Expression expression = (Expression) ASTNode.copySubtree(ast, (Expression) node.extendedOperands().get(node.extendedOperands().size() - 1));
//        ((Expression) node.extendedOperands().get(node.extendedOperands().size() - 1)).subtreeMatch(new PyASTMatcher(),expression);
        infix.setRightOperand(expression);

        InfixExpression left = ast.newInfixExpression();
        left.setOperator(node.getOperator());
        if (node.extendedOperands().size()==1){
            Expression expression1 = (Expression) ASTNode.copySubtree(ast, node.getRightOperand());
//            node.getRightOperand().subtreeMatch(new PyASTMatcher(),expression1);
            left.setRightOperand(expression1);
            Expression expression2 = (Expression) ASTNode.copySubtree(ast, node.getLeftOperand());
//            node.getLeftOperand().subtreeMatch(new PyASTMatcher(),expression2);
            left.setLeftOperand(expression2);
        }
        else if (node.extendedOperands().size()==2){
            Expression expression3 = (Expression) ASTNode.copySubtree(ast, (Expression) node.extendedOperands().get(0));
//            ((Expression) node.extendedOperands().get(0)).subtreeMatch(new PyASTMatcher(),expression3);
            left.setRightOperand(expression3);
            InfixExpression left_left = ast.newInfixExpression();
            left_left.setOperator(node.getOperator());
            Expression expression1 = (Expression) ASTNode.copySubtree(ast, node.getLeftOperand());
//            node.getLeftOperand().subtreeMatch(new PyASTMatcher(),expression1);
            left_left.setLeftOperand(expression1);
            Expression expression2 = (Expression) ASTNode.copySubtree(ast, node.getRightOperand());
//            node.getRightOperand().subtreeMatch(new PyASTMatcher(),expression2);
            left_left.setRightOperand(expression2);
            left.setLeftOperand(left_left);
        }
        else {
            InfixExpression left_left = ast.newInfixExpression();
            left_left.setOperator(node.getOperator());
            Expression expression1 = (Expression) ASTNode.copySubtree(ast, node.getLeftOperand());
//            node.getLeftOperand().subtreeMatch(new PyASTMatcher(),expression1);
            left_left.setLeftOperand(expression1);
            Expression expression2 = (Expression) ASTNode.copySubtree(ast, node.getRightOperand());
//            node.getRightOperand().subtreeMatch(new PyASTMatcher(),expression2);
            left_left.setRightOperand(expression2);

            for (int i = 0; i< node.extendedOperands().size()-2 ; i++){
                InfixExpression temp = ast.newInfixExpression();
                temp.setLeftOperand(left_left);
                temp.setOperator(node.getOperator());
                Expression expression3 = (Expression) ASTNode.copySubtree(ast, (Expression) node.extendedOperands().get(i));
//                ( (Expression) node.extendedOperands().get(i)).subtreeMatch(new PyASTMatcher(),expression3);
                temp.setRightOperand(expression3);
                left_left=temp;
            }
            Expression expression3 = (Expression) ASTNode.copySubtree(ast, (Expression) node.extendedOperands().get(node.extendedOperands().size() - 2));
//            ((Expression) node.extendedOperands().get(node.extendedOperands().size() - 2)).subtreeMatch(new PyASTMatcher(),expression3);
            left.setRightOperand(expression3);
            left.setLeftOperand(left_left);
        }
        infix.setLeftOperand(left);
        match = node.getOperator().toString().equals(o.getOperator().toString()) & this.safeSubtreeMatch(o, infix);
        if (match)
            return infix;
        else
            return null;
    }

    public boolean match(Initializer node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof Initializer){
            ((Initializer) other).setPyStartPosition(node.getPyStartPosition());
            ((Initializer) other).setPyLength(node.getPyLength());
            ((Initializer) other).setPyLine(node.getPyLine());
            ((Initializer) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(InstanceofExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof InstanceofExpression){
            ((InstanceofExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((InstanceofExpression) other).setPyLength(node.getPyLength());
            ((InstanceofExpression) other).setPyLine(node.getPyLine());
            ((InstanceofExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(IntersectionType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof IntersectionType){
            ((IntersectionType) other).setPyStartPosition(node.getPyStartPosition());
            ((IntersectionType) other).setPyLength(node.getPyLength());
            ((IntersectionType) other).setPyLine(node.getPyLine());
            ((IntersectionType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(Javadoc node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof Javadoc){
            ((Javadoc) other).setPyStartPosition(node.getPyStartPosition());
            ((Javadoc) other).setPyLength(node.getPyLength());
            ((Javadoc) other).setPyLine(node.getPyLine());
            ((Javadoc) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }


    public boolean match(LabeledStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof LabeledStatement){
            ((LabeledStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((LabeledStatement) other).setPyLength(node.getPyLength());
            ((LabeledStatement) other).setPyLine(node.getPyLine());
            ((LabeledStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(LambdaExpression node, Object other) {
        boolean match;
        if (!(other instanceof LambdaExpression)) {
            match= false;
        } else {
            LambdaExpression o = (LambdaExpression)other;
            match= node.hasParentheses() == o.hasParentheses() & this.safeSubtreeListMatch(node.parameters(), o.parameters()) & this.safeSubtreeMatch(node.getBody(), o.getBody());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof LambdaExpression){
            ((LambdaExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((LambdaExpression) other).setPyLength(node.getPyLength());
            ((LambdaExpression) other).setPyLine(node.getPyLine());
            ((LambdaExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(LineComment node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof LineComment){
            ((LineComment) other).setPyStartPosition(node.getPyStartPosition());
            ((LineComment) other).setPyLength(node.getPyLength());
            ((LineComment) other).setPyLine(node.getPyLine());
            ((LineComment) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MarkerAnnotation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof MarkerAnnotation){
            ((MarkerAnnotation) other).setPyStartPosition(node.getPyStartPosition());
            ((MarkerAnnotation) other).setPyLength(node.getPyLength());
            ((MarkerAnnotation) other).setPyLine(node.getPyLine());
            ((MarkerAnnotation) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MemberRef node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof MemberRef){
            ((MemberRef) other).setPyStartPosition(node.getPyStartPosition());
            ((MemberRef) other).setPyLength(node.getPyLength());
            ((MemberRef) other).setPyLine(node.getPyLine());
            ((MemberRef) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MemberValuePair node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof MemberValuePair){
            ((MemberValuePair) other).setPyStartPosition(node.getPyStartPosition());
            ((MemberValuePair) other).setPyLength(node.getPyLength());
            ((MemberValuePair) other).setPyLine(node.getPyLine());
            ((MemberValuePair) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MethodRef node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof MethodRef){
            ((MethodRef) other).setPyStartPosition(node.getPyStartPosition());
            ((MethodRef) other).setPyLength(node.getPyLength());
            ((MethodRef) other).setPyLine(node.getPyLine());
            ((MethodRef) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MethodRefParameter node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof MethodRefParameter){
            ((MethodRefParameter) other).setPyStartPosition(node.getPyStartPosition());
            ((MethodRefParameter) other).setPyLength(node.getPyLength());
            ((MethodRefParameter) other).setPyLine(node.getPyLine());
            ((MethodRefParameter) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(MethodDeclaration node, Object other) {
        boolean match=true;
        if (!(other instanceof MethodDeclaration)) {
            match = false;
        } else {
            MethodDeclaration o = (MethodDeclaration) other;
            int level = 15;
            if (node.isConstructor() == o.isConstructor() && this.safeSubtreeMatch(node.getJavadoc(), o.getJavadoc())) {

                if (!this.safeSubtreeListMatch(node.modifiers(), o.modifiers()) || !this.safeSubtreeListMatch(node.typeParameters(), o.typeParameters()) || !this.safeSubtreeMatch(node.getReturnType2(), o.getReturnType2())) {
                        match = false;
                }
                else if (this.safeSubtreeMatch(node.getName(), o.getName()) &  this.safePythonSubtreeListMatch(node.parameters(), o.parameters())) {

                    if (this.safeSubtreeMatch(node.getBody(), o.getBody()) && (!DOMASTUtil.isRecordDeclarationSupported(node.getAST()) || node.isCompactConstructor() == o.isCompactConstructor())) {
                        match = true;
                    }
                }
            }
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof MethodDeclaration){
            ((MethodDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((MethodDeclaration) other).setPyLength(node.getPyLength());
            ((MethodDeclaration) other).setPyLine(node.getPyLine());
            ((MethodDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }


    public final boolean safeSubtreeMatchMapArguments(List list1, List list2) {
        int size1 = list1.size();
        int size2 = list2.size();
        if (size1 != size2) {
            return false;
        } else {
            Iterator it1 = list1.iterator();
            Iterator it2 = list2.iterator();

            while(it1.hasNext()) {
                ASTNode n1 = (ASTNode)it1.next();
                ASTNode n2 = (ASTNode)it2.next();
                if (!n1.subtreeMatch(this, n2)) {
                    return false;
                }
            }

            return true;
        }
    }
    public boolean match(MethodInvocation node, Object other) {
        boolean match;
        if (other instanceof MethodInvocation){
            match= this.safeSubtreeMatch(node.getExpression(), ((MethodInvocation)other).getExpression())
                    & this.safeSubtreeMatch(node.getName(), ((MethodInvocation)other).getName())
                    & this.safePythonSubtreeListMatch(node.arguments(), ((MethodInvocation)other).arguments());
        }
        else if (other instanceof PrefixExpression){
            match = this.safeSubtreeMatch(node,((PrefixExpression) other).getOperand());
        }
        else{
            match=false;
        }

        if (DEBUG && !match ) {
            log.info("Unmatched " + node.getClass());
        }

        ((ASTNode) other).setPyStartPosition(node.getPyStartPosition());
        ((ASTNode) other).setPyLength(node.getPyLength());
        ((ASTNode) other).setPyLine(node.getPyLine());
        ((ASTNode) other).setPyColumnOffSet(node.getPyColumnOffSet());

        return match;
    }

    public boolean match(Modifier node, Object other) {
        boolean match;

        if (!(other instanceof Modifier)) {
            match =false;
        } else {
            Modifier o = (Modifier)other;
            match = node.getKeyword().toString().equals(o.getKeyword().toString());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            ((Modifier) other).setPyStartPosition(node.getPyStartPosition());
            ((Modifier) other).setPyLength(node.getPyLength());
            ((Modifier) other).setPyLine(node.getPyLine());
            ((Modifier) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ModuleDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ModuleDeclaration){
            ((ModuleDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((ModuleDeclaration) other).setPyLength(node.getPyLength());
            ((ModuleDeclaration) other).setPyLine(node.getPyLine());
            ((ModuleDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ModuleModifier node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ModuleModifier){
            ((ModuleModifier) other).setPyStartPosition(node.getPyStartPosition());
            ((ModuleModifier) other).setPyLength(node.getPyLength());
            ((ModuleModifier) other).setPyLine(node.getPyLine());
            ((ModuleModifier) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(NameQualifiedType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof NameQualifiedType){
            ((NameQualifiedType) other).setPyStartPosition(node.getPyStartPosition());
            ((NameQualifiedType) other).setPyLength(node.getPyLength());
            ((NameQualifiedType) other).setPyLine(node.getPyLine());
            ((NameQualifiedType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(NormalAnnotation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof NormalAnnotation){
            ((NormalAnnotation) other).setPyStartPosition(node.getPyStartPosition());
            ((NormalAnnotation) other).setPyLength(node.getPyLength());
            ((NormalAnnotation) other).setPyLine(node.getPyLine());
            ((NormalAnnotation) other).setPyColumnOffSet(node.getPyColumnOffSet());

        }
        return match;
    }

    public boolean match(NullLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof NullLiteral){
            ((NullLiteral) other).setPyStartPosition(node.getPyStartPosition());
            ((NullLiteral) other).setPyLength(node.getPyLength());
            ((NullLiteral) other).setPyLine(node.getPyLine());
            ((NullLiteral) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(NumberLiteral node, Object other) {
        boolean match;
        if (other instanceof PrefixExpression){
            match = node.getToken().toString().equals(other.toString());
        }
        else if (!(other instanceof NumberLiteral)) {
            match = false;
        } else {
            NumberLiteral o = (NumberLiteral)other;
            match = safeEquals(node.getToken(), o.getToken());
        }
        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof PrefixExpression){
            ((PrefixExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((PrefixExpression) other).setPyLength(node.getPyLength());
            ((PrefixExpression) other).setPyLine(node.getPyLine());
            ((PrefixExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        else if (match){
            ((NumberLiteral) other).setPyStartPosition(node.getPyStartPosition());
            ((NumberLiteral) other).setPyLength(node.getPyLength());
            ((NumberLiteral) other).setPyLine(node.getPyLine());
            ((NumberLiteral) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(OpensDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof OpensDirective){
            ((OpensDirective) other).setPyStartPosition(node.getPyStartPosition());
            ((OpensDirective) other).setPyLength(node.getPyLength());
            ((OpensDirective) other).setPyLine(node.getPyLine());
            ((OpensDirective) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PackageDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof PackageDeclaration){
            ((PackageDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((PackageDeclaration) other).setPyLength(node.getPyLength());
            ((PackageDeclaration) other).setPyLine(node.getPyLine());
            ((PackageDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ParameterizedType node, Object other) {
        boolean match;

        if (!(other instanceof ParameterizedType)) {
            match = false;
        } else {
            ParameterizedType o = (ParameterizedType)other;
            match = this.safeSubtreeMatch(node.getType(), o.getType()) & this.safePythonSubtreeListMatch(node.typeArguments(), o.typeArguments());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ParameterizedType){
            ((ParameterizedType) other).setPyStartPosition(node.getPyStartPosition());
            ((ParameterizedType) other).setPyLength(node.getPyLength());
            ((ParameterizedType) other).setPyLine(node.getPyLine());
            ((ParameterizedType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(ParenthesizedExpression node, Object other) {
        boolean match = super.match(node, other);
        if (!match)
            match = this.safeSubtreeMatch(node.getExpression(), other);


        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());

        ((ASTNode) other).setPyStartPosition(node.getPyStartPosition());
        ((ASTNode) other).setPyLength(node.getPyLength());
        ((ASTNode) other).setPyLine(node.getPyLine());
        ((ASTNode) other).setPyColumnOffSet(node.getPyColumnOffSet());

        return match;
    }

    public boolean match(PostfixExpression node, Object other) {
        boolean match;
        if (!(other instanceof PostfixExpression)) {
            match = false;
        } else {
            PostfixExpression o = (PostfixExpression)other;
            match = node.getOperator().toString().equals(o.getOperator().toString()) & this.safeSubtreeMatch(node.getOperand(), o.getOperand());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            ((PostfixExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((PostfixExpression) other).setPyLength(node.getPyLength());
            ((PostfixExpression) other).setPyLine(node.getPyLine());
            ((PostfixExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PrefixExpression node, Object other) {
        boolean match;
        if (other instanceof NumberLiteral && other.toString().equals(node.toString())) {
            match = true;
        }
        else if (!(other instanceof PrefixExpression)) {
            match = false;
        }
        else {
            PrefixExpression o = (PrefixExpression)other;
            match = node.getOperator().toString().equals(o.getOperator().toString()) & this.safeSubtreeMatch(node.getOperand(), o.getOperand());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (match){
            if (other instanceof NumberLiteral ){
                ((NumberLiteral) other).setPyStartPosition(node.getPyStartPosition());
                ((NumberLiteral) other).setPyLength(node.getPyLength());
                ((NumberLiteral) other).setPyLine(node.getPyLine());
                ((NumberLiteral) other).setPyColumnOffSet(node.getPyColumnOffSet());
            }
            else {
                ((PrefixExpression) other).setPyStartPosition(node.getPyStartPosition());
                ((PrefixExpression) other).setPyLength(node.getPyLength());
                ((PrefixExpression) other).setPyLine(node.getPyLine());
                ((PrefixExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
            }
        }
        return match;
    }

    public boolean match(PrimitiveType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof PrimitiveType){
            ((PrimitiveType) other).setPyStartPosition(node.getPyStartPosition());
            ((PrimitiveType) other).setPyLength(node.getPyLength());
            ((PrimitiveType) other).setPyLine(node.getPyLine());
            ((PrimitiveType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ProvidesDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ProvidesDirective){
            ((ProvidesDirective) other).setPyStartPosition(node.getPyStartPosition());
            ((ProvidesDirective) other).setPyLength(node.getPyLength());
            ((ProvidesDirective) other).setPyLine(node.getPyLine());
            ((ProvidesDirective) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(QualifiedName node, Object other) {
        boolean match;
        if (!(other instanceof QualifiedName)) {
            match = false;
        } else {
            QualifiedName o = (QualifiedName)other;
            match = this.safeSubtreeMatch(node.getQualifier(), o.getQualifier()) & this.safeSubtreeMatch(node.getName(), o.getName());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof QualifiedName){
            ((QualifiedName) other).setPyStartPosition(node.getPyStartPosition());
            ((QualifiedName) other).setPyLength(node.getPyLength());
            ((QualifiedName) other).setPyLine(node.getPyLine());
            ((QualifiedName) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ModuleQualifiedName node, Object other) {
        boolean match ;
        if (!(other instanceof ModuleQualifiedName)) {
            match = false;
        } else {
            ModuleQualifiedName o = (ModuleQualifiedName)other;
            match = this.safeSubtreeMatch(node.getModuleQualifier(), o.getModuleQualifier()) & this.safeSubtreeMatch(node.getName(), o.getName());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ModuleQualifiedName){
            ((ModuleQualifiedName) other).setPyStartPosition(node.getPyStartPosition());
            ((ModuleQualifiedName) other).setPyLength(node.getPyLength());
            ((ModuleQualifiedName) other).setPyLine(node.getPyLine());
            ((ModuleQualifiedName) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(QualifiedType node, Object other) {
        boolean match;
        if (!(other instanceof QualifiedType)) {
            match = false;
        } else {
            QualifiedType o = (QualifiedType)other;
            match = this.safeSubtreeMatch(node.getQualifier(), o.getQualifier()) & this.safePythonSubtreeListMatch(node.annotations(), o.annotations()) & this.safeSubtreeMatch(node.getName(), o.getName());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof QualifiedType){
            ((QualifiedType) other).setPyStartPosition(node.getPyStartPosition());
            ((QualifiedType) other).setPyLength(node.getPyLength());
            ((QualifiedType) other).setPyLine(node.getPyLine());
            ((QualifiedType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(RecordDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof RecordDeclaration){
            ((RecordDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((RecordDeclaration) other).setPyLength(node.getPyLength());
            ((RecordDeclaration) other).setPyLine(node.getPyLine());
            ((RecordDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(RequiresDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof RequiresDirective){
            ((RequiresDirective) other).setPyStartPosition(node.getPyStartPosition());
            ((RequiresDirective) other).setPyLength(node.getPyLength());
            ((RequiresDirective) other).setPyLine(node.getPyLine());
            ((RequiresDirective) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ReturnStatement node, Object other) {
        boolean match;
        if (node.getExpression() instanceof PyErrorExpression && other instanceof ReturnStatement && ((ReturnStatement) other).getExpression()==null)
            match=true;
        else
            match = super.match(node, other);
        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof ReturnStatement){
            ((ReturnStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((ReturnStatement) other).setPyLength(node.getPyLength());
            ((ReturnStatement) other).setPyLine(node.getPyLine());
            ((ReturnStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SimpleName node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match && other instanceof SimpleName)
            log.info("Unmatched "+node.getClass() +" "+node.getIdentifier()+" : "+ ((SimpleName)other).getIdentifier());
        else if(DEBUG && !match)
            log.info("Unmatched "+node.getClass() +" "+node.getIdentifier());

        if (other instanceof SimpleName){
            ((SimpleName) other).setPyStartPosition(node.getPyStartPosition());
            ((SimpleName) other).setPyLength(node.getPyLength());
            ((SimpleName) other).setPyLine(node.getPyLine());
            ((SimpleName) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SimpleType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof SimpleType){
            ((SimpleType) other).setPyStartPosition(node.getPyStartPosition());
            ((SimpleType) other).setPyLength(node.getPyLength());
            ((SimpleType) other).setPyLine(node.getPyLine());
            ((SimpleType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SingleMemberAnnotation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof SingleMemberAnnotation){
            ((SingleMemberAnnotation) other).setPyStartPosition(node.getPyStartPosition());
            ((SingleMemberAnnotation) other).setPyLength(node.getPyLength());
            ((SingleMemberAnnotation) other).setPyLine(node.getPyLine());
            ((SingleMemberAnnotation) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SingleVariableDeclaration node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof SingleVariableDeclaration){
            ((SingleVariableDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((SingleVariableDeclaration) other).setPyLength(node.getPyLength());
            ((SingleVariableDeclaration) other).setPyLine(node.getPyLine());
            ((SingleVariableDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(StringLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof StringLiteral){
            ((StringLiteral) other).setPyStartPosition(node.getPyStartPosition());
            ((StringLiteral) other).setPyLength(node.getPyLength());
            ((StringLiteral) other).setPyLine(node.getPyLine());
            ((StringLiteral) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperConstructorInvocation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof SuperConstructorInvocation){
            ((SuperConstructorInvocation) other).setPyStartPosition(node.getPyStartPosition());
            ((SuperConstructorInvocation) other).setPyLength(node.getPyLength());
            ((SuperConstructorInvocation) other).setPyLine(node.getPyLine());
            ((SuperConstructorInvocation) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperFieldAccess node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof SuperFieldAccess){
            ((SuperFieldAccess) other).setPyStartPosition(node.getPyStartPosition());
            ((SuperFieldAccess) other).setPyLength(node.getPyLength());
            ((SuperFieldAccess) other).setPyLine(node.getPyLine());
            ((SuperFieldAccess) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperMethodInvocation node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof SuperMethodInvocation){
            ((SuperMethodInvocation) other).setPyStartPosition(node.getPyStartPosition());
            ((SuperMethodInvocation) other).setPyLength(node.getPyLength());
            ((SuperMethodInvocation) other).setPyLine(node.getPyLine());
            ((SuperMethodInvocation) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SuperMethodReference node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof SuperMethodReference){
            ((SuperMethodReference) other).setPyStartPosition(node.getPyStartPosition());
            ((SuperMethodReference) other).setPyLength(node.getPyLength());
            ((SuperMethodReference) other).setPyLine(node.getPyLine());
            ((SuperMethodReference) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SwitchCase node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof SwitchCase){
            ((SwitchCase) other).setPyStartPosition(node.getPyStartPosition());
            ((SwitchCase) other).setPyLength(node.getPyLength());
            ((SwitchCase) other).setPyLine(node.getPyLine());
            ((SwitchCase) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }



    public boolean match(SwitchExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof SwitchExpression){
            ((SwitchExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((SwitchExpression) other).setPyLength(node.getPyLength());
            ((SwitchExpression) other).setPyLine(node.getPyLine());
            ((SwitchExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SwitchStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof SwitchStatement){
            ((SwitchStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((SwitchStatement) other).setPyLength(node.getPyLength());
            ((SwitchStatement) other).setPyLine(node.getPyLine());
            ((SwitchStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(SynchronizedStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof SynchronizedStatement){
            ((SynchronizedStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((SynchronizedStatement) other).setPyLength(node.getPyLength());
            ((SynchronizedStatement) other).setPyLine(node.getPyLine());
            ((SynchronizedStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TagElement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof TagElement){
            ((TagElement) other).setPyStartPosition(node.getPyStartPosition());
            ((TagElement) other).setPyLength(node.getPyLength());
            ((TagElement) other).setPyLine(node.getPyLine());
            ((TagElement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TextBlock node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if ( other instanceof TextBlock){
            ((TextBlock) other).setPyStartPosition(node.getPyStartPosition());
            ((TextBlock) other).setPyLength(node.getPyLength());
            ((TextBlock) other).setPyLine(node.getPyLine());
            ((TextBlock) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TextElement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof TextElement){
            ((TextElement) other).setPyStartPosition(node.getPyStartPosition());
            ((TextElement) other).setPyLength(node.getPyLength());
            ((TextElement) other).setPyLine(node.getPyLine());
            ((TextElement) other).setPyColumnOffSet(node.getPyColumnOffSet());

        }
        return match;
    }

    public boolean match(ThisExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ThisExpression){
            ((ThisExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((ThisExpression) other).setPyLength(node.getPyLength());
            ((ThisExpression) other).setPyLine(node.getPyLine());
            ((ThisExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(ThrowStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof ThrowStatement){
            ((ThrowStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((ThrowStatement) other).setPyLength(node.getPyLength());
            ((ThrowStatement) other).setPyLine(node.getPyLine());
            ((ThrowStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TryStatement node, Object other) {
        boolean match;
        if (!(other instanceof TryStatement)) {
            match= false;
        } else {
            TryStatement o = (TryStatement)other;

            match= this.safeSubtreeMatch(node.getBody(), o.getBody()) & this.safePythonSubtreeListMatch(node.catchClauses(), o.catchClauses()) & this.safeSubtreeMatch(node.getFinally(), o.getFinally());
        }
        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof TryStatement){
            ((TryStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((TryStatement) other).setPyLength(node.getPyLength());
            ((TryStatement) other).setPyLine(node.getPyLine());
            ((TryStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeDeclaration node, Object other) {
        boolean match;
        if (!(other instanceof TypeDeclaration)) {
            match = false;
        } else {
            TypeDeclaration o = (TypeDeclaration)other;
            match = node.isInterface() == o.isInterface() & this.safePythonSubtreeListMatch(node.modifiers(), o.modifiers())
                        & this.safeSubtreeMatch(node.getJavadoc(), o.getJavadoc())
                        & this.safeSubtreeMatch(node.getName(), o.getName())
                        & this.safePythonSubtreeListMatch(node.bodyDeclarations(), o.bodyDeclarations());
        }

        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof TypeDeclaration){
            ((TypeDeclaration) other).setPyStartPosition(node.getPyStartPosition());
            ((TypeDeclaration) other).setPyLength(node.getPyLength());
            ((TypeDeclaration) other).setPyLine(node.getPyLine());
            ((TypeDeclaration) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeDeclarationStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof TypeDeclarationStatement){
            ((TypeDeclarationStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((TypeDeclarationStatement) other).setPyLength(node.getPyLength());
            ((TypeDeclarationStatement) other).setPyLine(node.getPyLine());
            ((TypeDeclarationStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeLiteral node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof TypeLiteral){
            ((TypeLiteral) other).setPyStartPosition(node.getPyStartPosition());
            ((TypeLiteral) other).setPyLength(node.getPyLength());
            ((TypeLiteral) other).setPyLine(node.getPyLine());
            ((TypeLiteral) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeMethodReference node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof TypeMethodReference){
            ((TypeMethodReference) other).setPyStartPosition(node.getPyStartPosition());
            ((TypeMethodReference) other).setPyLength(node.getPyLength());
            ((TypeMethodReference) other).setPyLine(node.getPyLine());
            ((TypeMethodReference) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(TypeParameter node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof TypeParameter){
            ((TypeParameter) other).setPyStartPosition(node.getPyStartPosition());
            ((TypeParameter) other).setPyLength(node.getPyLength());
            ((TypeParameter) other).setPyLine(node.getPyLine());
            ((TypeParameter) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(UnionType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof UnionType){
            ((UnionType) other).setPyStartPosition(node.getPyStartPosition());
            ((UnionType) other).setPyLength(node.getPyLength());
            ((UnionType) other).setPyLine(node.getPyLine());
            ((UnionType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(UsesDirective node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof UsesDirective){
            ((UsesDirective) other).setPyStartPosition(node.getPyStartPosition());
            ((UsesDirective) other).setPyLength(node.getPyLength());
            ((UsesDirective) other).setPyLine(node.getPyLine());
            ((UsesDirective) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(VariableDeclarationExpression node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof VariableDeclarationExpression){
            ((VariableDeclarationExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((VariableDeclarationExpression) other).setPyLength(node.getPyLength());
            ((VariableDeclarationExpression) other).setPyLine(node.getPyLine());
            ((VariableDeclarationExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(VariableDeclarationFragment node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof VariableDeclarationFragment){
            ((VariableDeclarationFragment) other).setPyStartPosition(node.getPyStartPosition());
            ((VariableDeclarationFragment) other).setPyLength(node.getPyLength());
            ((VariableDeclarationFragment) other).setPyLine(node.getPyLine());
            ((VariableDeclarationFragment) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(VariableDeclarationStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof VariableDeclarationStatement){
            ((VariableDeclarationStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((VariableDeclarationStatement) other).setPyLength(node.getPyLength());
            ((VariableDeclarationStatement) other).setPyLine(node.getPyLine());
            ((VariableDeclarationStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(WhileStatement node, Object other) {
        boolean match;
        if (!(other instanceof WhileStatement)) {
            match= false;
        } else {
            WhileStatement o = (WhileStatement)other;
            match= this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getBody(), o.getBody());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof WhileStatement){
            ((WhileStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((WhileStatement) other).setPyLength(node.getPyLength());
            ((WhileStatement) other).setPyLine(node.getPyLine());
            ((WhileStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(WildcardType node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof WildcardType){
            ((WildcardType) other).setPyStartPosition(node.getPyStartPosition());
            ((WildcardType) other).setPyLength(node.getPyLength());
            ((WildcardType) other).setPyLine(node.getPyLine());
            ((WildcardType) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(YieldStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof YieldStatement){
            ((YieldStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((YieldStatement) other).setPyLength(node.getPyLength());
            ((YieldStatement) other).setPyLine(node.getPyLine());
            ((YieldStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyInExpression node, Object other) {
        boolean match;
        if (!(other instanceof PyInExpression)) {
            match= false;
        } else {
            PyInExpression o = (PyInExpression)other;
            match= this.safeSubtreeMatch(node.getLeftOperand(), o.getLeftOperand()) & this.safeSubtreeMatch(node.getRightOperand(), o.getRightOperand());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof PyInExpression){
            ((PyInExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((PyInExpression) other).setPyLength(node.getPyLength());
            ((PyInExpression) other).setPyLine(node.getPyLine());
            ((PyInExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyTupleExpression node, Object other) {
        boolean match ;
        if (!(other instanceof PyTupleExpression)) {
            match= false;
        } else {
            PyTupleExpression o = (PyTupleExpression)other;
            match= this.safePythonSubtreeListMatch(node.expressions(), o.expressions());
        }

        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof PyTupleExpression){
            ((PyTupleExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((PyTupleExpression) other).setPyLength(node.getPyLength());
            ((PyTupleExpression) other).setPyLine(node.getPyLine());
            ((PyTupleExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return true; //TODO due the bug in PyTuple grammer
    }

    public boolean match(PyGenerator node, Object other) {
        boolean match ;
        if (!(other instanceof PyGenerator)) {
            match= false;
        } else {
            PyGenerator o = (PyGenerator)other;
            match= this.safePythonSubtreeListMatch(node.getComparators(), o.getComparators()) & this.safeSubtreeMatch(node.getTargetExpression(), o.getTargetExpression());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof PyGenerator){
            ((PyGenerator) other).setPyStartPosition(node.getPyStartPosition());
            ((PyGenerator) other).setPyLength(node.getPyLength());
            ((PyGenerator) other).setPyLine(node.getPyLine());
            ((PyGenerator) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(PyComparator node, Object other) {
        boolean match;
        if (!(other instanceof PyComparator)) {
            match = false;
        } else {
            PyComparator o = (PyComparator)other;
            List nodeM = new ArrayList();
            List nodeN = new ArrayList();

            for (Object parameter : node.getValueExpression()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeM.add(para);
                }
            }
            for (Object parameter : o.getValueExpression()) {
                SingleVariableDeclaration para = (SingleVariableDeclaration)parameter;
                if (!(para.getName().getIdentifier().equals("DummyTerminalNode"))){
                    nodeN.add(para);
                }
            }
            if (node.internalGetConditionalExpression() != null  &  !(node.internalGetConditionalExpression() instanceof SimpleName && ((SimpleName) node.internalGetConditionalExpression()).getIdentifier().equals("DUMMY"))) {
                match = this.safeSubtreeMatch(node.getIteratorExpression(), o.getIteratorExpression()) & this.safeSubtreeMatch(node.getConditionalExpression(), o.getConditionalExpression()) & this.safePythonSubtreeListMatch(nodeM, nodeN);
            } else {
                match = this.safeSubtreeMatch(node.getIteratorExpression(), o.getIteratorExpression()) & this.safePythonSubtreeListMatch(nodeM, nodeN);
            }
        }

        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (match){
            ((PyComparator) other).setPyStartPosition(node.getPyStartPosition());
            ((PyComparator) other).setPyLength(node.getPyLength());
            ((PyComparator) other).setPyLine(node.getPyLine());
            ((PyComparator) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyNotInExpression node, Object other) {
        boolean match ;
        if (!(other instanceof PyNotInExpression)) {
            match= false;
        } else {
            PyNotInExpression o = (PyNotInExpression)other;
            match= this.safeSubtreeMatch(node.getLeftOperand(), o.getLeftOperand()) & this.safeSubtreeMatch(node.getRightOperand(), o.getRightOperand());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof PyNotInExpression){
            ((PyNotInExpression) other).setPyStartPosition(node.getPyStartPosition());
            ((PyNotInExpression) other).setPyLength(node.getPyLength());
            ((PyNotInExpression) other).setPyLine(node.getPyLine());
            ((PyNotInExpression) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyListComprehension node, Object other) {
        boolean match;
        if (!(other instanceof PyListComprehension)) {
            match= false;
        } else {
            PyListComprehension o = (PyListComprehension)other;
            match = this.safeSubtreeListMatch(node.getComparator(), o.getComparator()) & this.safeSubtreeMatch(node.getTargetExpression(), o.getTargetExpression());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof PyListComprehension){
            ((PyListComprehension) other).setPyStartPosition(node.getPyStartPosition());
            ((PyListComprehension) other).setPyLength(node.getPyLength());
            ((PyListComprehension) other).setPyLine(node.getPyLine());
            ((PyListComprehension) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyDictComprehension node, Object other) {
        boolean match ;
        if (other instanceof PySetComprehension){
                PySetComprehension o = (PySetComprehension)other;
                match = this.safePythonSubtreeListMatch(node.getComparator(), o.getComparator()) & this.safeSubtreeMatch(node.getTarget1Expression(), o.getTargetExpression());
        }
        else{
            match = super.match(node, other);
        }

        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof PyDictComprehension){
            ((PyDictComprehension) other).setPyStartPosition(node.getPyStartPosition());
            ((PyDictComprehension) other).setPyLength(node.getPyLength());
            ((PyDictComprehension) other).setPyLine(node.getPyLine());
            ((PyDictComprehension) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PySetComprehension node, Object other) {
        boolean match = super.match(node, other);
//        if (other instanceof PyDictComprehension){
//
//
//            PyDictComprehension o = (PyDictComprehension)other;
//            match = this.safeSubtreeListMatch(node.getComparator(), o.getComparator()) & this.safeSubtreeMatch(node.getTarget1Expression(), o.getTargetExpression());
//
//
//        }





        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof PySetComprehension){
            ((PySetComprehension) other).setPyStartPosition(node.getPyStartPosition());
            ((PySetComprehension) other).setPyLength(node.getPyLength());
            ((PySetComprehension) other).setPyLine(node.getPyLine());
            ((PySetComprehension) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }

        return match;
    }

    public boolean match(PyYieldReturnStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match )
            log.info("Unmatched "+node.getClass());
        if (other instanceof PyYieldReturnStatement){
            ((PyYieldReturnStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((PyYieldReturnStatement) other).setPyLength(node.getPyLength());
            ((PyYieldReturnStatement) other).setPyLine(node.getPyLine());
            ((PyYieldReturnStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyNonLocalStatement node, Object other) {
        boolean match = super.match(node, other);
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof PyNonLocalStatement){
            ((PyNonLocalStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((PyNonLocalStatement) other).setPyLength(node.getPyLength());
            ((PyNonLocalStatement) other).setPyLine(node.getPyLine());
            ((PyNonLocalStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }

    public boolean match(PyWithStatement node, Object other) {
        boolean match;
        if (!(other instanceof PyWithStatement)) {
            match =false;
        } else {
            PyWithStatement o = (PyWithStatement)other;
            match= this.safeSubtreeMatch(node.getExpression(), o.getExpression()) & this.safeSubtreeMatch(node.getBody(), o.getBody());
        }
        if (DEBUG && !match ) log.info("Unmatched "+node.getClass());
        if (other instanceof PyWithStatement){
            ((PyWithStatement) other).setPyStartPosition(node.getPyStartPosition());
            ((PyWithStatement) other).setPyLength(node.getPyLength());
            ((PyWithStatement) other).setPyLine(node.getPyLine());
            ((PyWithStatement) other).setPyColumnOffSet(node.getPyColumnOffSet());
        }
        return match;
    }
}
