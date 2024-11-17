package App.menu;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import App.system.System;
import App.system.data.*;
import App.system.data.AppointmentSystem;
import App.system.data.MedicineSystem;
import App.system.user.*;
import App.system.user.UserSystem;

/**
 * The StartMenu class represents the main entry point for users accessing the hospital management system.
 * It provides options to log in as different types of users or to quit the application.
 */
public class StartMenu extends Menu {
    HashMap<String, System> containers;

    /**
     * Constructs a StartMenu instance with the specified containers.
     *
     * @param containers The HashMap of containers representing various user and data storage containers.
     */
    public StartMenu(HashMap<String, System> containers) {
        this.containers = containers;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(java.lang.System.in);

        int choice;
        do {
            java.lang.System.out.println("Welcome to the HMS App");
            java.lang.System.out.println("----------------------------------------");
            java.lang.System.out.println("Hospital Start Menu");
            java.lang.System.out.println("0. Quit");
            java.lang.System.out.println("1. Log in");
            java.lang.System.out.print("Enter your choice: ");

            // Use try-catch to handle invalid input
            try {
                choice = sc.nextInt();

                switch (choice) {
                    case 0:
                        java.lang.System.out.println("Quitting...");
                        break;
                    case 1:
                        java.lang.System.out.print("Enter hospital ID: ");
                        String hospitalId = sc.next();
                        java.lang.System.out.print("Enter password: ");
                        String password = sc.next();
                        Menu userMenu = createUserMenu(hospitalId, password);
                        if (userMenu != null) {
                            userMenu.run();
                        } else {
                            java.lang.System.out.println("Wrong hospital ID or wrong password");
                        }
                        break;
                    default:
                        java.lang.System.out.println("Invalid choice");
                }
            } catch (InputMismatchException e) {
                java.lang.System.out.println("Invalid answer! Please enter a number.");
                sc.nextLine(); // Clear the invalid input from the scanner buffer
                choice = -1; // Set choice to a non-exiting value to continue the loop
            }
            java.lang.System.out.println("---------Thank you for using HMS----------\n\n\n\n");
        } while (choice != 0);

    }

    /**
     * Creates and returns a user-specific menu based on the given hospital ID and password.
     *
     * @param hospitalId The hospital ID of the user attempting to log in.
     * @param password   The password of the user.
     * @return A Menu object for the logged-in user, or null if the login fails.
     */
    public Menu createUserMenu(String hospitalId, String password) {
        PatientSystem patientContainer = (PatientSystem) (containers.get("Patient"));
        DoctorSystem doctorContainer = (DoctorSystem) (containers.get("Doctor"));
        PharmacistSystem pharmacistContainer = (PharmacistSystem) (containers.get("Pharmacist"));
        AdministratorSystem administratorContainer = (AdministratorSystem) (containers.get("Administrator"));
        AppointmentSystem appointmentContainer = (AppointmentSystem) (containers.get("Appointment"));
        AppointmentOutcomeRecordSystem appointmentOutcomeRecordContainer = (AppointmentOutcomeRecordSystem) (containers.get("AppointmentOutcome"));
        MedicineSystem medicineContainer = (MedicineSystem) (containers.get("Medicine"));
        ReplenishmentRequestSystem replenishmentRequestContainer = (ReplenishmentRequestSystem) (containers.get("StockReplenishmentRequest"));

        /*//put some sample appointments for testing
        appointmentContainer.addAppointment("2021-10-01 10:00", "P1001", "D001");
        appointmentContainer.addAppointment("2021-10-01 11:00", "P1002", "D002");
        appointmentContainer.addAppointment("2021-10-01 14:00", "P1003", "d001");*/

        if (patientContainer.containsUser(hospitalId) && patientContainer.getUserTypeByHospitalId(hospitalId).equals("Patient")) {
            String correctPassword = patientContainer.getUserByHospitalId(hospitalId).getPassword();
            if (password.equals(correctPassword)) {
                if (password.equals("password")) {
                    promptPasswordChange(patientContainer, hospitalId);  // Prompt to change default password
                }
                return new PatientMenu(
                        hospitalId,
                        patientContainer,
                        doctorContainer,
                        appointmentContainer,
                        appointmentOutcomeRecordContainer
                );
            } else return null;
        } else if (doctorContainer.containsUser(hospitalId) && doctorContainer.getUserTypeByHospitalId(hospitalId).equals("Doctor")) {
            String correctPassword = doctorContainer.getUserByHospitalId(hospitalId).getPassword();
            if (password.equals(correctPassword)) {
                if (password.equals("password")) {
                    promptPasswordChange(doctorContainer, hospitalId);  // Prompt to change default password
                }
                return new DoctorMenu(
                        hospitalId,
                        doctorContainer,
                        patientContainer,
                        appointmentContainer,
                        appointmentOutcomeRecordContainer,
                        medicineContainer
                );
            } else return null;
        } else if (pharmacistContainer.containsUser(hospitalId) && pharmacistContainer.getUserTypeByHospitalId(hospitalId).equals("Pharmacist")) {
            String correctPassword = pharmacistContainer.getUserByHospitalId(hospitalId).getPassword();
            if (password.equals(correctPassword)) {
                if (password.equals("password")) {
                    promptPasswordChange(pharmacistContainer, hospitalId);  // Prompt to change default password
                }
                return new PharmacistMenu(
                        hospitalId,
                        appointmentOutcomeRecordContainer,
                        medicineContainer,
                        replenishmentRequestContainer
                );
            } else return null;
        } else if (administratorContainer.containsUser(hospitalId) && administratorContainer.getUserTypeByHospitalId(hospitalId).equals("Administrator")) {
            String correctPassword = administratorContainer.getUserByHospitalId(hospitalId).getPassword();
            if (password.equals(correctPassword)) {
                if (password.equals("password")) {
                    promptPasswordChange(administratorContainer, hospitalId);  // Prompt to change default password
                }
                return new AdministratorMenu(
                        hospitalId,
                        administratorContainer,
                        patientContainer,
                        doctorContainer,
                        pharmacistContainer,
                        medicineContainer,
                        replenishmentRequestContainer,
                        appointmentContainer
                );
            } else return null;
        }
        return null;
    }


    /**
     * Prompts the user to change their default password.
     *
     * @param userContainer The container storing user information.
     * @param hospitalId    The hospital ID of the user.
     */
    private void promptPasswordChange(UserSystem userContainer, String hospitalId) {
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("You are using the default password. Please change it for security.");
        String newPassword;

        while (true) {
            java.lang.System.out.print("Enter new password: ");
            newPassword = sc.nextLine();

            // Check if the new password meets complexity requirements
            if (!isPasswordComplex(newPassword)) {
                java.lang.System.out.println("Password must be 6-20 characters and contain at least one digit, one lowercase, and one uppercase letter.");
                continue;
            }

            java.lang.System.out.print("Confirm new password: ");
            String confirmPassword = sc.nextLine();

            if (newPassword.equals(confirmPassword)) {
                userContainer.getUserByHospitalId(hospitalId).setPassword(newPassword);
                java.lang.System.out.println("Password changed successfully.");
                break;
            } else {
                java.lang.System.out.println("Passwords do not match. Please try again.");
            }
        }
    }


    /**
     * Checks if the given password meets complexity requirements.
     *
     * @param password The password to be checked.
     * @return true if the password meets the complexity requirements, otherwise false.
     */
    private boolean isPasswordComplex(String password) {
        if (password.length() < 6 || password.length() > 20) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUpper && hasLower && hasDigit;
    }

}
