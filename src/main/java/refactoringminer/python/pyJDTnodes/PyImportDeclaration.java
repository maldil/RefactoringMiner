package  refactoringminer.python.pyJDTnodes;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.SimpleName;

public class PyImportDeclaration extends ImportDeclaration {
    private Name asName = null;
    private AST astc = null;
    public static final ChildPropertyDescriptor AS_NAME_PROPERTY = new ChildPropertyDescriptor(ImportDeclaration.class, "asname", Name.class, false, false);

    public PyImportDeclaration(AST ast) {
        super(ast);
        astc=ast;
    }

    public void setasName(Name name) {
        if (name == null) {
            throw new IllegalArgumentException();
        } else {
            ASTNode oldChild = this.asName;
            preReplaceChildFromChild(oldChild, name, NAME_PROPERTY);
            this.asName = name;
            postReplaceChildFromChild(oldChild, name, NAME_PROPERTY);
        }
    }

    public Name getasName() {
        if (this.asName == null) {
            synchronized(this) {
                if (this.asName == null) {
                    preLazyInitFromChild();
                    this.asName = astc.newQualifiedName(new SimpleName(astc), new SimpleName(astc));
                    postLazyInitFromChild(this.asName, NAME_PROPERTY);
                }
            }
        }

        return this.asName;
    }

    @Override
    public ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
        if (property == AS_NAME_PROPERTY) {
            if (get) {
                return this.getasName();
            } else {
                this.setasName((Name)child);
                return null;
            }
        } else {
            return super.internalGetSetChildProperty(property, get, child);
        }
    }
}
