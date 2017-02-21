package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

public class MainController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		System.out.println("test2");
		
	}
	
	public void connectDB(ActionEvent event){
		System.out.println("The connectDB method starts");

		OracleDCN oracleDCN = new OracleDCN();
		try {
			oracleDCN.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("The connectDB method ends");
	}

}
