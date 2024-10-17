package RentalPS;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    public static Scanner scanner = new Scanner(System.in);
    public static List<User> Users = new ArrayList<>();
    public static List<Rental> Rentals = new ArrayList<>();
    public static List<String> inventory = new ArrayList<>();
    public static List<Payment> financialReport = new ArrayList<>();
    public static List<String> notifications = notifications = new ArrayList<>();

    public static void main(String[] args) {
        inventory.add("PS1");
        inventory.add("PS2");
        inventory.add("PS3");
        inventory.add("PS4");
        inventory.add("PS5");
        inventory.add("PS6 (gak dulu belum ada duit)");
        startApp();
    }

    public static void startApp() {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Selamat datang di Aplikasi Rental PlayStation!");
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
                    System.out.println("Sampai jumpa kembali!");
                    break;
                default:
                    System.out.println("Pilih menu dengan benar ya");
            }
        }
    }

    public static void register () {
        String username = input("Masukkan Username: ");
        String password = input("Masukkan Password: ");
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username Sudah Dipakai...");
                return;
            }
        }

        users.add(new User(username, password));
        System.out.println("Selamat Akun Sukses Dibuat...");
    }

    public static void login() {
        String username = input("Masukkan Username: ");
        String password = input("Masukkan Password: ");
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    System.out.println("Login Sukses");
                    System.out.println("Hello, " + username + "!");
                    showMainMenu(user);
                    return;
                }else {
                    System.out.println("Passwordnya Salah. Coba Lagi Aja...");
                    return;
                }
            }
        }

        System.out.println("Daftar dulu, karna akun anda belum terdaftar...");
    }

    public static void showMainMenu(User user) {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("FITUR PILIHAN");
            System.out.println("1. Memesan Konsol");
            System.out.println("2. Mengelola Inventaris / Hapus");
            System.out.println("3. Sistem Pembayaran");

            String choice = input("Pilih: ");
            switch (choice) {
                case "1":
                    ordereConsole(user);
                    break;
                case "2":
                    deleteRental(user);
                    break;
                case "3":
                    makePayment(user);
                    break;
                case "8":
                    isRunning = false;
                    System.out.println("Terimakasih Ya Sudah Menggunakan Aplikasi");
                    break;
                default:
                    System.out.println("Yah Salah Pilih Menu");
            }
        }
    }

    public static void ordereConsole(User user) {
        System.out.println("Pilihan Konsol: ");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i));
        }

        String choice = input("Pilihlah Nomor Konsol Yang Mau Disewa: ");
        String durationStr = input("Masukkan Durasi Sewa ( Contoh 1 jam / 1 hari): ");
        String[] parts = durationStr.split(" ");
        if (parts.length != 2) {
            System.out.println("Inputnya Tidak Valid. Masukkan Aja Lagi Format 'jam' Atau 'hari'." );
            return;
        }

        try {
            int duration = Integer.parseInt(parts[0]);
            String unit = parts[1].toLowerCase();
            if (unit.equals("jam") || unit.equals("hari")) {
                rentals.add(new Rental(user.getUsername(), inventory.get(Integer.parseInt(choice) - 1), duration, unit));
                notifications.add("Konsol " + inventory.get(Integer.parseInt(choice) - 1) + " Sukses Dipesan.");
                System.out.println("Konsol Sukses Dipesan.");
            }else {
                System.out.println("Unitnya Tidak Valid. Masukkan Aja 'jam' Atau 'hari'.");
            }
        }catch (NumberFormatException e) {
            System.out.println("Inputnya Tidak Valid.");
        }
    }

    public static void viewRemiders(User user) {
        System.out.println("Pengingat Anda: ");
        for (Rental rental : rentals) {
            if (rental.getUsername().equals(user.getUsername())) {
                System.out.println(" Sewa" + rental.getConsole() + " Dengan ID Sewa: " + rental.getRentalId() + ", Akan Berakhir Dalam " + rental.getReadableDuration() + ".");
            }
        }
    }

    public static void deleteRental(User user) {
        System.out.println("Daftar Sewa Anda: ");
        List<Rental> userRentals = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getUsername().equals(user.getUsername())) {
                userRentals.add(rental);
                System.out.println("ID Sewa: " + rental.getRentalId() + ", Konsol: " + rental.getConsole() + ", Durasi: " + rental.getReadableDuration() + ", Status: " + rental.getStatus());
            }
        }

        if (userRentals.isEmpty()) {
            System.out.println("Anda Tidak Memiliki Sewa Yang Dapat Dihapus...");
            return;
        }

        String rentalIdStr = input("Masukkan ID Sewa Yang Ingin Dihapus: ");
        try {
            int rentalId = Integer.parseInt(rentalIdStr);
            Rental rentalToDelete = null;
            for (Rental rental : userRentals) {
                rentalToDelete = rental;
                break;
            }
        }

        if (rentalToDelete != null) {
            rentals.remove(rentalToDelete);
            System.out.println("Sewa Dengan ID " + rentalId + " Berhasil Dihapus...");
            notifications.add("Sewa Dengan ID " + rentalId + " Telah Dihapus...");
        }else {
            System.out.println("ID Sewanya Tidak Valid.");
        }
    }catch (NumberFormatException e) {
        System.out.println("ID Sewanya Tidak Valid. ");
    }
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
