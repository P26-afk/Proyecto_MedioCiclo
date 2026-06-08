package proyecto_mediociclo;

import java.sql.*;

public class Facturacion {

    private int idFactura, idCliente, idEmpleado;
    private String numeroFactura; //estado;
    private Date fechaEmision;
    private double subtotal, descuento, iva, total;
    AccesoBD bd;

    public Facturacion() throws Exception {
        bd = new AccesoBD("localhost", "root", "1234", "Facturacion");
        bd.conectarBD();
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /*    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
     */
    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    //esto funciona para obtener el siguiente ID de factura

    public int obtenerSiguienteId() throws SQLException {
        int nuevoId;
        ResultSet rs = bd.consultarSQL("SELECT MAX(id_factura) AS maximo FROM factura");
        if (rs.next() && rs.getObject("maximo") != null) {
            nuevoId = rs.getInt("maximo") + 1;
        } else {
            nuevoId = 1;
        }
        return nuevoId;
    }
// Esto funciona para generar numeros de factura automatica

    public String generarNumeroFactura() throws SQLException {
        int siguiente = obtenerSiguienteId();
        return String.format("FACT-%06d", siguiente);
    }

//Esto funciona para crear nuevas facturas
    public void insertarFactura() throws SQLException {
        String sql = "INSERT INTO factura (id_cliente, id_empleado, numero_factura, subtotal, descuento, iva, total) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdCliente());
            pstm.setInt(2, getIdEmpleado());
            pstm.setString(3, generarNumeroFactura());
            pstm.setDouble(4, getSubtotal());
            pstm.setDouble(5, getDescuento());
            pstm.setDouble(6, getIva());
            pstm.setDouble(7, getTotal());
//El campo estado es BOOLEAN en la BD: true = active/pagada, false = anulada
            pstm.setInt(8, getIdFactura());
            pstm.executeUpdate();
            System.out.println("Factura actualizada exitosamente");

        }
    }
// Esto Funciona para anular facturas

    public void eliminarFactura() throws SQLException {
        String sql = "DELETE FROM factura WHERE id_factura=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdFactura());
            pstm.executeUpdate();
            System.out.println("Factura eliminada exitosamente");
        }
    }
// Esto funcina para mostrar todas las factura con informacion completa

    public ResultSet consultarFacturasCompletas() throws SQLException {
        String sql = "SELECT f.*,"
                + "CONCAT(c.nombres, '' , c.apellidos) as cliente, "
                + "CONCAT(e.nombres, '' , e.apellidos) as empleado "
                + "FROM factura f "
                + "INNER JOIN cliente c ON f.id_cliente = c.id_cliente "
                + "INNER JOIN empleado e ON f.id_empleado = e.id_empleado "
                + "ORDER BY f.fecha_emision DESC";
        return bd.consultarSQL(sql);
    }
// Esto funciona para buscar facturas por numero

    public ResultSet buscarPorNumero(String numeroBuscar) throws SQLException {
        String sql = "SELECT f.*, "
                + "CONCAT(c.nombres, '' , c.apellidos) as cliente, "
                + "CONCAT(e.nombres, '' , e.apellidos) as empleado "
                + "FROM factura f "
                + "INNER JOIN cliente c ON f.id_cliente = c.id_cliente "
                + "INNER JOIN empleado e ON f.id_empleado = e.id_empleado "
                + "WHERE f.numero_factura LIKE '%" + numeroBuscar + "%' "
                + "ORDER BY f.fecha_emision DESC";
        return bd.consultarSQL(sql);
    }
// Esto funciona para buscar factura por cliente

    public ResultSet buscarPorCliente(int idClienteBuscar) throws SQLException {
        String sql = "SELECT f.*, "
                + "CONCAT(c.nombres, '' , c.apellidos) as cliente, "
                + "CONCAT(e.nombres, '' , e.apellidos) as empleado "
                + "FROM factura f "
                + "INNER JOIN cliente c ON f.id_cliente = c.id_cliente "
                + "INNER JOIN empleado e ON f.id_empleado = e.id_empleado "
                + "WHERE f.id_cliente = " + idClienteBuscar + " "
                + "ORDER BY f.fecha_emision DESC";
        return bd.consultarSQL(sql);
    }
// Esto funciona para calcular totales automaticamente

    public void calcularTotales() {
        double ivaCalculado = (subtotal - descuento) * 0.12;
        setIva(ivaCalculado);
        setTotal((subtotal - descuento) + ivaCalculado);
    }
// esto funciona para verificar si un numero de factura ya existe

    public boolean numeroFacturaExiste(String numeroVerificar) throws SQLException {
        String sql = "SELECT COUNT (*) as total FROM factura WHERE numero_factura = ' " + numeroVerificar + "'";
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next()) {
            return rs.getInt("total") > 0;
        }
        return false;
    }

// Esto funciona para obtener facturas por rango de fechas
    public ResultSet consultarPorFechas(String fechaInicio, String fechaFin) throws SQLException {
        String sql = "SELECT f.*, "
                + "CONCAT(c.nombres, '' , c.apellidos) as cliente, "
                + "CONCAT(e.nombres, '' , e.apellidos) as empleado "
                + "FROM factura f "
                + "INNER JOIN cliente c ON f.id_cliente = c.id_cliente "
                + "INNER JOIN empleado e ON f.id_empleado = e.id_empleado "
                + "WHERE f.fecha_emision BETWEEN '" + fechaInicio + "' AND' " + fechaFin + "'"
                + "ORDER BY f.fecha_emision DESC";
        return bd.consultarSQL(sql);
    }
}
