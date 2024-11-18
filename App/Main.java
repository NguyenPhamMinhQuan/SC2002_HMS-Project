package App;

import java.util.HashMap;

import App.system.System;
import App.system.data.*;
import App.system.data.AppointmentSystem;
import App.system.data.MedicineSystem;
import App.system.user.*;
import App.system.user.PatientSystem;
import App.menu.StartMenu;


/**
 * The main class for running the Hospital Management System (HMS).
 * <p>
 * This class initializes all necessary system, sets up data paths, and starts the user interface for the HMS.
 * It also ensures that data is saved back to CSV files upon quitting the system.
 */
public class Main {

    /**
     * The main method that initializes system, runs the start menu, and exports data upon exit.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        String patientPath = "data/Patient_List.csv";
        String staffPath = "data/Staff_List.csv";
        String MedicinePath = "data/Medicine_List.csv";
        String MedicalRecordPath = "data/Medical_Record.csv";
        String AppointmentPath = "data/Appointment_List.csv";
        String AppointmentOutcomeRecordPath = "data/AppointmentOutcomeRecord_List.csv";
        String ReplenishmentRequestPath = "data/ReplenishmentRequest_List.csv";
        String DoctorAdditionals = "data/DoctorAdditionals_List.csv";
        String prescriptions_list = "data/Prescriptions_List.csv";

        PatientSystem patientSystem = new PatientSystem(patientPath, MedicalRecordPath);
        // Initialize separate system for each staff type
        DoctorSystem doctorSystem = new DoctorSystem(staffPath);
        PharmacistSystem pharmacistSystem = new PharmacistSystem(staffPath);
        AdministratorSystem administratorSystem = new AdministratorSystem(staffPath);

        MedicineSystem medicineSystem = new MedicineSystem(MedicinePath);
        AppointmentSystem appointmentSystem = new AppointmentSystem(AppointmentPath);
        AppointmentOutcomeRecordSystem appointmentOutcomeSystem = new AppointmentOutcomeRecordSystem(AppointmentOutcomeRecordPath);
        ReplenishmentRequestSystem replenishmentRequestSystem = new ReplenishmentRequestSystem(ReplenishmentRequestPath);

        HashMap<String, System> system = new HashMap<>();
        system.put("Patient", patientSystem);
        system.put("Doctor", doctorSystem);
        system.put("Pharmacist", pharmacistSystem);
        system.put("Administrator", administratorSystem);
        system.put("Medicine", medicineSystem);
        system.put("Appointment", appointmentSystem);
        system.put("AppointmentOutcome", appointmentOutcomeSystem);
        system.put("StockReplenishmentRequest", replenishmentRequestSystem);

        StartMenu startMenu = new StartMenu(system);
        startMenu.run();

        // Export data back to CSV files upon quitting
        patientSystem.exportPatientsToCSV(patientPath);
        doctorSystem.initializeStaffCSV(staffPath);
        doctorSystem.appendStaffToCSVByStaffType(staffPath, "Doctor");
        pharmacistSystem.appendStaffToCSVByStaffType(staffPath, "Pharmacist");
        administratorSystem.appendStaffToCSVByStaffType(staffPath, "Administrator");
        medicineSystem.exportMedicineToCSV(MedicinePath);
        appointmentSystem.exportAppointmentToCSV(AppointmentPath);
        replenishmentRequestSystem.exportReplenishmentRequestToCSV(ReplenishmentRequestPath);
        appointmentOutcomeSystem.exportAppointmentOutcomeRecordToCSV(AppointmentOutcomeRecordPath);
        appointmentOutcomeSystem.exportPrescriptionsToCSV(prescriptions_list);
        doctorSystem.exportDoctorAdditionalsToCSV(DoctorAdditionals);
        patientSystem.exportMedicalRecordsToCSV(MedicalRecordPath);

    }
}