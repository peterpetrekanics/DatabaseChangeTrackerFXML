package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;

public class MainController implements Initializable {
	static final String USERNAME = "system";
	static final String PASSWORD = "password";
	static String URL = null;
	OracleConnection conn = null;
	Properties prop = null;
	DatabaseChangeRegistration dcr = null;
	@FXML
    private TextField odbHostnameField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("this runs every time");
		
	}
	
	public void connectDB(ActionEvent event) throws SQLException{
		System.out.println("The connectDB method starts");
		conn = connect();
		System.out.println("Connection successful");
		prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
		dcr = conn
				.registerDatabaseChangeNotification(prop);
		try {
			dcr.addListener(new DatabaseChangeListener() {

				public void onDatabaseChangeNotification(DatabaseChangeEvent dce) {
					String myRowId = dce.getTableChangeDescription()[0]
							.getRowChangeDescription()[0].getRowid()
							.stringValue();
					System.out.println("Changed row id : " + myRowId);
					System.out.println("In case of db UPDATE or INSERT, this is the updated/new row's content: ");
					
					Statement stmt2;
					try {
						stmt2 = conn.createStatement();
						ResultSet rs2 = stmt2.executeQuery("select * from user_ where rowid='"+ myRowId +"'");
						while (rs2.next()) {
							ResultSetMetaData resultSetMetaData = rs2.getMetaData();
					        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {

					            int type = resultSetMetaData.getColumnType(i);
					            System.out.print("| ");
					            if (type == Types.VARCHAR || type == Types.CHAR) {
					                 System.out.print(rs2.getString(i));
					            } else {
					                 System.out.print(rs2.getLong(i));
					            }
					        }
						}
						
						rs2.close();
						stmt2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println();
				}
			});
//			System.out.println(dcr.getRegId());
//			System.out.println(dcr.getState());
			Statement stmt = conn.createStatement();
			((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);
			ResultSet rs = stmt.executeQuery("select * from user_");
//			while (rs.next()) {
//				System.out.println(rs.getString("col3"));
//			}
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
//		prop.setProperty(OracleConnection.NTF_LOCAL_HOST,"192.168.0.2");
		prop.setProperty("user", USERNAME);
		prop.setProperty("password", PASSWORD);
		if(odbHostnameField.getText().equals("")){
//			System.out.println("url null");
//			return null;
			URL = "jdbc:oracle:thin:system/password@//localhost:1521/lrtest1";
		} else {
			URL = "jdbc:oracle:thin:system/password@//"+odbHostnameField.getText()+":1521/dbtracker1";
		}
		
		return (OracleConnection) dr.connect(URL, prop);
	}

}
