 //Elaborado por: Ramirez Contreras Angel Humberto (5CV1) y Diaz Rosales Mauricio Yael (3CV15)
package mx.ipn.escom.compiladores;

import java.util.Stack;

public class SQL_Ascendente {
    private final Stack<Integer> stateStack;
    private final Stack<String> symbolStack;
    private int[][] actionTable;
    private int[][] gotoTable;

    public SQL_Ascendente() {
        stateStack = new Stack<>();
        symbolStack = new Stack<>();
        // Inicializa las tablas de acción y desplazamiento (shift) y las tablas de estado-goto
        initializeTables();
    }

    public void parse(String input) throws Error_Sintaxis {
        int index = 0;
        stateStack.push(0);

        while (index < input.length()) {
            int currentState = stateStack.peek();
            char currentSymbol = input.charAt(index);

            int action = actionTable[currentState][getSymbolIndex(currentSymbol)];

            if (action > 0) {
                // Desplazamiento (shift)
                stateStack.push(action);
                symbolStack.push(String.valueOf(currentSymbol));
                index++;
            } else if (action < 0) {
                // Reduce
                int production = -action;
                reduce(production);
            } else {
                // Error de sintaxis
                throw new Error_Sintaxis("Error de sintaxis en el indice " + index);
            }
        }

        // Verifica si la pila está en un estado final válido
        if (stateStack.peek() == 1 && index == input.length()) {
            System.out.println("Sintaxis correcta.");
        } else {
            throw new Error_Sintaxis("Error de sintaxis en el indice " + index);
        }
    }

    private void reduce(int production) {
        switch (production) {
            case 1 -> {

                System.out.println("Reducción de la cláusula SELECT");
            }
            case 2 -> {

                System.out.println("Reducción de la cláusula FROM");
            }
            case 3 -> {

                System.out.println("Reducción de la cláusula WHERE");
            }

        }

        int productionLength = getProductionLength(production);
        for (int i = 0; i < productionLength; i++) {
            stateStack.pop();
            symbolStack.pop();
        }

        char symbol = getNonTerminalSymbol(production);
        int newState = gotoTable[stateStack.peek()][getSymbolIndex(symbol)];
        stateStack.push(newState);
        symbolStack.push(String.valueOf(symbol));
    }

    private int getSymbolIndex(char symbol) {
        return switch (symbol) {
            case 'S' -> 0;
            case 'E' -> 1;
            case 'F' -> 2;
            case 'W' -> 3;
            default -> -1;
        }; // Símbolo no reconocido
    }

    // Retorna el símbolo no terminal para una producción
    private char getNonTerminalSymbol(int production) {
        return switch (production) {
            case 1 -> 'S';
            case 2 -> 'E';
            case 3 -> 'F';
            case 4 -> 'W';
            default -> '\0';
        };
    }

    private void initializeTables() {
        throw new UnsupportedOperationException("No compatible.");
    }

    private int getProductionLength(int production) {
        throw new UnsupportedOperationException("No compatible."); 
    }
}