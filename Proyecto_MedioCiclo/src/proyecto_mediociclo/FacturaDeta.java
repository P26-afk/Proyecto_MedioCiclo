/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_mediociclo;

import java.sql.*;

/**
 *
 * @author Pantos
 */
public class FacturaDeta {

    private int idDeatalle, idFactura, idProducto, cantidad;
    private double precioUnitario, subtotal;
    private AccesoBD bd;

    public FacturaDeta() throws Exception {
        bd = new AccesoBD("localhost", "root", "1234", "Facturacion");
        bd.conectarBD();
    }

    public AccesoBD getBd() {
        return bd;
    }

    public void setBd(AccesoBD bd) {
        this.bd = bd;
    }

    public int getIdDeatalle() {
        return idDeatalle;
    }

    public void setIdDeatalle(int idDeatalle) {
        this.idDeatalle = idDeatalle;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // Esto funciona para obtener el siguiente ID de detalle
    public int obtenerSiguienteid() throws SQLException {
        int nuevoId;
        ResultSet rs = bd.consultarSQL("SELECT MAX(id_detalle) AS maximo FROM detalle_factura");
        if (rs.next() && rs.getObject("maximo") != null) {
            nuevoId = rs.getInt("maximo") + 1;
        } else {
            nuevoId = 1;
        }
        return nuevoId;
    }

    //Esto funciona para calcular el subtoral automaticamente 
    public void calcularSubtotal() {
        setSubtotal(getCantidad() * getPrecioUnitario());
    }

    //Esto funciona para agragar items a una factura
    public void insertarDetalle() throws SQLException {
        //Calcular subtotal antes de insertar
        calcularSubtotal();
        String sql = "INSERT INTO detalle_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdProducto());
            pstm.setInt(2, getCantidad());
            pstm.setDouble(3, getPrecioUnitario());
            pstm.setDouble(4, getSubtotal());
            pstm.setInt(5, getIdDeatalle());
            pstm.executeUpdate();
            System.out.println("Item agregado a la factura exitosamente");
        }
    }    
        // Esto funciona para modificar items factura
    public void actualizarDetalle() throws SQLException {
        calcularSubtotal();
        
        String sql = "UPDATE detalle_factura SET id_producto=?, cantidad=?, precio_unitario=?, subtotal+? WHERE id_detalle=?";
                try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdProducto());
            pstm.setInt(2, getCantidad());
            pstm.setDouble(3, getPrecioUnitario());
            pstm.setDouble(4, getSubtotal());
            pstm.setInt(5, getIdDeatalle());
            pstm.executeUpdate();
            System.out.println("Item actualizado exitosamente");
        }
    }

    // Esto funciona para eliminar items de factura
    public void eliminarDetalle() throws SQLException {
        String sql = "DELETE FROM detalle_factura WHERE id_detalle=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdDeatalle());
            pstm.executeUpdate();
            System.out.println("Item eliminado exitosamente");
        }
    }

    // Esto funciona para mostrar todos los detalles de una factura especifica
    public ResultSet consultarDetallePorFactura(int idFacturaBuscar) throws SQLException {
        String sql = "SELECT df.*, p.nombre as producto, p.codigo "
                + "FROM detalle_factura df "
                + "INNER JOIN producto p ON df.id_producto = p.id_producto "
                + "WHERE df.id_factura = " + idFacturaBuscar + " "
                + " ORDER BY df.id_detalle";
        return bd.consultarSQL(sql);
    }

    //Esto funciona para obtener el total de una factura sumando sus detelles
    public double calcularTotalFactura(int idFacturaBucar) throws SQLException {
        String sql = "SELECT SUM(subtotal) as total FROM detalle_factura = " + idFacturaBucar;
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next() && rs.getObject("total") != null) {
            return rs.getDouble("total");
        }
        return 0.0;
    }

    // Esto funciona para verificar stock antes de agregar el detalle 
    public boolean verificarStock(int idProductoVerificar, int cantidadRequerida) throws SQLException {
        String sql = "SELECT stock_actuar FROM producto WHERE id_producto = " + idProductoVerificar;
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next()) {
            int stockDisponible = rs.getInt("stock_actual");
            return stockDisponible >= cantidadRequerida;
        }
        return false;
    }

    // Esto funciona para actualizar el stock despues de una venta
    public void actualizarStockProducto(int idProductoActualizar, int cantidadVendida) throws SQLException {
        String sql = "UPDATE producto SET stock_actual = stock_actual - ? WHERE id_producto = ?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, cantidadVendida);
            pstm.setInt(2, idProductoActualizar);
            pstm.executeUpdate();
            System.out.println("Stock actualizado correctamente");
        }
    }

    //Esto funciona para obtener productos mas vendidos
    public ResultSet productosTopVentas() throws SQLException {
        String sql = "SELECT p.nombre, p.codigo, SUM(df.cantidad) as total_vendido, SUM(df.subtotal) as total_ingresos "
                + "FROM detalles_factura df "
                + "INNER JOIN producto p ON df.id_producto = p.id_producto "
                + "GROUP BY df.id_producto, p.nombre, p.codigo "
                + "ORDER BY total_vendido DESC "
                + "LIMIT 10";
        return bd.consultarSQL(sql);
    }
    //Esto funciona para mostrar el detalle completo de una factura

    public void mostrarDetalleFactura(int idFacturaMostrar) throws SQLException {
        ResultSet rs = consultarDetallePorFactura(idFacturaMostrar);
        System.out.println("\n=== DETALLE DE FACTURA ===");
        double totalFactura = 0;

        while (rs.next()) {
            System.out.println("Producto: " + rs.getString("producto"));
            System.out.println("Codigo: " + rs.getString("codigo"));
            System.out.println("Cantidad: " + rs.getInt("cantidad"));
            System.out.println("Precio Unit.: $" + rs.getDouble("precio_unitario"));
            System.out.println("Subtotal: $" + rs.getDouble("subtotal"));
            System.out.println("-----------------------");

            totalFactura += rs.getDouble("subtotal");
        }

        System.out.println("SUBTOTAL FACTURA: $" + totalFactura);
    }
    //Esto funciona para procesar una venta completa (agregar detalle y actualizar stock)

    public void procesarVenta() throws SQLException {
        if (verificarStock(getIdProducto(), getCantidad())) {
            insertarDetalle();
            actualizarStockProducto(getIdProducto(), getCantidad());
            System.out.println("Venta procesada correctamente");
        } else {
            System.out.println("Error: Stock insuficiente para este producto");
        }
    }      
}
    


