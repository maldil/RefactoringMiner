package  refactoringminer.python;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PyComparator;
import org.eclipse.jdt.core.dom.PyDictComprehension;
import org.eclipse.jdt.core.dom.PyErrorExpression;
import org.eclipse.jdt.core.dom.PyGenerator;
import org.eclipse.jdt.core.dom.PyInExpression;
import org.eclipse.jdt.core.dom.PyListComprehension;
import org.eclipse.jdt.core.dom.PyNotInExpression;
import org.eclipse.jdt.core.dom.PySetComprehension;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.jpp.PyASTParser;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.Await;
import org.jpp.astnodes.ast.BoolOp;
import org.jpp.astnodes.ast.Bytes;
import org.jpp.astnodes.ast.Compare;
import org.jpp.astnodes.ast.Dict;
import org.jpp.astnodes.ast.DictComp;
import org.jpp.astnodes.ast.Ellipsis;
import org.jpp.astnodes.ast.ErrorExpr;
import org.jpp.astnodes.ast.ExtSlice;
import org.jpp.astnodes.ast.GeneratorExp;
import org.jpp.astnodes.ast.Lambda;
import org.jpp.astnodes.ast.ListComp;
import org.jpp.astnodes.ast.Set;
import org.jpp.astnodes.ast.SetComp;
import org.jpp.astnodes.ast.Slice;
import org.jpp.astnodes.ast.Starred;
import org.jpp.astnodes.ast.Tuple;
import org.jpp.astnodes.ast.UnaryOp;
import org.jpp.astnodes.ast.Yield;
import org.jpp.astnodes.ast.YieldFrom;
import org.jpp.astnodes.ast.arg;
import org.jpp.astnodes.ast.boolopType;
import org.jpp.astnodes.ast.cmpopType;
import org.jpp.astnodes.ast.comprehension;
import org.jpp.astnodes.ast.keyword;
import org.jpp.astnodes.ast.operatorType;
import org.jpp.astnodes.ast.unaryopType;
import org.jpp.astnodes.base.mod;
import org.jpp.astnodes.base.slice;
import org.jpp.heart.PyComplex;
import org.jpp.heart.PyFloat;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ArrayAccess;

import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.Call;
import org.jpp.astnodes.ast.List;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.ast.Num;
import org.jpp.astnodes.ast.Subscript;
import org.jpp.astnodes.ast.Str;
import org.jpp.astnodes.base.expr;
import org.jpp.heart.AstList;
import org.jpp.astnodes.ast.BinOp;
import org.jpp.astnodes.ast.IfExp;


import org.jpp.heart.PyLong;
import org.jpp.astnodes.ast.Index;
import  refactoringminer.python.pyerrors.ExpressionNotFound;
import  refactoringminer.python.pyerrors.NodeNotFoundException;
import  refactoringminer.python.typeinference.core.PyASTMatcher;
import  refactoringminer.python.typeinference.core.TypeASTNode;
import  refactoringminer.python.typeinference.core.TypeApproximator;
import  refactoringminer.python.typeinference.core.TypeStringToJDT;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;

public class MapPyExpressionsJDK extends PyMap {
    static Logger logger = Logger.getLogger(MapPyExpressionsJDK.class);
    public static Expression mapExpression(expr pyexp, AST ast, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, int start_char_pos, Map<TypeASTNode, String> typeNodes, PyCompilationUnit pyc) throws ExpressionNotFound, NodeNotFoundException {
        if (pyexp instanceof Num){
            if (((Num) pyexp).getN() instanceof PyLong){
                NumberLiteral numberLiteral = ast.newNumberLiteral(String.valueOf(((PyLong) ((Num) pyexp).getN()).getValue()));
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,numberLiteral);
                return numberLiteral;
            }
            else if (((Num) pyexp).getN() instanceof PyFloat ){
                if (String.valueOf(((PyFloat) ((Num) pyexp).getN()).getValue()).equals("Infinity")){
                    StringLiteral stringLiteral = ast.newStringLiteral();
                    MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,stringLiteral);
                    stringLiteral.setLiteralValue("inf");
                    return stringLiteral;
                }
                else if (String.valueOf(((PyFloat) ((Num) pyexp).getN()).getValue()).equals("-Infinity")){
                    StringLiteral stringLiteral = ast.newStringLiteral();
                    stringLiteral.setLiteralValue("-inf");
                    MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,stringLiteral);
                    return stringLiteral;
                }
                NumberLiteral numberLiteral = ast.newNumberLiteral(String.valueOf(((PyFloat) ((Num) pyexp).getN()).getValue()));
                numberLiteral.setPyObject(pyexp);
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,numberLiteral);
                return numberLiteral;
            }
            else if (((Num) pyexp).getN() instanceof PyComplex) {
                StringLiteral stringLiteral = ast.newStringLiteral();
                logger.debug("COMPLEX"+((PyComplex) ((Num) pyexp).getN()).real+"+"+((PyComplex) ((Num) pyexp).getN()).imag+"j");
                stringLiteral.setLiteralValue("COMPLEX"+((PyComplex) ((Num) pyexp).getN()).real+"+"+((PyComplex) ((Num) pyexp).getN()).imag+"j");
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,stringLiteral);
                return stringLiteral;
            }
            else {
                throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass());
            }

        }
        else if (pyexp instanceof Name){
            String id = mapPythonKeyWords(((Name) pyexp).getId().toString());
            if (((Name) pyexp).getInternalId().equals("True")){
                BooleanLiteral literal = ast.newBooleanLiteral(true);
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,literal);
                return literal;
            }
            else if ((((Name) pyexp).getInternalId().equals("False"))){
                BooleanLiteral literal = ast.newBooleanLiteral(false);
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,literal);
                return literal;
            }
            else{
                SimpleName simpleName = ast.newSimpleName(mapPythonKeyWords(id));
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,simpleName);
                simpleName.setPyObject(pyexp);
                return simpleName;
            }

        }
        else if (pyexp instanceof Call){
            MethodInvocation invocation = ast.newMethodInvocation();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,invocation);
            if (((Call) pyexp).getFunc() instanceof Name){
                SimpleName method_name = (SimpleName)mapExpression((expr) ((Call) pyexp).getFunc(), ast, import_nodes,start_char_pos,typeNodes,pyc );
                invocation.setName(method_name);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {  //TODO keyword arguments are not parsed by the JPyParser, Thus is a Bug
                    Expression expression = mapExpression((expr) arg, ast, import_nodes, start_char_pos + 1 + method_name.toString().length(), typeNodes, pyc);
                    if (expression instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression instanceof PyTupleExpression && ((PyTupleExpression) expression).expressions().size()==0){
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression);
                    }
                    else if (expression instanceof ParenthesizedExpression){
                        ASTNode node = ASTNode.copySubtree(ast, ((ParenthesizedExpression) expression).getExpression());
                        ((ParenthesizedExpression) expression).getExpression().subtreeMatch(new PyASTMatcher(), node);
                        invocation.arguments().add(node);
                    }
                    else{
                        invocation.arguments().add(expression);
                    }

                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    Expression expression = mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,start_char_pos+1+method_name.toString().length(),typeNodes, pyc);

                    if (expression instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression instanceof ParenthesizedExpression){
                        ASTNode expression1 =ASTNode.copySubtree(ast,((ParenthesizedExpression) expression).getExpression());
                        ((ParenthesizedExpression) expression).getExpression().subtreeMatch(new PyASTMatcher(),expression1);
                        invocation.arguments().add(expression1);
                    }
                    else if (expression instanceof PyTupleExpression && ((PyTupleExpression) expression).expressions().size()==0){
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression);
                    }
                    else{
                        invocation.arguments().add(expression);
                    }
                    logger.debug("Key ward is neglected : "+((keyword) karg).getArg());
                }
                invocation.setPyObject(pyexp);
                return invocation;
            }
            else if (((Call) pyexp).getFunc() instanceof Attribute){
                SimpleName method_name = ast.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords (((Attribute)((Call) pyexp).getFunc()).getAttr().toString()));
                MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) ((Call) pyexp).getFunc(),method_name);
                Expression expre;
                if (((Attribute)((Call) pyexp).getFunc()).getValue() instanceof Name && import_nodes.get(((Name)((Attribute)((Call) pyexp).getFunc()).getValue()).getId().toString())!=null){
                    org.eclipse.jdt.core.dom.Name name = import_nodes.get(((Name) ((Attribute) ((Call) pyexp).getFunc()).getValue()).getId().toString());
                    if (name.getFullyQualifiedName().contains("."))
                    {
//                        mod mod = PyASTParser.parsePython(name.getFullyQualifiedName());
//                        expre =  mapExpression((expr) mod.getChild(0).getChild(0),ast,import_nodes,0);
                        expre = ast.newName(name.getFullyQualifiedName().split("\\."));
                        MapPyStatementsTOJDK.updatePythonLineNumbers(((Name) ((Attribute) ((Call) pyexp).getFunc()).getValue()).getCharStartIndex(), name.getFullyQualifiedName().length(),((Name) ((Attribute) ((Call) pyexp).getFunc()).getValue()).getLineno() ,expre);

                    }
                    else{
                        SimpleName simpleName = ast.newSimpleName(name.getFullyQualifiedName());
                        MapPyStatementsTOJDK.updatePythonLineNumbers(((Name) ((Attribute) ((Call) pyexp).getFunc()).getValue()).getCharStartIndex(), name.getFullyQualifiedName().length() , ((Name) ((Attribute) ((Call) pyexp).getFunc()).getValue()).getLineno() ,simpleName);
                        expre = simpleName;
                    }
                }
                else
                {
                    expre = mapExpression( (expr) ((Attribute)((Call) pyexp).getFunc()).getValue(),ast, import_nodes,0,typeNodes, pyc);
                }

                method_name.setSourceRange(pyexp.getCharStartIndex()+expre.toString().length()+PyMap.totalCharGains,method_name.toString().length());

                invocation.setName(method_name);
                invocation.setExpression(expre);
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    Expression expression = mapExpression((expr) arg, ast, import_nodes, start_char_pos + 1 + method_name.toString().length(), typeNodes, pyc);
                    if (expression instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression instanceof PyTupleExpression && ((PyTupleExpression) expression).expressions().size()==0){
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression);
                    }
                    else if (expression instanceof ParenthesizedExpression){
                        ASTNode node = ASTNode.copySubtree(ast, ((ParenthesizedExpression) expression).getExpression());
                        ((ParenthesizedExpression) expression).getExpression().subtreeMatch(new PyASTMatcher(),node);
                        invocation.arguments().add(node);
                    }
                    else{
                        invocation.arguments().add(expression);
                    }
                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    Expression expression = mapExpression((expr) ((keyword) karg).getValue(), ast, import_nodes, start_char_pos + 1 + method_name.toString().length(), typeNodes, pyc);
                    if (expression instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression instanceof PyTupleExpression && ((PyTupleExpression) expression).expressions().size()==0){
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression);
                    }
                    else{
                        invocation.arguments().add(expression);
                    }
                }
                invocation.setSourceRange(start_char_pos,invocation.toString().length());
                return invocation;
            }
            else if (((Call) pyexp).getFunc() instanceof Call){
//                out = memory.cache(tree_builder)(X, connectivity=connectivity,
//                        n_clusters=n_clusters,
//                        return_distance=return_distance,
//                                         **kwargs)
                // need to be changed the arguments
                Expression expression =  mapExpression((expr) ((Call) ((Call) pyexp).getFunc()).getFunc(), ast, import_nodes, 0, typeNodes, pyc);
                if (expression instanceof QualifiedName){
                    SimpleName name = (SimpleName) ASTNode.copySubtree(ast, ((QualifiedName) expression).getName());
                    ((QualifiedName) expression).getName().subtreeMatch(new PyASTMatcher(),name);
                    invocation.setName(name);
                    Expression expression1 = (Expression) ASTNode.copySubtree(ast, ((QualifiedName) expression).getQualifier());
                    ((QualifiedName) expression).getQualifier().subtreeMatch(new PyASTMatcher(),expression1);
                    invocation.setExpression(expression1);
                }
                else if (expression instanceof MethodInvocation){
                    SimpleName simpleName = (SimpleName) ASTNode.copySubtree(ast, ((MethodInvocation) expression).getName());
                    ((MethodInvocation) expression).getName().subtreeMatch(new PyASTMatcher(),simpleName);
                    invocation.setName(simpleName);

                }
                else if (expression instanceof FieldAccess){
                    SimpleName name = (SimpleName) ASTNode.copySubtree(ast, ((FieldAccess) expression).getName());
                    ((FieldAccess) expression).getName().subtreeMatch(new PyASTMatcher(),name);
                    invocation.setName(name);
                    Expression expression1 = (Expression) ASTNode.copySubtree(ast, ((FieldAccess) expression).getExpression());
                    ((FieldAccess) expression).getExpression().subtreeMatch(new PyASTMatcher(),expression1);
                    invocation.setExpression(expression1);
                }
                else  {
                    invocation.setName((SimpleName) expression);
                }


//                invocation.setName(expression);

                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {  //TODO keyword arguments are not parsed by the JPyParser, Thus is a Bug
                    Expression expression1 = mapExpression((expr) arg, ast, import_nodes, 0, typeNodes, pyc);
                    if (expression1 instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression1 instanceof PyTupleExpression && ((PyTupleExpression) expression1).expressions().size()==0){
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression1);
                    }
                    else{
                        invocation.arguments().add(expression1);
                    }

                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    Expression expression1 = mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,0,typeNodes, pyc);
                    if (expression1 instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression1 instanceof PyTupleExpression && ((PyTupleExpression) expression1).expressions().size()==0){
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression1);
                    }
                    else{
                        invocation.arguments().add(expression1);
                    }
                    logger.debug("Key ward is neglected : "+((keyword) karg).getArg());
                }

                return invocation;
            }
            if (((Call) pyexp).getFunc() instanceof Subscript){
                Expression expression = mapExpression((expr) ((Call) pyexp).getFunc(), ast, import_nodes, 0, typeNodes, pyc);
                invocation.setExpression(expression);
                invocation.setName(ast.newSimpleName("f"));
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {
                    Expression expression1 = mapExpression((expr) arg, ast, import_nodes, 0, typeNodes, pyc);
                    if (expression1 instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression1 instanceof PyTupleExpression && ((PyTupleExpression) expression1).expressions().size()==0){
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression1);
                    }
                    else{
                        invocation.arguments().add(expression1);
                    }

                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    Expression expression1 = mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,0,typeNodes, pyc);
                    if (expression1 instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression1 instanceof PyTupleExpression && ((PyTupleExpression) expression1).expressions().size()==0){
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression1);
                    }
                    else{
                        invocation.arguments().add(expression1);
                    }
                    logger.debug("Key ward is neglected : "+((keyword) karg).getArg());
                }
                return invocation;


            }
            else if (((Call) pyexp).getFunc() instanceof Lambda){
                Expression expression = mapExpression((expr) ((Call) pyexp).getFunc(), ast, import_nodes, 0, typeNodes, pyc);
                ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
                MapPyStatementsTOJDK.updatePythonLineNumbers((expr) ((Call) pyexp).getFunc(),parenthesizedExpression);
                parenthesizedExpression.setExpression(expression);
                invocation.setExpression(parenthesizedExpression);
                invocation.setName(ast.newSimpleName("f"));
                for (Object arg : (AstList) ((Call) pyexp).getArgs()) {  //TODO keyword arguments are not parsed by the JPyParser, Thus is a Bug
                    Expression expression1 = mapExpression((expr) arg, ast, import_nodes, 0, typeNodes, pyc);
                    if (expression1 instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression1 instanceof PyTupleExpression && ((PyTupleExpression) expression1).expressions().size()==0){
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression1);
                    }
                    else{
                        invocation.arguments().add(expression1);
                    }

                }
                for (Object karg: (AstList) ((Call) pyexp).getKeywords()){
                    Expression expression1 = mapExpression((expr) ((keyword) karg).getValue(),ast, import_nodes,0,typeNodes, pyc);
                    if (expression1 instanceof PyErrorExpression){
                        continue;
//                        return ast.newPyErrorExpression();
                    }
                    else if (expression1 instanceof PyTupleExpression && ((PyTupleExpression) expression1).expressions().size()==0){
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression1).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        invocation.arguments().add(expression1);
                    }
                    else{
                        invocation.arguments().add(expression1);
                    }
                }
                return invocation;
            }
            else if (((Call) pyexp).getFunc() instanceof BinOp){
                invocation.setName(ast.newSimpleName("PyCpatDummy"));
                return invocation;
            }
            else if (((Call) pyexp).getFunc() instanceof BoolOp){
                invocation.setName(ast.newSimpleName("PyCpatDummy"));
                return invocation;
            }
            else{
                throw new NodeNotFoundException("Node not found for Call Object");
            }
        }
        else if (pyexp instanceof Subscript){
            ArrayAccess arrayAccess = ast.newArrayAccess();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,arrayAccess);
            if (((Subscript) pyexp).getSlice() instanceof ExtSlice) {
                PyTupleExpression tupleExpression = ast.newPyTupleExpression();
                if (((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().size()>0){
                    MapPyStatementsTOJDK.updatePythonLineNumbers(((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().get(0).getCharStartIndex(),
                            ((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().get(((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().size()-1).getCharStopIndex()
                            - ((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().get(0).getCharStartIndex(),
                            ((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().get(0).getLine(),
                            tupleExpression);
                }

                for (slice internalDim : ((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims()) {
                    if (internalDim instanceof Slice) {
                        StringLiteral stringLiteral = ast.newStringLiteral();
                        MapPyStatementsTOJDK.updatePythonLineNumbers(internalDim,stringLiteral);
                        String lower = ((Slice)internalDim).getInternalLower()==null ?
                                "":
                                mapExpression(((Slice)internalDim).getInternalLower(),ast, import_nodes,0,typeNodes, pyc).toString();
                        String step = ((Slice)internalDim).getInternalStep()==null? "":
                                mapExpression(((Slice)internalDim).getInternalStep(),ast, import_nodes,0,typeNodes, pyc).toString();
                        String upper = ((Slice)internalDim).getInternalUpper()==null? "":
                                mapExpression(((Slice)internalDim).getInternalUpper(),ast, import_nodes,0,typeNodes, pyc).toString();
                        stringLiteral.setLiteralValue(lower+"_PyCpatDummy_"+step+"_"+upper);
                        tupleExpression.expressions().add(stringLiteral);
                    } else {
                        Expression expression = mapExpression((expr) ((Index) internalDim).getValue(), ast, import_nodes, 0, typeNodes, pyc);
                        if (expression instanceof PyTupleExpression && ((PyTupleExpression) expression).expressions().size()==0){
                            ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                            ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        }
                        tupleExpression.expressions().add(expression);
                    }
                }
                arrayAccess.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes,0,typeNodes,pyc  ));
                arrayAccess.setIndex(tupleExpression);
                return arrayAccess;
//                for (int j = 0;j<slice_length;j++) {
//
//
//                    slice slice = ((ExtSlice) ((Subscript) pyexp).getSlice()).getInternalDims().get(j);
//                    if (arrayAccess1==null){
//                        arrayAccess1=ast.newArrayAccess();
//                    }
//                    else{
//                        ArrayAccess arrayAccess2 = ast.newArrayAccess();
//                        arrayAccess2.setArray(arrayAccess1);
//                        arrayAccess1=arrayAccess2;
//                    }
//                    if (slice instanceof Slice){
//                        StringLiteral stringLiteral = ast.newStringLiteral();
//                        stringLiteral.setLiteralValue("PythonSlice");
//                        arrayAccess1.setIndex(stringLiteral);
//                    }
//                    else if (slice instanceof Index){
//                        arrayAccess1.setIndex(mapExpression((expr) ((Index)slice).getValue() ,ast, import_nodes,0,typeNodes, pyc));
//                    }
//                    if (j==0){
//                        arrayAccess1.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes,0,typeNodes,pyc  ));
//                    }
//            }

//                Object o = ((AstList) ((ExtSlice) ((Subscript) pyexp).getSlice()).getDims()).get(slice_length - 1);
//                if (o instanceof Slice){
//                    StringLiteral stringLiteral = ast.newStringLiteral();
//                    stringLiteral.setLiteralValue("PythonSlice");
//                    arrayAccess1.setIndex(stringLiteral);
//                }
//                else if (o instanceof Index){
//                    arrayAccess1.setIndex(mapExpression((expr) ((Index)o).getValue() ,ast, import_nodes,0,typeNodes, pyc));
//                }
//

            }
            else{
                arrayAccess.setArray(mapExpression((expr)((Subscript) pyexp).getValue(),ast, import_nodes,0,typeNodes,pyc  ));
                if (((Subscript) pyexp).getSlice() instanceof Slice){
                    StringLiteral stringLiteral = ast.newStringLiteral();
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) ((Subscript) pyexp).getSlice(),stringLiteral);
                    String lower = ((Slice)((Subscript) pyexp).getSlice()).getInternalLower()==null ?
                            "":
                            mapExpression(((Slice)((Subscript) pyexp).getSlice()).getInternalLower(),ast, import_nodes,0,typeNodes, pyc).toString();
                    String step = ((Slice)((Subscript) pyexp).getSlice()).getInternalStep()==null? "":
                            mapExpression(((Slice)((Subscript) pyexp).getSlice()).getInternalStep(),ast, import_nodes,0,typeNodes, pyc).toString();
                    String upper = ((Slice)((Subscript) pyexp).getSlice()).getInternalUpper()==null? "":
                            mapExpression(((Slice)((Subscript) pyexp).getSlice()).getInternalUpper(),ast, import_nodes,0,typeNodes, pyc).toString();
                    stringLiteral.setLiteralValue(lower+"_PyCpatDummy_"+step+"_"+upper);
                    arrayAccess.setIndex(stringLiteral);
                }
                else{
                    Expression expression = mapExpression((expr) ((Index)((Subscript) pyexp).getSlice()).getValue(),ast, import_nodes,0,typeNodes, pyc);
                    if (expression instanceof PyTupleExpression && ((PyTupleExpression) expression).expressions().size()==0){
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                        ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                    }
                    arrayAccess.setIndex(expression);
                }

            }


            arrayAccess.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,pyexp.getCharStopIndex()-pyexp.getCharStartIndex());
            return arrayAccess;
        }
        else if (pyexp instanceof List){
            ArrayInitializer arrayInitializer = ast.newArrayInitializer();
            ((AstList) ((List) pyexp).getElts()).stream().filter(x->((expr)x).getParent()==null).forEach(x->((expr)x).setParent(pyexp));
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,arrayInitializer);
            for (Object elt : (AstList) ((List) pyexp).getElts()) {
                Expression expression = mapExpression((expr) elt,ast, import_nodes,0,typeNodes,pyc);
                if (expression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                else if (expression instanceof PyTupleExpression && ((PyTupleExpression) expression).expressions().size()==0){
                    ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                    ((PyTupleExpression) expression).expressions().add(ast.newSimpleName("PyCpatDummy"));
                    arrayInitializer.expressions().add(expression);
                }
                else{
                    arrayInitializer.expressions().add(expression);
                }
            }


            if (!(pyexp.getParent() instanceof List)){
                ArrayCreation arrayCreation = ast.newArrayCreation();
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,arrayCreation);
                Type type = TypeApproximator.getSimpleTypeApproximation(ast, pyexp);
                if (type!=null){
                    arrayCreation.setType((ArrayType) type);
                }
                else{
                    if (pyexp.getParent() instanceof Assign){
                        if (((AstList)((Assign)pyexp.getParent()).getTargets()).size()>1){
                            logger.fatal("have to handle more than one assignments");
                        }
                        else if (((AstList)((Assign)pyexp.getParent()).getTargets()).get(0) instanceof Attribute){
                            Object o = ((AstList) ((Assign) pyexp.getParent()).getTargets()).get(0);
                            String typeString = typeNodes.get(new TypeASTNode(((Attribute) o).getLineno(),
                                    ((Attribute) o).getCol_offset()+((Attribute) o).getValue().toString().length()+1, ((Attribute) o).getAttr().toString(), null));

                            updateArrayTpe(ast, arrayCreation, typeString,typeNodes);
                        }
                        else if (((AstList)((Assign)pyexp.getParent()).getTargets()).get(0) instanceof Name){
                            String typeString = typeNodes.get(new TypeASTNode(((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getLineno(),
                                    ((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getCol_offset(), ((Name) ((AstList)((Assign)pyexp.getParent()).getTargets()).get(0)).getInternalId(), null));
                            updateArrayTpe(ast, arrayCreation, typeString,typeNodes);
                        }
                        else
                        {
                            Type jdtType1 = TypeStringToJDT.getJDTType(ast, "List[Any]", 0);
                            arrayCreation.setType((ArrayType) jdtType1);
                            logger.fatal("Created any type");
                            logger.fatal("Assigned target is not an instance of Name");
                        }
                    }//TODO write a component that can infer types of the arrays
                }
                arrayCreation.setInitializer(arrayInitializer);
                return arrayCreation;
            }
            else
                return arrayInitializer;

        }
        else if (pyexp instanceof Attribute){
            Expression expression;

            if (((Attribute) pyexp).getValue() instanceof Name && import_nodes.get(((Name)(((Attribute) pyexp).getValue())).getId().toString())!=null)
            {
                org.eclipse.jdt.core.dom.Name name = import_nodes.get(((Name) (((Attribute) pyexp).getValue())).getId().toString());
                if (name.getFullyQualifiedName().contains("."))
                {
                    mod mod = PyASTParser.parsePython(name.getFullyQualifiedName());
                    expression = mapExpression((expr) mod.getChild(0).getChild(0),ast,import_nodes,0,typeNodes,pyc  );

                }
                else{
                    SimpleName simpleName = ast.newSimpleName(name.getFullyQualifiedName());
                    MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp ,simpleName);
                    simpleName.setSourceRange(name.getStartPosition()+PyMap.totalCharGains,name.getLength());
                    expression = simpleName;
                }
            }
            else{
                expression = mapExpression((expr) ((Attribute) pyexp).getValue(), ast, import_nodes,0,typeNodes,pyc  );
            }

            SimpleName simpleName = ast.newSimpleName(mapPythonKeyWords(((Attribute) pyexp).getAttr().toString()));
            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) ((Attribute) pyexp),simpleName);
            simpleName.setSourceRange(pyexp.getCharStartIndex()+PyMap.totalCharGains,((Attribute) pyexp).getAttr().toString().length());
            if (expression instanceof ArrayAccess || expression instanceof FieldAccess || expression instanceof MethodInvocation
                    || expression instanceof  ParenthesizedExpression || expression instanceof StringLiteral ||
                    expression instanceof PyTupleExpression || expression instanceof PrefixExpression){
                FieldAccess fieldAccess = ast.newFieldAccess();
                MapPyStatementsTOJDK.updatePythonLineNumbers( pyexp,fieldAccess);
                fieldAccess.setExpression(expression);
                fieldAccess.setName(simpleName);
                return fieldAccess;
            }
            else{
                    QualifiedName qualifiedName = ast.newQualifiedName((org.eclipse.jdt.core.dom.Name) expression, simpleName);
                    MapPyStatementsTOJDK.updatePythonLineNumbers(((Attribute) pyexp),qualifiedName);
                    qualifiedName.setSourceRange(pyexp.getCharStartIndex() + PyMap.totalCharGains, pyexp.getCharStopIndex() - pyexp.getCharStartIndex());
                    return qualifiedName;
            }
        }
        else if (pyexp instanceof Str){
            StringLiteral stringLiteral = ast.newStringLiteral();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,stringLiteral);
            stringLiteral.setLiteralValue(String.valueOf(((Str) pyexp).getInternalS()));
            return stringLiteral;
        }
        else if (pyexp instanceof BinOp){

            InfixExpression infixExpression = ast.newInfixExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,infixExpression);
            Expression leftExpression =  mapExpression(((BinOp) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
            Expression rightExpression = mapExpression(((BinOp) pyexp).getInternalRight(),ast,import_nodes,0,typeNodes,pyc  );
            infixExpression.setLeftOperand(leftExpression);
            infixExpression.setRightOperand(rightExpression);
            if (rightExpression instanceof PyErrorExpression || leftExpression instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }

            if (((BinOp) pyexp).getInternalOp()== operatorType.Mult){
                infixExpression.setOperator(InfixExpression.Operator.TIMES);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Add){
                infixExpression.setOperator(InfixExpression.Operator.PLUS);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Sub) {
                infixExpression.setOperator(InfixExpression.Operator.MINUS);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.MatMult) {
                logger.error("Undefined MatMult operation found");
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Div) {
                infixExpression.setOperator(InfixExpression.Operator.DIVIDE);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Mod) {
                infixExpression.setOperator(InfixExpression.Operator.REMAINDER);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.Pow) {
                infixExpression.setOperator(InfixExpression.Operator.POW);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.LShift) {
                infixExpression.setOperator(InfixExpression.Operator.LEFT_SHIFT);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.RShift) {
                infixExpression.setOperator(InfixExpression.Operator.RIGHT_SHIFT_SIGNED);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.BitOr) {
                infixExpression.setOperator(InfixExpression.Operator.OR);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.BitXor) {
                infixExpression.setOperator(InfixExpression.Operator.XOR);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.BitAnd) {
                infixExpression.setOperator(InfixExpression.Operator.AND);
            }
            else if (((BinOp) pyexp).getInternalOp()== operatorType.FloorDiv) { //TODO make this InfixExpression.Operator.FLOORDIV
                infixExpression.setOperator(InfixExpression.Operator.DIVIDE);
            }
            ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,parenthesizedExpression);
            parenthesizedExpression.setExpression(infixExpression);
            return  parenthesizedExpression;
        }
        else if (pyexp instanceof IfExp){
            ConditionalExpression conditionalExpression = ast.newConditionalExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,conditionalExpression);
            Expression thenExpression = mapExpression(((IfExp) pyexp).getInternalBody(), ast, import_nodes, 0, typeNodes, pyc);
            Expression elseExpression = mapExpression(((IfExp) pyexp).getInternalOrelse(), ast, import_nodes, 0, typeNodes, pyc);
            Expression expression = mapExpression(((IfExp) pyexp).getInternalTest(), ast, import_nodes, 0, typeNodes, pyc);
            if (!(thenExpression instanceof PyErrorExpression || elseExpression instanceof PyErrorExpression || expression instanceof PyErrorExpression)){
                conditionalExpression.setThenExpression(thenExpression);
                conditionalExpression.setElseExpression(elseExpression);
                conditionalExpression.setExpression(expression);

            }else {
                return ast.newPyErrorExpression();
            }
            ParenthesizedExpression paraExpression = ast.newParenthesizedExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,paraExpression);
            paraExpression.setExpression(conditionalExpression);
            return paraExpression;
        }
        else if (pyexp instanceof Compare){
             AstList comparators = (AstList) ((Compare) pyexp).getComparators();
            if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.In)){
                PyInExpression pyInExpression = ast.newPyInExpression();
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,pyInExpression);
                Expression leftExpression =  mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
                Expression rightExpression = mapExpression((expr) (comparators).get(0), ast, import_nodes, 0, typeNodes, pyc);
                if (leftExpression instanceof PyErrorExpression || rightExpression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                pyInExpression.setLeftOperand(leftExpression);
                pyInExpression.setRightOperand(rightExpression);
                ParenthesizedExpression para = ast.newParenthesizedExpression();
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,para);
                para.setExpression(pyInExpression);
                return para;
            }
            else if (((AstList)((Compare) pyexp).getOps()).get(0).equals(cmpopType.NotIn)){
                PyNotInExpression pyNotInExpression = ast.newPyNotInExpression();
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,pyNotInExpression);
                Expression leftExpression = mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
                Expression rightExpression = mapExpression((expr) (comparators).get(0),ast,import_nodes,0,typeNodes,pyc  );
                if (leftExpression instanceof PyErrorExpression || rightExpression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                pyNotInExpression.setLeftOperand(leftExpression);
                pyNotInExpression.setRightOperand(rightExpression);
                ParenthesizedExpression para = ast.newParenthesizedExpression();
                MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,para);
                para.setExpression(pyNotInExpression);
                return para;
            }
            InfixExpression infixExpression = ast.newInfixExpression();
            Expression leftExpression = mapExpression(((Compare) pyexp).getInternalLeft(),ast,import_nodes,0,typeNodes,pyc  );
            if (leftExpression instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }


            InfixExpression expression0 = null;
            ArrayList<InfixExpression> compa  = new ArrayList<>();
            if ((comparators).size()>1){
                for (int y = 0; y < comparators.size(); y++ ){
                    if (y==0){
                        InfixExpression expression = ast.newInfixExpression();
                        Expression rightExpression1 =mapExpression((expr) comparators.get(y),ast,import_nodes,0,typeNodes,pyc);
                        if (leftExpression instanceof PyInExpression){
                            ParenthesizedExpression para = ast.newParenthesizedExpression();
                            para.setExpression(leftExpression);
                            expression.setLeftOperand(para);
                        }
                        else{
                            expression.setLeftOperand(leftExpression);
                        }
                        if (leftExpression instanceof PyInExpression){
                            ParenthesizedExpression para = ast.newParenthesizedExpression();
                            para.setExpression(rightExpression1);
                            expression.setRightOperand(para);
                        }
                        else{
                            expression.setRightOperand(rightExpression1);
                        }
                        MapPyStatementsTOJDK.updatePythonLineNumbers(((Compare) pyexp).getInternalLeft().getCharStartIndex(), ((expr) comparators.get(y)).getCharStopIndex(),
                                ((expr) comparators.get(y)).getLine()
                                 ,expression);
                        expression.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(y)));
                        compa.add(expression);
                    }
                    else{
                        InfixExpression expression = ast.newInfixExpression();
                        Expression leftExpression1 =mapExpression((expr) comparators.get(y-1),ast,import_nodes,0,typeNodes,pyc);
                        Expression rightExpression1 =mapExpression((expr) comparators.get(y),ast,import_nodes,0,typeNodes,pyc);
                        MapPyStatementsTOJDK.updatePythonLineNumbers(((expr) comparators.get(y-1)).getCharStartIndex(), ((expr) comparators.get(y-1)).getCharStopIndex() ,
                                ((expr) comparators.get(y-1)).getLine(),expression);

                        expression.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(y)));
                        if (leftExpression instanceof PyInExpression){
                            ParenthesizedExpression para = ast.newParenthesizedExpression();
                            para.setExpression(leftExpression1);
                            MapPyStatementsTOJDK.updatePythonLineNumbers( ((Compare) pyexp).getInternalLeft().getCharStartIndex() , para.toString().length() ,
                                    ((Compare) pyexp).getInternalLeft().getLine() ,para);
                            expression.setLeftOperand(para);
                        }
                        else{
                            expression.setLeftOperand(leftExpression1);
                        }
                        if (leftExpression instanceof PyInExpression){
                            ParenthesizedExpression para = ast.newParenthesizedExpression();
                            MapPyStatementsTOJDK.updatePythonLineNumbers( ((Compare) pyexp).getInternalLeft().getCharStartIndex() , para.toString().length() ,((Compare) pyexp).getInternalLeft().getLine() ,para);
                            para.setExpression(rightExpression1);
                            expression.setRightOperand(para);
                        }
                        else {
                            expression.setRightOperand(rightExpression1);
                        }
                        compa.add(expression);
                    }






//                    if (y==0){
//                        expression0 = ast.newInfixExpression();
//                        Expression leftExpression1 =mapExpression((expr) comparators.get(0),ast,import_nodes,0,typeNodes,pyc);
//                        Expression rightExpression1 =mapExpression((expr) comparators.get(1),ast,import_nodes,0,typeNodes,pyc);
//                        if (leftExpression1 instanceof PyErrorExpression || rightExpression1 instanceof PyErrorExpression){
//                            return ast.newPyErrorExpression();
//                        }
//                        expression0.setLeftOperand(leftExpression1);
//                        expression0.setRightOperand(rightExpression1);
//                        expression0.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(y)));
//                    }
//                    else if (y>1){
//                        InfixExpression expression = ast.newInfixExpression();
//                        Expression leftExpression1= mapExpression((expr) (comparators).get(y),ast,import_nodes,0,typeNodes,pyc  );
//                        if (leftExpression1 instanceof PyErrorExpression){
//                            return ast.newPyErrorExpression();
//                        }
//                        expression.setLeftOperand(leftExpression1);
//                        expression.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(y)));
//                        expression.setRightOperand(expression0);
//                        expression0=expression;
//                    }
                }
                InfixExpression firstExpression = ast.newInfixExpression();
                firstExpression.setLeftOperand(compa.get(0));
                firstExpression.setRightOperand(compa.get(1));
                MapPyStatementsTOJDK.updatePythonLineNumbers( compa.get(0).getPyStartPosition() , firstExpression.toString().length() , compa.get(0).getPyLine() ,firstExpression);
                firstExpression.setOperator(CONDITIONAL_AND);
                for (int y = 2; y < compa.size(); y++ ){
                    InfixExpression expression = ast.newInfixExpression();
                    expression.setOperator(CONDITIONAL_AND);
                    expression.setLeftOperand(firstExpression);
                    expression.setRightOperand(compa.get(y));
                    MapPyStatementsTOJDK.updatePythonLineNumbers( compa.get(0).getPyStartPosition() , expression.toString().length(), compa.get(0).getPyLine() ,expression);
                    firstExpression = expression;
                }
                ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
                parenthesizedExpression.setExpression(firstExpression);

                return parenthesizedExpression;
            }
            infixExpression.setLeftOperand(leftExpression);
            MapPyStatementsTOJDK.updatePythonLineNumbers( ((Compare) pyexp).getInternalLeft().getCharStartIndex() , infixExpression.toString().length() , ((Compare) pyexp).getInternalLeft().getLine() ,infixExpression);
            infixExpression.setOperator(setOperator(((Compare) pyexp).getInternalOps().get(0)));
            if (comparators.size()>1){
                infixExpression.setRightOperand(expression0);
            }
            else if (comparators.size()==0){
                infixExpression.setRightOperand(ast.newSimpleName("PyCpatDummy"));
            }
            else{
                Expression rightExpression =  mapExpression((expr) (comparators).get(0),ast,import_nodes,0,typeNodes,pyc  );
                if (rightExpression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                infixExpression.setRightOperand(rightExpression);
            }
            ParenthesizedExpression parenthesizedExpression = ast.newParenthesizedExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers( ((Compare) pyexp).getInternalLeft().getCharStartIndex() , infixExpression.toString().length(),((Compare) pyexp).getInternalLeft().getLine()  ,parenthesizedExpression);
            parenthesizedExpression.setExpression(infixExpression);

            return parenthesizedExpression;
        }
        else if (pyexp instanceof BoolOp){
            ParenthesizedExpression paraexpression = ast.newParenthesizedExpression();
            InfixExpression infixExpression = ast.newInfixExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,paraexpression);
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,infixExpression);
            int values = ((AstList)((BoolOp) pyexp).getValues()).size();



            if (((BoolOp) pyexp).getInternalValues().size()==0){
                infixExpression.setLeftOperand(ast.newSimpleName("PyCpatDummy"));
                infixExpression.setRightOperand(ast.newSimpleName("PyCpatDummy"));
                updateOperation((BoolOp) pyexp, infixExpression);
                return infixExpression;
            }

            Expression rightExpression = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(values-1),ast,import_nodes,0,typeNodes,pyc  );
            if (rightExpression instanceof PyErrorExpression){
                ast.newPyErrorExpression();
            }
            infixExpression.setRightOperand(rightExpression);
            InfixExpression temp = infixExpression;
            if (values==2){
                Expression left1Expression = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(0),ast,import_nodes,0,typeNodes,pyc  );
                if (left1Expression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                infixExpression.setLeftOperand(left1Expression);
            }
            else{
                for (int i = values-2; i >= 0; i--) {
                    if (i==0){
                        Expression leftTempExpression = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i),ast,import_nodes,0,typeNodes,pyc  );
                        if (leftTempExpression instanceof PyErrorExpression){
                            return ast.newPyErrorExpression();
                        }
                        temp.setLeftOperand(leftTempExpression);
                    }
                    else{
                        InfixExpression leftinfixExpression = ast.newInfixExpression();
                        Expression rightOperand = mapExpression ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i),ast,import_nodes,0,typeNodes,pyc  );
                        if (rightOperand instanceof PyErrorExpression){
                            return ast.newPyErrorExpression();
                        }
                        leftinfixExpression.setRightOperand(rightOperand);
                        if (((BoolOp) pyexp).getInternalOp().equals(boolopType.And)){
                            leftinfixExpression.setOperator(CONDITIONAL_AND);
                        }
                        else if (((BoolOp) pyexp).getInternalOp().equals(boolopType.Or)){
                            leftinfixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
                        }
                        else {
                            assert false;
                            logger.fatal("Unmapped operator found "+ ((BoolOp) pyexp).getOp());
                        }
                        MapPyStatementsTOJDK.updatePythonLineNumbers( ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i)).getCharStartIndex(),leftinfixExpression.toString().length(),
                                ((expr) ((AstList)((BoolOp) pyexp).getValues()).get(i)).getLine()  ,leftinfixExpression);
                        temp.setLeftOperand(leftinfixExpression);

                        temp=leftinfixExpression;
                    }
                }
            }
            updateOperation((BoolOp) pyexp, infixExpression);

            paraexpression.setExpression(infixExpression);
            return paraexpression;
        }
        else if (pyexp instanceof UnaryOp){
            PrefixExpression prefixExpression = ast.newPrefixExpression();

            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,prefixExpression);

            Expression operand = mapExpression(((UnaryOp) pyexp).getInternalOperand(),ast,import_nodes,0,typeNodes,pyc  );
            if (operand instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }
            prefixExpression.setOperand(operand);
            if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.Not)){
                prefixExpression.setOperator(PrefixExpression.Operator.NOT);
            }
            else if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.USub)){
                prefixExpression.setOperator(PrefixExpression.Operator.MINUS);
            }
            else if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.UAdd)){
                prefixExpression.setOperator(PrefixExpression.Operator.PLUS);
            }
            else if (((UnaryOp) pyexp).getInternalOp().equals(unaryopType.Invert)){
                prefixExpression.setOperator(PrefixExpression.Operator.COMPLEMENT);
            }
            else{
                logger.fatal("Unhandle Unary operator");
            }
            return prefixExpression;
        }
        else if (pyexp instanceof Starred){
            Expression starsExpression = mapExpression((expr) ((Starred) pyexp).getValue(),ast,import_nodes,0,typeNodes,pyc  );
            if (starsExpression instanceof PyErrorExpression){
                return ast.newPyErrorExpression();
            }
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,starsExpression);
            return starsExpression; //TODO handle stars, for now we neglect the stars
        }
        else if (pyexp instanceof Tuple){
            PyTupleExpression pyTupleExpression = ast.newPyTupleExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,pyTupleExpression);
            for (int i = ((Tuple) pyexp).getInternalElts().size()-1;i>=0;i--){
                Expression  expression = mapExpression(((Tuple) pyexp).getInternalElts().get(i),ast,import_nodes,0,typeNodes,pyc  );
                if (expression instanceof PyErrorExpression){
                    return ast.newPyErrorExpression();
                }
                pyTupleExpression.expressions().add(expression);
            }




//            for (expr elt : ((Tuple) pyexp).getInternalElts()) {
//                Expression  expression = mapExpression(elt,ast,import_nodes,0,typeNodes,pyc  );
//                if (expression instanceof PyErrorExpression){
//                    return ast.newPyErrorExpression();
//                }
//                pyTupleExpression.expressions().add(expression);
//            }
            if (((Tuple) pyexp).getInternalElts().size()==1){
                SimpleName dummy = ast.newSimpleName("PyCpatDummy");
                pyTupleExpression.expressions().add(dummy);
            }
            else if (((Tuple) pyexp).getInternalElts().size()==0){
                pyTupleExpression.expressions().add(ast.newSimpleName("PyCpatDummy"));
                pyTupleExpression.expressions().add(ast.newSimpleName("PyCpatDummy"));
            }
            return pyTupleExpression;

        }
        else if (pyexp instanceof ListComp){
            PyListComprehension pyListComp = ast.newPyListComprehension();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,pyListComp);
            Expression target = mapExpression((expr) ((ListComp) pyexp).getElt(),ast,import_nodes,0,typeNodes,pyc  );
            if (target instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            pyListComp.setTargetExpression(target);
            int gen_number = 0;
            for (comprehension generator : ((ListComp) pyexp).getInternalGenerators()) {
                gen_number++;
                PyComparator comparator = ast.newPyComarator();
                MapPyStatementsTOJDK.updatePythonLineNumbers(generator,comparator);
                Expression iterator = mapExpression(generator.getInternalIter(), ast, import_nodes, 0, typeNodes, pyc);
                if (iterator instanceof PyErrorExpression){return  ast.newPyErrorExpression();}
                comparator.setIteratorExpression(iterator);
                if (generator.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    comparator.getValueExpression().add(parameter_dummy);
                    for (Object elt : (AstList) ((Tuple) generator.getTarget()).getElts()) {
                        if (elt instanceof Tuple){
                            for (expr internalElt : ((Tuple) elt).getInternalElts()) {
                                if(internalElt instanceof Tuple){
                                    for (expr elt1 : ((Tuple) internalElt).getInternalElts()) {
                                        SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                                        SimpleName name = ast.newSimpleName(((Name) elt1).getInternalId());
                                        MapPyStatementsTOJDK.updatePythonLineNumbers(elt1,name);
                                        MapPyStatementsTOJDK.updatePythonLineNumbers(elt1,lo_parameter);
                                        lo_parameter.setName(name);
                                        String typeString = typeNodes.get(new TypeASTNode(((Name)elt1).getLineno(),
                                                ((Name)elt1).getCol_offset(), ((Name)elt1).getId().toString(), null));
                                        Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                                        lo_parameter.setType(jdtType);
                                        comparator.getValueExpression().add(lo_parameter);
                                    }
                                }
                                else {
                                    SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                                    SimpleName name = ast.newSimpleName(((Name) internalElt).getInternalId());
                                    lo_parameter.setName(name);

                                    MapPyStatementsTOJDK.updatePythonLineNumbers(internalElt,name);
                                    MapPyStatementsTOJDK.updatePythonLineNumbers(internalElt,lo_parameter);

                                    String typeString = typeNodes.get(new TypeASTNode(((Name)internalElt).getLineno(),
                                            ((Name)internalElt).getCol_offset(), ((Name)internalElt).getId().toString(), null));
                                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                                    lo_parameter.setType(jdtType);
                                    comparator.getValueExpression().add(lo_parameter);
                                }
                            }
                        }
                        else{
                            SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                            SimpleName name = ast.newSimpleName(mapPythonKeyWords(((Name) elt).getInternalId()));

                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,name);
                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,lo_parameter);

                            lo_parameter.setName(name);
                            String typeString = typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                                    ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));


                            Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                            lo_parameter.setType(jdtType);
                            comparator.getValueExpression().add(lo_parameter);
                        }
                    }
                }
                else if (generator.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    comparator.getValueExpression().add(parameter_dummy);

                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    SimpleName name = ast.newSimpleName(mapPythonKeyWords(((Name) (generator.getTarget())).getInternalId()));

                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),name);
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),parameter);

                    parameter.setName(name);
                    String typeString = typeNodes.get(new TypeASTNode(((Name)(generator.getTarget())).getLineno(),
                            ((Name)generator.getTarget()).getCol_offset(), ((Name)(generator.getTarget())).getId().toString(), null));
                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
                    parameter.setType(jdtType);
                    comparator.getValueExpression().add(parameter);
                }
                else{
                    logger.fatal("unknown generator loop value expression"+generator.getTarget());
                }
                if (generator.getInternalIfs().size()==0){
                    if (((ListComp) pyexp).getInternalGenerators().size()>1 && gen_number==1){
                        SimpleName conditional = ast.newSimpleName("DUMMY_IF");
                        comparator.setConditionalExpression(conditional);
                    }
                    pyListComp.getComparator().add(comparator);
                }
                else {
                    Expression conditional = mapExpression(generator.getInternalIfs().get(0), ast, import_nodes, 0, typeNodes, pyc);
                    comparator.setConditionalExpression(conditional);
                    pyListComp.getComparator().add(comparator);
                }
                if (generator.getInternalIfs().size()>1){
                    logger.fatal("Number of Conditional Ifs in generators are more than one");
                }

            }
            return pyListComp;
        }
        else if(pyexp instanceof GeneratorExp) {
            PyGenerator pyGenerator = ast.newPyGenerator();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,pyGenerator);
            Expression targetExpression = mapExpression((expr) ((GeneratorExp) pyexp).getElt(),ast,import_nodes,0,typeNodes,pyc  );
            if (targetExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            pyGenerator.setTargetExpression(targetExpression);
            for (comprehension generator : ((GeneratorExp) pyexp).getInternalGenerators()) {
                PyComparator comparator = ast.newPyComarator();
                MapPyStatementsTOJDK.updatePythonLineNumbers(generator,comparator);
                Expression iterator = mapExpression(generator.getInternalIter(), ast, import_nodes, 0, typeNodes, pyc);
                if (iterator instanceof PyErrorExpression){return  ast.newPyErrorExpression();}
                comparator.setIteratorExpression(iterator);
                if (generator.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    SimpleName terminalNode = ast.newSimpleName("DummyTerminalNode");
                    parameter_dummy.setName(terminalNode);
                    SimpleType typeNode = ast.newSimpleType(ast.newName("DummyTerminalTypeNode"));
                    parameter_dummy.setType(typeNode);

                    comparator.getValueExpression().add(parameter_dummy);
                    for (Object elt : (AstList) ((Tuple) generator.getTarget()).getElts()) {

                        if (elt instanceof Tuple){
                            for (Object elt1 : (AstList) ((Tuple) elt).getElts()) {
                                if (elt1 instanceof Tuple){
                                    for (Object elt2 : (AstList) ((Tuple) elt1).getElts()) {
                                        SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                                        SimpleName name = ast.newSimpleName(((Name) elt2).getId().toString());
                                        lo_parameter.setName(name);
                                        String typeString = typeNodes.get(new TypeASTNode(((Name) elt2).getLineno(),
                                                ((Name) elt2).getCol_offset(), ((Name) elt2).getId().toString(), null));
                                        Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                                        MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt2,lo_parameter);
                                        MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt2,jdtType);
                                        MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt2,name);

                                        lo_parameter.setType(jdtType);
                                        comparator.getValueExpression().add(lo_parameter);
                                    }
                                }
                                else {

                                    SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                                    SimpleName name = ast.newSimpleName(((Name) elt1).getId().toString());
                                    lo_parameter.setName(name);
                                    String typeString = typeNodes.get(new TypeASTNode(((Name) elt1).getLineno(),
                                            ((Name) elt1).getCol_offset(), ((Name) elt1).getId().toString(), null));
                                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt1,lo_parameter);
                                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt1,jdtType);
                                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt1,name);

                                    lo_parameter.setType(jdtType);
                                    comparator.getValueExpression().add(lo_parameter);
                                }
                            }
                        }
                        else{
                            SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                            SimpleName name = ast.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords     (((Name) elt).getId().toString()));
                            lo_parameter.setName(name);
                            String typeString = typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                                    ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));
                            Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,lo_parameter);
                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,jdtType);
                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,name);

                            lo_parameter.setType(jdtType);
                            comparator.getValueExpression().add(lo_parameter);
                        }

                    }
                }
                else if (generator.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    comparator.getValueExpression().add(parameter_dummy);

                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    SimpleName name = ast.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(((Name) (generator.getTarget())).getInternalId()));

                    String typeString = typeNodes.get(new TypeASTNode(((Name)(generator.getTarget())).getLineno(),
                            ((Name)generator.getTarget()).getCol_offset(), ((Name)(generator.getTarget())).getId().toString(), null));
                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),name);
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),jdtType);
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),parameter_dummy);


                    parameter.setName(name);
                    parameter.setType(jdtType);
                    comparator.getValueExpression().add(parameter);
                }
                else{
                    logger.fatal("unknown generator loop value expression"+generator.getTarget());
                }
                if (generator.getInternalIfs().size()==0){
                    pyGenerator.getComparators().add(comparator);
                    continue;
                }
                else {
                    Expression conditional = mapExpression(generator.getInternalIfs().get(0), ast, import_nodes, 0, typeNodes, pyc);
                    comparator.setConditionalExpression(conditional);
                    pyGenerator.getComparators().add(comparator);
                }
                if (generator.getInternalIfs().size()>1){
                    logger.fatal("Number of Conditional Ifs in generators are more than one");
                }

            }
            return pyGenerator;
        }
        else if (pyexp instanceof Dict){
            AstList keys = (AstList) (((Dict) pyexp).getKeys());
            AstList values = (AstList) (((Dict) pyexp).getValues());
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,methodInvocation);
            SimpleName map = ast.newSimpleName("Map");
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,map);
            methodInvocation.setExpression(map);

            methodInvocation.setName(ast.newSimpleName("of"));
            methodInvocation.setPyObject(pyexp);
            for (int i=0;i<keys.size();i++){
                Expression keyExpression = mapExpression((expr) keys.get(i), ast, import_nodes, 0, typeNodes,pyc );
                Expression valueExpression = mapExpression((expr) values.get(i), ast, import_nodes, 0, typeNodes,pyc);
                if (keyExpression instanceof PyErrorExpression && valueExpression instanceof PyErrorExpression){return ast.newPyErrorExpression();}

                if (keyExpression instanceof org.eclipse.jdt.core.dom.Name && isClassImport(pyc,(org.eclipse.jdt.core.dom.Name)keyExpression)){
                    ClassInstanceCreation keyclassInstanceCreation = ast.newClassInstanceCreation();
                    MapPyStatementsTOJDK.updatePythonLineNumbers((expr) keys.get(i),keyclassInstanceCreation);
                    keyclassInstanceCreation.setType(ast.newSimpleType((org.eclipse.jdt.core.dom.Name) keyExpression));
                    methodInvocation.arguments().add(keyclassInstanceCreation);
                }
                else{
                    methodInvocation.arguments().add(keyExpression);
                }
                if (valueExpression instanceof org.eclipse.jdt.core.dom.Name && isClassImport(pyc,(org.eclipse.jdt.core.dom.Name)valueExpression)){
                    ClassInstanceCreation valueclassInstanceCreation = ast.newClassInstanceCreation();
                    MapPyStatementsTOJDK.updatePythonLineNumbers((expr) keys.get(i),valueclassInstanceCreation);
                    valueclassInstanceCreation.setType(ast.newSimpleType((org.eclipse.jdt.core.dom.Name) valueExpression));
                    methodInvocation.arguments().add(valueclassInstanceCreation);
                }
                else
                    {
                    methodInvocation.arguments().add(valueExpression);
                }
            }
            return methodInvocation;
        }
        else if (pyexp instanceof DictComp){
            PyDictComprehension pyDictComp = ast.newPyDictComprehension();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,pyDictComp);
            Expression target1 = mapExpression((expr) ((DictComp) pyexp).getKey(),ast,import_nodes,0,typeNodes,pyc  );
            Expression target2 = mapExpression((expr) ((DictComp) pyexp).getValue(),ast,import_nodes,0,typeNodes,pyc  );
            if (target1 instanceof PyErrorExpression || target2 instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            pyDictComp.setTarget1Expression(target1);
            pyDictComp.setTarget2Expression(target2);
            int gen_number=0;
            for (comprehension generator : ((DictComp) pyexp).getInternalGenerators()) {
                gen_number++;
                PyComparator comparator = ast.newPyComarator();
                MapPyStatementsTOJDK.updatePythonLineNumbers(generator,comparator);
                Expression iterator = mapExpression(generator.getInternalIter(), ast, import_nodes, 0, typeNodes, pyc);
                if (iterator instanceof PyErrorExpression){return  ast.newPyErrorExpression();}
                comparator.setIteratorExpression(iterator);
                if (generator.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    comparator.getValueExpression().add(parameter_dummy);

                    for (Object elt : (AstList) ((Tuple) generator.getTarget()).getElts()) {
                        if (elt instanceof Tuple){
                            for (Object elt1 : (AstList) ((Tuple) elt).getElts()) {
                                SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                                SimpleName name = ast.newSimpleName(mapPythonKeyWords(((Name) elt1).getInternalId()));
                                String typeString = typeNodes.get(new TypeASTNode(((Name)elt1).getLineno(),
                                        ((Name)elt1).getCol_offset(), ((Name)elt1).getId().toString(), null));
                                Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                                MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt1,name);
                                MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt1,jdtType);
                                MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt1,lo_parameter);

                                lo_parameter.setName(name);
                                lo_parameter.setType(jdtType);
                                comparator.getValueExpression().add(lo_parameter);
                            }
                        }
                        else {
                            SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                            SimpleName name = ast.newSimpleName(mapPythonKeyWords(((Name) elt).getInternalId()));
                            lo_parameter.setName(name);
                            String typeString = typeNodes.get(new TypeASTNode(((Name) elt).getLineno(),
                                    ((Name) elt).getCol_offset(), ((Name) elt).getId().toString(), null));
                            Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,name);
                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,jdtType);
                            MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,lo_parameter);

                            lo_parameter.setType(jdtType);
                            comparator.getValueExpression().add(lo_parameter);
                        }
                    }
                }
                else if (generator.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    comparator.getValueExpression().add(parameter_dummy);


                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    SimpleName name = ast.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(((Name) (generator.getTarget())).getInternalId()));

                    String typeString = typeNodes.get(new TypeASTNode(((Name)(generator.getTarget())).getLineno(),
                            ((Name)generator.getTarget()).getCol_offset(), ((Name)(generator.getTarget())).getId().toString(), null));
                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),name);
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),jdtType);
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),parameter);

                    parameter.setName(name);
                    parameter.setType(jdtType);
                    comparator.getValueExpression().add(parameter);
                }
                else{
                    logger.fatal("unknown generator loop value expression"+generator.getTarget());
                }
                if (generator.getInternalIfs().size()==0){
                    if (((DictComp) pyexp).getInternalGenerators().size()>1 && gen_number==1){
                        SimpleName conditional = ast.newSimpleName("DUMMY_IF");
                        comparator.setConditionalExpression(conditional);
                    }
                    pyDictComp.getComparator().add(comparator);
                }
                else {
                    Expression conditional = mapExpression(generator.getInternalIfs().get(0), ast, import_nodes, 0, typeNodes, pyc);
                    comparator.setConditionalExpression(conditional);
                    pyDictComp.getComparator().add(comparator);
                }
                if (generator.getInternalIfs().size()>1){
                    logger.fatal("Number of Conditional Ifs in generators are more than one");
                }

            }
            return pyDictComp;

        }

        else if (pyexp instanceof ErrorExpr){
            return ast.newPyErrorExpression();
        }

        else if (pyexp instanceof SetComp){
            PySetComprehension setComprehension = ast.newPySetComprehension();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,setComprehension);

            Expression target = mapExpression((expr) ((SetComp) pyexp).getElt(),ast,import_nodes,0,typeNodes,pyc  );
            if (target instanceof PyErrorExpression){return ast.newPyErrorExpression();}
            setComprehension.setTargetExpression(target);
            int gen_number = 0;
            for (comprehension generator : ((SetComp) pyexp).getInternalGenerators()) {
                gen_number++;
                PyComparator comparator = ast.newPyComarator();
                MapPyStatementsTOJDK.updatePythonLineNumbers(generator,comparator);
                Expression iterator = mapExpression(generator.getInternalIter(), ast, import_nodes, 0, typeNodes, pyc);
                if (iterator instanceof PyErrorExpression){return  ast.newPyErrorExpression();}
                comparator.setIteratorExpression(iterator);
                if (generator.getTarget() instanceof Tuple){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    comparator.getValueExpression().add(parameter_dummy);
                    for (Object elt : (AstList) ((Tuple) generator.getTarget()).getElts()) {
                        SingleVariableDeclaration lo_parameter = ast.newSingleVariableDeclaration();
                        SimpleName name = ast.newSimpleName(((Name) elt).getId().toString());

                        String typeString = typeNodes.get(new TypeASTNode(((Name)elt).getLineno(),
                                ((Name)elt).getCol_offset(), ((Name)elt).getId().toString(), null));
                        Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                        MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,name);
                        MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,jdtType);
                        MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) elt,lo_parameter);

                        lo_parameter.setName(name);
                        lo_parameter.setType(jdtType);
                        comparator.getValueExpression().add(lo_parameter);
                    }
                }
                else if (generator.getTarget() instanceof Name){
                    SingleVariableDeclaration parameter_dummy = ast.newSingleVariableDeclaration();
                    parameter_dummy.setName(ast.newSimpleName( "DummyTerminalNode"));
                    parameter_dummy.setType(ast.newSimpleType(ast.newName("DummyTerminalTypeNode")));
                    comparator.getValueExpression().add(parameter_dummy);
                    SingleVariableDeclaration parameter = ast.newSingleVariableDeclaration();
                    SimpleName name = ast.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(((Name) (generator.getTarget())).getInternalId()));
                    parameter.setName(name);
                    String typeString = typeNodes.get(new TypeASTNode(((Name)(generator.getTarget())).getLineno(),
                            ((Name)generator.getTarget()).getCol_offset(), ((Name)(generator.getTarget())).getId().toString(), null));
                    Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);

                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),name);
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),jdtType);
                    MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) generator.getTarget(),parameter);

                    parameter.setType(jdtType);
                    comparator.getValueExpression().add(parameter);
                }
                else{
                    logger.fatal("unknown generator loop value expression"+generator.getTarget());
                }
                if (generator.getInternalIfs().size()==0){
                    if (((SetComp) pyexp).getInternalGenerators().size()>1 && gen_number==1){
                        SimpleName conditional = ast.newSimpleName("DUMMY_IF");
                        comparator.setConditionalExpression(conditional);
                    }
                    setComprehension.getComparator().add(comparator);
                }
                else {
                    Expression conditional = mapExpression(generator.getInternalIfs().get(0), ast, import_nodes, 0, typeNodes, pyc);
                    comparator.setConditionalExpression(conditional);
                    setComprehension.getComparator().add(comparator);
                }
                if (generator.getInternalIfs().size()>1){
                    logger.fatal("Number of Conditional Ifs in generators are more than one");
                }

            }

            return setComprehension;

        }
        else if (pyexp instanceof Set){
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,methodInvocation);
            SimpleName set = ast.newSimpleName("set");
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp.getCharStartIndex(),3,pyexp.getLine(),set);
            methodInvocation.setName(set);

            ((Set) pyexp).getInternalElts().stream().forEach(x->
            {
                try {
                    methodInvocation.arguments().add(mapExpression(x,ast,import_nodes,0,typeNodes,pyc));
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return methodInvocation;
        }
        else if (pyexp instanceof Lambda){
            LambdaExpression expression = ast.newLambdaExpression();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,expression);
            for (arg internalArg : ((Lambda) pyexp).getInternalArgs().getInternalArgs()) {
                assert(internalArg.getInternalAnnotation()==null);
                VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
                MapPyStatementsTOJDK.updatePythonLineNumbers(internalArg,fragment);
                SimpleName name = ast.newSimpleName(mapPythonKeyWords(internalArg.getInternalArg()));
                MapPyStatementsTOJDK.updatePythonLineNumbers(internalArg,name);
                fragment.setName(name);
                expression.parameters().add(fragment);
            }
            Expression expression1 = MapPyExpressionsJDK.mapExpression((expr) ((Lambda) pyexp).getBody(), ast, import_nodes, 0, typeNodes, pyc);
            if (expression1 instanceof PyErrorExpression){
                expression.setBody(ast.newSimpleName("PyCpatDummy"));
            }
            else {
                expression.setBody(expression1);
            }

            return expression;


        }
        else if (pyexp instanceof Ellipsis){
            String id = mapPythonKeyWords("PyEllipsis");
            SimpleName simpleName = ast.newSimpleName(id);
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,simpleName);
            simpleName.setPyObject(pyexp);
            return simpleName;
        }
        else if (pyexp instanceof Bytes){

            MethodInvocation methodInvocation = ast.newMethodInvocation();
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp,methodInvocation);
            StringLiteral stringLiteral = ast.newStringLiteral();
            stringLiteral.setLiteralValue(((Bytes) pyexp).getInternalS());
            MapPyStatementsTOJDK.updatePythonLineNumbers( pyexp,stringLiteral);
            methodInvocation.setExpression(stringLiteral);
            SimpleName bytes = ast.newSimpleName("getBytes");
            MapPyStatementsTOJDK.updatePythonLineNumbers(pyexp.getCharStartIndex(),8,pyexp.getLine() , bytes);
            methodInvocation.setName(bytes);

            return methodInvocation;
        }
        else if (pyexp instanceof Await){
            return MapPyExpressionsJDK.mapExpression(((Await) pyexp).getInternalValue(),ast,import_nodes,0,typeNodes,pyc);
//            return ast.newPyErrorExpression(); //TODO implement this Issue ID = 12
        }
        else if (pyexp instanceof Yield){
            return MapPyExpressionsJDK.mapExpression(((Yield) pyexp).getInternalValue(),ast,import_nodes,0,typeNodes,pyc);
          //TODO implement this Issue ID = 13
        }
        else if (pyexp instanceof YieldFrom){
            return MapPyExpressionsJDK.mapExpression(((YieldFrom) pyexp).getInternalValue(),ast,import_nodes,0,typeNodes,pyc);
            //TODO implement this Issue ID = 13
        }
        else {
            logger.fatal("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
            assert false;
            throw new ExpressionNotFound("Corresponding Expression is not Found "+pyexp.getClass() + pyexp.toStringTree());
        }
    }

    private static void updateOperation(BoolOp pyexp, InfixExpression infixExpression) {
        if (pyexp.getInternalOp().equals(boolopType.And)){
            infixExpression.setOperator(CONDITIONAL_AND);
        }
        else if (pyexp.getInternalOp().equals(boolopType.Or)){
            infixExpression.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
        }
        else {
            logger.fatal("Unmapped operator found "+ pyexp.getOp());
        }
    }

    private static InfixExpression.Operator setOperator(cmpopType pyexp) {
        if ( pyexp.equals(cmpopType.Eq)){
           return InfixExpression.Operator.EQUALS;
        }
        else if ( pyexp.equals(cmpopType.IsNot)){
            return InfixExpression.Operator.NOT_EQUALS;
        }
        else if (pyexp.equals(cmpopType.Is)){
            return InfixExpression.Operator.EQUALS;
        }
        else if (pyexp.equals(cmpopType.NotEq)){
            return InfixExpression.Operator.NOT_EQUALS;
        }
        else if (pyexp.equals(cmpopType.Lt)){
            return InfixExpression.Operator.LESS;
        }
        else if (pyexp.equals(cmpopType.LtE)){
            return InfixExpression.Operator.LESS_EQUALS;
        }
        else if (pyexp.equals(cmpopType.Gt)){
            return InfixExpression.Operator.GREATER;
        }
        else if (pyexp.equals(cmpopType.GtE)){
            return InfixExpression.Operator.GREATER_EQUALS;
        }
        else{
            logger.fatal("Unmapped operator found "+pyexp);
        }
        return null;
    }

    public static String mapPythonKeyWords(String keyword){
        if (keyword.equals("default")){
            return "def";
        }
        else if (keyword.equals("extends")){
                return "Extends";
        }
        else if (keyword.equals("public")){
            return "Public";
        }
        else if (keyword.equals("throw")){
            return "Throw";
        }
        else if (keyword.equals("int")){
            return "Int";
        }
        else if (keyword.equals("void")){
            return "Void";
        }
        else if (keyword.equals("private")){
            return "Private";
        }
        else if (keyword.equals("enum")){
            return "enum_";
        }
        else if (keyword.equals("double")){
            return "Double";
        }
        else if (keyword.equals("float")){
            return "Float";
        }
        else if (keyword.equals("str")){
            return "String";
        }
        else if (keyword.equals("super")){
            return "raise";
        }
        else if (keyword.equals("case")){
            return "Case";
        }
        else if (keyword.equals("char")){
            return "Char";
        }
        else if (keyword.equals("package")){
            return "Package";
        }
        else if (keyword.equals("new")){
            return "New";
        }
        else if (keyword.equals("true")){
            return "True";
        }
        else if (keyword.equals("false")){
            return "False";
        }
        else if (keyword.equals("static")){
            return "Static";
        }
        else if (keyword.equals("this")){
            return "self";
        }
        else if (keyword.equals("switch")){
            return "Switch";
        }
        else if (keyword.equals("do")){
            return "Do";
        }
        else if (keyword.equals("long")){
            return "Long";
        }
        else if (keyword.equals("final")){
            return "Final";
        }
        else if (keyword.equals("implements")){
            return "implement";
        }
        else if (keyword.equals("interface")){
            return "Interface";
        }
        else if (keyword.equals("boolean")){
            return "Boolean";
        }
        else if (keyword.equals("null")){
            return "Null";
        }
        return keyword;

    }


    private static void updateArrayTpe(AST ast, ArrayCreation arrayCreation, String typeString, Map<TypeASTNode, String> typeNodes) throws NodeNotFoundException {
        if (typeString !=null) {
            Type jdtType = TypeStringToJDT.getJDTType(ast, typeString, 0);
            if (jdtType!=null && !(jdtType instanceof SimpleType)) {
                arrayCreation.setType((ArrayType) jdtType);
            }
            else
            {
                Type jdtType1 = TypeStringToJDT.getJDTType(ast, "List[PyTypeError]", 0);
                arrayCreation.setType((ArrayType) jdtType1);
//                logger.fatal("Created PyTypeError type"+ typeString);
            }
        }
        else{
            Type jdtType1 = TypeStringToJDT.getJDTType(ast, "List[PyTypeError]", 0);
            arrayCreation.setType((ArrayType) jdtType1);
//            logger.fatal("Created PyTypeError type "+ typeString);
        }
    }

    public static boolean isClassImport(PyCompilationUnit cmp, org.eclipse.jdt.core.dom.Name name){
        ArrayList<org.eclipse.jdt.core.dom.Name> importnames = (ArrayList<org.eclipse.jdt.core.dom.Name>) cmp.imports().stream().map(x->((ImportDeclaration)x).getName()).collect(Collectors.toList());
        for (org.eclipse.jdt.core.dom.Name importname : importnames) {
            if (importname.isSimpleName() && name.isSimpleName() ) {
                if (importname.getFullyQualifiedName().equals(name.getFullyQualifiedName())) return true;
            }
            else if (importname.isQualifiedName() && name.isSimpleName()){
                if (((QualifiedName)importname).getName().getIdentifier().equals(name.getFullyQualifiedName())) return true;
            }
            else if (importname.isQualifiedName() && name.isQualifiedName()){
                if (importname.getFullyQualifiedName().equals(name.getFullyQualifiedName())) return true;
                String[] split_import = importname.getFullyQualifiedName().split("\\.");
                String[] split_name = name.getFullyQualifiedName().split("\\.");

                for (int i=0;i<split_import.length;i++){
                    java.util.List<String> collect = Arrays.stream(split_import).skip(i).collect(Collectors.toList());
                    if ((split_import.length - i)<=split_name.length){
                        java.util.List<String> collect1 = Arrays.stream(split_name).limit(split_import.length - i).collect(Collectors.toList());
                        if (collect1.equals(collect)){
                            return true;
                        }
                    }

                }
            }
            else if (importname.isSimpleName() && name.isQualifiedName()){
                String[] split_name = name.getFullyQualifiedName().split("\\.");
                if (split_name[0].equals(importname.getFullyQualifiedName())) return true;
            }

        }
        return false;
    }


}
