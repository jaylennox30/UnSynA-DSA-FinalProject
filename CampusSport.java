import java.util.Scanner;

public class CampuSport {

    private ReservationManager rm;
    private Scanner sc;

    public CampuSport() {
        rm = new ReservationManager();
        sc = new Scanner(System.in);
    }

    public void startSystem() {
        int choice;

        do {
            System.out.println("\n=== CAMPUSPORT SYSTEM ===");
            System.out.println("1. Student");
            System.out.println("2. Administrator");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Student Menu");
                    break;
                case 2:
                    System.out.println("Administrator Menu");
                    break;
                case 3:
                    System.out.println("Exiting CampuSport...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 3);
    }

    public static void main(String[] args) {
        CampuSport app = new CampuSport();
        app.startSystem();
    }
}
