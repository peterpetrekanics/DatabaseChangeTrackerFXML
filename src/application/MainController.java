package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;

public class MainController implements Initializable {
	static final String USERNAME = "system";
	static final String PASSWORD = "password";
	static String URL = "jdbc:oracle:thin:system/password@//localhost:1521/dbtracker1";
	OracleConnection conn = null;
	Properties prop = null;
	DatabaseChangeRegistration dcr = null;

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
		
		prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
		dcr = conn
				.registerDatabaseChangeNotification(prop);
		System.out.println("running1");
		try {
			dcr.addListener(new DatabaseChangeListener() {

				public void onDatabaseChangeNotification(DatabaseChangeEvent dce) {
					System.out.println("Changed row id : "
							+ dce.getTableChangeDescription()[0]
									.getRowChangeDescription()[0].getRowid()
									.stringValue());
				}
			});
			System.out.println("r2");
			System.out.println(dcr.getRegId());
			System.out.println(dcr.getState());
			Statement stmt = conn.createStatement();
			((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);
			ResultSet rs = stmt.executeQuery("select * from table1 where id=1");
			while (rs.next()) {
				System.out.println(rs.getString("col3"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			if (conn != null) {
				conn.unregisterDatabaseChangeNotification(dcr);
				conn.close();
				System.out.println("closed");
			}
			throw ex;
		}
		
		
		System.out.println("The connectDB method ends");
	}
	
	public void disconnectDB(ActionEvent event) throws SQLException{
		System.out.println("The disconnectDB method starts");
		System.out.println(dcr.getState());
//		conn.abort();
//		conn.close(prop);
		if(conn!=null) {
			conn.unregisterDatabaseChangeNotification(dcr);
			conn.close();
//			conn = null;
			System.out.println("connection is closed: " + conn.isClosed());
			System.out.println("db notifications stopped");
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
