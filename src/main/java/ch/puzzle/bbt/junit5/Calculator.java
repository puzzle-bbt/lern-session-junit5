package ch.puzzle.bbt.junit5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Calculator {
    String instanceName = "default Calculator";

    MathLibrary mathLibrary = new MathLibrary();
    ListMathLibrary listMathLibrary = new ListMathLibrary();

    final Map<String, Integer> resultatMap = new HashMap<>();

    public Calculator() {
    }

    public Calculator(String name) {
        instanceName = name;
    }

    public void setName(String name) throws BusinessException {
        if (name == null) {
            throw new BusinessException("the name is null");
        }
        this.instanceName = name;
        mathLibrary.setName("MathLibrary_" + name);
    }

    public Integer sum(Integer firstValue, Integer secondValue) throws BusinessException {
        if (firstValue == null) {
            throw new BusinessException("first value is null");
        }
        if (secondValue == null) {
            throw new BusinessException("second value is null");
        }
        return firstValue + secondValue;
    }

    public int multiply(Integer firstValue, Integer secondValue) throws BusinessException {
        if (firstValue == null) {
            throw new BusinessException("first value is null");
        }
        if (secondValue == null) {
            throw new BusinessException("second value is null");
        }
        return mathLibrary.multiply(firstValue, secondValue);
    }

    public Integer sumNumbers(Integer... numbers) {
        listMathLibrary.reset();
        Arrays.stream(numbers).forEach(number -> listMathLibrary.addNumber(number));
        return listMathLibrary.sumNumbers();
    }

    public void putResult(String name, Integer result) throws BusinessException {
        if (name == null) {
            throw new BusinessException("the name is null");
        }
        if (result == null) {
            throw new BusinessException("the result is null");
        }
        this.resultatMap.put(name, result);
    }

    public Integer sumAllResults() {
        return sumNumbers(this.resultatMap.values().toArray(Integer[]::new));
    }
}
