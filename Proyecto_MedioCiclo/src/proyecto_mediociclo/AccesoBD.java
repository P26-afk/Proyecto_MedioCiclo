/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_mediociclo;

/**
 *
 * @author Usuario
 */
import java.sql.*;

public class AccesoBD {

    private final String host, user, contra, bd;
    private Connection conexion;

    public AccesoBD(String host, String user, String contra, String bd) {
        this.host = host;
        this.user = user;
        this.contra = contra;
        this.bd = bd;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getContra() {
        return contra;
    }

    public String getBd() {
        return bd;
    }

    public void conectarBD() throws Exception {
        try {
            String urlConexion = "jdbc:mysql://" + getHost() + "/" + getBd();
            conexion = DriverManager.getConnection(urlConexion, getUser(), getContra());
            System.out.println("Conexion establecida correctamente");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            throw new Exception("Problema de conexion con la base de datos", e);
        }
    }

    public void ejecutarSQL(String sql) throws SQLException {
        try {
            Statement sentencia = conexion.createStatement();
            sentencia.executeUpdate(sql);
            System.out.println("Operacion ejecutada exitosamente");
        } catch (SQLException e) {
            System.out.println("Error en la operacion: " + e.getMessage());
            throw e;
        }
    }

    public ResultSet consultarSQL(String sql) throws SQLException {
        ResultSet resultado;
        try {
            Statement sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
            return resultado;
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
            throw e;
        }
    }

    public void desconectarBd() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("Conexion cerrada correctamente");
        }
    }

    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e){
            return false;
        }
    }
}
