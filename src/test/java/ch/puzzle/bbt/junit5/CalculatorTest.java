package ch.puzzle.bbt.junit5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorTest {

    @InjectMocks Calculator calculator;
    @Mock MathLibrary mathLibrary;
    @Mock ListMathLibrary listMathLibrary;

    @BeforeEach
    void setUp() {
        calculator.mathLibrary = mathLibrary;
        calculator.listMathLibrary = listMathLibrary;
    }


    /**
     * Verify local variables.
     */
    @Test
    void testResult() throws Exception {
        assertEquals(calculator.resultatMap.size(), 0);
        calculator.putResult("Summe 1", 35);
        assertEquals(calculator.resultatMap.size(), 1);
        assertEquals(calculator.resultatMap.get("Summe 1"), 35);
    }
    @Test
    void testResult2() throws Exception {
//        when(calculator)
        assertEquals(calculator.resultatMap.size(), 0);
        calculator.putResult("Summe 1", 35);
        assertEquals(calculator.resultatMap.size(), 1);
    }

    /**
     * Simple test.
     */
    @Test
    void testSum() throws Exception {
        // given
        // when
        int result = calculator.sum(3, 2);
        // then
        assertEquals(5, result);
    }

    /**
     * Test for a exception.
     */
    @Test
    void testSum_withException() {
        // given
        // when
        Exception exception = assertThrows(Exception.class, () ->
                calculator.sum(null, 2));
        assertEquals("first value is null", exception.getMessage());
    }

    /**
     * Define a exception for a method, that has a return value.
     */
    @Test
    void defineExceptionForAMethod() {
        // given
        when(mathLibrary.multiply(any(), any())).thenThrow(new RuntimeException("TestException"));
        // when
        Exception exception = assertThrows(Exception.class, () ->
                calculator.multiply(1, 2));
        assertEquals("TestException", exception.getMessage());
    }

    /**
     * Define a exception for a method, that return void.
     */
    @Test
    void defineExceptionForAMethodThatReturnVoid() throws Exception {
        // given
        doThrow(new Exception("TestException")).when(mathLibrary).setName(any());
        // when
        Exception exception = assertThrows(Exception.class, () ->
            calculator.setName("theName"));
        assertEquals("TestException", exception.getMessage());
    }

    /**
     * Prepare the mock for the math library implementation.
     */
    @Test
    void oneWhenCondition() throws Exception {
        // given
        when(mathLibrary.multiply(any(Integer.class), anyInt())).thenReturn(6);
        // when
        int result = calculator.multiply(3, 8);
        // then
        assertEquals(6, result);
        // Don't work:
        // verify(mathLibrary, times(1)).multiply(anyInt(), 8);
        // Because: When using matchers, all arguments have to be provided by matchers.
        verify(mathLibrary, times(1)).multiply(anyInt(), eq(8));
    }

    /**
     * Use more when conditions for the same mock.
     */
    @Test
    void moreWhenConditions() throws Exception {
        // given
        when(mathLibrary.multiply(anyInt(), anyInt())).thenReturn(6);
        when(mathLibrary.multiply(3, 3)).thenReturn(9);
        // when then
        assertEquals(9, calculator.multiply(3, 3));
        assertEquals(6, calculator.multiply(5, 5)); // is true
    }

    /**
     * Use stubbing when conditions for the same mock.
     */
    @Test
    void sequenceOfWhenConditions() throws Exception {
        // given
        when(mathLibrary.multiply(anyInt(), anyInt()))
                .thenReturn(6)
                .thenReturn(9)
                .thenReturn(42);
        // when then
        assertEquals(6, calculator.multiply(3, 3));
        assertEquals(9, calculator.multiply(5, 5));
        assertEquals(42, calculator.multiply(2, 1)); // is also true
        assertEquals(42, calculator.multiply(3, 1)); // yes 3x1 is also 42
        assertEquals(42, calculator.multiply(4, 4)); // 42 is always right :-)
    }

    /**
     * Use stubbing doNothin and doThrow conditions for the same mock.
     */
    @Test
    void sequenceOfConditions() throws Exception {
        // given
        doNothing()
        .doThrow(new RuntimeException("Test RuntimeException"))
        .doNothing()
        .when(mathLibrary).setName("Test");
        // when then
        mathLibrary.setName("Test");
        assertThrows(RuntimeException.class, () -> mathLibrary.setName("Test"));
        mathLibrary.setName("Test");
        mathLibrary.setName("Test");
    }

    /**
     * Use a dummy implementation for prepare the mock.
     */
    @Test
    void testMultiplication_withDummyImplementation() throws Exception {
        // given
        when(mathLibrary.multiply(anyInt(), anyInt())).thenAnswer(answer -> {
            int v1 = answer.getArgument(0);
            int v2 = answer.getArgument(1);
            return v1 * v2 - 2;
        });
        // when then
        assertNotEquals(9, calculator.multiply(3, 3)); // is false
        assertEquals(7, calculator.multiply(3, 3)); // is true
        assertEquals(23, calculator.multiply(5, 5)); // is true
    }

    /**
     * Verify the invocations of the library.
     */
    @Test
    void verivyNumberOfInvocations() throws Exception {
        // given
        when(listMathLibrary.sumNumbers()).thenReturn(22);
        // when
        int result = calculator.sumNumbers(2,5,5,10);
        // then
        assertEquals(22, result);
        verifyNoInteractions(mathLibrary);

        verify(listMathLibrary, never()).setName("two");
        verify(listMathLibrary, times(0)).setName(anyString());
        // verifyNoMoreInteractions(listMathLibrary); // -> fail
        verify(listMathLibrary, times(4)).addNumber(anyInt());
        verifyNoMoreInteractions(listMathLibrary); // -> true

        verify(listMathLibrary, atLeastOnce()).addNumber(5);
        verify(listMathLibrary, atLeast(1)).addNumber(5);
        verify(listMathLibrary, atLeast(2)).addNumber(5);
        verify(listMathLibrary, atMost(2)).addNumber(5);
        verify(listMathLibrary, atMost(3)).addNumber(5);
        verify(listMathLibrary, atMost(4)).addNumber(any());
    }

    @Test
    void verivyNoMoreInteractions() {
        // given
        when(listMathLibrary.sumNumbers()).thenReturn(22);
        // when
        calculator.sumNumbers(2, 5, 5, 10);
        // then
        verify(listMathLibrary, times(1)).sumNumbers();
        verify(listMathLibrary, times(4)).addNumber(anyInt());
        verifyNoMoreInteractions(listMathLibrary);
    }

    @Test
    void verivyInOrderOfInvocations() {
        // given
        when(listMathLibrary.sumNumbers()).thenReturn(22);
        // when
        int result = calculator.sumNumbers(2,5,5,10);
        // then
        assertEquals(22, result);
        verifyNoMoreInteractions(mathLibrary);

        InOrder inOrder = inOrder(listMathLibrary);
        inOrder.verify(listMathLibrary).addNumber(2);
        inOrder.verify(listMathLibrary, calls(2)).addNumber(5);
        inOrder.verify(listMathLibrary).addNumber(10);
        inOrder.verify(listMathLibrary).sumNumbers();

        verify(listMathLibrary).addNumber(10);
        verify(listMathLibrary).addNumber(2);
    }

    @Test
    void verifyMethodArguments() throws Exception {
        // given
        // when
        calculator.setName("Hugo");
        // then
        verify(mathLibrary, times(1)).setName("MathLibrary_Hugo");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mathLibrary, times(1)).setName(argumentCaptor.capture());
        assertEquals("MathLibrary_Hugo", argumentCaptor.getValue());
    }

    @Test
    void verifyMethodArgumentsWithArgumentThat() throws Exception {
        // given
        // when
        calculator.setName("Hugo");
        calculator.setName("Fritz");

        verify(mathLibrary, times(2)).setName(argThat(name ->
                name.startsWith("MathLibrary_")));
    }

    @Test
    @Disabled(value = "an disabled test")
    void disabledTest() {
        // this test is disabled
        fail("this test fail always");
    }
}