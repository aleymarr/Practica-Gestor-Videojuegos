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