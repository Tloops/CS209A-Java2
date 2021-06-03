package cn.edu.sustech.gol;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cn.edu.sustech.gol.model.GOLWrapper;
import cn.edu.sustech.gol.model.GameOfLife;
import cn.edu.sustech.gol.view.GameOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private ObservableList<GameOfLife> gol = FXCollections.observableArrayList();

	public MainApp(){
		gol.add(new GameOfLife());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Game Of Life");
		this.primaryStage.setResizable(false);

		showGameOverview();
	}

	private void showGameOverview() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/GameOverview.fxml"));
			AnchorPane gameOverview = (AnchorPane) loader.load();

			Scene scene = new Scene(gameOverview);
			primaryStage.setScene(scene);
			primaryStage.show();

			GameOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public File getGOLFilePath() {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    String filePath = prefs.get("filePath", null);
	    if (filePath != null) {
	        return new File(filePath);
	    } else {
	        return null;
	    }
	}

	public void setGOLFilePath(File file) {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    if (file != null) {
	        prefs.put("filePath", file.getPath());

	        // Update the stage title.
	        primaryStage.setTitle("Game Of Life - " + file.getName());
	    } else {
	        prefs.remove("filePath");

	        // Update the stage title.
	        primaryStage.setTitle("Game Of Life");
	    }
	}

	public void loadGOLDataFromFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(GOLWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // Reading XML from the file and unmarshalling.
	        GOLWrapper wrapper = (GOLWrapper) um.unmarshal(file);

	        gol.clear();
	        gol.add(new GameOfLife(wrapper.getGeneration(), wrapper.getGrid()));

	        // Save the file path to the registry.
	        setGOLFilePath(file);

	    } catch (Exception e) { // catches ANY exception
	    	Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Error");
            alert.setHeaderText("Failure occurred!");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
	    }
	}

	public void saveGOLDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(GOLWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            GOLWrapper wrapper = new GOLWrapper();
            wrapper.setGeneration(gol.get(0).getGeneration());
            wrapper.setGrid(gol.get(0).getGrid());

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setGOLFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(this.getPrimaryStage());
            alert.setTitle("Error");
            alert.setHeaderText("Failure occurred!");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

	public Stage getPrimaryStage(){
		return primaryStage;
	}

	public ObservableList<GameOfLife> getGol(){
		return gol;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
