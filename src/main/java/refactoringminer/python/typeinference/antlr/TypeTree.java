package  refactoringminer.python.typeinference.antlr;

import org.antlr.runtime.tree.CommonTree;

public class TypeTree  {
    private CommonTree tree;
    private boolean error;

    public TypeTree(CommonTree tree) {
        this.tree=tree;
    }


    public void setTree(CommonTree tree) {
        this.tree = tree;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public CommonTree getTree() {
        return tree;
    }

    public boolean isError() {
        return error;
    }
}
