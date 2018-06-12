package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * It is necessary to convert any file to .txt format for this application to
 * work http://pdftotext.com/pt/
 * 
 * @author Israel Deorce @date/06/2019
 *
 */
public class App extends Application {
	private Button btnFileChooser;
	private TextField ref;
	private Button btnRun;
	
	private String folderPath;
	private static TextArea textArea = new TextArea();

	@Override
	public void start(Stage primaryStage) {

		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: #008774;");

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);

		
		Label label2 = new Label("");
		label2.setTextFill(Color.web("#FFFFFF"));		
		
		btnFileChooser = new Button("Click to Choose a Folder");
		btnFileChooser.setPrefSize(150, 20);
		btnFileChooser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				File selectedDirectory = directoryChooser.showDialog(primaryStage);
				if (selectedDirectory != null) {
					folderPath = selectedDirectory.getAbsolutePath();
					label2.setText(folderPath);
				} else {
				}
			}
		});

		Label label = new Label("Type a String");
		label.setTextFill(Color.web("#FFFFFF"));

		ref = new TextField();

		btnRun = new Button("Run!");
		btnRun.setPrefSize(50, 20);
		btnRun.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (ref != null) {
					String[] splited = ref.getText().split("\\s+");
					getArticles(folderPath, ref.getText(), splited);
				} else {
					//Nao vou fazer agora, cala a boca!
				}
			}
		});
		
		hbox.getChildren().addAll(btnFileChooser, label, ref, btnRun);

//		GridPane gp = new GridPane();
//		gp.setMinSize(200, 100);
//		gp.setPadding(new Insets(10, 10, 10, 10));
//		gp.setHgap(5);
//		gp.setVgap(5);
//		gp.setAlignment(Pos.CENTER);
//		gp.setGridLinesVisible(true);
//		
//		gp.add(textArea, 0, 0);
		
		textArea.setEditable(false);
		
		HBox hbox2 = new HBox();
		hbox2.setPadding(new Insets(15, 12, 15, 12));
		hbox2.setSpacing(10);
		hbox2.getChildren().addAll(label2);
		
		HBox hbox3 = new HBox();
		hbox3.setPadding(new Insets(15, 12, 15, 12));
		hbox3.setSpacing(10);
		hbox3.getChildren().add(textArea);
		
		pane.setTop(hbox);
		pane.setCenter(hbox2);
		pane.setBottom(hbox3);

		Scene scene = new Scene(pane, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mapas com JavaFX");
		primaryStage.show();
	}

	// ##########################################################################################

	// How many files contains the sentence
	public static int nOccurences = 0;

	/**
	 * Method that gets every file from a given folder and send its path to the
	 * streamService Method
	 * 
	 * @param folderPath
	 * @param ref
	 * @param splited
	 */
	public static void getArticles(String folderPath, String ref, String[] splited) {
		System.out.println(folderPath);
		try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
			paths.filter(Files::isRegularFile).forEach(p -> streamService(p, ref, splited));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Method that verifies the occurrence of a given string in a given text file
	 * and informs the user
	 * 
	 * @param folderPath
	 * @param ref
	 * @param splited
	 */
	private static void streamService(Path path, String ref, String[] splited) {
		boolean hasRef = false;
		String format = "%-23s%s%n";
		StringBuilder builder = new StringBuilder();
		
		try (Scanner sc = new Scanner(Files.newBufferedReader(path, Charset.forName("ISO-8859-1")))) {
			sc.useDelimiter("(,|\\s|\\$)+"); // separadores
			while (sc.hasNext()) {
				if (sc.next().equalsIgnoreCase(splited[0])) {
					hasRef = true;
					for (int i = 1; i < splited.length; i++) {
						if (!sc.next().equalsIgnoreCase(splited[i]))
							hasRef = false;
					}
					if (hasRef) {
						nOccurences++;
						textArea.setText(textArea.getText() + nOccurences + " - #CONTAINS! " + path + "\n"); 
						System.out.printf(format, nOccurences + " - #CONTAINS!", path);
						return;
					}
				}
			}
			if (!hasRef) {
				nOccurences++;
				textArea.setText(textArea.getText() + nOccurences + " - DO NOT Contains! " + path + "\n");
				System.out.printf(format, nOccurences + " - DO NOT Contains!", path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
