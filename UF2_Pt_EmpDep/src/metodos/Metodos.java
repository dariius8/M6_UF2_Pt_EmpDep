package metodos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.mysql.jdbc.CallableStatement;

public class Metodos {

	static final String url = "jdbc:mysql://localhost:3306/emp_dep";

	public static void menu() {
		Scanner lector = new Scanner(System.in);
		int i = 0;
		while (i != 4) {
			System.out.println("\nMENU");
			System.out.println("   1. Insertar nuevo departamento(Devops) y un empleado(Lopez)");
			System.out.println("   2. Igualar la comision de todos los vendedores (Stored Procedure)");
			System.out.println("   3. Listar todos los empleados de un departamento (Stored Procedure)");
			System.out.println("   4. Salir");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 5) {
				switch (i) {
				case 1:
					insertarEmpDep();
					break;
				case 2:
					igualarComisionSP();
					break;
				case 3:
					listarEmpleadosSP();
					break;
				default:
					System.out.println("\nAdios!");
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void insertarEmpDep() {
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// insert departamento pasandole datos
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO departamentos VALUES(?, ?, ?)");
			pstmt.setInt(1, 50);
			pstmt.setString(2, "DEVOPS");
			pstmt.setString(3, "VALENCIA");
			pstmt.executeUpdate();
			System.out.println("\nDepartamento 'Devops' insertado correctamente.");
			// select tabla departamentos para ver los cambios
			pstmt = conn.prepareStatement("SELECT * FROM departamentos");
			ResultSet rs = pstmt.executeQuery();
			System.out.println("\n---Tabla departamentos---");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
			}
			// insert empleado pasandole datos
			pstmt = conn.prepareStatement("INSERT INTO empleados VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, 8000);
			pstmt.setString(2, "LOPEZ");
			pstmt.setString(3, "PROGRAMADR");
			pstmt.setInt(4, 7450);
			// fecha string -> date
			String fecha = "2020-01-13";
			Date parse_fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
			java.sql.Date fecha_alta = new java.sql.Date(parse_fecha.getTime());
			pstmt.setDate(5, fecha_alta);
			pstmt.setDouble(6, 1500);
			pstmt.setDouble(7, 0);
			pstmt.setInt(8, 50);
			pstmt.executeUpdate();
			System.out.println("\nEmpleado 'Lopez' insertado correctamente.");
			// select tabla empleados para ver los cambios
			pstmt = conn.prepareStatement("SELECT * FROM empleados");
			rs = pstmt.executeQuery();
			System.out.println("\n---Tabla empleados---");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getInt(4)
						+ "\t" + rs.getDate(5) + "\t" + rs.getDouble(6) + "\t" + rs.getDouble(7) + "\t" + rs.getInt(8));
			}
			conn.close();
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.err.println(e + "\nError! No se ha podido insertar.");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void igualarComisionSP() {
		Scanner lector = new Scanner(System.in);
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// pedimos por consola la nueva comision
			System.out.println("\nIntroduce la nueva comision:");
			float comision = lector.nextFloat();
			// llamamos al procedure y le pasamos la comision
			CallableStatement cstmt = (CallableStatement) conn.prepareCall("{call igualarComision(?)}");
			cstmt.setFloat(1, comision);
			cstmt.execute();
			System.out.println("\nComision de todos los vendedores igualada a " + comision + " correctamente.");
			// select tabla empleados para ver los cambios
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM empleados");
			ResultSet rs = pstmt.executeQuery();
			System.out.println("\n---Tabla empleados---");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getInt(4)
						+ "\t" + rs.getDate(5) + "\t" + rs.getDouble(6) + "\t" + rs.getDouble(7) + "\t" + rs.getInt(8));
			}
			conn.close();
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.err.println(e + "\nError! No se ha podido insertar.");
		}
	}

	public static void listarEmpleadosSP() {
		Scanner lector = new Scanner(System.in);
		try {
			// conexion
			Connection conn = DriverManager.getConnection(url, "root", "");
			// pedimos por consola el nombre del departamento
			System.out.println(
					"\nIntroduce el nombre de departamento (contabilidad, investigacion, ventas, produccion, devops):");
			String departamento = lector.nextLine();
			// llamamos al procedure y le pasamos el departamento
			CallableStatement cstmt = (CallableStatement) conn.prepareCall("{call listarEmpleados(?)}");
			cstmt.setString(1, departamento);
			cstmt.execute();
			ResultSet rs = cstmt.getResultSet();
			System.out.println("\n---Listando todos los empleados de ese departamento---");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getInt(4)
						+ "\t" + rs.getDate(5) + "\t" + rs.getDouble(6) + "\t" + rs.getDouble(7) + "\t" + rs.getInt(8));
			}
			conn.close();
			rs.close();
		} catch (SQLException e) {
			System.err.println(e + "\nError! No se ha podido insertar.");
		}
	}
}
