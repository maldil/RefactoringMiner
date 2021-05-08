package  refactoringminer.python;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import java.util.ArrayList;
//import org.jpp.astnodes.AnalyzingParser;

public class PyCompilationUnit extends CompilationUnit{
    private ArrayList<FieldDeclaration> global_variables = new ArrayList<>();
    PyCompilationUnit(AST ast){
        super(ast);
    }

    public void setGlobal_variables(FieldDeclaration global_variables) {
        this.global_variables.add(global_variables);
    }

    public ArrayList<FieldDeclaration> getGlobal_variables() {
        return global_variables;
    }

}
