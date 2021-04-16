package cn.edu.sustech.gol.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameOfLife {

	public final static int[][] BLOCK = new int[][]{
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
	};

	public final static int[][] BLINKER = new int[][]{
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
	};

	public final static int[][] SPACE_SHIP = new int[][]{
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
        { 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
	};

	private final IntegerProperty generation;
	private int[][] grid = new int[10][10];

	public GameOfLife(){
		this(0);
	}

	public GameOfLife(int generation){
		this(generation, new int[][]{
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
		});
	}

	public GameOfLife(int generation, int[][] grid){
		this.generation = new SimpleIntegerProperty();
		this.generation.set(generation);
		this.grid = grid;
	}

	public void nextGeneration()
    {
        int[][] future = new int[10][10];

        // Loop through every cell
        for (int l = 1; l < 10 - 1; l++)
        {
            for (int m = 1; m < 10 - 1; m++)
            {
                // finding no Of Neighbours that are alive
                int aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        aliveNeighbours += grid[l + i][m + j];

                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                aliveNeighbours -= grid[l][m];

                // Implementing the Rules of Life

                // Cell is lonely and dies
                if ((grid[l][m] == 1) && (aliveNeighbours < 2))
                    future[l][m] = 0;

                // Cell dies due to over population
                else if ((grid[l][m] == 1) && (aliveNeighbours > 3))
                    future[l][m] = 0;

                // A new cell is born
                else if ((grid[l][m] == 0) && (aliveNeighbours == 3))
                    future[l][m] = 1;

                // Remains the same
                else
                    future[l][m] = grid[l][m];
            }
        }
        grid = future;
        setGeneration(getGeneration()+1);
//        System.out.printf("Current generation: %d\n", currentGeneration);
//        System.out.println("Next Generation");
//        displayGrid();
    }

	public void resetGrid() {
		setGeneration(0);
		grid = new int[][]{
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
		};
	}

	public void resetGrid(int[][] pattern) {
		setGeneration(0);
		grid = pattern;
	}

	public int[][] getGrid(){
		return grid;
	}

	public int getGeneration(){
		return generation.get();
	}

	public void setGeneration(int generation){
		this.generation.set(generation);
	}

	public IntegerProperty generationProperty(){
		return generation;
	}

}
