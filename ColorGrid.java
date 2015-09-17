/**
 * HackerRank Problem - https://www.hackerrank.com/contests/noi-ph/challenges/color-grid
 */
 
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    // Define a class for Operation
    //
    // For the sake of time, I have kept the inputs
    // public. Otherwise the attributes are protected/private
    // with public getters and setters
    //
    public static class Operation {
        public String type; // COL or ROW - Maybe make this an enum? It wouldn't matter because even to
                            // make an enum from String input, we'll need to compare the String to a 
                            // value at least once, so instead of enum, we just compare the type String
                            // when we want to do an operation.
        public int value; // Value of row or column
        public int color; // Color value
    }
    
    // Color Grid - Two Dimensional Array
    private int[][] grid;
    
    // Number of Rows
    private int rows;
    
    // Number of Columns - Even though rows = columns for this problem, this will make the code
    // extensible should we want to support N x M Grid
    private int columns;
    
    // Number of Operations
    private int numOperations;
    
    // LinkedHashMap of Operations - I chose LinkedHashMap because:
    // 1. The order of operations is important for this problem. So I could have used
    //    a List, but because of #2 below, I chose LinkedHashMap
    //
    // 2. Since the number of P (operations) is much greater than Grid Size (N), a
    //    majority of operations will be just overwritten by future operations. So I chose
    //    to keep operations as a LinkedHashMap to preserve order and to do constant time
    //    lookup and removal
    //
    //
    // Key --> String which is combination of type (i.e. ROW or COL) + Number of ROW/COL
    // Value --> Operation to perform
    //
    private LinkedHashMap<String, Operation> operations;
    
    /**
     * Main Method
     */
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution.*/
        
        // Create a new Solution object - I don't like keeping methods and members
        // static, so better to work with an object instead of making all methods
        // and members static
        Solution solution = new Solution();
        
        boolean gridInitialized = false;
        solution.operations = new LinkedHashMap<String, Operation> ();
        
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()) {
            // Initialize the Grid first
            if(! gridInitialized) {
                String gridSize = in.nextLine();
                
                // Initialize the COlor Grid
                gridInitialized = solution.initializeGrid(gridSize);
                
                // If we failed to initialize the Grid, return to stop
                // execution
                if(! gridInitialized) {
                    return;
                }
            }
            
            // Grid has been initialized - Read other inputs
            else {
                String strOperation = in.nextLine();
                Operation operation = solution.operationFromString(strOperation);
                if(operation != null) {
                    String key = operation.type + operation.value; // ROWX or COLX is the key
                    
                    // If LinkedHashMap already has this operation, then
                    // remove the existing object from Map, and then put 
                    // the new object in Map so that the order of execution
                    // is maintained
                    if(solution.operations.containsKey(key)) {
                        solution.operations.remove(key);
                    }
                    
                    // Add the operation to the Map
                    solution.operations.put(key, operation);
                }
            }
        }
        
        // We have read all the input. Now perform the operations
        //
        // Get key set from Map
        Collection<String> keys = solution.operations.keySet();
        
        // Iterate over the keys, and do operations
        for(String str : keys) {
            Operation opr = solution.operations.get(str);
            solution.doOperation(opr);
        }
        
        // All operations have been completed, calculate the sum
        // and print it ou
        System.out.println(solution.calculateSum());
    }
    
    /**
     * Method to initialize the Color grid
     * and other attributes
     */
    private boolean initializeGrid(String gridSize) {
        boolean success = false;
        
        // Return false if input String is null or empty
        if(gridSize == null ||
            gridSize.isEmpty()) {
            return false; // return hard-coded false instead of returning valus of 'success' here to make it clear
        }
        
        // Proceed
        // Tokenize on one or more whitespaces
        String[] tokens = gridSize.split("\\s+");
        
        // Only proceed if we have at least 2 values
        // in the tokenized array
        if(tokens.length >= 2){
            // If the input values in token are not int values
            // catch the exception and return
            try {
                // Rows and Columns
                rows = Integer.parseInt(tokens[0]);
                columns = rows;
                
                // We don't really need/use numOperations (which is P)
                numOperations = Integer.parseInt(tokens[1]);
                
                // Create the Grid        
                grid = new int[rows][columns];
                success = true;
            }
            catch (Exception e) {
                // Nothing to do - Maybe log if this were in real world
            }
        }
        
        return success;
    }
    
    /**
     * Method to create an Operation object from
     * input String.
     */
    private Operation operationFromString(final String opString) {
        Operation operation = null;
        // Check if operation String is not null and not empty
        if(opString != null &&
            ! opString.isEmpty()) {
            // Tokenize operation String
            String[] operationTokens = opString.split("\\s+");
            try {
                // Since we are doing the array index and
                // type conversions in a try, it will be handled in the Exception
                String type = operationTokens[0];
                int value = Integer.parseInt(operationTokens[1]);
                int color = Integer.parseInt(operationTokens[2]);
                
                // Create an operation object as there hasn't 
                // been any exception, so we are good to go
                operation = new Operation();
                operation.type = type;
                operation.value = value;
                operation.color = color;
            }
            catch (Exception e) {
                // Nothing to do - Maybe log if this were in real world
            }
        }
        
        return operation;
    }
    
    /**
     * Method to perform a Color operation
     * on a row or a column based on the type
     *
     * NOTE: Keep in mind that value of I is One-based, not Zero-based
     * while arrays are Zero-based, so we'll have to compensate for 
     * that.
     */
    private void doOperation(final Operation operation) {
        if(operation == null) {
            return;
        }
        
        // Decrement the value of I (row or column) because
        // this value is passed as One-based while an array is
        // defined as Zero-based
       
        // No need for error checking on 'value'
        // because 1 <= I <= N
        int zbValue = operation.value - 1; // Zero-based value
        
        // Column 'Coloring'
        if(operation.type.equalsIgnoreCase("COL")) {
            for(int i = 0; i < rows; i++) {
                grid[i][zbValue] = operation.color;
            }
        }
        
        // Row 'Coloring'
        else {
            for(int i = 0; i < columns; i++) {
                grid[zbValue][i] = operation.color;
            }
        }
    }
    
    /**
     * Method to calculate the Sum of colors in the
     * grid
     */
    private long calculateSum() {
        long sum = 0;
        
        for(int i=0; i<rows; i++) {
            for(int j=0; j< columns; j++) {
                sum += grid[i][j];
            }
        }
        
        return sum;
    }
}