package  refactoringminer.python;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.jpp.astnodes.Visitor;
import org.jpp.astnodes.ast.ClassDef;
import org.jpp.astnodes.ast.ErrorMod;
import org.jpp.astnodes.ast.FunctionDef;
import org.jpp.astnodes.ast.ImportFrom;
import org.jpp.astnodes.ast.Module;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jpp.PyASTParser;
import org.jpp.astnodes.PythonTree;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.base.mod;
import  refactoringminer.python.pyerrors.ExpressionNotFound;
import  refactoringminer.python.pyerrors.NodeNotFoundException;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PythonASTUtil {
    static Logger logger = Logger.getLogger(PythonASTUtil.class);
    private Map<TypeASTNode, String> typeinformation=new HashMap<>();;
    public PyCompilationUnit parseSource(String content, Map<TypeASTNode, String> typeinfo,String packageName) {
        mod ast = PyASTParser.parsePython(content);
        logger.debug(ast.toStringTree());
        this.typeinformation=typeinfo;
        return createPyCompilationUnit(ast,packageName);
    }

    public static ASTNode parseJavaSource(String source) {

            ASTParser parser = ASTParser.newParser(AST.JLS15);

			Map<String, String> options = JavaCore.getOptions();
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
			options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
			parser.setCompilerOptions(options);
			parser.setResolveBindings(false);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setStatementsRecovery(true);
			parser.setSource(source.toCharArray());


//
//
//
//
//
//
//
//        Map options = JavaCore.getOptions();
//        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
//        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
//        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
//        ASTParser parser = ASTParser.newParser(AST.JLS4);
//        parser.setSource(source.toCharArray());
//        parser.setCompilerOptions(options);
          ASTNode ast = parser.createAST(null);
          return ast;
    }

    public PyCompilationUnit createPyCompilationUnit(mod ast,String packageName){
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
        AST asn = new AST(options);
        PyASTVisitor pyNodeCounter = new PyASTVisitor();  //This is to count the number of classes and methods,
                                                            // if it is zero only one Dummy class will be created
        try {
            pyNodeCounter.visit(ast);           //This could be a performance overhead
        } catch (Exception e) {
            e.printStackTrace();
        }
        PyCompilationUnit pyc = new PyCompilationUnit(asn);
        PackageDeclaration declaration = asn.newPackageDeclaration();

        declaration.setName(asn.newName(Arrays.stream(packageName.split("\\.")).map(MapPyExpressionsJDK::mapPythonKeyWords).collect(Collectors.joining(".")).split("\\.")));
        pyc.setPackage(declaration);

        updatePythonLineNumbers(ast, pyc);
        if (ast==null|| ast instanceof ErrorMod || ((Module)ast).getInternalBody().size()==0){ return pyc;}
        MapPyStatementsTOJDK pyStatementsTOJDK = new MapPyStatementsTOJDK(this.typeinformation);
        PyMap.totalCharGains=0;
        PyMap.currentMethodGain=0;
        HashMap<String, Name> import_nodes = getImportsAndAlias(ast, asn);
        logger.debug("Import and Alias Names : "+import_nodes);
        int startChar = 0;
        TypeDeclaration dummyClass = asn.newTypeDeclaration();
        Modifier.ModifierKeyword keyword= new Modifier.ModifierKeyword("public",1);
        Modifier modifier = asn.newModifier(keyword);
        dummyClass.setModifier(modifier);
        SimpleName simpleName = asn.newSimpleName("PyDummyClass1");
        dummyClass.setName(simpleName);
        ArrayList<ASTNode> global_stmts = new ArrayList<ASTNode>();
        Object currentCursor = null;
        TypeDeclaration currentHolder = null;
        TypeDeclaration otherCurrentHolder = null;
        int number_of_dummy_classes = 1;
        int number_of_dummy_methods = 1;
        boolean update_line_numbers_method_dec = false;
        boolean update_other_current_holder = false;
        for (PythonTree ch : ast.getChildren()){
            logger.debug(ch.toString());
            try {
                ArrayList<?> nodes = pyStatementsTOJDK.getMappingPyNode(asn,ch,import_nodes,startChar, pyc);
                int nodeNumber=0;

                for (Object node : Objects.requireNonNull(nodes)) {

                    logger.debug(node.toString());
                    startChar+=node.toString().length();
                    logger.debug("Start Char : "+startChar);
                    if (node instanceof ImportDeclaration)
                    {
                        if (currentHolder!=null && (currentCursor instanceof MethodDeclaration || currentCursor ==null)){
                            pyc.setTypes(currentHolder);
                            currentHolder=null;
                        }

                        pyc.setImport((ImportDeclaration) node);

                    }
                    else if (node instanceof TypeDeclaration)
                    {
                        if (currentHolder!=null && (currentCursor instanceof MethodDeclaration|| currentCursor==null)){
                            pyc.setTypes(currentHolder);
                            currentHolder=null;
                        }
                        if (otherCurrentHolder!=null){
                            pyc.setTypes(otherCurrentHolder);
                            otherCurrentHolder=null;

                        }
                        pyc.setTypes((TypeDeclaration) node);
                    }
//                    else if (node instanceof MethodDeclaration){
//                        pyc.setTypes(node);
//                    }
                    else if (node instanceof MethodDeclaration){
                        if (currentHolder !=null){
                            currentHolder.bodyDeclarations().add(node);
                            //pyc.setTypes(dummyClass);
                        }
                        else {
                            currentHolder=asn.newTypeDeclaration();
                            currentHolder.setModifier(asn.newModifier(new Modifier.ModifierKeyword("public",1)));
                            currentHolder.setName(asn.newSimpleName("PyDummyClass"+number_of_dummy_classes));
                            currentHolder.bodyDeclarations().add(node);
                            MapPyStatementsTOJDK.updatePythonLineNumbers(ch,currentHolder);
                            number_of_dummy_classes+=1;
                        }

                        if (otherCurrentHolder!=null){
                            pyc.setTypes(otherCurrentHolder);
                            otherCurrentHolder=null;

                        }
                    }
                    else{









                    }
                    //Uncomment below code if you want to assign assign statements as class atttributes
//                    else if (node instanceof ExpressionStatement && ((ExpressionStatement) node).getExpression() instanceof
//                            Assignment && ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide() instanceof SimpleName &&
//                            (pyNodeCounter.classDef+ pyNodeCounter.funcDef!=0)) {
//                        VariableDeclarationFragment variableDeclarationFragment = asn.newVariableDeclarationFragment();
//                        SimpleName name = asn.newSimpleName(((SimpleName) ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getIdentifier());
//                        MapPyStatementsTOJDK.updatePythonLineNumbers(ch,name);
//                        MapPyStatementsTOJDK.updatePythonLineNumbers(ch,variableDeclarationFragment);
//                        variableDeclarationFragment.setName(name);
//
//                        Expression rightHandSide = ((Assignment) ((ExpressionStatement) node).getExpression()).getRightHandSide();
//                        Expression initilizer = (Expression) ASTNode.copySubtree(asn,rightHandSide );
//                        MapPyStatementsTOJDK.updatePythonLineNumbers(ch,initilizer);
//                        variableDeclarationFragment.setInitializer(initilizer);
//                        FieldDeclaration fieldDeclaration = asn.newFieldDeclaration(variableDeclarationFragment);
//
//                        Type type = TypeApproximator.getSimpleTypeApproximation(asn, (expr)rightHandSide.getPyObject());
//                        if (type==null){
//                            String typeString = typeinformation.get(new TypeASTNode((((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getPyStartPosition(),
//                                    ((Assignment) ((ExpressionStatement) node).getExpression()).getLeftHandSide().getPyColumnOffSet(), ((SimpleName) ((Assignment)
//                                    ((ExpressionStatement) node).getExpression()).getLeftHandSide()).getIdentifier(), null));
//                            Type jdtType = TypeStringToJDT.getJDTType(asn, typeString, 0);
//                            fieldDeclaration.setType(jdtType);
//                        }
//                        else{
//                            fieldDeclaration.setType(type);
//                        }
//                        fieldDeclaration.modifiers().add(asn.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
//                        fieldDeclaration.modifiers().add(asn.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
//                        global_stmts.add(fieldDeclaration);
//                        if (otherCurrentHolder!=null){
//                            pyc.setTypes(otherCurrentHolder);
//                            otherCurrentHolder=null;
//
//                        }
//
//                    }





//                    else {
//                        if (currentHolder!=null && (currentCursor instanceof MethodDeclaration|| currentCursor==null)){
//                            pyc.setTypes(currentHolder);
//                            currentHolder=null;
//                        }
//                        if (!(node instanceof ExpressionStatement && (((ExpressionStatement) node).getExpression() instanceof StringLiteral ||
//                                ((ExpressionStatement) node).getExpression() instanceof SimpleName  ||
//                                ((ExpressionStatement) node).getExpression() instanceof ArrayAccess ||
//                                ((ExpressionStatement) node).getExpression() instanceof FieldAccess ||
//                                ((ExpressionStatement) node).getExpression() instanceof PyTupleExpression ||
//                                ((ExpressionStatement) node).getExpression() instanceof PyInExpression ||
//                                ((ExpressionStatement) node).getExpression() instanceof PrefixExpression))
//                        ){
//                            if (otherCurrentHolder==null){
//                                otherCurrentHolder=asn.newTypeDeclaration();
//                                otherCurrentHolder.setModifier(asn.newModifier(new Modifier.ModifierKeyword("public",1)));
//                                otherCurrentHolder.setName(asn.newSimpleName("PyDummyClass"+number_of_dummy_classes));
//                                update_other_current_holder =true;
//                                MethodDeclaration methoddec = asn.newMethodDeclaration();
//                                methoddec.setName(asn.newSimpleName(("PyDummyMethod"+number_of_dummy_methods)));
//                                methoddec.setBody(asn.newBlock());
//                                update_line_numbers_method_dec = true;
//
//                                if (pyNodeCounter.classDef+ pyNodeCounter.funcDef==0 && number_of_dummy_classes==1){
//                                    updateVariableTypes(ast, asn, methoddec,import_nodes);
//                                }
//
//                                if (node instanceof MethodDeclaration) {
//                                    TypeDeclaration typeDec = asn.newTypeDeclaration();
//                                    SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) node).getName());
//                                    ((MethodDeclaration) node).getName().subtreeMatch(new PyASTMatcher(),name);
//                                    typeDec.setName(name);
//                                    typeDec.bodyDeclarations().add(node);
//                                    methoddec.getBody().statements().add(typeDec);
//                                    if (update_line_numbers_method_dec){
//                                        MapPyStatementsTOJDK.updatePythonLineNumbers(ch,methoddec);
//                                    }
//
//                                }
//                                else if (node instanceof TypeDeclaration ){
//                                    TypeDeclarationStatement dummyClassLocal = asn.newTypeDeclarationStatement((TypeDeclaration) node);
//                                    methoddec.getBody().statements().add(dummyClassLocal);
//                                    if (update_line_numbers_method_dec){
//                                        MapPyStatementsTOJDK.updatePythonLineNumbers(ch,methoddec);
//                                    }
//
//                                }
//                                else if (node instanceof ImportDeclaration ) {
//                                    pyc.imports().add(node);
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof QualifiedName){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof SimpleName){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof PyTupleExpression){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof ParenthesizedExpression){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof ArrayAccess){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof StringLiteral){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof PyInExpression){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof FieldAccess){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof StringLiteral){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof PrefixExpression){
//                                    continue;
//                                }
//                                else{
////                                    methoddec.getBody().statements().add(node);
//                                    if (update_line_numbers_method_dec){
//                                        MapPyStatementsTOJDK.updatePythonLineNumbers(ch,methoddec);
//                                    }
//
//                                }
//                                if (otherCurrentHolder.bodyDeclarations().size()==0){
//                                    MapPyStatementsTOJDK.updatePythonLineNumbers(ch,otherCurrentHolder);
//                                }
//                                otherCurrentHolder.bodyDeclarations().add(methoddec);
//
//                                number_of_dummy_classes+=1;
//                                number_of_dummy_methods+=1;
//                            }
//                            else{
//                                if (node instanceof MethodDeclaration) {
//                                    TypeDeclaration typeDec = asn.newTypeDeclaration();
//                                    SimpleName name = (SimpleName) ASTNode.copySubtree(asn, ((MethodDeclaration) node).getName());
//                                    ((MethodDeclaration) node).getName().subtreeMatch(new PyASTMatcher(),name);
//                                    typeDec.setName(name);
//                                    typeDec.bodyDeclarations().add(node);
////                                    pyc.imports().add(typeDec);
//                                    ((MethodDeclaration)otherCurrentHolder.bodyDeclarations().get(0)).getBody().statements().add(typeDec);
//                                }
//                                else if (node instanceof TypeDeclaration ){
//                                    TypeDeclarationStatement dummyClassLocal = asn.newTypeDeclarationStatement((TypeDeclaration) node);
//                                    ((MethodDeclaration)otherCurrentHolder.bodyDeclarations().get(0)).getBody().statements().add(dummyClassLocal);
//                                }
//                                else if (node instanceof ImportDeclaration ) {
//                                    ((MethodDeclaration)otherCurrentHolder.bodyDeclarations().get(0)).getBody().statements().add(node);
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof QualifiedName){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof SimpleName){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof PyTupleExpression){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof ParenthesizedExpression){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof ArrayAccess){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof StringLiteral){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof PyInExpression){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof FieldAccess){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof StringLiteral){
//                                    continue;
//                                }
//                                else if (node instanceof ExpressionStatement && ((ExpressionStatement)node).getExpression() instanceof PrefixExpression){
//                                    continue;
//                                }
//                                else{
//                                    if (otherCurrentHolder.bodyDeclarations().size()==0){
//                                        MethodDeclaration methoddec = asn.newMethodDeclaration();
//                                        methoddec.setName(asn.newSimpleName(("PyDummyMethod"+number_of_dummy_methods)));
//                                        methoddec.setBody(asn.newBlock());
//                                        MapPyStatementsTOJDK.updatePythonLineNumbers(ch,methoddec);
//                                        otherCurrentHolder.bodyDeclarations().add(methoddec);
//                                    }
//
////                                    ((MethodDeclaration)otherCurrentHolder.bodyDeclarations().get(0)).getBody().statements().add(node);
//                                }
//                            }
//                        }
////                        logger.fatal("Not implemented statement "+node+ node.toString());
//                    }
                    currentCursor = node;
                }
            } catch (NodeNotFoundException | ExpressionNotFound e) {
                e.printStackTrace();
            }

        }
        if (currentHolder!=null && currentHolder.bodyDeclarations().size()>0){
            pyc.setTypes(currentHolder);
        }
        if (otherCurrentHolder!=null && otherCurrentHolder.bodyDeclarations().size()>0){
            pyc.setTypes(otherCurrentHolder);
        }

        pyc.types().stream().filter(sc -> sc instanceof TypeDeclaration).forEach(x->((TypeDeclaration) x).bodyDeclarations()
                .addAll(global_stmts.stream().map(y->{
                    ASTNode node = ASTNode.copySubtree(asn, y);
                    ((ASTNode)y).subtreeMatch(new PyASTMatcher(),node);
                    return node;
                }).collect(Collectors.toList())));

        pyc.types().stream().filter(sc -> sc instanceof TypeDeclaration).forEach(x->((TypeDeclaration) x).bodyDeclarations()
                .addAll(pyc.getGlobal_variables().stream().map(y->
                        {
                            ASTNode node = ASTNode.copySubtree(asn, y);
                            ((ASTNode)y).subtreeMatch(new PyASTMatcher(),node);
                            return node;
                        }
                ).collect(Collectors.toList())));
        return pyc;
    }

    private void updatePythonLineNumbers(PythonTree ast, ASTNode node) {
        node.setPyStartPosition(ast.getCharStartIndex());
        node.setPyLength(ast.getCharStartIndex()- ast.getCharStopIndex());
        node.setPyLine(ast.getLine());
        node.setPyColumnOffSet(ast.getCharPositionInLine());
    }

    private void updateVariableTypes(mod ast, AST asn, MethodDeclaration method, HashMap<String, Name> import_nodes) throws NodeNotFoundException {
        Set<TypeDecNeeds> variableNeedsDeclaration=null;
        try {
            variableNeedsDeclaration=MapPyStatementsTOJDK.getVariabelNeedsDecleration(ast, import_nodes);
        } catch (Exception e) {
            logger.error(e);
        }


        Map<String, List<TypeDecNeeds>> collect = variableNeedsDeclaration.stream().collect(Collectors.groupingBy(TypeDecNeeds::getName));
        for (Map.Entry<String, List<TypeDecNeeds>> entry : collect.entrySet()){
            if (entry.getValue().size()==1){
                TypeDecNeeds typeDecNeeds = entry.getValue().get(0);
                String typeString = this.typeinformation.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                if (typeString!=null && (typeString.equals("module")  ))
                    continue;
                VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, typeDecNeeds, typeString,0);
                if (method.getBody() == null) {
                    method.setBody(asn.newBlock());
                }
                method.getBody().statements().add(varDecStat);
            }
            else{
                Set<String> hash_Set= new HashSet<>();
                for (TypeDecNeeds typeDecNeeds : entry.getValue()) {
                    String typeString = this.typeinformation.get(new TypeASTNode(typeDecNeeds.getRow(), typeDecNeeds.getCol_offset(), typeDecNeeds.getName(), null));
                    if (typeString!=null && (typeString.equals("module") ))
                        continue;
                    hash_Set.add(typeString);
                }
                if (hash_Set.size()==1){
                    VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), hash_Set.iterator().next(),0);
                    if (method.getBody() == null) {
                        method.setBody(asn.newBlock());
                    }
                    method.getBody().statements().add(varDecStat);
                }
                else{
                    List<String> collect1 = new ArrayList<>();
                    hash_Set.forEach(x->{
                        if (x!=null && !(x.equals("Any") || x.equals("PyTypeError") ||  x.equals("None")) ){
                            collect1.add(x);
                        } });
                    if (collect1.size()==0){
                        if (hash_Set.contains("Any")){
                            VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), "Any",0);
                            if (method.getBody() == null) {
                                method.setBody(asn.newBlock());
                            }
                            method.getBody().statements().add(varDecStat);
                        }
                        else if (hash_Set.contains("PyTypeError")){
                            VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), "PyTypeError",0);
                            if (method.getBody() == null) {
                                method.setBody(asn.newBlock());
                            }
                            method.getBody().statements().add(varDecStat);
                        }
                    }
                    else if (collect1.size()==1){
                        VariableDeclarationStatement varDecStat = TypeStringToJDT.mapTypeStringToTypeTree(asn, entry.getKey(), collect1.get(0),0);
                        if (method.getBody() == null) {
                            method.setBody(asn.newBlock());
                        }
                        method.getBody().statements().add(varDecStat);
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
                        if (method.getBody() == null) {
                            method.setBody(asn.newBlock());
                        }
                        method.getBody().statements().add(varDecStat);
                    }
                }
            }
        }
    }

    class PyASTVisitor extends Visitor {
        private int classDef= 0;
        private int funcDef= 0;
        @Override
        public Object visitClassDef(ClassDef node) throws Exception {
            classDef+=1;
            return super.visitClassDef(node);
        }
        @Override
        public Object visitFunctionDef(FunctionDef node) throws Exception {
            classDef+=1;
            return super.visitFunctionDef(node);
        }
    }


    public HashMap<String, Name> getImportsAndAlias(mod ast, AST asn) {
        MapPyStatementsTOJDK pyStatementsTOJDK = new MapPyStatementsTOJDK(this.typeinformation);
        HashMap<String,Name> import_nodes = new HashMap<>();
        for (PythonTree ch : ast.getChildren()){ //collect import statements to resolve alias names.
            if (ch instanceof Import || ch instanceof ImportFrom){
                try {
                    for (Object o : pyStatementsTOJDK.getMappingPyNode(asn, ch, null,0,null )) {
                        if (((ImportDeclaration)o).getasName() !=null){
                            import_nodes.put(((ImportDeclaration)o).getasName().getFullyQualifiedName(),((ImportDeclaration)o).getName());
                        }
                    }
                } catch (NodeNotFoundException | ExpressionNotFound e) {
                    logger.error(e);
                }
            }
        }
        return import_nodes;
    }


    public static List<Import> getImportStatements(PythonTree tree){
//        PyVisitor importVisitor = new PyVisitor();
//        try {
//            tree.accept(importVisitor);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        ArrayList<Import> imports = new ArrayList<Import>();
        for (PythonTree ch: tree.getChildren()){
            if (ch instanceof Import) {
                imports.add((Import)ch);
                AST asn = new AST();
                ImportDeclaration jdtimport = new ImportDeclaration(asn);
            }

        }
        return imports;
    }

}
