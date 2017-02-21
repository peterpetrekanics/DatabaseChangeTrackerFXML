package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;


public class OracleDCN {
	static final String USERNAME = "system";
	static final String PASSWORD = "password";
	static String URL = "jdbc:oracle:thin:system/password@//localhost:1521/dbtracker1";

	public void mymain(String[] args) {
		OracleDCN oracleDCN = new OracleDCN();
		try {
			oracleDCN.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void run() throws Exception {
		OracleConnection conn = connect();
		Properties prop = new Properties();
		prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");
		DatabaseChangeRegistration dcr = conn
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
	}

	OracleConnection connect() throws SQLException {
		OracleDriver dr = new OracleDriver();
		Properties prop = new Properties();
		prop.setProperty("user", OracleDCN.USERNAME);
		prop.setProperty("password", OracleDCN.PASSWORD);
		return (OracleConnection) dr.connect(OracleDCN.URL, prop);
	}
}
