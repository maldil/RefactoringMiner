package  refactoringminer.python.typeinference.core;

public class TypeDecNeeds {
    String name;
    int row;
    int col_offset;

    public String getName() {
        return name;
    }

    public int getRow() {
        return row;
    }

    public int getCol_offset() {
        return col_offset;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol_offset(int col_offset) {
        this.col_offset = col_offset;
    }
}
