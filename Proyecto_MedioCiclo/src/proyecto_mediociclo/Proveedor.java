package proyecto_mediociclo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Proveedor {

    private int idProveedor;
    private String ruc, nombre, exp, telefono, email, direccion;
//    private boolean estado;
    AccesoBD bd;

    public Proveedor() throws Exception {
        bd = new AccesoBD("localhost", "root", "1234", "Facturacion");
        bd.conectarBD();

    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdproveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
     */
    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /*
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
     */
    public int obtenerSiguienteId() throws SQLException {
        int nuevoId;
        ResultSet rs = bd.consultarSQL("SELECT MAX(id_proveedor) AS maximo FROM proveedor");
        if (rs.next() && rs.getObject("maximo") != null) {
            nuevoId = rs.getInt("maximo") + 1;
        } else {
            nuevoId = 1;
        }
        return nuevoId;
    }

    public void insertarProveedor() throws SQLException {
        String sql = "INSERT INTO proveedor (nombre,ruc, exp, telefono, email, direccion) VALUES ( ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getNombre());
            pstm.setString(2, getRuc());
            pstm.setString(3, getExp());
            pstm.setString(4, getTelefono());
            pstm.setString(5, getEmail());
            pstm.setString(6, getDireccion());
            pstm.executeUpdate();
            System.out.println("Proveedor registrado correctamente");
        }
    }

    // Esto funciona para actualizar datos de proveedores
    public void actualizarProveedor() throws SQLException {
        String sql = "UPDATE proveedor SET ruc=?, nombre=?, exp=?, telefono=?, email=?, direccion=? WHERE id_proveedor=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getNombre());
            pstm.setString(2, getRuc());
            pstm.setString(3, getExp());
            pstm.setString(4, getTelefono());
            pstm.setString(5, getEmail());
            pstm.setString(6, getDireccion());
            pstm.setInt(7, getIdProveedor());
            pstm.executeUpdate();
            System.out.println("Proveedor actualizado correctamente");
        }
    }

    public void eleminarProveedor() throws SQLException {
        String sql = "DELETE FROM proveedor WHERE id_proveedor=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdProveedor());
            pstm.executeUpdate();
            System.out.println("Proveedor eliminado correctamente");
        }
    }

    public ResultSet consultarProveedores() throws SQLException {
        String sql = "SELECT * FROM proveedor ORDER BY nombre";
        return bd.consultarSQL(sql);
    }

    public boolean rucExiste(String rucVerificar) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM proveedor WHERE ruc = '" + rucVerificar + "'";
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next()) {
            return rs.getInt("total") > 0;
        }
        return false;
    }

}
