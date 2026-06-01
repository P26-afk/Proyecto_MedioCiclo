package proyecto_mediociclo;

import java.util.Date;
import java.sql.*;

public class Producto {

    private int idProduct, idCatego, idProve, stockActu, stockMax, stockMin;
    private String codigo, nombre, descrip;
    private double precCompra, precVenta;
    private Date fechaIngreso;
    AccesoBD bd;

    public Producto() throws Exception {
        bd = new AccesoBD("localhost", "root", "1234", "Facturacion");
        bd.conectarBD();
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdCatego() {
        return idCatego;
    }

    public void setIdCatego(int idCatego) {
        this.idCatego = idCatego;
    }

    public int getIdProve() {
        return idProve;
    }

    public void setIdProve(int idProve) {
        this.idProve = idProve;
    }

    public int getStockActu() {
        return stockActu;
    }

    public void setStockActu(int stockActu) {
        this.stockActu = stockActu;
    }

    public int getStockMax() {
        return stockMax;
    }

    public void setStockMax(int stockMax) {
        this.stockMax = stockMax;
    }

    public int getStockMin() {
        return stockMin;
    }

    public void setStockMin(int stockMin) {
        this.stockMin = stockMin;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public double getPrecCompra() {
        return precCompra;
    }

    public void setPrecCompra(double precCompra) {
        this.precCompra = precCompra;
    }

    public double getPrecVenta() {
        return precVenta;
    }

    public void setPrecVenta(double precVenta) {
        this.precVenta = precVenta;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    /*    
    public int obtenerSigID() throws SQLException{
        int nuevoID;
        ResultSet rs= bd.consultarSQL("SELECT MAX(id_producto) AS maximo FROM producto");
        if (rs.next() && rs.getObject("maximo") != null) {
            nuevoID = rs.getInt("maximo")+1;
        }else{
            nuevoID=1;
        }
        return nuevoID;
    }
     */
    public void insertProduct() throws SQLException {
        String sql = "INSERT INTO producto (id_categoria, id_proveedor, codigo, nombre, descripcion, precio_compra, precio_venta, stock_actual, stock_minimo, stock_maximo, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdCatego());
            pstm.setInt(2, getIdProve());
            pstm.setString(3, getCodigo());
            pstm.setString(4, getNombre());
            pstm.setString(5, getDescrip());
            pstm.setDouble(6, getPrecCompra());
            pstm.setDouble(7, getPrecVenta());
            pstm.setInt(8, getStockActu());
            pstm.setInt(9, getStockMin());
            pstm.setInt(10, getStockMax());
            pstm.setInt(11, getIdProduct());
        }
    }

    public void eliminarProduct() throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, getIdProduct());
            pstm.executeUpdate();
            System.out.println("Producto eliminado exitosamente");
        }
    }

    public ResultSet consultarProduct() throws SQLException {
        String sql = "SELECT p.*, c.nombre as categoria, pr.razon_social as proveedor "
                + "FROM producto p "
                + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria "
                + "INNER JOIN proveedor pr ON p.id_proveedor = pr.id_proveedor "
                + "ORDER BY p.nombre";
        return bd.consultarSQL(sql);
    }

    public ResultSet buscarPorNom(String nombre) throws SQLException {
        String sql = "SELECT p.*, c.nombre as categoria, pr.razon_social as proveedor "
                + "FROM producto p "
                + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria "
                + "INNER JOIN proveedor pr ON p.id_proveedor = pr.id_proveedor "
                + "WHERE p.nombre LIKE '%" + nombre + "%' "
                + "ORDER BY p.nombre";
        return bd.consultarSQL(sql);
    }

    public ResultSet buscarPorCategoria(int idCategoriaB) throws SQLException {
        String sql = "SELECT p.*, c.nombre as categoria, pr.razon_social as proveedor "
                + "FROM producto p "
                + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria "
                + "INNER JOIN proveedor pr ON p.id_proveedor = pr.id_proveedor "
                + "WHERE p.id_categoria = " + idCategoriaB + " "
                + "ORDER BY p.nombre";
        return bd.consultarSQL(sql);
    }

    public ResultSet productosStockBajo() throws SQLException {
        String sql = "SELECT p.*, c.nombre as categoria "
                + "FROM producto p "
                + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria "
                + "WHERE p.stock_actual <= p.stock_minimo "
                + "ORDER BY p.stock_actual";
        return bd.consultarSQL(sql);
    }

    public boolean codigoExiste(String codigoVerificar) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM producto WHERE codigo = '" + codigoVerificar + "'";
        ResultSet rs = bd.consultarSQL(sql);
        if (rs.next()) {
            return rs.getInt("total") > 0;
        }
        return false;
    }

    public void actualizarStock(int nuevoStock) throws SQLException {
        String sql = "UPDATE producto SET stock_actual=? WHERE id_producto=?";
        try (PreparedStatement pstm = bd.conexion.prepareStatement(sql)) {
            pstm.setInt(1, nuevoStock);
            pstm.setInt(2, getIdProduct());
            pstm.executeUpdate();
            System.out.println("Stock actualizado correctamente");
        }
    }

    public ResultSet listarTodos() throws SQLException {
        String sql = "SELECT id_producto, nombre, precio_venta From producto";
        return bd.consultarSQL(sql);
    }

    public ResultSet buscarPorID(int id) throws SQLException {
        String sql = "SELECT * From producto WHERE id_producto= " + id;
        return bd.consultarSQL(sql);
    }
}
