package  refactoringminer.python.pyvisitors;

import org.apache.log4j.Logger;
import org.jpp.astnodes.Visitor;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.ast.Tuple;
import org.jpp.astnodes.base.expr;
import org.jpp.heart.AstList;
import  refactoringminer.python.typeinference.core.TypeDecNeeds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PyVisitor extends Visitor {
    static Logger logger = Logger.getLogger(Visitor.class);
    private ArrayList<Import> imports = new ArrayList<Import>();
    private Set<Assign> assignment = new HashSet<Assign>();
    private Set<Name> name= new HashSet<Name>();
    private Set<Attribute> attributes= new HashSet<Attribute>();
    private Set<TypeDecNeeds> typeDecNeeds = new HashSet<TypeDecNeeds>();
    private HashMap<String, org.eclipse.jdt.core.dom.Name> import_as_names;
    public PyVisitor(HashMap<String, org.eclipse.jdt.core.dom.Name> import_nodes) {
        import_as_names=import_nodes;
    }

    @Override
    public Object visitImport(Import node) throws Exception {
        imports.add(node);
        return super.visitImport(node);
    }

    @Override
    public Object visitAssign(Assign node) throws Exception {
        for (Object ch : (AstList) node.getTargets()){
            if (ch instanceof Name){
                TypeDecNeeds typeDecNode = new TypeDecNeeds();
                typeDecNode.setName(((Name) ch).getInternalId());
                typeDecNode.setRow(((Name) ch).getLine());
                typeDecNode.setCol_offset(((Name) ch).getCol_offset());
                typeDecNeeds.add(typeDecNode);
            }
            if (ch instanceof Tuple){
                for (expr internalElt : ((Tuple) ch).getInternalElts()) {
                    if (internalElt instanceof Name) {
                        TypeDecNeeds typeDecNode = new TypeDecNeeds();
                        typeDecNode.setName(((Name) internalElt).getInternalId());
                        typeDecNode.setRow((internalElt).getLine());
                        typeDecNode.setCol_offset(((Name) internalElt).getCol_offset());
                        typeDecNeeds.add(typeDecNode);
                    }
                }
            }
            assignment.add(node);
        }


        return super.visitAssign(node);
    }

    @Override
    public Object visitName(Name node) throws Exception {
        name.add(node);
        return super.visitName(node);
    }

    @Override
    public Object visitAttribute(Attribute node) throws Exception {
        attributes.add(node);
        if (node.getInternalValue() instanceof Name){
            if (import_as_names.get(((Name) node.getInternalValue()).getInternalId())==null && !((Name) node.getInternalValue()).getInternalId().equals("self")){
                TypeDecNeeds typeDecNode = new TypeDecNeeds();
                typeDecNode.setName(((Name) node.getInternalValue()).getInternalId());
                typeDecNode.setRow(( node.getInternalValue()).getLine());
                typeDecNode.setCol_offset(((Name) node.getInternalValue()).getCol_offset());
                typeDecNeeds.add(typeDecNode);
            }

        }
        return super.visitAttribute(node);
    }


    public Set<TypeDecNeeds> getTypeDecNeeds() {

        return typeDecNeeds;
    }

    public ArrayList<Import> getImports() {
        return imports;
    }

    public Set<Assign> getAssignment() {
        return assignment;
    }

    public Set<Name> getName() {
        return name;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<Import> getImport(){
        return imports;
    }
}
