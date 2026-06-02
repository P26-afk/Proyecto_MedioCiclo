/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_mediociclo;

import java.sql.*;

/**
 *
 * @author crisv
 */
public class Categoria {
    
    private int idCategoria;
    private String nombre, descripcion;
    private boolean estado;
    AccesoBD bd;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Categoria() throws Exception {
        bd=new AccesoBD("localhost","root","1234","Facturacion");
        bd.conectarBD();
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public int obtenerSiguienteId() throws SQLException {
        int nuevoId;
        ResultSet rs = bd.consultarSQL("SELECT MAX(id_categoria) AS maximo FROM categoria");
        if (rs.next() && rs.getObject("maximo") != null) {
            nuevoId = rs.getInt("maximo") + 1;
        } else {
            nuevoId = 1;
        }
        return nuevoId;
    }
    public void insertarCategoria() throws SQLException {
        String sql = "INSERT INTO categoria (nombre, descripcion, estado) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getNombre());
            pstm.setString(2, getDescripcion());
            pstm.setBoolean(3, isEstado());
            pstm.executeUpdate();
            System.out.println("Categoria agregada correctamente");
        }
    }
    public void actualizarCategoria() throws SQLException {
        String sql = "UPDATE categoria SET nombre=?, descripcion=?, estado=? WHERE id_categoria=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getNombre());
            pstm.setString(2, getDescripcion());
            pstm.setBoolean(3, isEstado());
            pstm.setInt(4, getIdCategoria());
            pstm.executeUpdate();
            System.out.println("Categoria actualizada correctamente");
        }
    }
    
    public void eliminarCategoria() throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdCategoria());
            pstm.executeUpdate();
            System.out.println("Categoria eliminada correctamente");
        }
        
    }
 
    public ResultSet consultarCategorias() throws SQLException {
        String sql = "SELECT * FROM categoria ORDER BY nombre";   
        return bd.consultarSQL(sql);
    }
    
    public ResultSet buscarPorNombre(String nombreBuscar) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE nombre LIKE '%" + nombreBuscar + "%' ORDER BY nombre";
        return bd.consultarSQL(sql);
    }
    
    public boolean existeCategoria(String nombre) throws SQLException {
        String sql = "SELECT COUNT (*) as total FROM categoria WHERE nombre = '" + nombre + "'";
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next()) {
            return rs.getInt("total") > 0;
        }
        return false;
    }
    
}
