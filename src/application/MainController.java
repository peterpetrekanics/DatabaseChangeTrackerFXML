package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;

public class MainController implements Initializable {
	static final String USERNAME = "system";
	static final String PASSWORD = "password";
	static String URL = "jdbc:oracle:thin:system/password@//localhost:1521/dbtracker1";
	OracleConnection conn = null;
	Properties prop = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("this runs every time");
		
	}
	
	public void connectDB(ActionEvent event) throws SQLException{
		System.out.println("The connectDB method starts");

//		OracleDCN oracleDCN = new OracleDCN();
//		try {
//			oracleDCN.run();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		
		conn = connect();
		
		System.out.println("The connectDB method ends");
	}
	
	public void disconnectDB(ActionEvent event) throws SQLException{
		System.out.println("The disconnectDB method starts");

//		conn.abort();
//		conn.close(prop);
		if(conn!=null) {
			conn.close();
		
//		conn = null;
			System.out.println("connection is closed: " + conn.isClosed());
		}
		
		System.out.println("The disconnectDB method ends");
	}
	
	public void test(ActionEvent event) throws SQLException{
		System.out.println("The test method starts");
		
		if(conn!=null) {
		System.out.println("connection is closed: " + conn.isClosed());
		}
		
		System.out.println("The test method ends");
	}
	
	OracleConnection connect() throws SQLException {
		OracleDriver dr = new OracleDriver();
		prop = new Properties();
		prop.setProperty("user", USERNAME);
		prop.setProperty("password", PASSWORD);
		return (OracleConnection) dr.connect(URL, prop);
	}

}
