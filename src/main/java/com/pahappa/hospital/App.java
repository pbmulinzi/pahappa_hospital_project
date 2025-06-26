package com.pahappa.hospital;

import com.pahappa.hospital.menus.*;
import com.pahappa.hospital.utils.HibernateUtil;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.hibernate.SessionFactory;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DoctorMenu doctormenu = new DoctorMenu();
    private static final PatientMenu patientmenu = new PatientMenu();
    private static final AppointmentMenu appointmentmenu = new AppointmentMenu();
    private static final BillingMenu billingmenu = new BillingMenu();
    private static final StaffMenu staffmenu = new StaffMenu();
    private static final MedicalRecordMenu medicalRecordMenu = new MedicalRecordMenu();




    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        showMainMenu();
        sessionFactory.close();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== HOSPITAL MANAGEMENT SYSTEM ===");
            System.out.println("1. Doctor Management");
            System.out.println("2. Patient Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Billing Management");
            System.out.println("5. Medical Records Management");
            System.out.println("6. Staff Management");
            System.out.println("0. Exit System");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        doctormenu.manageDoctors();
                        break;
                    case 2:
                        patientmenu.managePatients();
                        break;
                    case 3:
                        appointmentmenu.manageAppointments();
                        break;
                    case 4:
                        billingmenu.manageBilling();
                        break;
                    case 5:
                        medicalRecordMenu.manageMedicalRecords();
                        break;
                    case 6:
                        staffmenu.manageStaff();
                        break;
                    case 0:
                        System.out.println("Exiting system...");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Try again.");
                scanner.nextLine();
            }

        }
    }

}
