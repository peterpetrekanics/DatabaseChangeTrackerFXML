// Working version!  However, this only worked on a local database (no remote yet)
// Info: I am using Oracle 12c with ojdbc7.jar
// The code was created based on: http://stackoverflow.com/questions/21697500/jdbc-oracle-database-change-notification-duplicate-events
// Further reading: https://docs.oracle.com/database/121/JJDBC/dbchgnf.htm#JJDBC28820
// SQL command to check if oracle db notifs are still active:
// select TABLE_NAME from USER_CHANGE_NOTIFICATION_REGS

package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainView.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Oracle database tracker");;
			primaryStage.setScene(scene);
//			System.out.println("test1");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
