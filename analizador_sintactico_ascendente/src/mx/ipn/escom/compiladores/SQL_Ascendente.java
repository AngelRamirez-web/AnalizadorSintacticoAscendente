 //Elaborado por: Ramirez Contreras Angel Humberto (5CV1) y Diaz Rosales Mauricio Yael (3CV15)
package mx.ipn.escom.compiladores;

import java.util.Scanner;
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
    
    
    public static void main(String[] args) {
        SQL_Ascendente parser = new SQL_Ascendente();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese una cadena SQL: ");
        String input = scanner.nextLine();

        try {
            parser.parse(input);
            System.out.println("Analisis sintactico exitoso.");
        } catch (SQL_Ascendente.Error_Sintaxis e) {
            System.out.println("Error de sintaxis: " + e.getMessage());
        }
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

    private void reduce(int production) throws Error_Sintaxis {
        switch (production) {
            case 1 -> System.out.println("Reduccion de la clausula SELECT");
            case 2 -> System.out.println("Reduccion de la clausula FROM");
            case 3 -> System.out.println("Reduccion de la clausula WHERE");
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

private int getSymbolIndex(char symbol) throws Error_Sintaxis {
    switch (symbol) {
        case 'S' -> {
            return 0;
            }
        case 'E' -> {
            return 1;
            }
        case 'F' -> {
            return 2;
            }
        case 'W' -> {
            return 3;
            }
        default -> throw new Error_Sintaxis("Simbolo no reconocido: " + symbol);
    }
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
        // Inicializa las tablas de acción y desplazamiento (shift) y las tablas de estado-goto

        // Tabla de acción
        actionTable = new int[][] {
            // Estado 0
            { 2, 0, 0, 0 }, // Acciones para los símbolos S, E, F, W 
            // Estado 1
            { 0, 3, 0, 0 }, // Acciones para los símbolos S, E, F, W 
            // Estado 2
            { 0, 0, 0, 0 }, // Acciones para los símbolos S, E, F, W 
        };

        // Tabla de estado-goto
        gotoTable = new int[][] {
            // Estado 0
            { 1, 0, 0, 0 }, // Transiciones para los símbolos S, E, F, W
            // Estado 1
            { 0, 0, 0, 0 }, // Transiciones para los símbolos S, E, F, W
        };
    }

    private int getProductionLength(int production) {
        return switch (production) {
            case 1 -> 1; // Longitud de la producción 1
            case 2 -> 1; // Longitud de la producción 2
            case 3 -> 1; // Longitud de la producción 3
            default -> 0; // Producción no reconocida
        };
    }

    private static class Error_Sintaxis extends Exception {
        public Error_Sintaxis(String message) {
            super(message);
        }
    }
}
