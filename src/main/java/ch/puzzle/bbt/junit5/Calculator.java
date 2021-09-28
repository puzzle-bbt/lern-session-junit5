package ch.puzzle.bbt.junit5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Calculator {
    String instanceName = "default Calculator";

    MathLibrary mathLibrary = new MathLibrary();
    ListMathLibrary listMathLibrary = new ListMathLibrary();

    Map<String, Integer> resultatMap = new HashMap<>();

    public Calculator() {
    }

    public Calculator(String name) {
        instanceName = name;
    }

    public void setName(String name) throws Exception {
        if (name == null) {
            throw new Exception("the name is null");
        }
        this.instanceName = name;
        mathLibrary.setName("MathLibrary_" + name);
    }

    public Integer sum(Integer firstValue, Integer secondValue) throws Exception {
        if (firstValue == null) {
            throw new Exception("first value is null");
        }
        if (secondValue == null) {
            throw new Exception("second value is null");
        }
        return firstValue + secondValue;
    }

    public int multiply(Integer firstValue, Integer secondValue) throws Exception {
        if (firstValue == null) {
            throw new Exception("first value is null");
        }
        if (secondValue == null) {
            throw new Exception("second value is null");
        }
        return mathLibrary.multiply(firstValue, secondValue);
    }

    public Integer sumNumbers(Integer... numbers) {
        Arrays.stream(numbers).forEach(number -> listMathLibrary.addNumber(number));
        return listMathLibrary.sumNumbers();
    }

    public void putResult(String name, Integer result) throws Exception {
        if (name == null) {
            throw new Exception("the name is null");
        }
        if (result == null) {
            throw new Exception("the result is null");
        }
        this.resultatMap.put(name, result);
    }
}
