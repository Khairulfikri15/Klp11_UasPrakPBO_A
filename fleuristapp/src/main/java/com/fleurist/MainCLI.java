package com.fleurist;

import com.fleurist.account.Akun;
import com.fleurist.account.Admin;
import com.fleurist.account.Customer;
import com.fleurist.driver.AdminDriver;
import com.fleurist.driver.CustomerDriver;
import com.fleurist.driver.Driver;
import java.util.Scanner;

public class MainCLI {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // Inisialisasi default (akun & default product)
        Driver.init();

        // Menu login / registrasi
        while (true) {
            System.out.println("=======================================");
            System.out.println("         FLEURIST BOUQUET SHOP         ");
            System.out.println("=======================================");
            System.out.println("1. Login");
            System.out.println("2. Registrasi Customer Baru");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu: ");
            String pilih = input.nextLine();

            switch (pilih) {
                case "1":
                    handleLogin(input);
                    break;
                case "2":
                    handleRegistrasi(input);
                    break;
                case "3":
                    System.out.println("Terima kasih telah menggunakan Fleurist!");
                    return; // Keluar dari main method
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void handleLogin(Scanner input) {
        System.out.print("Username: ");
        String username = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();

        Akun akun = Driver.login(username, password);

        if (akun == null) {
            System.out.println("Username atau password salah!");
            return;
        }

        System.out.println("Login berhasil sebagai " + (akun instanceof Admin ? "Admin" : "Customer"));

        if (akun instanceof Admin admin) {
            // Memanggil AdminDriver CLI
            AdminDriver.run(admin, Driver.getCatalog());
        } else if (akun instanceof Customer customer) {
            // Memanggil CustomerDriver CLI
            CustomerDriver.run(customer, Driver.getCatalog());
        }
    }

    private static void handleRegistrasi(Scanner input) {
        System.out.print("Masukkan username baru: ");
        String username = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();
        System.out.print("Masukkan nama lengkap: ");
        String nama = input.nextLine();

        for (Akun a : Driver.getAkunList()) {
            if (a.getUsername().equals(username)) {
                System.out.println("Username sudah digunakan!");
                return;
            }
        }

        Customer customerBaru = new Customer(username, password, nama);
        Driver.getAkunList().add(customerBaru);
        System.out.println("Registrasi berhasil! Silakan login.");
    }
}
