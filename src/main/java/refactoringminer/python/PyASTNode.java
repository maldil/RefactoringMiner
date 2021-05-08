package  refactoringminer.python;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;

public abstract class PyASTNode extends ASTNode {
    public static final int WITH_STATEMENT = 104;
    public static final int ASYNC_FOR__STATEMENT = 105;
    public static final int ASYNC_FUNC__STATEMENT = 106;
    public static final int ASYNC_WITH__STATEMENT = 107;
    public static final int LIST_COMPREHENSION = 108;
    public static final int DICT_COMPREHENSION = 109;
    public static final int GENERATOR = 109;
    public PyASTNode(AST ast) {
        super(ast);
    }

}
