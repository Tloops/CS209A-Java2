package cn.edu.sustech.gol.view;

import java.io.File;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import cn.edu.sustech.gol.MainApp;
import cn.edu.sustech.gol.model.GameOfLife;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class GameOverviewController {
	@FXML
	private TextArea generationTextArea;

	@FXML
	private GridPane gridPane;

	private MainApp mainApp;

	private Rectangle[][] rectangles;

	public GameOverviewController(){}

	@FXML
	private void initialize(){
		rectangles = new Rectangle[10][10];
		generationTextArea.setText("0");
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				rectangles[i][j] = new Rectangle(34.8, 34.8, new Color(0, 0, 0, 0));
				gridPane.add(rectangles[i][j], j, i);
			}
		}
	}

	private void showGameDetails(GameOfLife gol){
		if(gol != null){
			generationTextArea.setText(String.valueOf(gol.getGeneration()));
			int[][] currentGrid = gol.getGrid();
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 10; j++){
					if(currentGrid[i][j] == 0)
						rectangles[i][j].setFill(new Color(0, 0, 0, 0));
					else if(currentGrid[i][j] == 1)
						rectangles[i][j].setFill(Color.BLACK);
				}
			}
		}
		else{
			System.err.print("Something wrong");
		}
	}

	@FXML
	private void handlePlay(){
		GameOfLife gol = mainApp.getGol().get(0);
		gol.nextGeneration();
		showGameDetails(gol);
	}

	@FXML
	private void handleReset(){
		GameOfLife gol = mainApp.getGol().get(0);
		gol.resetGrid();
		showGameDetails(gol);
	}

	@FXML
	private void handlePreset1(){
		GameOfLife gol = mainApp.getGol().get(0);
		gol.resetGrid(GameOfLife.BLOCK);
		showGameDetails(gol);
	}

	@FXML
	private void handlePreset2(){
		GameOfLife gol = mainApp.getGol().get(0);
		gol.resetGrid(GameOfLife.BLINKER);
		showGameDetails(gol);
	}

	@FXML
	private void handlePreset3(){
		GameOfLife gol = mainApp.getGol().get(0);
		gol.resetGrid(GameOfLife.SPACE_SHIP);
		showGameDetails(gol);
	}

	@FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadGOLDataFromFile(file);
        }

        GameOfLife gol = mainApp.getGol().get(0);
		showGameDetails(gol);
    }

	@FXML
    private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.saveGOLDataToFile(file);
		}

		GameOfLife gol = mainApp.getGol().get(0);
		showGameDetails(gol);
	}

	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
}
