package  refactoringminer.python.typeinference.core;

public class TypeASTNode {
    private int lineNumber;
    private int col_offset;
    private String nodeName;
    private String type;

    public TypeASTNode(int lineNumber, int col_offset, String nodeName, String type) {
        this.lineNumber = lineNumber;
        this.col_offset = col_offset;
        this.nodeName = nodeName;
        this.type = type;
    }

    public TypeASTNode(int lineNumber, int col_offset, String nodeName) {
        this.lineNumber = lineNumber;
        this.col_offset = col_offset;
        this.nodeName = nodeName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getCol_offset() {
        return col_offset;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setCol_offset(int col_offset) {
        this.col_offset = col_offset;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return "AST Node [Line number=" + lineNumber + ", Variable name=" + nodeName + ", Column offset=" + col_offset +", Type=" + type +"]";
//        return "Assert.assertEquals(information.get(new core.TypeASTNode(" + lineNumber + ", " + col_offset + ", \"" + nodeName +"\")),\"" + type+"\");";
    }
    @Override
    public int hashCode() {
        return (lineNumber+nodeName+col_offset).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TypeASTNode other = (TypeASTNode) obj;
        if (!(lineNumber + nodeName+col_offset).equals(other.lineNumber + other.nodeName + other.col_offset))
            return false;
        return true;
    }
}
