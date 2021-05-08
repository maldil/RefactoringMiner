package  refactoringminer.python.typeinference.core;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import  refactoringminer.python.MapPyExpressionsJDK;
import  refactoringminer.python.PyMap;
import  refactoringminer.python.pyerrors.NodeNotFoundException;
import  refactoringminer.python.typeinference.antlr.TypeInfo;
import  refactoringminer.python.typeinference.antlr.TypeTree;


public class TypeStringToJDT extends PyMap {
    static Logger logger = Logger.getLogger(TypeStringToJDT.class);
    public static VariableDeclarationStatement mapTypeStringToTypeTree(AST ast, TypeDecNeeds needs, String typeString, int startPosition) throws NodeNotFoundException {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        VariableDeclarationStatement variableDeclarationStatement = ast.newVariableDeclarationStatement(variableDeclarationFragment);
        if(typeString==null){
            typeString = "PyTypeError";
        }
        Type jdtType = getJDTType(ast, typeString, startPosition);
        assert jdtType != null;
        jdtType.setSourceRange(startPosition,jdtType.toString().length());
        variableDeclarationStatement.setType(jdtType);

        SimpleName simpleName = ast.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(needs.getName()));
        simpleName.setSourceRange(startPosition + jdtType.toString().length()+1,simpleName.toString().length());
        variableDeclarationFragment.setName(simpleName);

        variableDeclarationFragment.setSourceRange(startPosition+ jdtType.toString().length()+1,variableDeclarationFragment.toString().length());

        return variableDeclarationStatement;
    }

    public static VariableDeclarationStatement mapTypeStringToTypeTree(AST ast, String needs, String typeString, int startPosition) throws NodeNotFoundException {
        VariableDeclarationFragment variableDeclarationFragment = ast.newVariableDeclarationFragment();
        VariableDeclarationStatement variableDeclarationStatement = ast.newVariableDeclarationStatement(variableDeclarationFragment);
        if(typeString==null){
            typeString = "PyTypeError";
        }


        Type jdtType = getJDTType(ast, typeString, startPosition);

        assert jdtType != null;
        jdtType.setSourceRange(startPosition,jdtType.toString().length());
        variableDeclarationStatement.setType(jdtType);

        SimpleName simpleName = ast.newSimpleName(MapPyExpressionsJDK.mapPythonKeyWords(needs)  );
        simpleName.setSourceRange(startPosition + jdtType.toString().length()+1,simpleName.toString().length());
        variableDeclarationFragment.setName(simpleName);

        variableDeclarationFragment.setSourceRange(startPosition+ jdtType.toString().length()+1,variableDeclarationFragment.toString().length());

        return variableDeclarationStatement;
    }

    public static Type getJDTType(AST ast, String typeString, int startPosition) throws NodeNotFoundException {
        if(typeString==null){
            typeString = "PyTypeError";
        }
        TypeInfo typeInfo = new TypeInfo();
        TypeTree typeTree=null;
        try {
            typeTree = typeInfo.getTypeTree(typeString);
            if (typeTree.isError()){
                logger.fatal("Error when parsing Type String :"+typeString);
                return ast.newSimpleType(ast.newName("PyTypeError"));
            }
            else{
                return convertToJDTType(ast,typeTree.getTree(),startPosition, false);
            }

        } catch (RecognitionException e) {
            logger.fatal("Type tree formation error");
            logger.error(e);
            return ast.newSimpleType(ast.newName("PyTypeError"));
        }

    }

    private static Type convertToJDTType(AST ast, CommonTree typeTree, int startPosition, boolean forDict) throws NodeNotFoundException {
        CommonTree tree = typeTree;

        if (tree.getChildren()==null){
            if (tree.getText().equals("int")){
                if (forDict){
                    return ast.newSimpleType(ast.newName("Integer"));
                }
                PrimitiveType primitiveType = ast.newPrimitiveType(PrimitiveType.INT);
                primitiveType.setSourceRange(startPosition,primitiveType.toString().length());
                return primitiveType;
            }
            else if (tree.getText().equals("long")){
                if (forDict){
                    return ast.newSimpleType(ast.newName("Long"));
                }
                PrimitiveType primitiveType = ast.newPrimitiveType(PrimitiveType.LONG);
                primitiveType.setSourceRange(startPosition,primitiveType.toString().length());
                return primitiveType;
            }
            else if (tree.getText().equals("float")){
                if (forDict){
                    return ast.newSimpleType(ast.newName("Float"));
                }
                PrimitiveType primitiveType = ast.newPrimitiveType(PrimitiveType.FLOAT);
                primitiveType.setSourceRange(startPosition,primitiveType.toString().length());
                return primitiveType;
            }
            else if (tree.getText().equals("str")){
                SimpleType string = ast.newSimpleType(ast.newName("String"));
                return string;
            }
            else if (tree.getText().equals("Any")){
                SimpleType any = ast.newSimpleType(ast.newName("Any"));
                any.setSourceRange(startPosition,3);
                return any;
            }
            else if (tree.getText().equals("nothing")){
                SimpleType any = ast.newSimpleType(ast.newName("Nothing"));
                any.setSourceRange(startPosition,3);
                return any;
            }
            else if (!tree.getText().contains("...") && tree.getText().contains(".")){

//                    mod mod = PyASTParser.parsePython(tree.getText());
//                    Expression expression = MapPyExpressionsJDK.mapExpression((expr) mod.getChild(0).getChild(0), ast, new HashMap<>());
//                    SimpleType simpleType = ast.newSimpleType((Name) expression);


                    QualifiedName qualifiedName = (QualifiedName) ast.newName(tree.getText().split("\\."));
                    qualifiedName.setSourceRange(startPosition,qualifiedName.toString().length());
                    SimpleType simpleType = ast.newSimpleType(qualifiedName);
                    simpleType.setSourceRange(startPosition,simpleType.toString().length());
                    return simpleType;

//                Name any = ast.newName("Any");
//                any.setSourceRange(startPosition,4);
//                return ast.newSimpleType(any);
            }
            else if (tree.getText().contains("()")){
                return ast.newSimpleType(ast.newName("EmptyBracket"));
            }
            else if (tree.getText().contains("...")){
                return ast.newSimpleType(ast.newName("ThreeDots"));
            }
            else if (tree.getText().equals("super")){
                return ast.newSimpleType(ast.newName("Super"));
            }
            else{
                logger.debug("SimpleType was assigned to  : "+tree.getText());
                SimpleType simpleType=null;
                try {
                    simpleType = ast.newSimpleType(ast.newName(tree.getText()));
                }
                catch (IllegalArgumentException arg){
                    logger.info("");
                }
                return simpleType;
            }

        }
        else if(tree.getChildren().size()==1){
            if (tree.getText().equals("List")){
                return ast.newArrayType(convertToJDTType(ast, (CommonTree) tree.getChild(0), startPosition+5, false));

            }
            else if (tree.getText().equals("Dict")){
                ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Map")));
                paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) tree.getChild(0),0, true));
                return paraType;
            }
            else if (tree.getText().equals("Set")){
                ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Set")));
                paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) tree.getChild(0),0, true));
                return paraType;
//                return ast.newArrayType(convertToJDTType(ast, (CommonTree) tree.getChild(0), startPosition+5, false));
            }
            else if (tree.getText().equals("Union")){
                ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Union")));
                for (Object child : tree.getChildren()) {
                    paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
                }
                return ast.newArrayType(paraType);
            }
            else if (tree.getText().equals("Tuple")){
                ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Tuple")));
                for (Object child : tree.getChildren()) {
                    paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
                }
                return ast.newArrayType(paraType);
            }
            else if (tree.getText().equals("Optional")){
                ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Optional")));
                for (Object child : tree.getChildren()) {
                    paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
                }
                return ast.newArrayType(paraType);
            }
            else if (tree.getText().equals("Type")){
                if (tree.getChildren().size()==1){
                    return convertToJDTType(ast, (CommonTree) tree.getChildren().get(0),0, true);
                }
                else {
                    logger.fatal("Corresponding Python node is not found : " + tree.getText());
                    throw new NodeNotFoundException("Corresponding Python node is not found : " + tree.getText());
                }
            }
            else {
                ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName(typeTree.getText())));
                for (Object child : tree.getChildren()) {
                    try {
                        paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child, 0, true));
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println();
                    }

                }
                return ast.newArrayType(paraType);
            }
        }
        else if (tree.getText().equals("Union")){
            ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Union")));
            for (Object child : tree.getChildren()) {
                paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
            }
            return paraType;
//            String typeString = "Union";
//            for (Object child : tree.getChildren()) {
//                typeString+="_";
//                typeString+= ((CommonTree) child).toString();
//            }

//            return ast.newSimpleType(ast.newName(typeString));
        }
        else if (tree.getText().equals("Dict")){
            ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Map")));
            for (Object child : tree.getChildren()) {
//                if (child.toString().equals("Callable") ){
//                    Type jdtType = getJDTType(ast, "PyTypeError", startPosition);
//                    paraType.typeArguments().add(jdtType);
//                }
//                else
                    paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
            }
            return paraType;
        }
        else if (tree.getText().equals("Tuple")){
            ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Tuple")));
            for (Object child : tree.getChildren()) {
                paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
            }
            return paraType;
        }
        else if (tree.getText().equals("List")){

            ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Union")));
            for (Object child : tree.getChildren()) {
                paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
            }

            return ast.newArrayType(paraType);
        }
        else if (tree.getText().equals("Callable")){
            ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName("Callable")));
            for (Object child : tree.getChildren()) {
                paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
            }

            return paraType;
        }
        else
        {
            ParameterizedType paraType = ast.newParameterizedType(ast.newSimpleType(ast.newName(typeTree.getText())));
            for (Object child : tree.getChildren()) {
                paraType.typeArguments().add(convertToJDTType(ast, (CommonTree) child,0, true));
            }
            return paraType;
        }

    }


}
