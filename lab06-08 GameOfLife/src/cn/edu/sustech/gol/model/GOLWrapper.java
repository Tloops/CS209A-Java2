package cn.edu.sustech.gol.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gols")
public class GOLWrapper {

	private int generation;
    private int[][] grid;

    @XmlElement(name = "grid")
    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    @XmlElement(name = "generation")
    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int g){
        this.generation = g;
    }

}
