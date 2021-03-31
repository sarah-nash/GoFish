/******************************************************************************
 *  Compilation:  javac LifeProcedural.java
 *  Execution:    java LifeProcedural [int grid size] [double population density]
 *
 *  John Conway's Game of Life: procedural implementation (not object-oriented)
 *
 *  % java LifeProcedural 20 0.33
 *      Creates a 20x20 grid of cells, about 33% of which are occupied at startup
 *
 * @author Caitrin Eaton
 *
 ******************************************************************************/

import java.util.Random;    // for initializing the population
import javax.swing.*;       // for animating the population over time, in a graphics window
import java.awt.*;

public class LifeProcedural {

    /**
     * Visualize the grid in a graphics window.
     * @param grid          the boolean[][] that represents the world in which occupied cells are true, unoccupied false
     * @param generation    the integer generation number, ie the number of times the simulation has iterated
     * @param flux          the number of cells that changed during this generation
     * @param window        the JFrame graphics window in which the grid will be displayed
     * @param visGrid      the JPanel[][] set of rectangles that represents the visualized population
     */
    public static void visualizeGrid(boolean[][] grid, int generation, int flux, JFrame window, JPanel[][] visGrid) {
        // Use the window's title to track the number of generations that have passed and the current flux
        window.setTitle( "Functional Life: Generation " + generation + ", Flux " + flux );

        // Configure the color of each cell according to its state: occupied or unoccupied
        int gridSize = grid.length;
        long maxDelayMS = 500;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++ ){
                if ( grid[row][col] ) {
                    visGrid[row][col].setBackground(Color.BLACK);  // color occupied cells black
                } else {
                    visGrid[row][col].setBackground(Color.WHITE);  // color unoccupied cells white
                }
            }
        }
        window.repaint(maxDelayMS);
        try {
            Thread.sleep(maxDelayMS);
        } catch ( InterruptedException e ){ } // Timing a little off. Just don't worry about it.
    }

    /**
     * Configure the graphics window in which the population will be visualized.
     * @param grid          the boolean[][] that represents the world in which occupied cells are true, unoccupied false
     * @param window        the JFrame graphics window in which the grid will be displayed
     * @param vis_grid      the JPanel[][] set of rectangles that represents the visualized population
     */
    public static void initGraphics(boolean[][] grid, JFrame window, JPanel[][] vis_grid ) {
        // Determine the size of the window
        int gridSize = grid.length;
        int cellSize = 20;
        int windowSize = gridSize * cellSize;

        // Configure the graphics window
        window.setTitle("Functional Life: Generation ?, Flux ?");
        window.setSize(windowSize, windowSize);
        window.setLayout( new GridLayout(gridSize, gridSize) );

        // Configure the grid of rectangles that visualize each cell
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++ ){
                vis_grid[row][col] = new JPanel();
                vis_grid[row][col].setBorder(BorderFactory.createLineBorder(Color.lightGray));
                window.add( vis_grid[row][col] );
                if ( grid[row][col] ) {
                    vis_grid[row][col].setBackground(Color.BLACK);  // color occupied cells black
                } else {
                    vis_grid[row][col].setBackground(Color.WHITE);  // color unoccupied cells white
                }
            }
        }

        // Make the graphics window visible
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        long maxDelayMS = 2000;
        window.repaint(maxDelayMS);

        // Hold the initial population steady for 2 seconds before starting the simulation
        try {
            Thread.sleep(maxDelayMS);
        } catch ( InterruptedException e ){ } // Timing a little off. Just don't worry about it.
    }

    /**
     * Apply John Conway's rules of Life to the determination of a single cell's next state.
     * @param currentState  boolean that represents the cell's current state: true for occupied, false for unoccupied
     * @param neighbors     integer number of occupied neighbor cells, in the range [0, 8]
     * @return boolean      that represent the cell's next state: true for occupied, false for unoccupied
     */
    private static boolean updateCell(boolean currentState, int neighbors ){
        return (neighbors == 3) || ((currentState) && (neighbors == 2));
    }

    /**
     * Here is my original implementation of updateCell(). Can you see how it is logically equivalent
     * to the much shorter updateCell() function, above? Which implementation do you prefer, as the reader?
     * @param currentState  boolean that represents the cell's current state: true for occupied, false for unoccupied
     * @param neighbors     integer number of occupied neighbor cells, in the range [0, 8]
     * @return nextState    boolean that represent the cell's next state: true for occupied, false for unoccupied
     */
    private static boolean updateCellExplained(boolean currentState, int neighbors ){
        boolean nextState;
        if ((currentState) && (neighbors == 2 || neighbors  == 3)){
            // An occupied cell with 2 or 3 neighbors survives into the next generation
            nextState = true;
        } else if ((!currentState) && (neighbors == 3)){
            // An empty cell with exactly 3 neighbors is born
            nextState = true;
        } else {
            // Occupied cells with too few (<2) or too many (>3) neighbors die.
            // Unoccupied cells without exactly 3 neighbors remain unoccupied.
            nextState = false;
        }
        return nextState;
    }

    /**
     * Check the central cell's 8-cell Moore neighborhood, counting all occupied neighbors.
     * @param grid  boolean[][] that represents the world, in which populated cells are true, unpopulated false
     * @param row   integer row index of the central cell
     * @param col   integer column index of the central cell
     * @return neighbors integer number of occupied neighboring cells, in the range [0, 8]
     */
    private static int countNeighbors(boolean[][] grid, int row, int col ){
        // Check the central cell's 8-cell Moore neighborhood, counting all occupied neighbors
        int size = grid.length;
        int neighbors = 0;
        for (int dr = -1; dr <= 1; dr++) {
            if (((row + dr) >= 0) && ((row + dr) < size)){          // Check to see if neighboring row exists.
                for (int dc = -1; dc <= 1; dc++) {
                    if (((col + dc) >= 0) && (col + dc < size)){    // Check to see if neighboring column exists.
                        if (grid[row + dr][col + dc]){              // Check to see if neighboring cell is occupied.
                            if( (dr != 0) || (dc != 0)) {               // Don't count the central cell as its own neighbor
                                neighbors = neighbors + 1;
                            }
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Apply the rules of John Conway's Game of Life until the population achieves stasis or the max # generations.
     * @param grid              boolean[][] that represents the world, in which populated cells are true, unpopulated false
     * @param maxGenerations    integer maximum number of generations to simulate
     * @return updatedGrid      boolean[][] 2D array that represents the world. Each occupied element is true, and each unoccupied cell is false
     */
    private static boolean[][] simulate(boolean[][] grid, int maxGenerations) {

        // Ask the world how big it is
        int size = grid.length;
        boolean[][] updatedGrid = new boolean[size][size];

        // Visualize the world
        JFrame window = new JFrame();
        JPanel[][] visGrid = new JPanel[size][size];
        initGraphics( grid, window, visGrid);

        // Apply the rules of John Conway's Game of Life until the population achieves stasis or the max # generations.
        int generation = 0;
        int flux = 1;
        boolean currentState;
        boolean nextState;
        int neighbors;
        while((flux > 0) && (generation < maxGenerations)){
            // Update each cell based on its current state and neighbor count
            flux = 0;
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    currentState = grid[row][col];                      // Determine this cell's current state
                    neighbors = countNeighbors( grid, row, col );       // Count this cell's neighbors
                    nextState = updateCell(currentState, neighbors );   // Determine this cell's next state
                    updatedGrid[row][col] = nextState;
                    // Keep track of changing cells in order to detect stasis
                    if (nextState != currentState) {
                        flux++;
                    }
                }
            }
            generation += 1;
            copyGrid(updatedGrid, grid );
            //printGrid( grid, generation, flux );
            visualizeGrid( grid, generation, flux, window, visGrid);
        }
        return updatedGrid;
    }

    /**
     * Copy the contents of the source grid into the destination grid
     * @param source         the boolean[][] grid to be read from (copied)
     * @param destination    the boolean[][] grid to be written to
     */
    public static void copyGrid(boolean[][] source, boolean[][] destination ) {
        int size = source.length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                destination[row][col] = source[row][col];
            }
        }
    }

    /**
     * @param grid          the boolean[][] that represents the world in which occupied cells are true, unoccupied false
     * @param generation    the integer generation number, ie the number of times the simulation has iterated
     * @param flux          the number of cells that changed during this generation
     */
    public static void printGrid(boolean[][] grid, int generation, int flux ) {
        int size = grid.length;
        char[] subpopulation = new char[size];
        System.out.println( "\nGeneration: " + generation +  ", Flux: " + flux + "\n");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col]) {
                    subpopulation[col] = 'O';
                } else {
                    subpopulation[col] = ' ';
                }
            }
            System.out.println(subpopulation);
        }
    }

    /**
     * @param size      the integer width and height of the grid
     * @param density   the double population density in the range [0, 1], ie the percentage of occupied grid locations
     * @return grid     the boolean[size][size] 2D array that represents the world. Each occupied element is true, and each unoccupied cell is false
     */
    public static boolean[][] populate(int size, double density ) {
        boolean[][] grid = new boolean[size][size];
        double occupancy;
        Random occupier = new Random();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                occupancy = occupier.nextDouble();
                if (occupancy < density) {
                    grid[row][col] = true;
                } else {
                    grid[row][col] = false;
                }
            }
        }
        return grid;
    }

    /**
     * @param args the commandline arguments:
     *             -- args[0] = grid size (int) sets the number of cells along each side of the square grid
     *             -- args[1] = population density (double) the percent of cells initially occupied (alive)
     */
    public static void main( String[] args ) {

        // Parse commandline arguments
        int gridSize = 25;
        double populationDensity = 0.33;
        System.out.println( "args: " + args + ", length " + args.length );
        for (int i = 0 ;i < args.length; i++){
            System.out.println( "args[" + i + "] = " + args[i] );
        }
        if (args.length > 0) {
            gridSize = Integer.parseInt( args[0] );
        }
        if (args.length > 1) {
            populationDensity = Double.parseDouble( args[1] );
        }

        // Create a world and populate it with cells
        boolean[][] world = populate(gridSize, populationDensity);
        System.out.println( "World: " + world );

        // Semi-helpful for testing, but mostly just here to remind us in class that each
        // row of a 2D array is itself a reference to another array elsewhere on the heap.
        for (int i=0; i < world.length; i++){
            System.out.println( "world[" + i + "] = " + world[i]);
            System.out.println( "world[" + i + "][0] = " + world[i][0]);
        }
        printGrid( world, 0, 0 );

        // Let the simulation play out for a fixed number (endOfTheWorld) of frames
        int endOfTheWorld = 25;
        boolean[][] worldsEnd = simulate( world, endOfTheWorld);
    }

}
