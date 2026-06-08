package proyecto_mediociclo;

import java.sql.*;

public class Cliente {

    private int idCliente;
    private String CI, nom, ape, telf, email, direc;
    AccesoBD bd;

    public Cliente() throws Exception {
        bd = new AccesoBD("localhost", "root", "1234", "Facturacion");
        bd.conectarBD();
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getCI() {
        return CI;
    }

    public String getNom() {
        return nom;
    }

    public String getApe() {
        return ape;
    }

    public String getTelf() {
        return telf;
    }

    public String getEmail() {
        return email;
    }

    public String getDirec() {
        return direc;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setCI(String CI) {
        this.CI = CI;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDirec(String direc) {
        this.direc = direc;
    }

    // Funcion para obtener el siguiente ID del cliente
    public int obtenerID() throws SQLException {
        int nuevoId;
        ResultSet rs = bd.consultarSQL("SELECT MAX(id_cliente) AS maximo FROM cliente");
        if (rs.next() && rs.getObject("maximo") != null) {
            nuevoId = rs.getInt("maximo") + 1;
        } else {
            nuevoId = 1;
        }
        return nuevoId;
    }

    // Inserta los clientes al sistema
    public void insertarCli() throws SQLException {
        String sql = "INSERT INTO cliente (cedula, nombre, apellido, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getCI());
            pstm.setString(2, getNom());
            pstm.setString(3, getApe());
            pstm.setString(4, getTelf());
            pstm.setString(5, getDirec());
            pstm.setString(6, getEmail());
            pstm.executeUpdate();
            System.out.println("Cliente Registrado en la base de datos con Exito");
        }
    }

    //Actualiza la informacion del cliente
    public void actualizarCli() throws SQLException {
        String sql = "UPDATE cliente SET cedula=?, nombre=?, apellido=?, telefono=?, email=?, direccion=? WHERE id_cliente=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getCI());
            pstm.setString(2, getNom());
            pstm.setString(3, getApe());
            pstm.setString(4, getTelf());
            pstm.setString(5, getDirec());
            pstm.setString(6, getEmail());
            pstm.executeUpdate();
            System.out.println("Datos del Cliente actualizados");
        }
    }

    // Eliminacion de datos de clientes
    public void eliminarCli() throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setString(1, getCI());
            pstm.executeUpdate();
            System.out.println("Los datos del Cliente fueron eliminados");
        }
    }

    // Ordena a los clientes por apellidos
    public ResultSet consuCli() throws SQLException {
        String sql = "SELECT * FROM cliente ORDER BY apellidos, nombres";
        return bd.consultarSQL(sql);
    }

    // Busca a los clientes por nombre
    public ResultSet buscaCI(String buscar) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE nombres LIKE '%" + buscar
                + "%' OR apellidos LIKE '%" + buscar + "%' ORDER BY apellidos";
        return bd.consultarSQL(sql);
    }

    // Verifica si la cedula ya existe en el registro
    public boolean CIexis(String si) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM cliente WHERE cedula = '" + si + "'";
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next()) {
            return rs.getInt("total") > 0;
        }
        return false;
    }

    // Busca al cliente por su ID
    public ResultSet buscId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id_cliente = " + id;
        return bd.consultarSQL(sql);
    }
}
