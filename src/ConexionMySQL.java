import java.sql.*;

public class ConexionMySQL {

	private String db = "world";
	private String login = "root";
	private String pwd = "root";
	private String url = "jdbc:mysql://localhost/" + db
			+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private Connection conexion;

	public ConexionMySQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(url, login, pwd);
			System.out.println("-> Conexión con MySQL establecida");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver JDBC No encontrado");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error al conectarse a la BD");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error general de Conexión");
			e.printStackTrace();
		}
	}

	public void terminar() {
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void consultaStatement(String query, int columna) {
		try {
			Statement stmt = conexion.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			while (rset.next())
				System.out.println(rset.getString(columna));
			rset.close();
			stmt.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	public void consultaPrepared(String query, String cod, int columna) {
		try {
			PreparedStatement pstmt = conexion.prepareStatement(query);
			pstmt.setString(1, cod);
			ResultSet rset = pstmt.executeQuery();
			while (rset.next())
				System.out.println(rset.getString(columna));
			rset.close();
			pstmt.close();
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	public void consultaPrepareCall(int id) {
		String procedimiento = "{call superpoblado(?)}";
		try {
			CallableStatement ctmt = conexion.prepareCall(procedimiento);
			ctmt.setInt(1, id);
			ctmt.execute();
			ResultSet rset = ctmt.getResultSet();
			while (rset.next())
				System.out.println(rset.getString(2));
			rset.close();
			ctmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insertar(String usr, String pwd) {
		int resultado = 0;
		try {
			String query = "INSERT INTO users (usr,pwd) VALUES (?,?)";
			PreparedStatement pstmt = conexion.prepareStatement(query);
			pstmt.setString(1, usr);
			pstmt.setString(2, pwd);
			resultado = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public int modificar(String pwd) {
		int resultado = 0;
		try {
			String query = "UPDATE users SET pwd = ?";
			PreparedStatement pstmt = conexion.prepareStatement(query);
			pstmt.setString(1, pwd);
			resultado = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public int borrar(String usr) {
		int resultado = 0;
		try {
			String query = "DELETE FROM users WHERE usr = ?";
			PreparedStatement pstmt = conexion.prepareStatement(query);
			pstmt.setString(1, usr);
			resultado = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resultado;
	}

	public void estructuraTabla() {
		try {
			String query = "SELECT * FROM city2";
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numeroColumnas = rsmd.getColumnCount();
			for (int i = 1; i <= numeroColumnas; i++) {
				System.out.println(rsmd.getColumnName(i) + " - " + rsmd.getColumnTypeName(i));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
	}
	
	public void infoBaseDatos () {
		try {
			DatabaseMetaData dbmd = conexion.getMetaData();
			System.out.println("URL: " + dbmd.getURL());
			System.out.println("Usuario: " + dbmd.getUserName());
			System.out.println("Driver: " + dbmd.getDriverName());
			ResultSet misTablas = dbmd.getTables(null, null, null, null);
			System.out.println("TABLAS");
			while (misTablas.next()) {
				System.out.println("-> " + misTablas.getString("TABLE_NAME"));
			}
			misTablas.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		ConexionMySQL ejemplo = new ConexionMySQL();
//		ejemplo.consultaStatement("SELECT * FROM world.city", 2);
//		ejemplo.consultaPrepared("SELECT * FROM world.city WHERE CountryCode = ?", "ESP", 2);
//		ejemplo.consultaPrepareCall(3000000);
//		System.out.println(ejemplo.insertar("pedro", "secreto"));
//		System.out.println(ejemplo.modificar("oculto"));
//		System.out.println(ejemplo.borrar("pedro"));
		ejemplo.estructuraTabla();
//		ejemplo.infoBaseDatos();
		ejemplo.terminar();
	}
}
