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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
	private Button btnRun;

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: #008774;");

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);

		btnFileChooser = new Button("Click to Choose a Folder");
		btnFileChooser.setPrefSize(150, 20);
		btnFileChooser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				File selectedDirectory = directoryChooser.showDialog(primaryStage);
				if (selectedDirectory != null) {
					String folderPath = selectedDirectory.getAbsolutePath();
					String ref = ("Pair Programming Illuminated");
					String[] splited = ref.split("\\s+");
					getArticlesArticle(folderPath, ref, splited);
				} else {
				}
			}
		});

		Label label = new Label("Type a String");
		label.setTextFill(Color.web("#FFFFFF"));

		TextField notification = new TextField();

		btnRun = new Button("Run!");
		btnRun.setPrefSize(50, 20);

		hbox.getChildren().addAll(btnFileChooser, label, notification, btnRun);

		pane.setTop(hbox);

		Scene scene = new Scene(pane, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mapas com JavaFX");
		primaryStage.show();

		String folderPath = ("C:/Users/Israel/Desktop/folder");
		String ref = ("Pair Programming Illuminated");
		String[] splited = ref.split("\\s+");
		// For checking if the splited version of the reference is correct
		// for(int i=0; i<splited.length;i++) {
		// System.out.println(splited[i]);
		// }
		// findReferencesByArticle(folderPath, ref, splited);

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
	public static void getArticlesArticle(String folderPath, String ref, String[] splited) {
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
						System.out.printf(format, nOccurences + " - #CONTAINS!", path);
						return;
					}
				}
			}
			if (!hasRef) {
				nOccurences++;
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
