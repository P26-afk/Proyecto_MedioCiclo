package proyecto_mediociclo;

import java.util.Date;
import java.sql.*;

public class Empleado {

    private int idEmpleado;
    private String nom, ape, cargo, dire, correo,ci, telf;
    private Date fechContra;
    private double sueldo;
    AccesoBD bd;

    public Empleado() throws Exception {
        bd = new AccesoBD("localhost", "root", "1234", "Facturacion");
        bd.conectarBD();
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDire() {
        return dire;
    }

    public void setDire(String dire) {
        this.dire = dire;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechContra() {
        return fechContra;
    }

    public void setFechContra(Date fechContra) {
        this.fechContra = fechContra;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public void nuevoEmpleado() {

    }

    public void insertarEmpleado() throws SQLException {
        String sql = "INSERT INTO empleado (cedula, nombres, apellidos, cargo, telefono, email, direccion, salario) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getCi());
            pstm.setString(2, getNom());
            pstm.setString(3, getApe());
            pstm.setString(4, getCargo());
            pstm.setString(5, getTelf());
            pstm.setString(6, getCorreo());
            pstm.setString(7, getDire());
            pstm.setDouble(8, getSueldo());
            pstm.executeUpdate();
            System.out.println("Empleado Contratado - Registrado");

        }
    }

    public void actualizarEmpleados() throws SQLException {
        String sql = "UPDATE empleado Set cedula=?,nombres=?,apellidos=?,cargo=?,telefono=?,email=?,direccion=?,salario=? WHERE id_empleado=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getCi());
            pstm.setString(2, getNom());
            pstm.setString(3, getApe());
            pstm.setString(4, getCargo());
            pstm.setString(5, getTelf());
            pstm.setString(6, getCorreo());
            pstm.setString(7, getDire());
            pstm.setDouble(8, getSueldo());
            pstm.executeUpdate();
            System.out.println("Datos del empleado actualizados");
        }
    }

    public void eliminarEmpleado() throws SQLException {
        String sql = "DELETE FROM empleado WHERE id_empleado=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getCi());
            pstm.executeUpdate();
            System.out.println("Empleado dado de baja");
        }
    }

    public boolean empleadoExiste(String cedulaVerificar) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM empleado WHERE cedula='" + cedulaVerificar + "'";
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next()) {
            return rs.getInt("total") > 0;
        }
        return false;
    }

    public ResultSet consultarEmpleados() throws SQLException {
        String sql = "SELECT * FROM empleado ORDER BY apellidos, nombres";
        return bd.consultarSQL(sql);
    }

    public ResultSet buscarPorCriterio(String criterio) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE apellidos LIKE '%" + criterio + "%' OR cedula LIKE '%" + criterio + "%' OR cargo LIKE '%" + criterio + "%' ORDER BY apellidos";
        return bd.consultarSQL(sql);
    }

    // Buscar empleado por ID
    public ResultSet buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE id_empleado = " + id;
        return bd.consultarSQL(sql);
    }

    // Buscar empleado por cédula
    public ResultSet buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT * FROM empleado WHERE cedula = '" + cedula + "'";
        return bd.consultarSQL(sql);
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public ResultSet listarActivos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
