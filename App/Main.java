package App;
import java.util.HashMap;

import App.container.Container;
import App.container.data.AppointmentContainer;
import App.container.data.AppointmentOutcomeRecordContainer;
import App.container.data.MedicineContainer;
import App.container.data.ReplenishmentRequestContainer;
import App.container.user.AdministratorContainer;
import App.container.user.DoctorContainer;
import App.container.user.PatientContainer;
import App.container.user.PharmacistContainer;
import App.container.user.StaffContainer;
import App.menu.StartMenu;


/**
 * The main class for running the Hospital Management System (HMS).
 * 
 * This class initializes all necessary containers, sets up data paths, and starts the user interface for the HMS.
 * It also ensures that data is saved back to CSV files upon quitting the system.
 */
public class Main {
    
    /**
     * The main method that initializes containers, runs the start menu, and exports data upon exit.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        String patientPath="data/Patient_List.csv";
        String staffPath="data/Staff_List.csv";
        String MedicinePath="data/Medicine_List.csv";
        String MedicalRecordPath="data/Medical_Record.csv";
        String AppointmentPath="data/Appointment_List.csv";
        String AppointmentOutcomeRecordPath="data/AppointmentOutcomeRecord_List.csv";
        String ReplenishmentRequestPath="data/ReplenishmentRequest_List.csv";
        String DoctorAdditionals= "data/DoctorAdditionals_List.csv";
        String prescriptions_list = "data/Prescriptions_List.csv";//note the last two should also update in respective classes if the path is changed
        
        PatientContainer patientContainer=new PatientContainer(patientPath,MedicalRecordPath);
        // Initialize separate containers for each staff type
        DoctorContainer doctorContainer = new DoctorContainer(staffPath);
        PharmacistContainer pharmacistContainer = new PharmacistContainer(staffPath);
        AdministratorContainer administratorContainer = new AdministratorContainer(staffPath);
        
        MedicineContainer medicineContainer=new MedicineContainer(MedicinePath);
        AppointmentContainer appointmentContainer=new AppointmentContainer(AppointmentPath);
        AppointmentOutcomeRecordContainer appointmentOutcomeContainer=new AppointmentOutcomeRecordContainer(AppointmentOutcomeRecordPath);
        ReplenishmentRequestContainer replenishmentRequestContainer = new ReplenishmentRequestContainer(ReplenishmentRequestPath);

        HashMap<String,Container> containers=new HashMap<>();
        containers.put("Patient",patientContainer);
        containers.put("Doctor",doctorContainer);
        containers.put("Pharmacist",pharmacistContainer);
        containers.put("Administrator",administratorContainer);
        containers.put("Medicine",medicineContainer);
        containers.put("Appointment",appointmentContainer);
        containers.put("AppointmentOutcomeRecord",appointmentOutcomeContainer);
        containers.put("ReplenishmentRequest",replenishmentRequestContainer);

        StartMenu startMenu=new StartMenu(containers);
        startMenu.run();

        // Export data back to CSV files upon quitting
        patientContainer.exportPatientsToCSV(patientPath);
        ((StaffContainer)doctorContainer).initializeStaffCSV(staffPath);
        doctorContainer.appendStaffToCSVByStaffType(staffPath, "Doctor");
        pharmacistContainer.appendStaffToCSVByStaffType(staffPath, "Pharmacist");
        administratorContainer.appendStaffToCSVByStaffType(staffPath, "Administrator");
        medicineContainer.exportMedicineToCSV(MedicinePath);
        appointmentContainer.exportAppointmentToCSV(AppointmentPath);
        replenishmentRequestContainer.exportReplenishmentRequestToCSV(ReplenishmentRequestPath);
        appointmentOutcomeContainer.exportAppointmentOutcomeRecordToCSV(AppointmentOutcomeRecordPath);
        appointmentOutcomeContainer.exportPrescriptionsToCSV(prescriptions_list);
        doctorContainer.exportDoctorAdditionalsToCSV(DoctorAdditionals);
        patientContainer.exportMedicalRecordsToCSV(MedicalRecordPath);

    }
}