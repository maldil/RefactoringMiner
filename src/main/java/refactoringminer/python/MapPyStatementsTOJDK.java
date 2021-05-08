package  refactoringminer.python;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PyErrorExpression;
import org.eclipse.jdt.core.dom.PyInExpression;
import org.eclipse.jdt.core.dom.PyNonLocalStatement;
import org.eclipse.jdt.core.dom.PyTupleExpression;
import org.eclipse.jdt.core.dom.PyWithStatement;
import org.eclipse.jdt.core.dom.PyYieldReturnStatement;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.jpp.astnodes.PythonErrorNode;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Assert;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.AsyncFunctionDef;
import org.jpp.astnodes.ast.AsyncWith;
import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.AugAssign;
import org.jpp.astnodes.ast.Break;
import org.jpp.astnodes.ast.Call;
import org.jpp.astnodes.ast.ClassDef;
import org.jpp.astnodes.ast.Continue;
import org.jpp.astnodes.ast.Delete;
import org.jpp.astnodes.ast.ErrorStmt;
import org.jpp.astnodes.ast.ExceptHandler;
import org.jpp.astnodes.ast.Expr;
import org.jpp.astnodes.ast.For;
import org.jpp.astnodes.ast.FunctionDef;
import org.jpp.astnodes.ast.Global;
import org.jpp.astnodes.ast.If;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.ast.ImportFrom;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.ast.Nonlocal;
import org.jpp.astnodes.ast.Pass;
import org.jpp.astnodes.ast.Raise;
import org.jpp.astnodes.ast.Return;
import org.jpp.astnodes.ast.Str;
import org.jpp.astnodes.ast.TryExcept;
import org.jpp.astnodes.ast.TryFinally;
import org.jpp.astnodes.ast.Tuple;
import org.jpp.astnodes.ast.While;
import org.jpp.astnodes.ast.With;
import org.jpp.astnodes.ast.Yield;
import org.jpp.astnodes.ast.alias;
import org.jpp.astnodes.ast.arg;
import org.jpp.astnodes.ast.arguments;
import org.jpp.astnodes.ast.operatorType;
import org.jpp.astnodes.ast.withitem;
import org.jpp.astnodes.base.expr;
import org.jpp.astnodes.base.stmt;
import org.jpp.heart.AstList;
import org.jpp.heart.PyNone;
import org.jpp.heart.PyObject;
import org.jpp.heart.PyUnicode;
import  refactoringminer.python.pyerrors.ExpressionNotFound;
import  refactoringminer.python.pyerrors.NodeNotFoundException;
import  refactoringminer.python.pyvisitors.PyVisitor;
import  refactoringminer.python.typeinference.core.PyASTMatcher;
import  refactoringminer.python.typeinference.core.TypeASTNode;
import  refactoringminer.python.typeinference.core.TypeDecNeeds;
import  refactoringminer.python.typeinference.core.TypeStringToJDT;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MapPyStatementsTOJDK extends PyMap{
    static Logger logger = Logger.getLogger(MapPyStatementsTOJDK.class);
    private Map<TypeASTNode, String> typeNodes;
    private int augmentedChars=0;

    public MapPyStatementsTOJDK(Map<TypeASTNode, String> typeNodes) {
        this.typeNodes = typeNodes;
    }

    public ArrayList<?> getMappingPyNode(AST asn, PythonTree node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, int startChar, PyCompilationUnit pyc) throws NodeNotFoundException, ExpressionNotFound {
        if (node instanceof Import){
            int start_import = startChar;
            ArrayList<ImportDeclaration> list_imports = new ArrayList<>();
            for (PyObject pyObject : ((Import) node).getNames().asIterable()) {
                ImportDeclaration import_dec =  asn.newImportDeclaration();
                updatePythonLineNumbers(node,import_dec);
                String[] import_name = Arrays.stream(((alias) pyObject).getName().toString().split("\\.")).sequential().map(MapPyExpressionsJDK::mapPythonKeyWords).toArray(String[]::new);
                org.eclipse.jdt.core.dom.Name name = asn.newName(import_name);

                updatePythonLineNumbers(((alias) pyObject).getInternalNameNodes().get(0).getCharStartIndex() ,name.toString().length(), ((alias) pyObject).getInternalNameNodes().get(0).getLineno() ,name);

                import_dec.setName(name);
                PyObject asname = ((alias) pyObject).getAsname();
                if (!(asname instanceof PyNone)){
                    org.eclipse.jdt.core.dom.Name name1 = asn.newName(MapPyExpressionsJDK.mapPythonKeyWords(asname.asString()));
                    updatePythonLineNumbers(((alias) pyObject),name1);
                    import_dec.setasName(name1 );
                }

                list_imports.add(import_dec);
            }
            return list_imports;
        }
        else if (node instanceof ImportFrom){
            ArrayList<ImportDeclaration> list_imports = new ArrayList<>();
            String[] module = ((ImportFrom) node).getModule().toString().split("\\.");

            for (PyObject pyObject : ((ImportFrom) node).getNames().asIterable()) {
                ImportDeclaration import_dec =  asn.newImportDeclaration();
                updatePythonLineNumbers(node,import_dec);
                String[] import_name;
                if (((ImportFrom) node).getModule().toString().equals("")){
                    import_name = ((alias) pyObject).getName().toString().split("\\.");
                }
                else{
                    import_name = (String[]) ArrayUtils.addAll(module,((alias) pyObject).getName().toString().split("\\."));
                }
                List<String> import_name_clean = Arrays.stream(import_name).filter(x-> !x.equals("*")).collect(Collectors.toList());
                import_name_clean = import_name_clean.stream().map(x->MapPyExpressionsJDK.mapPythonKeyWords(x)).collect(Collectors.toList());
                String[] cleaned_import_names = new String[import_name_clean.size()];
                import_name_clean.toArray(cleaned_import_names);



                org.eclipse.jdt.core.dom.Name name = asn.newName(cleaned_import_names);
                updatePythonLineNumbers((alias) pyObject,name);

                import_dec.setName(name);
                PyObject asname = ((alias) pyObject).getAsname();
                if (!(asname instanceof PyNone)){
                    import_dec.setasName(asn.newName(MapPyExpressionsJDK.mapPythonKeyWords(asname.asString())  ));
                }
                int im_length = import_dec.toString().length();
                import_dec.setSourceRange(startChar,im_length);
                startChar+=im_length;
                list_imports.add(import_dec);
            }

            return list_imports;
        }
        else if (node instanceof ClassDef){
            int start_char_pos =startChar;
            TypeDeclaration classdec = asn.newTypeDeclaration();
            updatePythonLineNumbers(node,classdec);
            Modifier modifier = asn.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
            classdec.setModifier(modifier);

            ArrayList<TypeDeclaration> list_class = new ArrayList<>();
            SimpleName simpleName = asn.newSimpleName(((ClassDef) node).getInternalName());
            updatePythonLineNumbers(node.getCharStartIndex()+6,((ClassDef) node).getInternalName().length(), node.getLine() ,simpleName);
            classdec.setName(simpleName);

            if (((AstList)((ClassDef) node).getBases()).size()>0){
                Expression className = MapPyExpressionsJDK.mapExpression((expr)
                        ((AstList) ((ClassDef) node).getBases()).get(0),asn,import_nodes,0,typeNodes,pyc);
                if (className instanceof PyErrorExpression){return new ArrayList<>();}  //If python can not parse the class name correctly there is no point of converting this further
                if (className instanceof MethodInvocation){
                    org.eclipse.jdt.core.dom.Name name = (org.eclipse.jdt.core.dom.Name)
                            ASTNode.copySubtree(asn, ((MethodInvocation) className).getName());
                    ((MethodInvocation) className).getName().subtreeMatch(new PyASTMatcher(),name);
                    SimpleType simpleType = asn.newSimpleType(name);
                    updatePythonLineNumbers((expr)((AstList) ((ClassDef) node).getBases()).get(0),simpleType);
                    classdec.setSuperclassType(simpleType);
                }
                else if (className instanceof ArrayAccess){
                    org.eclipse.jdt.core.dom.Name name = (org.eclipse.jdt.core.dom.Name)
                            ASTNode.copySubtree(asn, ((ArrayAccess) className).getArray());
                    ((ArrayAccess) className).getArray().subtreeMatch(new PyASTMatcher(),name);
                    SimpleType simpleType = asn.newSimpleType(name);
                    updatePythonLineNumbers((expr)((AstList) ((ClassDef) node).getBases()).get(0),simpleType);
                    classdec.setSuperclassType(simpleType);
                }
                else {
                    org.eclipse.jdt.core.dom.Name className1 = (org.eclipse.jdt.core.dom.Name) className;
                    updatePythonLineNumbers((expr)((AstList) ((ClassDef) node).getBases()).get(0),className1);
                    SimpleType simpleType = asn.newSimpleType(className1);
                    classdec.setSuperclassType(simpleType); //TODO handle multiple inheritance
                }
            }
            else
                start_char_pos+=2+2;//{,\n , space, space

            MethodDeclaration methodDeclaration = null;
            int number_of_dummy_methods = 0;
            boolean updated_line = true;
            for (Object ch : (AstList)((ClassDef) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos,pyc )) {
                    if (o instanceof BodyDeclaration){
                        if (methodDeclaration!=null){
                            classdec.bodyDeclarations().add(methodDeclaration);
                            methodDeclaration=null;

                        }
                        classdec.bodyDeclarations().add(o);
                        start_char_pos+=o.toString().length()+2+2 +2*(o.toString().lines().count()-1) ; //space,space,two spaces in parameters
                    }
                    else if (o instanceof ImportDeclaration){
                        updatePythonLineNumbers((PythonTree) ch,(ASTNode)o);
                        pyc.imports().add(o);
                    }
                    else {
                        if (methodDeclaration==null){
                            number_of_dummy_methods++;
                            updated_line=false;
                            methodDeclaration = asn.newMethodDeclaration();
                            Block block = asn.newBlock();
//                            updatePythonLineNumbers(((PythonTree)((AstList) ((ClassDef) node).getBody()).get(0)).getCharStartIndex(),
//                                    ((PythonTree)((AstList)((ClassDef) node).getBody()).get(((AstList) ((ClassDef) node).getBody()).size()-1)).getCharStopIndex()-
//                                    ((PythonTree)((AstList) ((ClassDef) node).getBody()).get(0)).getCharStartIndex(),
//                                    ((PythonTree)((AstList) ((ClassDef) node).getBody()).get(0)).getLine()
//                                    ,block);

                            methodDeclaration.setBody(block);
                            SimpleName name = asn.newSimpleName(((ClassDef) node).getName().toString() + "_" + number_of_dummy_methods);
                            methodDeclaration.setName(name);
                        }
                        if (!((o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof StringLiteral)||
                                o instanceof EmptyStatement ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof SimpleName) ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof ArrayAccess) ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof QualifiedName)||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof PyTupleExpression) ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof PyInExpression) ||
                                ( o instanceof ExpressionStatement && ((ExpressionStatement) o).getExpression() instanceof PrefixExpression)
                        )){
                            methodDeclaration.getBody().statements().add(o);
                            if (!updated_line){
                                MapPyStatementsTOJDK.updatePythonLineNumbers((PythonTree) ch,methodDeclaration);
                                updated_line=true;
                            }
                        }
                    }

                }

            }
            if (methodDeclaration!=null){
                classdec.bodyDeclarations().add(methodDeclaration);
            }
            classdec.setSourceRange(startChar,classdec.toString().length());
            list_class.add(classdec);
            return list_class;
        }
        else if (node instanceof FunctionDef){
            MethodDeclaration methoddec = getMethodDeclaration(asn, node, import_nodes, startChar, pyc, 0);
            ArrayList<MethodDeclaration> list_method = new ArrayList<>();
            list_method.add(methoddec);
            return list_method;
        }
        else if (node instanceof Assign){
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            Assignment assign = asn.newAssignment();
            updatePythonLineNumbers(node,assign);
            assign.setOperator(new Assignment.Operator("="));
            Expression rightHandExpression = MapPyExpressionsJDK.mapExpression(((Assign) node).getInternalValue(), asn, import_nodes, 0, typeNodes, pyc);
            assign.setRightHandSide(rightHandExpression);
            PyTupleExpression tupleExpression = asn.newPyTupleExpression();
            Expression expression = null;
            if (((Assign) node).getInternalTargets().size()>1){
                for (expr target : ((Assign) node).getInternalTargets()) {
                    Expression leftExpression = MapPyExpressionsJDK.mapExpression(target, asn, import_nodes, 0, typeNodes,pyc );
                    if (!(leftExpression instanceof  PyErrorExpression)){
                        tupleExpression.expressions().add(leftExpression);
                    }
                }
                updatePythonLineNumbers(((Assign) node).getInternalTargets().get(0).getCharStartIndex(),
                        ((Assign) node).getInternalTargets().get(((Assign) node).getInternalTargets().size()-1).getCharStartIndex()
                                -((Assign) node).getInternalTargets().get(0).getCharStartIndex(), ((Assign) node).getInternalTargets().get(0).getLine()  ,tupleExpression);
                assign.setLeftHandSide(tupleExpression);
                if (!(tupleExpression.expressions().size()==0 || rightHandExpression instanceof PyErrorExpression)){
                    ExpressionStatement statement = asn.newExpressionStatement(assign);
                    updatePythonLineNumbers(node,statement);
                    list_assign.add(statement);
                }
                return list_assign;
            }
            else{
                expression = MapPyExpressionsJDK.mapExpression(((Assign) node).getInternalTargets().get(0), asn, import_nodes, 0, typeNodes, pyc);
                assign.setLeftHandSide(expression);
                if (!(expression instanceof PyErrorExpression || rightHandExpression instanceof PyErrorExpression)){
                    ExpressionStatement statement = asn.newExpressionStatement(assign);
                    updatePythonLineNumbers(node,statement);
                    list_assign.add(statement);

                }
                return list_assign;
            }

//            for (expr target : ((Assign) node).getInternalTargets()) {
//                Assignment assign = asn.newAssignment();
//                assign.setOperator(new Assignment.Operator("="));
//                Expression leftExpression = MapPyExpressionsJDK.mapExpression(target, asn, import_nodes, 0, typeNodes,pyc );
//                assign.setLeftHandSide(leftExpression);
//                Expression rightHandExpression = MapPyExpressionsJDK.mapExpression(((Assign) node).getInternalValue(), asn, import_nodes, 0, typeNodes, pyc);
//                assign.setRightHandSide(rightHandExpression);
//                ExpressionStatement statement = asn.newExpressionStatement(assign);
//                if (!(leftExpression instanceof PyErrorExpression || rightHandExpression instanceof PyErrorExpression )){
//                    list_assign.add(statement);
//                }
//            }

        }
        else if (node instanceof For){
            //TODO change the grammer to add else statement
            int start_char_pos =startChar;
            EnhancedForStatement forstmt = asn.newEnhancedForStatement();
            updatePythonLineNumbers(node,forstmt);
            ArrayList<EnhancedForStatement> list_for = new ArrayList<>();
            if (((For) node).getTarget() instanceof Name){
                SingleVariableDeclaration parameter_dummy = asn.newSingleVariableDeclaration();
                parameter_dummy.setName(asn.newSimpleName( "DummyTerminalNode"));
                parameter_dummy.setType(asn.newSimpleType(asn.newName("DummyTerminalTypeNode")));

                SingleVariableDeclaration parameter = updateForLoopLocalVariables(asn, (Name) ((For) node).getTarget());
                forstmt.setParameter(parameter_dummy);
                forstmt.Parameters().add(parameter);
            }
            else if (((For) node).getTarget() instanceof Tuple){
                SingleVariableDeclaration parameter_dummy = asn.newSingleVariableDeclaration();
                parameter_dummy.setName(asn.newSimpleName( "DummyTerminalNode"));
                parameter_dummy.setType(asn.newSimpleType(asn.newName("DummyTerminalTypeNode")));
                forstmt.setParameter(parameter_dummy);
                for (Object elt : (AstList) ((Tuple) ((For) node).getTarget()).getElts()) {
                    if (elt instanceof Tuple){
                        for (Object elt1 : (AstList) ((Tuple) elt).getElts()) {
                            if (elt1 instanceof Tuple){
                                for (Object elt2 : (AstList) ((Tuple) elt1).getElts()) {
                                    SingleVariableDeclaration lo_parameter = updateForLoopLocalVariables(asn, (Name) elt2);
                                    forstmt.Parameters().add(lo_parameter);
                                }
                            }
                            else{
                                SingleVariableDeclaration lo_parameter = updateForLoopLocalVariables(asn, (Name) elt1);
                                forstmt.Parameters().add(lo_parameter);
                            }
                        }
                    }
                    else{
                        SingleVariableDeclaration lo_parameter = updateForLoopLocalVariables(asn, (Name) elt);
                        forstmt.Parameters().add(lo_parameter);
                    }

                }
            }
            else{
                logger.error("The mapping for the corresponding for loop parameter is not found");
            }
            start_char_pos+=3+2+2+2; //for , space, ( , 4space
//
//            int para_int = parameter.toString().length();
//            parameter.setSourceRange(start_char_pos,para_int);
//            start_char_pos+=para_int+1+1+1; //space, : , space



            Expression expression = MapPyExpressionsJDK.mapExpression((expr)((For) node).getIter(),asn,import_nodes,start_char_pos,typeNodes,pyc );
            if (expression instanceof PyErrorExpression){return new ArrayList<>();}
            forstmt.setExpression(expression);
            int number_of_parents = get_Number_Of_Parent_Statements(node);
            start_char_pos += 4; //), space,{,new line
            int start_of_for_loop = start_char_pos+number_of_parents*2;
            for (Object ch : (AstList)((For) node).getBody()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,start_char_pos+=number_of_parents*2, pyc)) {
                    if (forstmt.getBody() ==null){
                        Block block = asn.newBlock();
                        forstmt.setBody(block);
                        updatePythonLineNumbers(((PythonTree) ((AstList) ((For) node).getBody()).get(0)).getCharStartIndex(),
                                ((PythonTree) ((AstList) ((For) node).getBody()).get(((AstList)((For) node).getOrelse()).size()-1)).getCharStopIndex(),  ((PythonTree) ((AstList) ((For) node).getBody()).get(0)).getLine()   ,block);

                    }
                    if (o instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        updatePythonLineNumbers((PythonTree) ch,typeDec);
                        SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                        ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                        typeDec.setName(name);
                        typeDec.bodyDeclarations().add(o);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        updatePythonLineNumbers((PythonTree) ch,dummyClass);
                        ((Block)forstmt.getBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        updatePythonLineNumbers((PythonTree) ch,dummyClass);
                        ((Block)forstmt.getBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ImportDeclaration ){
                        pyc.imports().add(o);
                    }
                    else {
                        ((Block)forstmt.getBody()).statements().add(o);
                    }
                }
                forstmt.getBody().setSourceRange(start_of_for_loop, node.toString().length());
            }
            for (Object ch : (AstList)((For) node).getOrelse()){
                for (Object o : getMappingPyNode(asn, (PythonTree) ch,import_nodes,0, pyc)) {
                    if (forstmt.getElseBody() ==null){
                        Block block = asn.newBlock();
                        forstmt.setElseBody(block);
                        updatePythonLineNumbers(((PythonTree) ((AstList)((For) node).getOrelse()).get(0)).getCharStartIndex(),
                                ((PythonTree) ((AstList)((For) node).getOrelse()).get(((AstList)((For) node).getOrelse()).size()-1)).getCharStopIndex(),
                                ((PythonTree) ((AstList)((For) node).getOrelse()).get(0)).getLine(),block);
                    }
                    if (o instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                        ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                        typeDec.setName(name);
                        typeDec.bodyDeclarations().add(o);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        ((Block)forstmt.getElseBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        ((Block)forstmt.getElseBody()).statements().add(dummyClass);
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else {
                        ((Block)forstmt.getElseBody()).statements().add(o);
                    }
                }
            }

            forstmt.setSourceRange(startChar,node.toString().length());
            list_for.add(forstmt);
            return list_for;

        }
        else if (node instanceof AugAssign){
            Assignment assign = asn.newAssignment();
            updatePythonLineNumbers(node,assign);
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            if (((AugAssign) node).getInternalOp()== operatorType.Div){
                assign.setOperator(Assignment.Operator.DIVIDE_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp()== operatorType.Add){
                assign.setOperator(Assignment.Operator.PLUS_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp()== operatorType.Sub){
                assign.setOperator(Assignment.Operator.MINUS_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.BitAnd){
                assign.setOperator(Assignment.Operator.BIT_AND_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.BitOr){
                assign.setOperator(Assignment.Operator.BIT_OR_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.Mult){
                assign.setOperator(Assignment.Operator.TIMES_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.LShift){
                assign.setOperator(Assignment.Operator.LEFT_SHIFT_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.RShift){
                assign.setOperator(Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.Pow){
                assign.setOperator(Assignment.Operator.POWER_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.FloorDiv){//TODO solve issues/9
                assign.setOperator(Assignment.Operator.DIVIDE_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.Mod){//TODO solve issues/9
                assign.setOperator(Assignment.Operator.REMAINDER_ASSIGN);
            }
            else if (((AugAssign) node).getInternalOp() == operatorType.BitXor){//TODO solve issues/9
                assign.setOperator(Assignment.Operator.BIT_XOR_ASSIGN);
            }

            else {
                assert  false;
                throw new NodeNotFoundException("Operator is not implemented :"+((AugAssign) node).getInternalOp().name());
            }
            Expression leftExpression =  MapPyExpressionsJDK.mapExpression((expr) ((AugAssign) node).getTarget(),asn, import_nodes,0,typeNodes,pyc );
            Expression rightExpression =  MapPyExpressionsJDK.mapExpression((expr)((AugAssign) node).getValue(),asn, import_nodes,0,typeNodes,pyc );
            if (leftExpression instanceof PyErrorExpression || rightExpression instanceof PyErrorExpression){return new ArrayList<>();}
            assign.setLeftHandSide(leftExpression);
            assign.setRightHandSide(rightExpression);

            ExpressionStatement statement = asn.newExpressionStatement(assign);
            updatePythonLineNumbers(node,statement);
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Return){
            ReturnStatement statement = asn.newReturnStatement();
            updatePythonLineNumbers(node,statement);
            ArrayList<ReturnStatement> list_assign = new ArrayList<>();
            if ( ((Return) node).getValue()!=null){
                statement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((Return) node).getValue(),asn, import_nodes,0,typeNodes,pyc ));
            }
            statement.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof Expr){
            if (((Expr) node).getValue() instanceof Call){ //Add super invocation
                Call value = (Call)((Expr) node).getValue();
//                if (value.getFunc() instanceof Name && ((Name) value.getFunc()).getId().equals("isinstance")){
//                    InstanceofExpression instanceofExpression = asn.newInstanceofExpression();
//                    instanceofExpression.setLeftOperand(MapPyExpressionsJDK.mapExpression((expr) ((AstList) value.getArgs()).get(0),asn, import_nodes,0,typeNodes));
//                    instanceofExpression.setLeftOperand(MapPyExpressionsJDK.mapExpression((expr) ((AstList) value.getArgs()).get(1),asn, import_nodes,0,typeNodes));
//                    ArrayList<InstanceofExpression> list_assign = new ArrayList<>();
//                    list_assign.add(instanceofExpression);
//                    return list_assign ;
//                }
                if (value.getFunc() instanceof Name && ((Name) value.getFunc()).getId().equals("super")){
                    SuperConstructorInvocation constructorInvocation = asn.newSuperConstructorInvocation();
                    updatePythonLineNumbers(value,constructorInvocation);
                    for (Object arg : (AstList) value.getArgs()) {
                        constructorInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes,pyc ));
                    }
                    ArrayList<SuperConstructorInvocation> list_assign = new ArrayList<>();
                    list_assign.add(constructorInvocation);
                    return list_assign ;
                }
                else if (value.getFunc() instanceof Attribute
                        && ((Attribute) value.getFunc()).getValue() instanceof Call
                        && ((Call) ((Attribute) value.getFunc()).getValue()).getFunc() instanceof Name
                        && ((Name) ((Call) ((Attribute) value.getFunc()).getValue()).getFunc()).getId().toString().equals("super")
                        && ((Attribute) value.getFunc()).getAttr().toString().equals("__init__")){

                    SuperConstructorInvocation constructorInvocation = asn.newSuperConstructorInvocation();
                    updatePythonLineNumbers(value,constructorInvocation);
                    for (Object arg : (AstList) ((Call) ((Attribute) value.getFunc()).getValue()).getArgs()) {
                        constructorInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes,pyc ));
                    }
                    ArrayList<SuperConstructorInvocation> list_assign = new ArrayList<>();
                    list_assign.add(constructorInvocation);
                    return list_assign ;
                }
            }
            else if (((Expr) node).getValue() instanceof Yield){
                PyYieldReturnStatement yieldReturnStatement = asn.newPyYieldReturnStatement();
                updatePythonLineNumbers((PythonTree) ((Expr) node).getValue(),yieldReturnStatement);
                if ( ((Yield) ((Expr) node).getValue()).getInternalValue()==null){
//                    yieldReturnStatement.setExpression(asn.newSimpleName("PyCpatDummy"));
                }
                else{
                    yieldReturnStatement.setExpression(MapPyExpressionsJDK.mapExpression(((Yield) ((Expr) node).getValue()).getInternalValue(),asn,import_nodes,0,typeNodes,pyc));

                }
                ArrayList<PyYieldReturnStatement> list_assign = new ArrayList<>();
                list_assign.add(yieldReturnStatement);
                return list_assign ;
            }

            Expression exp  = MapPyExpressionsJDK.mapExpression((expr)((Expr) node).getValue(),asn, import_nodes,0,typeNodes,pyc );



            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            ExpressionStatement expstmt= asn.newExpressionStatement(exp);
            updatePythonLineNumbers((PythonTree) ((Expr) node).getValue(),expstmt);
            expstmt.setSourceRange(node.getCharStartIndex()+PyMap.totalCharGains,node.getCharStopIndex()-node.getCharStartIndex());
            list_assign.add(expstmt);
            return list_assign;

        }
        else if (node instanceof If){
            IfStatement ifStatement = asn.newIfStatement();
            updatePythonLineNumbers(node,ifStatement);
            Expression expression = MapPyExpressionsJDK.mapExpression((expr) ((If) node).getTest(), asn, import_nodes, 0, typeNodes, pyc);

            if (expression instanceof PyErrorExpression){return new ArrayList<>();}
            if (expression instanceof ParenthesizedExpression){
                Expression expression1 = (Expression) ASTNode.copySubtree(asn, ((ParenthesizedExpression) expression).getExpression());
                ((ParenthesizedExpression) expression).getExpression().subtreeMatch(new PyASTMatcher(),expression1);
                ifStatement.setExpression(expression1);
            }
            else {
                ifStatement.setExpression(expression);
            }
            ((AstList) ((If) node).getBody()).stream().forEach(
                    ob-> {
                        try {
                            for (Object o : getMappingPyNode(asn, (PythonTree) ob, import_nodes, 0, pyc)) {
                                if (o instanceof MethodDeclaration) {
                                    TypeDeclaration typeDec = asn.newTypeDeclaration();
                                    SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                                    ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                                    typeDec.setName(name);
                                    typeDec.bodyDeclarations().add(o);
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                    ((Block)ifStatement.getThenStatement()).statements().add(dummyClass);
                                }
                                else if (o instanceof TypeDeclaration ){
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                    ((Block)ifStatement.getThenStatement()).statements().add(dummyClass);
                                }
                                else if (o instanceof ImportDeclaration){
                                    pyc.imports().add(o);
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                    continue;
                                }
                                else{
                                    ((Block)ifStatement.getThenStatement()).statements().add(o);
                                }
                            }

                        } catch (NodeNotFoundException e) {
                            e.printStackTrace();
                        } catch (ExpressionNotFound expressionNotFound) {
                            expressionNotFound.printStackTrace();
                        }
                    });
            if (((AstList)((If) node).getOrelse()).size()>0){
                if (((AstList)((If) node).getOrelse()).size()==1 && ((AstList)((If) node).getOrelse()).get(0) instanceof If ){
                    ifStatement.setElseStatement((Statement) getMappingPyNode
                            (asn, (PythonTree) ((AstList)((If) node).getOrelse()).get(0),import_nodes,0, pyc).get(0));
                }
                else{
                    ((AstList) ((If) node).getOrelse()).stream().forEach(
                            ob-> {
                                try {
                                    if (ifStatement.getElseStatement()==null){
                                        Block block = asn.newBlock();
                                        updatePythonLineNumbers(((PythonTree) (((AstList) ((If) node).getOrelse()).get(0))).getCharStartIndex(),
                                                ((PythonTree) (((AstList) ((If) node).getOrelse()).get(((AstList) ((If) node).getOrelse()).size()-1))).getCharStartIndex(),
                                                ((PythonTree) (((AstList) ((If) node).getOrelse()).get(0))).getLine()  ,block);
                                        ifStatement.setElseStatement(block);
                                    }
                                    for (Object o : getMappingPyNode(asn,
                                            (PythonTree) ob, import_nodes, 0, pyc)) {
                                        if (o instanceof MethodDeclaration) {
                                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                                            SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                                            ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                                            typeDec.bodyDeclarations().add(o);
                                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                            ((Block)ifStatement.getElseStatement()).statements().add(dummyClass);
                                        }
                                        else if (o instanceof TypeDeclaration ){
                                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                            ((Block)ifStatement.getElseStatement()).statements().add(dummyClass);
                                        }
                                        else if (o instanceof ImportDeclaration){
                                            pyc.imports().add(o);
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                                            continue;
                                        }
                                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                            continue;
                                        }
                                        else{
                                            ((Block)ifStatement.getElseStatement()).statements().add(o);
                                        }
                                    }
                                } catch (NodeNotFoundException e) {
                                    e.printStackTrace();
                                } catch (ExpressionNotFound expressionNotFound) {
                                    expressionNotFound.printStackTrace();
                                }
                            });
                }
            }


            ArrayList<IfStatement> list_assign = new ArrayList<>();
            list_assign.add(ifStatement);
            return list_assign;
        }
        else if (node instanceof Raise){
            ThrowStatement throwStatement = asn.newThrowStatement();
            updatePythonLineNumbers(node,throwStatement);
            if ( ((Raise) node).getExc()==null){
                ClassInstanceCreation classInstanceCreation = asn.newClassInstanceCreation();
                classInstanceCreation.setType(asn.newSimpleType(asn.newSimpleName("PyCpatDummy")));
                throwStatement.setExpression(classInstanceCreation);
            }else{
                Expression expression = MapPyExpressionsJDK.mapExpression((expr) ((Raise) node).getExc(), asn, import_nodes, 0, typeNodes, pyc);
                if (expression instanceof PyErrorExpression){
                    throwStatement.setExpression(asn.newSimpleName("PyCpatDummy"));
                }
                else {
                    throwStatement.setExpression(expression);
                }
            }

//
//            ClassInstanceCreation classInstanceCreation = asn.newClassInstanceCreation();
//            if (((Raise) node).getExc() instanceof Call){
//                Expression throwName = MapPyExpressionsJDK.mapExpression((expr) ((Call) ((Raise) node).getExc()).getFunc(), asn, import_nodes, 0, typeNodes, pyc);
//                if (throwName instanceof MethodInvocation){
//                    classInstanceCreation.setType(asn.newSimpleType((org.eclipse.jdt.core.dom.Name) ASTNode.copySubtree(asn,((MethodInvocation) throwName).getName())));
//                    ((MethodInvocation)throwName).arguments().stream().forEach(x->classInstanceCreation.arguments().add(ASTNode.copySubtree(asn, (ASTNode) x)));
//                }
//                else{
//
//                    classInstanceCreation.setType(asn.newSimpleType((org.eclipse.jdt.core.dom.Name) throwName));
//                    ((AstList)((Call) ((Raise) node).getExc()).getArgs()).stream().forEach(o->
//                    {
//                        try {
//                            Expression expression = MapPyExpressionsJDK.mapExpression((expr) o, asn, import_nodes, 0, typeNodes, pyc);
//                            classInstanceCreation.arguments().add(expression);
//                        } catch (ExpressionNotFound expressionNotFound) {
//                            expressionNotFound.printStackTrace();
//                        } catch (NodeNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//            }
//            else{
//                classInstanceCreation.setType(asn.newSimpleType(asn.newSimpleName("PythonException")));
//                logger.info("PythonException was assigned to empty raise statment");
//            }
//            throwStatement.setExpression(classInstanceCreation);
            ArrayList<ThrowStatement> list_assign = new ArrayList<>();
            list_assign.add(throwStatement);
            return list_assign;
        }
        else if (node instanceof TryExcept){
            TryStatement tryStatement = asn.newTryStatement();
            updatePythonLineNumbers(node,tryStatement);
            Block block = asn.newBlock();

            ((AstList)((TryExcept) node).getBody()).stream().forEach(x-> {
                try {
                    for (Object o : getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc)) {
                        updatePythonLineNumbers(((PythonTree) (((AstList)((TryExcept) node).getBody()).get(0))).getCharStartIndex(),
                                ((PythonTree) (((AstList)((TryExcept) node).getBody()).get(((AstList)((TryExcept) node).getBody()).size()-1))).getCharStartIndex() ,
                                ((PythonTree) (((AstList)((TryExcept) node).getBody()).get(0))).getLine(),block);
                        if (o instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                            ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                            typeDec.setName(name);
                            typeDec.bodyDeclarations().add(o);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            block.statements().add(dummyClass);
                        }
                        else if (o instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                            block.statements().add(dummyClass);
                        }
                        else if (o instanceof ImportDeclaration ) {
                            pyc.imports().add(o);
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else{
                            block.statements().add(o);
                        }
                    }
//                    ArrayList<?> mappingPyNode = getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc);
//                    block.statements().addAll(mappingPyNode.stream().filter(Predicate.not(z->z instanceof ImportDeclaration)).collect(Collectors.toList()));
//                    pyc.imports().addAll(mappingPyNode.stream().filter(z->z instanceof ImportDeclaration).collect(Collectors.toList()));

                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });

            ((AstList)((TryExcept) node).getHandlers()).stream().forEach(x-> {
                try {
                    for (Object o : getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc)) {
                        if (o instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                            ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                            updateLineNumbers(((MethodDeclaration) o).getName(),name);
                            typeDec.bodyDeclarations().add(o);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            tryStatement.catchClauses().add(dummyClass);
                        }
                        else if (o instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                            tryStatement.catchClauses().add(dummyClass);
                        }
                        else if (o instanceof ImportDeclaration ) {
                            pyc.imports().add(o);
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else{
                            tryStatement.catchClauses().add(o);
                        }
                    }

                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            if (((TryExcept) node).getInternalOrelse().size()>0){
                Block block1 = asn.newBlock();
                tryStatement.setElse(block1);
            }

            (((TryExcept) node).getInternalOrelse()).stream().forEach(x->
                    {
                        try {
                            for (Object o : getMappingPyNode(asn, x, import_nodes, 0, pyc)) {
                                if (o instanceof MethodDeclaration) {
                                    TypeDeclaration typeDec = asn.newTypeDeclaration();
                                    SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                                    ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                                    typeDec.setName(name);
                                    typeDec.bodyDeclarations().add(o);
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                    tryStatement.getElse().statements().add(dummyClass);
                                }
                                else if (o instanceof TypeDeclaration ){
                                    TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                    tryStatement.getElse().statements().add(dummyClass);
                                }
                                else if (o instanceof ImportDeclaration ) {
                                    pyc.imports().add(o);
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                                    continue;
                                }
                                else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                    continue;
                                }
                                else{
                                    tryStatement.getElse().statements().add(o);
                                }
                            }
                        } catch (NodeNotFoundException e) {
                            e.printStackTrace();
                        } catch (ExpressionNotFound expressionNotFound) {
                            expressionNotFound.printStackTrace();
                        }
                    }
            );

            tryStatement.setBody(block);


            ArrayList<TryStatement> list_assign = new ArrayList<>();
            list_assign.add(tryStatement);
            return list_assign;
        }
        else if (node instanceof ExceptHandler){
            ArrayList<CatchClause> list_assign = new ArrayList<>();
            CatchClause catchClause = asn.newCatchClause();
            updatePythonLineNumbers(node,catchClause);
            Block block = asn.newBlock();
            ((AstList)((ExceptHandler) node).getBody()).stream().forEach(x-> {
                try {
                    getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc).stream().filter
                            (Predicate.not(z->z instanceof ImportDeclaration)).collect(Collectors.toList()).forEach(state->{
                        if (state instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) state).getName());
                            ((MethodDeclaration) state).getName().subtreeMatch(new PyASTMatcher(),name);
                            typeDec.setName(name);

                            typeDec.bodyDeclarations().add(state);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            block.statements().add(dummyClass);
                        }
                        else if (state instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) state);
                            block.statements().add(dummyClass);
                        }
                        else if (state instanceof ImportDeclaration ) {
                            pyc.imports().add(state);
                        }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof QualifiedName){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof SimpleName){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof PyTupleExpression){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof ParenthesizedExpression){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof ArrayAccess){ }
                        else if (state instanceof ExpressionStatement && ((ExpressionStatement)state).getExpression() instanceof StringLiteral){ }
                        else{
                            block.statements().add(state);
                        }
                    });
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            SingleVariableDeclaration singleVarDec = asn.newSingleVariableDeclaration();
            if (((ExceptHandler) node).getExceptType()!=null){
                Expression expression = MapPyExpressionsJDK.mapExpression((expr) ((ExceptHandler) node).getExceptType(), asn, import_nodes, 0, typeNodes, pyc);
                updatePythonLineNumbers((expr) ((ExceptHandler) node).getExceptType(),singleVarDec);
                if (expression instanceof  PyTupleExpression){
                    UnionType unionType = asn.newUnionType();
                    updatePythonLineNumbers((expr) ((ExceptHandler) node).getExceptType(),unionType);


                    ((PyTupleExpression)expression).expressions().stream().forEach(x-> {
                                org.eclipse.jdt.core.dom.Name name  = (org.eclipse.jdt.core.dom.Name) ASTNode.copySubtree(asn, (ASTNode) x);
                                ((ASTNode) x).subtreeMatch(new PyASTMatcher(),name);
                                SimpleType type = asn.newSimpleType(name);
                                updateLineNumbers(type, (ASTNode) x);
                                unionType.types().add(type);
                                }
                            );
                    singleVarDec.setType(unionType);
                }
                else if (expression instanceof ParenthesizedExpression){
                    SimpleType type = asn.newSimpleType(asn.newSimpleName("PyCpatDummy"));
                    singleVarDec.setType(type);
                }
                else if (expression instanceof MethodInvocation){
                    SimpleType type = asn.newSimpleType(asn.newSimpleName("PyCpatDummy"));
                    singleVarDec.setType(type);
                }
                else{
                    SimpleType type = asn.newSimpleType((org.eclipse.jdt.core.dom.Name) expression);
                    updatePythonLineNumbers((expr) ((ExceptHandler) node).getExceptType(),type);
                    singleVarDec.setType(type);
                }
            }
            else{
                SimpleType type = asn.newSimpleType(asn.newSimpleName("PyCpatDummy"));
                singleVarDec.setType(type);
            }

            if (((ExceptHandler) node).getName()!=null){
                if (((ExceptHandler) node).getName() instanceof PyUnicode){
                    SimpleName simpleName = asn.newSimpleName(((PyUnicode) ((ExceptHandler) node).getName()).getString());
                    updatePythonLineNumbers(node.getCharStopIndex()-((ExceptHandler) node).getInternalName().length(),
                            ((ExceptHandler) node).getInternalName().length(), node.getLine()     ,simpleName);
                    singleVarDec.setName(simpleName);
                }
                else if (((ExceptHandler) node).getName() instanceof PyNone){
                    SimpleName simpleName = asn.newSimpleName("PyCpatDummy");
                    singleVarDec.setName(simpleName);
                }
                else{
                    Expression name = MapPyExpressionsJDK.mapExpression((expr) ((ExceptHandler) node).getName(), asn, import_nodes, 0, typeNodes, pyc);
                    singleVarDec.setName((SimpleName) name);
                }
            }

            catchClause.setException(singleVarDec);
            catchClause.setBody(block);
            list_assign.add(catchClause);
            return list_assign;
        }
        else if (node instanceof Delete){
            MethodInvocation methodInvocation = asn.newMethodInvocation();
            updatePythonLineNumbers(node,methodInvocation);
            SimpleName del = asn.newSimpleName("del");
            updatePythonLineNumbers(node.getCharStartIndex(),3, node.getLine() ,del);
            methodInvocation.setName(del);
            for (Object arg : (AstList) ((Delete) node).getTargets()) {  //TODO keyword arguments are not parsed by the JPyParser, Thus is a Bug
                methodInvocation.arguments().add(MapPyExpressionsJDK.mapExpression((expr) arg,asn, import_nodes,0,typeNodes,pyc ));
            }
            ArrayList<ExpressionStatement> list_assign = new ArrayList<>();
            ExpressionStatement statement = asn.newExpressionStatement(methodInvocation);
            updatePythonLineNumbers(node,statement);
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof With) {
            PyWithStatement pyWithStatement = getPyWithStatement(asn, (With) node, import_nodes, pyc);
            updatePythonLineNumbers(node,pyWithStatement);
            ArrayList<PyWithStatement> list_assign = new ArrayList<>();
            list_assign.add(pyWithStatement);
            return list_assign;
        }
        else if (node instanceof Break){
            BreakStatement breakStatement = asn.newBreakStatement();
            updatePythonLineNumbers(node,breakStatement);
            ArrayList<BreakStatement> list_assign = new ArrayList<>();
            list_assign.add(breakStatement);
            return list_assign;
        }
        else if (node instanceof Continue){
            ContinueStatement continueStatement = asn.newContinueStatement();
            updatePythonLineNumbers(node,continueStatement);
            ArrayList<ContinueStatement> list_assign = new ArrayList<>();
            list_assign.add(continueStatement);
            return list_assign;
        }
        else if (node instanceof Pass){
            EmptyStatement statement = asn.newEmptyStatement();
            updatePythonLineNumbers(node,statement);
            ArrayList<EmptyStatement> list_assign = new ArrayList<>();
            list_assign.add(statement);
            return list_assign;
        }
        else if (node instanceof While){
            WhileStatement statement = asn.newWhileStatement();
            updatePythonLineNumbers(node,statement);
            statement.setExpression(MapPyExpressionsJDK.mapExpression((expr) ((While) node).getTest(),asn,import_nodes,0,typeNodes,pyc));
            AstList body = (AstList) ((While) node).getBody();
            Block block = asn.newBlock();
            updatePythonLineNumbers(((PythonTree)body.get(0)).getCharStartIndex(),
                    ((PythonTree)body.get(body.size()-1)).getCharStopIndex()-
                    ((PythonTree)body.get(body.size()-1)).getCharStopIndex(), ((PythonTree)body.get(0)).getLine()  ,block);
            statement.setBody(block);
            for (Object ob : body) {
                for (Object stmt : getMappingPyNode(asn, (PythonTree) ob, import_nodes, 0, pyc)) {
                    if (stmt instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) stmt).getName());
                        ((MethodDeclaration) stmt).getName().subtreeMatch(new PyASTMatcher(),name);
                        typeDec.setName(name);
                        typeDec.bodyDeclarations().add(stmt);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        ((Block)statement.getBody()).statements().add(dummyClass);
                    }
                    else if (stmt instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) stmt);
                        ((Block)statement.getBody()).statements().add(dummyClass);
                    }
                    else if (stmt instanceof ImportDeclaration ) {
                        pyc.imports().add(stmt);
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (stmt instanceof ExpressionStatement && ((ExpressionStatement)stmt).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else {
                        ((Block)statement.getBody()).statements().add(stmt);
                    }
                }
            }
            ArrayList<WhileStatement> list_assign = new ArrayList<>();
            list_assign.add(statement);
            return list_assign;

        }
        else if (node instanceof Global){//TODO extend python parser to Globals,
            for (Name name :((Global) node).getInternalNameNodes()){
                VariableDeclarationFragment declarationFragment = asn.newVariableDeclarationFragment();
                declarationFragment.setPyObject(name);
                SimpleName simpleName = asn.newSimpleName(name.getInternalId());
                updatePythonLineNumbers(name,simpleName);
                declarationFragment.setName(simpleName);
                FieldDeclaration declaration = asn.newFieldDeclaration(declarationFragment);

                String typeString = this.typeNodes.get(new TypeASTNode(name.getLineno(),
                        name.getCol_offset(), name.getInternalId(), null));

                Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
                if (jdtType!=null){
                    declaration.setType(jdtType);
                }
                pyc.setGlobal_variables(declaration);
            }
            return new ArrayList<>();
        }
        else if (node instanceof ErrorStmt)
        {
            return new ArrayList<>();
        }
        else if (node instanceof Assert){
            AssertStatement assertStatement = asn.newAssertStatement();
            updatePythonLineNumbers(node,assertStatement);
            Expression expression1 = MapPyExpressionsJDK.mapExpression((expr) ((Assert) node).getTest(), asn, import_nodes, 0, typeNodes, pyc);
            if (!(expression1 instanceof PyErrorExpression)){
                assertStatement.setExpression(expression1);
            }
            else
            {
                assertStatement.setExpression(asn.newSimpleName("PyCpatDummy"));
            }
            if (((Assert) node).getInternalMsg()!=null){
                Expression expression = MapPyExpressionsJDK.mapExpression(((Assert) node).getInternalMsg(), asn, import_nodes, 0, typeNodes, pyc);
                if (!(expression instanceof PyErrorExpression))
                    assertStatement.setMessage(expression);

            }
            ArrayList<AssertStatement> list_assign = new ArrayList<>();
            list_assign.add(assertStatement);
            return list_assign;
        }
        else if (node instanceof TryFinally){
            TryStatement tryStatement = asn.newTryStatement();
            updatePythonLineNumbers(node,tryStatement);
            if (((TryFinally) node).getInternalBody().size()==1 &&
                    getMappingPyNode(asn,((TryFinally) node).getInternalBody().get(0), import_nodes, 0, pyc).get(0) instanceof TryStatement){
                TryStatement trystmt = (TryStatement)getMappingPyNode(asn,((TryFinally) node).getInternalBody().get(0), import_nodes, 0, pyc).get(0);
                for (Object clause : trystmt.catchClauses()) {
                    ASTNode node1 = ASTNode.copySubtree(asn, (ASTNode) clause);
                    ((ASTNode) clause).subtreeMatch(new PyASTMatcher(),node1);
                    tryStatement.catchClauses().add(node1);
                }
                Block block = (Block) ASTNode.copySubtree(asn, trystmt.getBody());
                Block block1 = (Block) ASTNode.copySubtree(asn, trystmt.getElse());
                if (block!=null){
                    trystmt.getBody().subtreeMatch(new PyASTMatcher(),block);
                    tryStatement.setBody(block);
                }
                if (block1!=null) {
                    trystmt.getElse().subtreeMatch(new PyASTMatcher(), block1);
                    tryStatement.setElse(block1);
                }

            }
            else{
                Block block = asn.newBlock();
                updatePythonLineNumbers(((PythonTree) ((AstList)((TryFinally) node).getBody()).get(0)).getCharStartIndex(),
                        ((PythonTree) ((AstList)((TryFinally) node).getBody()).get(((AstList)((TryFinally) node).getBody()).size()-1)).getCharStopIndex()   -
                                ((PythonTree) ((AstList)((TryFinally) node).getBody()).get(0)).getCharStartIndex(),
                        ((PythonTree) ((AstList)((TryFinally) node).getBody()).get(((AstList)((TryFinally) node).getBody()).size()-1)).getLine(),
                        block);
                ((AstList)((TryFinally) node).getBody()).stream().forEach(x-> {
                    try {
                        for (Object o : getMappingPyNode(asn, (PythonTree) x, import_nodes, 0, pyc)) {
                            if (o instanceof MethodDeclaration) {
                                TypeDeclaration typeDec = asn.newTypeDeclaration();
                                SimpleName name = (SimpleName) ASTNode.copySubtree (asn,((MethodDeclaration) o).getName());
                                ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                                typeDec.setName(name);
                                typeDec.bodyDeclarations().add(o);
                                TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                                block.statements().add(dummyClass);
                            }
                            else if (o instanceof TypeDeclaration ){
                                TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                                block.statements().add(dummyClass);
                            }
                            else if (o instanceof ImportDeclaration ) {
                                pyc.imports().add(o);
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                                continue;
                            }
                            else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                                continue;
                            }
                            else {
                                block.statements().add(o);
                            }
                        }
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    } catch (ExpressionNotFound expressionNotFound) {
                        expressionNotFound.printStackTrace();
                    }
                });
                tryStatement.setBody(block);
            }

            Block final_block = asn.newBlock();
            updatePythonLineNumbers(((((TryFinally) node).getInternalFinalbody()).get(0)).getCharStartIndex(),
                    ((((TryFinally) node).getInternalFinalbody()).get(((AstList)((TryFinally) node).getFinalbody()).size()-1)).getCharStopIndex()   -
                            ((PythonTree) ((AstList)((TryFinally) node).getFinalbody()).get(0)).getCharStartIndex(), ((((TryFinally) node).getInternalFinalbody()).get(0)).getLine() ,final_block);

            (((TryFinally) node).getInternalFinalbody()).stream().forEach(x-> {

                try {
                    for (Object o : getMappingPyNode(asn, x, import_nodes, 0, pyc)) {
                        if (o instanceof MethodDeclaration) {
                            TypeDeclaration typeDec = asn.newTypeDeclaration();
                            SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                            ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                            typeDec.setName(name);
                            typeDec.bodyDeclarations().add(o);
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                            final_block.statements().add(dummyClass);
                        }
                        else if (o instanceof TypeDeclaration ){
                            TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                            final_block.statements().add(dummyClass);
                        }
                        else if (o instanceof ImportDeclaration ) {
                            pyc.imports().add(o);
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                            continue;
                        }
                        else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                            continue;
                        }
                        else{
                            final_block.statements().add(o);
                        }
                    }
                } catch (NodeNotFoundException e) {
                    e.printStackTrace();
                } catch (ExpressionNotFound expressionNotFound) {
                    expressionNotFound.printStackTrace();
                }
            });
            tryStatement.setFinally(final_block);


//            if (((TryFinally) node).getInternalBody().size()>1){
//                logger.fatal("Size of body of try final is larger than one");
//                assert false;
//            }
//            if (((TryFinally) node).getInternalBody().get(0) instanceof TryExcept){
//                ArrayList<?> mappingPyNode = getMappingPyNode(asn, ((TryFinally) node).getInternalBody().get(0), import_nodes, 0, pyc);
//                TryStatement except =(TryStatement)mappingPyNode.get(0);
//
//                Block finalBlock = asn.newBlock();
//                ((TryFinally) node).getInternalFinalbody().stream().forEach(x-> {
//                    try {
//                        finalBlock.statements().addAll(getMappingPyNode(asn,x,import_nodes,0,pyc));
//                    } catch (NodeNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (ExpressionNotFound expressionNotFound) {
//                        expressionNotFound.printStackTrace();
//                    }
//                });
//                except.setFinally(finalBlock);
//                ArrayList<TryStatement> list_assign = new ArrayList<>();
//                list_assign.add(except);
//                return list_assign;
//            }
//            else{
//                logger.fatal("Type of the try final body is not try except");
//                assert false;
//                return new ArrayList<>();
//            }
            ArrayList<TryStatement> list_assign = new ArrayList<>();
            list_assign.add(tryStatement);
            return list_assign;

        }

        else if (node instanceof Nonlocal){
            ArrayList<PyNonLocalStatement> list_assign = new ArrayList<>();
            for (Name nameNode : ((Nonlocal) node).getInternalNameNodes()) {
                PyNonLocalStatement nonlocal = asn.newNonLocal();
                updatePythonLineNumbers(node,nonlocal);
                nonlocal.setExpression(MapPyExpressionsJDK.mapExpression(nameNode,asn,import_nodes,0,typeNodes,pyc));
                list_assign.add(nonlocal);
            }
            return list_assign;
        }
        else if (node instanceof AsyncFunctionDef){
            MethodDeclaration methoddec = getMethodDeclaration(asn, node, import_nodes, startChar, pyc, 0);
            updatePythonLineNumbers(node,methoddec);
            ArrayList<MethodDeclaration> list_method = new ArrayList<>();
            list_method.add(methoddec);
            return list_method;
        }
        else if (node instanceof AsyncWith){
            PyWithStatement pyWithStatement = getPyWithStatement(asn,  (AsyncWith)node, import_nodes, pyc);
            updatePythonLineNumbers(node,pyWithStatement);
            ArrayList<PyWithStatement> list_assign = new ArrayList<>();
            list_assign.add(pyWithStatement);
            return list_assign;
        }
        else {

            logger.fatal("Corresponding Python node is not found : found "+node.getClass()+" Class "+node.toString());
            assert false;
            return new ArrayList<>();
        }

    }

    private void updateLineNumbers(ASTNode source, ASTNode destination) {
        destination.setPyColumnOffSet(source.getPyColumnOffSet());
        destination.setPyStartPosition(source.getPyStartPosition());
        destination.setPyLine(source.getPyLine());
        destination.setPyLength(source.getPyLength());
    }


    private PyWithStatement getPyWithStatement(AST asn, stmt node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, PyCompilationUnit pyc) throws ExpressionNotFound, NodeNotFoundException {
        PyWithStatement pyWithStatement = asn.newPyWithStatement();
        updatePythonLineNumbers(node,pyWithStatement);
        List<withitem> internalItems = node instanceof With ? ((With) node).getInternalItems(): ((AsyncWith) node).getInternalItems();

        if (internalItems.size() > 1) {
            logger.error("With item contain more than one item in the header");
        }
        expr internalContext_expr = internalItems.get(0).getInternalContext_expr();
        Expression expression = MapPyExpressionsJDK.mapExpression(internalContext_expr, asn, import_nodes, 0, typeNodes, pyc);
        if (expression instanceof PyErrorExpression){
            expression =  asn.newSimpleName("PyCpatDummy");
        }

        pyWithStatement.setExpression(expression);
        expr optional_vars = internalItems.get(0).getInternalOptional_vars();

        Block block = asn.newBlock();
        if (optional_vars != null && optional_vars instanceof Name) {
            String typeString = this.typeNodes.get(new TypeASTNode(((Name) optional_vars).getLineno(), ((Name) optional_vars).getCol_offset(), ((Name) optional_vars).getInternalId(), null));
            VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, ((Name) optional_vars).getInternalId(), typeString, 0);
            block.statements().add(varDecStat);
        }
        List<stmt> internalBody = node instanceof With ? ((With) node).getInternalBody():((AsyncWith) node).getInternalBody();
        if (internalBody.size()>0)
            updatePythonLineNumbers(internalBody.get(0).getCharStartIndex(),internalBody.get(internalBody.size()-1).getCharStopIndex()
                    -internalBody.get(0).getCharStartIndex(),   internalBody.get(0).getLine()    ,block);
        internalBody.stream().forEach(x -> {
            try {
                updatePythonLineNumbers(internalBody.get(0).getCharStartIndex(),internalBody.get(internalBody.size()-1).getCharStopIndex()-
                        internalBody.get(0).getCharStartIndex(),  internalBody.get(0).getLine()      ,block);
                for (Object o : getMappingPyNode(asn, x, import_nodes, 0, pyc)) {

                    if (o instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                        ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name);
                        typeDec.setName(name);
                        typeDec.bodyDeclarations().add(o);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        block.statements().add(dummyClass);
                    }
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        block.statements().add(dummyClass);
                    }
                    else if (o instanceof ImportDeclaration ) {
                        pyc.imports().add(o);
                    }
                    else if (o instanceof PythonErrorNode){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else{
                        block.statements().add(o);
                    }
                }
            } catch (NodeNotFoundException e) {
                e.printStackTrace();
            } catch (ExpressionNotFound expressionNotFound) {
                expressionNotFound.printStackTrace();
            }
        });
        pyWithStatement.setBody(block);
        return pyWithStatement;
    }


    private MethodDeclaration getMethodDeclaration(AST asn, PythonTree node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes, int startChar, PyCompilationUnit pyc, int start_char_pos) throws NodeNotFoundException, ExpressionNotFound {
        Set<TypeDecNeeds> variableNeedsDeclaration=null;
        try {
            variableNeedsDeclaration=getVariabelNeedsDecleration(node,import_nodes);
        } catch (Exception e) {
            logger.error(e);
        }
        MethodDeclaration methoddec = asn.newMethodDeclaration();
        updatePythonLineNumbers(node,methoddec);
        String name = node instanceof FunctionDef ? ((FunctionDef) node).getInternalName() : ((AsyncFunctionDef) node).getInternalName();

        SimpleName methodName = asn.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(name));
        updatePythonLineNumbers(node.getCharStartIndex()+4,methodName.toString().length(), node.getLine() ,methodName);

        methoddec.setName(methodName);

        //We assign only the parameter self. Type of other parameters are assigned
        int numb_args=0;
        arguments args = (arguments) (node instanceof FunctionDef ? ((FunctionDef) node).getArgs() : ((AsyncFunctionDef) node).getArgs());

        for (Object arg : (AstList) args.getArgs()) {
            if (((arg) arg).getArg().toString().equals("self") && node.getParent() instanceof ClassDef){
                SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
                SimpleType simpleType = asn.newSimpleType(asn.newName(((ClassDef) node.getParent()).getInternalName()));
                updatePythonLineNumbers(((PythonTree) arg).getCharStartIndex()+4,simpleType.toString().length(), ((PythonTree) arg).getLine()   ,simpleType);
                SimpleName simpleName = asn.newSimpleName(((arg) arg).getArg().toString());
                updatePythonLineNumbers((PythonTree) arg,simpleName);

                parameter.setName(simpleName);
//TODO uncomment above
                parameter.setType(simpleType);
                updatePythonLineNumbers(((PythonTree) arg).getCharStartIndex(),parameter.toString().length() ,((PythonTree) arg).getLine()  ,parameter);
                parameter.setSourceRange(start_char_pos,parameter.toString().length());

                methoddec.parameters().add(parameter);

            }else {
                SingleVariableDeclaration parameter = asn.newSingleVariableDeclaration();
                SimpleType simpleType = asn.newSimpleType(asn.newName("Any"));
                SimpleName simpleName = asn.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords (((arg) arg).getArg().toString()));
                parameter.setName(simpleName);
                parameter.setType(simpleType);
                methoddec.parameters().add(parameter);
            }

        }
        start_char_pos +=3; // ),{ newline


        int number_of_par =  get_Number_Of_Parent_Statements(node);

        if (variableNeedsDeclaration != null) {

            Map<String, List<TypeDecNeeds>> collect = variableNeedsDeclaration.stream().collect(Collectors.groupingBy(TypeDecNeeds::getName));
            for (Map.Entry<String, List<TypeDecNeeds>> entry : collect.entrySet()){
                if (entry.getValue().size()==1){
                    TypeDecNeeds typeDecNeeds = entry.getValue().get(0);
                    String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                    if (typeString!=null && (typeString.equals("module")  ))
                        continue;
                    VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, typeDecNeeds, typeString,0);
                    if (methoddec.getBody() == null) {
                        methoddec.setBody(asn.newBlock());
                    }
                    varDecStat.setSourceRange(start_char_pos,varDecStat.toString().length());
                    methoddec.getBody().statements().add(varDecStat);
                }
                else{
                    Set<String> hash_Set= new HashSet<>();
                    for (TypeDecNeeds typeDecNeeds : entry.getValue()) {
                        String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                        if (typeString!=null && (typeString.equals("module")))
                            continue;
                        hash_Set.add(typeString);
                    }
                    if (hash_Set.size()==1){
                        VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), hash_Set.iterator().next(),0);
                        if (methoddec.getBody() == null) {
                            methoddec.setBody(asn.newBlock());
                        }
                        methoddec.getBody().statements().add(varDecStat);
                    }
                    else{
                        List<String> collect1 = hash_Set.stream().filter(Predicate.not(x -> {
                            if (x == null) return true;
                            return x.equals("Any") || x.equals("PyTypeError") || x.equals("None") || x.equals("nothing");
                        })).collect(Collectors.toList());
                        if (collect1.size()==0){
                            if (hash_Set.contains("Any")){
                                VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), "Any",0);
                                if (methoddec.getBody() == null) {
                                    methoddec.setBody(asn.newBlock());
                                }
                                methoddec.getBody().statements().add(varDecStat);
                            }
                            else if (hash_Set.contains("PyTypeError")){
                                VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), "PyTypeError",0);
                                if (methoddec.getBody() == null) {
                                    methoddec.setBody(asn.newBlock());
                                }
                                methoddec.getBody().statements().add(varDecStat);
                            }
                        }
                        else if (collect1.size()==1){
                            VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), collect1.get(0),0);
                            if (methoddec.getBody() == null) {
                                methoddec.setBody(asn.newBlock());
                            }
                            methoddec.getBody().statements().add(varDecStat);
                        }
                        else{
                            StringBuilder unionTypeStr = new StringBuilder("Union[");
                            int type_length =0;
                            for (String type:collect1){
                                type_length+=1;
                                if (type_length==collect1.size()) {
                                    unionTypeStr.append(type);
                                }
                                else {
                                    unionTypeStr.append(type).append(",");
                                }
                            }
                            unionTypeStr.append("]");
                            VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), unionTypeStr.toString(),0);
                            if (methoddec.getBody() == null) {
                                methoddec.setBody(asn.newBlock());
                            }
                            methoddec.getBody().statements().add(varDecStat);
                        }
                    }
                }
            }

//                for (TypeDecNeeds typeDecNeeds : variableNeedsDeclaration) {
//                    String typeString = this.typeNodes.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
//                    start_char_pos+=2*number_of_par;// two spaces
//                    VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, typeDecNeeds, typeString,start_char_pos);
//                    if (methoddec.getBody() == null) {
//                        methoddec.setBody(asn.newBlock());
//                    }
//                    varDecStat.setSourceRange(start_char_pos,varDecStat.toString().length());
//                    start_char_pos+=varDecStat.toString().length();
//                    methoddec.getBody().statements().add(varDecStat);
//
//                }
        }


        //two spaces
        AstList body = node instanceof FunctionDef? (AstList)((FunctionDef) node).getBody(): (AstList)((AsyncFunctionDef) node).getBody();
        for (Object ch : body){
            if (ch instanceof Expr && ((Expr) ch).getValue() instanceof Str){
                continue;  //remove comments
            }
            for (Object o : getMappingPyNode(asn, (PythonTree) ch, import_nodes, start_char_pos, pyc)) {
                if (methoddec.getBody() ==null){
                    Block block = asn.newBlock();
                    updatePythonLineNumbers((PythonTree)body.get(0),block);
                    methoddec.setBody(block);
                }
                if (o instanceof ImportDeclaration){
                    pyc.imports().add(o);
                }
                else{
                    if (o instanceof MethodDeclaration) {
                        TypeDeclaration typeDec = asn.newTypeDeclaration();
                        SimpleName name1 = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) o).getName());
                        ((MethodDeclaration) o).getName().subtreeMatch(new PyASTMatcher(),name1);
                        typeDec.setName(name1);
                        typeDec.bodyDeclarations().add(o);
                        updatePythonLineNumbers((PythonTree) ch,typeDec);
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement(typeDec);
                        updatePythonLineNumbers((PythonTree) ch,dummyClass);
                        methoddec.getBody().statements().add(dummyClass);
                    }
                    else if (o instanceof TypeDeclaration ){
                        TypeDeclarationStatement dummyClass = asn.newTypeDeclarationStatement((TypeDeclaration) o);
                        updatePythonLineNumbers((PythonTree) ch,dummyClass);
                        methoddec.getBody().statements().add(dummyClass);
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof QualifiedName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof SimpleName){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyTupleExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ParenthesizedExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof ArrayAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PyInExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof FieldAccess){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof PrefixExpression){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof NumberLiteral){
                        continue;
                    }
                    else if (o instanceof ExpressionStatement && ((ExpressionStatement)o).getExpression() instanceof StringLiteral){
                        continue;
                    }
                    else {
                        methoddec.getBody().statements().add(o);
                    }
                }
            }
        }
        methoddec.setSourceRange(startChar, methoddec.toString().length());
        return methoddec;
    }

    private SingleVariableDeclaration updateForLoopLocalVariables(AST asn, Name elt) throws NodeNotFoundException {
        SingleVariableDeclaration lo_parameter = asn.newSingleVariableDeclaration();
        updatePythonLineNumbers(elt,lo_parameter);
        lo_parameter.setName(asn.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(elt.getId().toString())));
        String typeString = this.typeNodes.get(new TypeASTNode(elt.getLineno(),
                elt.getCol_offset(), elt.getId().toString(), null));

        Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
        if (jdtType != null) {
            lo_parameter.setType(jdtType);
        } else
            logger.error("Type of for loop variable is not updated");
        return lo_parameter;
    }

    public static Set<TypeDecNeeds> getVariabelNeedsDecleration(PythonTree node, HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes) throws Exception {
        PyVisitor pyVisitor = new PyVisitor(import_nodes);
        pyVisitor.visit(node);
        return pyVisitor.getTypeDecNeeds();
    }

    private int get_Number_Of_Parent_Statements(PythonTree node){
        PythonTree parent = node.getParent();

        int number_of_parents = (parent==null) ? 0 : 1;
        while (parent!=null){
            if (parent instanceof stmt){
                number_of_parents+=1;
            }
            parent=parent.getParent();
        }
        return number_of_parents;
    }

    public static void updatePythonLineNumbers(PythonTree ast, ASTNode node) {
        node.setPyStartPosition(ast.getCharStartIndex());
        node.setPyLength(ast.getCharStopIndex()- ast.getCharStartIndex());
        node.setPyLine(ast.getLine());
        node.setPyColumnOffSet(ast.getCharPositionInLine());
    }

    public static void updatePythonLineNumbers(int startChar, int length, int lineNUmber,  ASTNode node) {
        int intitialStart = startChar;
        node.setPyStartPosition(startChar);
        node.setPyLength(length);
        node.setPyLine(lineNUmber);
        if (node instanceof QualifiedName){
//            startChar = startChar+ node.toString().length();
            int dots = StringUtils.countMatches(node.toString(), ".");
            org.eclipse.jdt.core.dom.Name quals = ((QualifiedName) node).getQualifier();

            for (int i=0;i<dots;i++){
                if(quals.isQualifiedName()){
                    quals.setPyStartPosition(intitialStart);
                    quals.setPyLength(quals.toString().length());
                    ((QualifiedName)quals).getName().setPyStartPosition(intitialStart+((QualifiedName) quals).getQualifier().toString().length());
                    ((QualifiedName)quals).getName().setPyLength(((QualifiedName)quals).getName().toString().length());
                    quals = ((QualifiedName) quals).getQualifier();
                    startChar=startChar+1+quals.toString().length();
                }
                else{
                    quals.setPyStartPosition(startChar);
                    quals.setPyLength(quals.toString().length());
                    startChar=startChar+quals.toString().length()+1;
                }
            }
            ((QualifiedName)node).getName().setPyStartPosition(intitialStart + ((QualifiedName)node).getQualifier().toString().length());
            ((QualifiedName)node).getName().setPyLength(((QualifiedName)node).getName().toString().length());
        }

    }

}