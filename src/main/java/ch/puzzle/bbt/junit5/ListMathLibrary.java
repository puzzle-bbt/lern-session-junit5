package ch.puzzle.bbt.junit5;

import java.util.ArrayList;
import java.util.List;

public class ListMathLibrary {

    String name = "standard ListMathLibrary";

    List<Integer> numberList = new ArrayList<>();

    public void setName(String name) throws Exception {
        if(name == null) {
            throw new Exception("the name is null");
        }
        this.name  = name;
    }

    public void addNumber(Integer number) {
        numberList.add(number);
    }

    public Integer sumNumbers() {
        return numberList.stream().mapToInt(i -> i).sum();
    }
}
