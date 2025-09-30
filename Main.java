import java.io.*;   // Librerías para leer y escribir archivos
import java.util.*; // Librerías para usar listas, mapas y colecciones

public class Main {

    // ======== MÉTODO PRINCIPAL ========
    // Aquí arranca el programa
    public static void main(String[] args) {
        try {
            // 1. Leer productos desde el archivo productos.txt
            Map<String, Producto> productos = leerProductos("productos.txt");

            // 2. Leer vendedores desde el archivo vendedores.txt
            Map<String, Vendedor> vendedores = leerVendedores("vendedores.txt");

            // 3. Buscar todos los archivos que empiecen con "ventas"
            File carpeta = new File("."); // el punto significa la carpeta actual
            File[] archivos = carpeta.listFiles((dir, name) -> name.startsWith("ventas"));

            if (archivos != null) {
                // 4. Recorrer cada archivo de ventas
                for (File archivo : archivos) {
                    leerVentas(archivo, productos, vendedores);
                }
            }

            // 5. Crear los reportes de salida
            generarReporteVendedores(vendedores);
            generarReporteProductos(productos);

            System.out.println("Reportes generados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error ejecutando programa: " + e.getMessage());
        }
    }

    // ======== MÉTODOS DE LECTURA ========

    // Lee el archivo de productos y guarda en un mapa
    // clave = id del producto (ej: P1)
    // valor = objeto Producto con nombre y precio
    public static Map<String, Producto> leerProductos(String filename) throws IOException {
        Map<String, Producto> productos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String id = partes[0];
                String nombre = partes[1];
                int precio = Integer.parseInt(partes[2]);
                productos.put(id, new Producto(nombre, precio));
            }
        }
        return productos;
    }

    // Lee el archivo de vendedores
    // clave = tipoDoc+id (ej: CC1234)
    // valor = objeto Vendedor con nombre y apellido
    public static Map<String, Vendedor> leerVendedores(String filename) throws IOException {
        Map<String, Vendedor> vendedores = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String clave = partes[0] + partes[1]; // CC+1234
                String nombre = partes[2] + " " + partes[3];
                vendedores.put(clave, new Vendedor(nombre));
            }
        }
        return vendedores;
    }

    // Lee un archivo de ventas
    // Suma ventas a productos y a vendedores
    public static void leerVentas(File archivo, Map<String, Producto> productos, Map<String, Vendedor> vendedores) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            // primera línea: tipoDoc;id
            String linea = br.readLine();
            if (linea == null) return;

            String[] cabecera = linea.split(";");
            String claveVendedor = cabecera[0] + cabecera[1]; // ejemplo: CC1234
            Vendedor vendedor = vendedores.get(claveVendedor);

            // resto de líneas: producto;cantidad;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String productoId = partes[0];
                int cantidad = Integer.parseInt(partes[1]);

                // actualizar vendedor
                if (vendedor != null) {
                    vendedor.totalVentas += cantidad;
                }

                // actualizar producto
                Producto producto = productos.get(productoId);
                if (producto != null) {
                    producto.totalVendido += cantidad;
                }
            }
        }
    }

    // ======== MÉTODOS DE REPORTE ========

    // Genera archivo con ventas por vendedor
    public static void generarReporteVendedores(Map<String, Vendedor> vendedores) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_vendedores.txt"))) {
            for (Vendedor v : vendedores.values()) {
                bw.write(v.nombre + ";" + v.totalVentas);
                bw.newLine();
            }
        }
    }

    // Genera archivo con ventas por producto
    public static void generarReporteProductos(Map<String, Producto> productos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_productos.txt"))) {
            for (Map.Entry<String, Producto> entry : productos.entrySet()) {
                Producto p = entry.getValue();
                bw.write(p.nombre + ";" + p.totalVendido);
                bw.newLine();
            }
        }
    }
}

// ======== CLASES DE APOYO ========

// Producto → guarda nombre, precio y total vendido
class Producto {
    String nombre;
    int precio;
    int totalVendido;

    Producto(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.totalVendido = 0;
    }
}

// Vendedor → guarda nombre y total de ventas realizadas
class Vendedor {
    String nombre;
    int totalVentas;

    Vendedor(String nombre) {
        this.nombre = nombre;
        this.totalVentas = 0;
    }
}
 	