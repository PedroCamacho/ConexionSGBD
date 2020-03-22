import java.sql.*;

public class ConexionOracle {

	//Atributos de la clase
	private String login = "SYSTEM"; 
	private String pwd = "root";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private Connection conexion;
		
	//Constructor que crea la conexión
	public ConexionOracle () {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conexion=DriverManager.getConnection(url,login,pwd);
			System.out.println ("-> Conexión con ORACLE establecida");
		} catch (ClassNotFoundException e) {	
			System.out.println ("Driver JDBC No encontrado");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println ("Error al conectarse a la BD");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println ("Error general de Conexión");
			e.printStackTrace();
		}
	}
	
	public void terminar () {
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
	
	public void consultaPrepared (String query,int cod, int columna){	 
		try{
			PreparedStatement pstmt = conexion.prepareStatement (query);
			pstmt.setInt(1,cod);
			ResultSet rset = pstmt.executeQuery();
			while(rset.next())
				System.out.println(rset.getString(columna));
			rset.close();
			pstmt.close();
		} catch(SQLException s){
			s.printStackTrace();
		}
	}

	public void consultaPrepareCall (int cat) {
		String procedimiento = "{?=call pedro.cantidadProductos (?)}";
		try {
			CallableStatement cstm = conexion.prepareCall(procedimiento);
			// Se indica que el primer interrogante es de salida.
			cstm.registerOutParameter(1,Types.NUMERIC);
			cstm.setInt(2,cat);
			cstm.execute();
			// Se recoge el resultado del primer interrogante.
			int resultado = cstm.getInt(1);
			System.out.println("La cantidad de productos es: " + resultado);
	   		cstm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int insertar (String usr, String pwd){
		int resultado=0;
		try {
			String query = "INSERT INTO pedro.users (usr,pwd) VALUES (?,?)";
			PreparedStatement pstmt = conexion.prepareStatement (query);
			pstmt.setString(1,usr);
			pstmt.setString(2,pwd);
			resultado = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resultado;
	}
	
	public int modificar(String pwd) {
		int resultado=0;
		try {
			String query = "UPDATE pedro.users SET pwd = ?";
			PreparedStatement pstmt = conexion.prepareStatement (query);
			pstmt.setString(1,pwd);
			resultado = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resultado;
	}
	
	public int borrar (String usr) {
		int resultado=0;
		try {
			String query = "DELETE FROM Pedro.users WHERE usr = ?";
			PreparedStatement pstmt = conexion.prepareStatement (query);
			pstmt.setString(1,usr);
			resultado = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resultado;
	}
	
	public void estructuraTabla() {
		try {
			String query = "SELECT * FROM pedro.empleados";
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
			//Catalogo - Esquema - Tabla - Tipo
			ResultSet misTablas = dbmd.getTables("PEDRO", "PEDRO", null, null);
			System.out.println("TABLAS");
			while (misTablas.next()) {
				System.out.println("-> " + misTablas.getString("TABLE_NAME"));
			}
			misTablas.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void main (String [] args){
		ConexionOracle ejemplo = new ConexionOracle();
//		ejemplo.consultaStatement("SELECT * FROM pedro.clientes", 3);
//		ejemplo.consultaPrepared("SELECT * FROM pedro.productos WHERE existencia > ?", 200, 4);
//		ejemplo.consultaPrepareCall(100);
//		System.out.println(ejemplo.insertar("pedro", "secreto"));
//		System.out.println(ejemplo.modificar("oculto"));
//		System.out.println(ejemplo.borrar("pedro"));
		ejemplo.estructuraTabla();
//		ejemplo.infoBaseDatos();
		ejemplo.terminar();
	}


}
