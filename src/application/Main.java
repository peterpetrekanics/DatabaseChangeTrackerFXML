// Working version!  However, this only worked on a local database (no remote yet)
// Info: I am using Oracle 12c with ojdbc7.jar
// The code was created based on: http://stackoverflow.com/questions/21697500/jdbc-oracle-database-change-notification-duplicate-events
// Further reading: https://docs.oracle.com/database/121/JJDBC/dbchgnf.htm#JJDBC28820
// SQL command to check if oracle db notifs are still active:
// select TABLE_NAME from USER_CHANGE_NOTIFICATION_REGS

// Important note for remote host connection:
// To make remote connection work, you need to use the IP address instead of hostname
// in your start / run: netca  -> Local Net Service Name configuration

// In case of a db Update, this sql might come handy: select * from table1 where rowid='AAAWc+AABAAAX9RAAE'
// Further reading on rowid: https://www.experts-exchange.com/articles/931/Decoding-the-Oracle-ROWID-and-some-Base64-for-fun.html

// The exported executable jar file has to be ran with: java.jar -jar exported.jar
// to be able to see the console output

// TODO: implement the long types when printing the sql columns:
// http://stackoverflow.com/questions/16882971/retrieve-entire-row-with-resultset

// TODO: overwrite first resultset where a fixed table name is used currently - it
// should be flexibe, based on the db used

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
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
