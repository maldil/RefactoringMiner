package  refactoringminer.python.typeinference.antlr;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;



public class TypeInfo {
    static Logger logger = Logger.getLogger(TypeInfo.class);
    public TypeTree getTypeTree(String typeString) throws RecognitionException {
        PyTypeLexer lexer = new PyTypeLexer(new ANTLRStringStream(typeString));
        PyTypeParser parser = new PyTypeParser(new CommonTokenStream(lexer));
        CommonTree tree = parser.parse().getTree();
        TypeTree typeTree = new TypeTree(tree);
        typeTree.setError(errors(tree));
        if (typeTree.isError()){
            logger.error("Type Parsing Error for : "+typeString);
        }
        return typeTree;
    }

    public boolean errors(CommonTree tree){
        if (tree instanceof CommonErrorNode)
            return true;
        else
            if (tree.getChildren()!=null){
                for (Object child : tree.getChildren()) {
                    return errors((CommonTree)child);
                }
            }
        return false;
    }

}
