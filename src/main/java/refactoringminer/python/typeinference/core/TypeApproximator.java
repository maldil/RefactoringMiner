package  refactoringminer.python.typeinference.core;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Type;
import org.jpp.astnodes.ast.Dict;
import org.jpp.astnodes.ast.List;
import org.jpp.astnodes.ast.Num;
import org.jpp.astnodes.ast.Str;
import org.jpp.astnodes.base.expr;
import org.jpp.heart.AstList;
import org.jpp.heart.PyComplex;
import org.jpp.heart.PyFloat;
import org.jpp.heart.PyInteger;
import org.jpp.heart.PyLong;
import  refactoringminer.python.pyerrors.NodeNotFoundException;


import java.util.HashSet;
import java.util.Set;

public class TypeApproximator {
    private static Logger log = Logger.getLogger(TypeApproximator.class);
    public static Type getSimpleTypeApproximation(AST ast, expr expression){
        try {
            String s = getTypeString(ast, expression);
            if (s!=null){
                return TypeStringToJDT.getJDTType(ast, s, 0);
            }
            else {
                log.debug("Unable to approximate the type");
                return null;
            }
        } catch (NodeNotFoundException e) {
            e.printStackTrace();
        }
        log.warn("Unable to approximate the type");
        return null;
    }

    private static String getTypeString(AST ast, expr node){
        if (node instanceof List){
            String typeString = getTypeString(ast, (List) node);
            if (typeString!=null){
                String listString = "List[";
                listString+=typeString;
                return listString+"]";
            }
        }
        else if (node instanceof Dict){
            String typeString = getTypeString(ast, (Dict) node);
            if (typeString!=null){
                String listString = "Dict[";
                listString+=typeString;
                return listString+"]";
            }
        }
        else if (node instanceof org.jpp.astnodes.ast.Set){
            String typeString = getTypeString(ast, (org.jpp.astnodes.ast.Set) node);
            if (typeString!=null){
                String listString = "Set[";
                listString+=typeString;
                return listString+"]";
            }

        }
        return null;
    }

    private static String getTypeString(AST ast, List node){
        return getTypeStringFromAstList(ast, (AstList) node.getElts());

    }

    private static String getTypeString(AST ast, org.jpp.astnodes.ast.Set node){
        return getTypeStringFromAstList(ast, (AstList)node.getElts());

    }

    private static String getTypeString(AST ast, Dict node){
        String typeKeys = getTypeStringFromAstList(ast, (AstList) node.getKeys());
        String typeValues = getTypeStringFromAstList(ast, (AstList) node.getValues());

        if (typeKeys!=null && typeKeys.equals("float")){
            typeKeys="Float";
        }
        else if (typeKeys!=null && typeKeys.equals("int")){
            typeKeys+="Integer";
        }
        if (typeValues!=null && typeValues.equals("float")){
            typeValues="Float";
        }
        else if (typeValues!=null && typeValues.equals("int")){
            typeValues+="Integer";
        }

        if (typeKeys!=null && typeValues!=null){
            return typeKeys +", "+typeValues;
        }
        else
            return null;
    }


    private static String getTypeStringFromAstList(AST ast, AstList node){
        Set<String> hasString =  new HashSet<>();
        for (Object elt :  node) {

            String s = typeString(ast, (expr) elt);
            if (s!=null){
                hasString.add(typeString(ast,(expr) elt));
            }
        }
        if (hasString.size()==0){
            return null;
        }
        else if (hasString.size()==1){
            return hasString.iterator().next();
        }
        else {
            if (hasString.size()==0){
                return null;
            }
            StringBuilder types = new StringBuilder("Union[");
            int number = 0;
            for (String s : hasString) {
                number+=1;
                if (number==hasString.size()){
                    types.append(s);
                }
                else{
                    types.append(s).append(",");
                }
            }
            return types.toString()+"]";
        }
    }


    private static String typeString(AST ast,expr expression){
        if (expression instanceof Str){
            return "String";
        }
        else if (expression instanceof Num){
            if(((Num)expression).getN() instanceof PyFloat){
                return "float";
            }
            else if (((Num)expression).getN() instanceof PyInteger){
                return "int";
            }
            else if (((Num)expression).getN() instanceof PyLong){
                return "int";
            }
            else if (((Num)expression).getN() instanceof PyComplex){
                return "PyComplex";
            }
            else{
                log.warn(((Num) expression).getN().getClass());
                assert false;
            }

        }
        else if (expression instanceof List){
            return getTypeString (ast,expression);
        }
        else if (expression instanceof org.jpp.astnodes.ast.Set){
            return getTypeString (ast,expression);
        }
        else if (expression instanceof Dict){
            return getTypeString (ast,expression);
        }

        return null;
    }
}
