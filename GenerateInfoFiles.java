import java.io.BufferedWriter;
//Escribir texto en un archivo pero lo hace de forma eficiente, porque no manda cada letra directo al disco, sino que las guarda en un “bloque de memoria” (buffer) y luego las escribe juntas.Eso es como hacer un solo viaje grande en lugar de muchos viajes pequeños.
import java.io.FileWriter;
//Es la clase que escribe texto en un archivo.
import java.io.IOException;
//Excepción que se lanza si algo sale mal al leer/escribir archivos
import java.util.Random;
//Sirve para generar números aleatorios


public class GenerateInfoFiles {

    // Listas de vendedores con formato TipoDocumento;NúmeroDocumento;NombresVendedor1;ApellidosVendedor1. El arreglo de Strings es una lista 	de palabras
	
	//private → solo se usa dentro de esta clase
	//static → es un dato común a toda la clase 
	//final → no se puede cambiar
	
    private static final String[] NOMBRES = {"Carlos", "Ana", "Luis", "María", "Sofía", "Pedro"};
    private static final String[] APELLIDOS = {"Gómez", "Rodríguez", "Pérez", "López", "Martínez", "Ramírez"};
    private static final String[] TIPOS_DOC = {"CC", "TI", "CE"};

    //Aquí se crea un objeto de la clase Random. Este objeto sirve para generar números aleatorios.
    private static final Random random = new Random();

    /**
     * Método principal.
     * Al ejecutarse, genera los tres tipos de archivos de prueba.
     */
    public static void main(String[] args) {
        try {
            // Generar archivo de productos
            createProductsFile(10); // genera 10 productos

            // Generar archivo de vendedores
            createSalesManInfoFile(5); // genera 5 vendedores

            // Generar archivo de ventas para cada vendedor
            for (int i = 1; i <= 5; i++) {
                String tipoDoc = TIPOS_DOC[random.nextInt(TIPOS_DOC.length)];
                long id = 1000 + random.nextInt(9000);
                createSalesMenFile(8, tipoDoc, id);
            }

            System.out.println("Archivos generados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error generando archivos: " + e.getMessage());
        }
    }

    // ================== MÉTODOS PARA CREAR ARCHIVOS ==================

    /**
     * Genera un archivo con información de productos.
     * Formato: IDProducto;NombreProducto;PrecioPorUnidad
     */
    public static void createProductsFile(int productsCount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("productos.txt"))) {
            for (int i = 1; i <= productsCount; i++) {
                String id = "P" + i;
                String nombre = "Producto" + i;
                int precio = 1000 + random.nextInt(9000); // precios entre 1000 y 9999
                writer.write(id + ";" + nombre + ";" + precio);
                writer.newLine();
            }
        }
    }

    /**
     * Genera un archivo con información de vendedores.
     * Formato: TipoDocumento;NúmeroDocumento;Nombres;Apellidos
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendedores.txt"))) {
            for (int i = 1; i <= salesmanCount; i++) {
                String tipoDoc = TIPOS_DOC[random.nextInt(TIPOS_DOC.length)];
                long id = 1000 + random.nextInt(9000);
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
                writer.write(tipoDoc + ";" + id + ";" + nombre + ";" + apellido);
                writer.newLine();
            }
        }
    }

    /**
     * Genera un archivo de ventas para un vendedor.
     * Formato:
     *   Primera línea: TipoDocumento;NúmeroDocumento
     *   Luego: IDProducto;Cantidad;
     */
    public static void createSalesMenFile(int randomSalesCount, String tipoDoc, long id) throws IOException {
        String fileName = "ventas_" + tipoDoc + "_" + id + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // primera línea con la info del vendedor
            writer.write(tipoDoc + ";" + id);
            writer.newLine();

            // ventas aleatorias
            for (int i = 1; i <= randomSalesCount; i++) {
                String productoId = "P" + (1 + random.nextInt(10)); // productos P1..P10
                int cantidad = 1 + random.nextInt(20); // cantidades de 1 a 20
                writer.write(productoId + ";" + cantidad + ";");
                writer.newLine();
            }
        }
    }
}
