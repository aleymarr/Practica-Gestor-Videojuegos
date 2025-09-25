import main.java.exception.PersistenceException;
import main.java.model.VideoGame;
import main.java.system.FileSystem;
import main.java.system.PersistentSystem;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final PersistentSystem persistence = new FileSystem();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- GESTOR DE VIDEOJUEGOS ---");
        System.out.println("Por favor, introduce tu identificador de usuario para iniciar sesión: ");
        String userId = scanner.nextLine();
        System.out.println("\n¡Bienvenido, " + userId + "!");

        boolean exit = false;
        while (!exit) {
            showMenu();
            System.out.print("Elige una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    listVideoGames(userId);
                    break;
                case "2":
                    addVideoGame(userId);
                    break;
                case "3":
                    updateVideoGame(userId);
                    break;
                case "4":
                    deleteVideoGame(userId);
                    break;
                case "5":
                    searchVideoGame(userId);
                    break;
                case "6":
                    importData(userId);
                    break;
                case "7":
                    exportData(userId);
                    break;
                case "8":
                    createBackup(userId);
                    break;
                case "0":
                    exit = true;
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.err.println("Opción no válida. Por favor, intenta de nuevo.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void showMenu() {
        System.out.println("--- MENÚ PRINCIPAL ---");
        System.out.println("1. Listar todos los videojuegos");
        System.out.println("2. Añadir un nuevo videojuego");
        System.out.println("3. Editar un videojuego");
        System.out.println("4. Eliminar un videojuego");
        System.out.println("5. Buscar un videojuego por ID");
        System.out.println("6. Importar datos desde otro fichero");
        System.out.println("7. Exportar datos a otro fichero");
        System.out.println("8. Crear una copia de seguridad");
        System.out.println("0. Salir del programa");
    }

    private static void listVideoGames(String userId) {
        System.out.println("\n--- LISTA DE VIDEOJUEGOS ---");
        try {
            List<VideoGame> games = persistence.listAllVideoGames(userId);
            if (games.isEmpty()) {
                System.out.println("No tienes ningún videojuego registrado.");
            } else {
                for (VideoGame game : games) {
                    System.out.println(game.toString());
                }
            }
        } catch (PersistenceException e) {
            System.err.println("ERROR AL LISTAR: " + e.getMessage());
        }
    }

    private static void addVideoGame(String userId) {
        System.out.println("\n--- AÑADIR NUEVO VIDEOJUEGO ---");
        try {
            System.out.print("ID (único): ");
            String id = scanner.nextLine();
            System.out.print("Título: ");
            String title = scanner.nextLine();
            System.out.print("Género: ");
            String genre = scanner.nextLine();
            System.out.print("Compañía: ");
            String company = scanner.nextLine();
            System.out.print("Fecha de lanzamiento (AAAA-MM-DD): ");
            LocalDate releaseDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Horas jugadas: ");
            int hoursPlayed = Integer.parseInt(scanner.nextLine());
            System.out.print("¿Es favorito? (s/n): ");
            boolean isFavorite = scanner.nextLine().equalsIgnoreCase("s");

            VideoGame newGame = new VideoGame(id, title, genre, company, releaseDate, hoursPlayed, isFavorite);
            persistence.saveVideoGame(userId, newGame);
            System.out.println("¡Videojuego añadido con éxito!");

        } catch (DateTimeParseException e) {
            System.err.println("ERROR: El formato de la fecha es incorrecto. Debe ser AAAA-MM-DD.");
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Las horas jugadas deben ser un número entero.");
        } catch (PersistenceException e) {
            System.err.println("ERROR AL GUARDAR: " + e.getMessage());
        }
    }

    private static void updateVideoGame(String userId) {
        System.out.println("\n--- EDITAR VIDEOJUEGO ---");
        System.out.print("Introduce el ID del videojuego que quieres editar: ");
        String id = scanner.nextLine();
        try {
            VideoGame game = persistence.loadVideoGame(userId, id);

            System.out.println("Editando: " + game.getTitle());
            System.out.print(
                    "Nuevas horas jugadas (deja en blanco para no cambiar - actual: " + game.getHoursPlayed() + "): ");
            String hoursStr = scanner.nextLine();
            if (!hoursStr.isEmpty()) {
                game.setHoursPlayed(Integer.parseInt(hoursStr));
            }

            System.out.print("¿Es favorito? (s/n, deja en blanco para no cambiar - actual: "
                    + (game.isFavorite() ? "Sí" : "No") + "): ");
            String favStr = scanner.nextLine();
            if (!favStr.isEmpty()) {
                game.setFavorite(favStr.equalsIgnoreCase("s"));
            }

            persistence.updateVideoGame(userId, game);
            System.out.println("¡Videojuego actualizado con éxito!");

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Las horas jugadas deben ser un número entero.");
        } catch (PersistenceException e) {
            System.err.println("ERROR AL ACTUALIZAR: " + e.getMessage());
        }
    }

    private static void deleteVideoGame(String userId) {
        System.out.println("\n--- ELIMINAR VIDEOJUEGO ---");
        System.out.print("Introduce el ID del videojuego que quieres eliminar: ");
        String id = scanner.nextLine();
        try {
            persistence.deleteVideoGame(userId, id);
            System.out.println("¡Videojuego eliminado con éxito!");
        } catch (PersistenceException e) {
            System.err.println("ERROR AL ELIMINAR: " + e.getMessage());
        }
    }

    private static void searchVideoGame(String userId) {
        System.out.println("\n--- BUSCAR VIDEOJUEGO ---");
        System.out.print("Introduce el ID del videojuego a buscar: ");
        String id = scanner.nextLine();
        try {
            VideoGame game = persistence.loadVideoGame(userId, id);
            System.out.println("Juego encontrado:");
            System.out.println(game.toString());
        } catch (PersistenceException e) {
            System.err.println("ERROR EN LA BÚSQUEDA: " + e.getMessage());
        }
    }

    private static void importData(String userId) {
        System.out.println("\n--- IMPORTAR DATOS ---");
        System.out.print("Introduce la ruta completa del archivo CSV a importar (ej: C:/temp/import.csv): ");
        String path = scanner.nextLine();
        try {
            persistence.importVideoGames(userId, path);
            System.out.println("¡Datos importados con éxito! Los juegos existentes no fueron modificados.");
        } catch (PersistenceException e) {
            System.err.println("ERROR AL IMPORTAR: " + e.getMessage());
        }
    }

    private static void exportData(String userId) {
        System.out.println("\n--- EXPORTAR DATOS ---");
        System.out.print("Introduce la ruta completa donde guardar el archivo CSV (ej: C:/temp/export.csv): ");
        String path = scanner.nextLine();
        try {
            persistence.exportVideoGames(userId, path);
            System.out.println("¡Datos exportados con éxito a " + path + "!");
        } catch (PersistenceException e) {
            System.err.println("ERROR AL EXPORTAR: " + e.getMessage());
        }
    }

    private static void createBackup(String userId) {
        System.out.println("\n--- CREAR COPIA DE SEGURIDAD ---");
        try {
            persistence.createBackup(userId);
            System.out.println("¡Copia de seguridad creada con éxito en la carpeta 'data/" + userId + "/backup/'!");
        } catch (PersistenceException e) {
            System.err.println("ERROR AL CREAR BACKUP: " + e.getMessage());
        }
    }
}