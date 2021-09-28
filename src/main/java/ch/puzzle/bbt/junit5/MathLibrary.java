package ch.puzzle.bbt.junit5;

public class MathLibrary {

    String name = "standard MathLibrary";

    Integer multiply(Integer value1, Integer value2) {
        return value1 * value2;
    }

    public void setName(String name) throws BusinessException {
        if(name == null) {
            throw new BusinessException("the name is null");
        }
        this.name  = name;
    }
}
