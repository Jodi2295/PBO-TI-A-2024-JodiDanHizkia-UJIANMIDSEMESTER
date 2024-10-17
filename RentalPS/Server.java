package RentalPS;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    public static Scanner scanner = new Scanner(System.in);
    public static List<User> users = new ArrayList<>();
    public static List<Rental> rentals = new ArrayList<>();
    public static List<String> inventory = new ArrayList<>();
    public static List<Payment> financialReport = new ArrayList<>();
    public static List<Review> reviews = new ArrayList<>();
    public static List<String> notifications = new ArrayList<>();

    public static void main(String[] args) {
        inventory.add("PS1");
        inventory.add("PS2");
        inventory.add("PS3");
        inventory.add("PS4");
        inventory.add("PS5");
        inventory.add("PS6(gak dulu belum ada duit)");
        startApp();
    }

    public static void startApp() {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Selamat datang di Aplikasi Rental PS!");
            System.out.println("1. Daftar Akun");
            System.out.println("2. Login");
            System.out.println("3. Keluar");
            String choice = input("Pilih: ");
            switch (choice) {
                case "1":
                    register();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    isRunning = false;
                    System.out.println("Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilih menu dengan benar ya.");
            }
        }
    }

    public static void register() {
        String username = input("Masukkan username: ");
        String password = input("Masukkan password: ");
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username sudah dipakai.");
                return;
            }
        }
        users.add(new User(username, password));
        System.out.println("Selamat akun sukses dibuat.");
    }

    public static void login() {
        String username = input("Masukkan username: ");
        String password = input("Masukkan password: ");
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    System.out.println("Login sukses");
                    System.out.println("Hello, " + username + "!");
                    showMainMenu(user);
                    return;
                } else {
                    System.out.println("Passwordnya salah. Coba aja lagi.");
                    return;
                }
            }
        }
        System.out.println("Buat dulu akunnya ya");
    }

    public static void showMainMenu(User user) {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("MENU");
            System.out.println("1. Memesan Konsol");
            System.out.println("2. Mengelola Inventaris / Hapus");
            System.out.println("3. Sistem Pembayaran");
            System.out.println("4. Laporan Keuangan");
            System.out.println("5. Notifikasi Pengingat");
            System.out.println("6. Riwayat Sewa");
            System.out.println("7. Ulasan");
            System.out.println("8. Keluar");

            String choice = input("Pilih: ");
            switch (choice) {
                case "1":
                    orderConsole(user);
                    break;
                case "2":
                    deleteRental(user);
                    break;
                case "3":
                    makePayment(user);
                    break;
                case "4":
                    viewFinancialReport();
                    break;
                case "5":
                    viewReminders(user);
                    break;
                case "6":
                    viewRentalHistory(user);
                    break;
                case "7":
                    writeReview(user);
                    break;
                case "8":
                    isRunning = false;
                    System.out.println("Terima kasih ya sudah menggunakan aplikasi");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Yah salah pilih menu.");
            }
        }
    }

    public static void orderConsole(User user) {
        System.out.println("Pilihan Konsol:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i));
        }
        String choice = input("Pilih nomor konsol yang mau disewa: ");
        String durationStr = input("Masukkan durasi sewa (contohnya 1 jam / 1 hari): ");
        String[] parts = durationStr.split(" ");
        if (parts.length != 2) {
            System.out.println("Inputnya tidak valid. masukkan aja dalam format 'jumlah unit'.");
            return;
        }
        try {
            int duration = Integer.parseInt(parts[0]);
            String unit = parts[1].toLowerCase();
            if (unit.equals("jam") || unit.equals("hari")) {
                rentals.add(new Rental(user.getUsername(), inventory.get(Integer.parseInt(choice) - 1), duration, unit));
                notifications.add("Konsol " + inventory.get(Integer.parseInt(choice) - 1) + " berhasil dipesan.");
                System.out.println("Konsol berhasil dipesan.");
            } else {
                System.out.println("Unitnya tidak valid. masukkan aja  'jam' atau 'hari'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Inputnya tidak valid.");
        }
    }

    public static void viewReminders(User user) {
        System.out.println("Pengingat Anda:");
        for (Rental rental : rentals) {
            if (rental.getUsername().equals(user.getUsername())) {
                System.out.println(" Sewa " + rental.getConsole() + " dengan ID Sewa: " + rental.getRentalId() + ", akan berakhir dalam " + rental.getReadableDuration() + ".");
            }
        }
    }

    public static void deleteRental(User user) {
        System.out.println("Daftar Sewa Anda:");
        List<Rental> userRentals = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getUsername().equals(user.getUsername())) {
                userRentals.add(rental);
                System.out.println("ID Sewa: " + rental.getRentalId() +
                        ", Konsol: " + rental.getConsole() +
                        ", Durasi: " + rental.getReadableDuration() +
                        ", Status: " + rental.getStatus());
            }
        }
        if (userRentals.isEmpty()) {
            System.out.println("Anda tidak memiliki sewa yang dapat dihapus.");
            return;
        }
        String rentalIdStr = input("Masukkan ID Sewa yang ingin dihapus: ");
        try {
            int rentalId = Integer.parseInt(rentalIdStr);
            Rental rentalToDelete = null;
            for (Rental rental : userRentals) {
                if (rental.getRentalId() == rentalId) {
                    rentalToDelete = rental;
                    break;
                }
            }
            if (rentalToDelete != null) {
                rentals.remove(rentalToDelete);
                System.out.println("Sewa dengan ID " + rentalId + " berhasil dihapus.");
                notifications.add("Sewa dengan ID " + rentalId + " telah dihapus.");
            } else {
                System.out.println("ID Sewanya tidak valid.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID Sewanya tidak valid.");
        }
    }

    public static void viewFinancialReport() {
        int totalIncome = 0;
        System.out.println("Riwayat Pembayaran:");
        for (Payment payment : financialReport) {
            System.out.println("Username: " + payment.getUsername() + ", Jumlah: " + payment.getAmount() + ", Tanggal: " + payment.getDate());
            totalIncome += payment.getAmount();
        }
        System.out.println("Total Pendapatan: " + totalIncome);
    }

    public static void viewRentalHistory(User user) {
        System.out.println("Riwayat Sewa Anda:");
        for (Rental rental : rentals) {
            if (rental.getUsername().equals(user.getUsername())) {
                System.out.println("ID Sewa: " + rental.getRentalId() +
                        ", Konsol: " + rental.getConsole() +
                        ", Durasi: " + rental.getReadableDuration() +
                        ", Status: " + rental.getStatus());
            }
        }
    }

    public static void writeReview(User user) {
        String reviewText = input("Tulis ulasan Anda: ");
        reviews.add(new Review(user.getUsername(), reviewText));
        System.out.println("Terima kasih sudah berpartisipasi untuk ulasan ya!");
    }

    public static void makePayment(User user) {
        System.out.println("Daftar Sewa yang belum Dibayar:");
        List<Rental> unpaidRentals = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getUsername().equals(user.getUsername()) && !rental.isPaid()) {
                unpaidRentals.add(rental);
                System.out.println("ID Sewa: " + rental.getRentalId() +
                        ", Konsol: " + rental.getConsole() +
                        ", Durasi: " + rental.getReadableDuration());
            }
        }

        String rentalIdStr = input("Masukkan ID Sewa yang mau dibayar: ");
        Rental selectedRental = null;
        try {
            int rentalId = Integer.parseInt(rentalIdStr);
            for (Rental rental : unpaidRentals) {
                if (rental.getRentalId() == rentalId) {
                    selectedRental = rental;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID Sewa tidak valid.");
            return;
        }
        if (selectedRental != null) {
            selectedRental.markAsPaid();
            int amount = calculateAmount(selectedRental);
            String date = java.time.LocalDate.now().toString();
            financialReport.add(new Payment(user.getUsername(), amount, date));
            System.out.println("Pembayaran sukses dilakukan untuk ID Sewa: " + selectedRental.getRentalId() +
                    ", konsol: " + selectedRental.getConsole() + ", jumlah: " + amount);
        } else {
            System.out.println("ID Sewa yang  dimasukkan tidak valid.");
        }
    }

    public static int calculateAmount(Rental rental) {
        int amount = 0;
        if (rental.getUnit().equals("jam")) {
            amount = rental.getDuration() * 5000;
        }
        if (rental.getUnit().equals("hari")) {
            amount = rental.getDuration() * 50000;
        }
        return amount;
    }

    public static String input(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class Rental {
    private static int idCounter = 1;
    private int rentalId;
    private String username;
    private String console;
    private int duration;
    private String unit;
    private boolean isPaid;

    public Rental(String username, String console, int duration, String unit) {
        this.rentalId = idCounter++;
        this.username = username;
        this.console = console;
        this.duration = duration;
        this.unit = unit;
        this.isPaid = false;
    }

    public int getRentalId() {
        return rentalId;
    }

    public String getUsername() {
        return username;
    }

    public String getConsole() {
        return console;
    }

    public int getDuration() {
        return duration;
    }

    public String getUnit() {
        return unit;
    }

    public String getStatus() {
        return isPaid ? "paid" : "active";
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void markAsPaid() {
        this.isPaid = true;
    }

    public String getReadableDuration() {
        return duration + " " + unit;
    }
}

class Review {
    private String username;
    private String reviewText;

    public Review(String username, String reviewText) {
        this.username = username;
        this.reviewText = reviewText;
    }
}

class Payment {
    private String username;
    private int amount;
    private String date;

    public Payment(String username, int amount, String date) {
        this.username = username;
        this.amount = amount;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}