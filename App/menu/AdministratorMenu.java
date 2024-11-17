package App.menu;

import java.util.List;
import java.util.Scanner;

import App.system.data.AppointmentSystem;
import App.system.data.MedicineSystem;
import App.system.data.ReplenishmentRequestSystem;
import App.system.user.*;
import App.system.user.AdministratorSystem;
import App.models.Appointment;
import App.models.Medicine;
import App.models.StockReplenishmentRequest;
import App.user.Administrator;
import App.user.Doctor;
import App.user.Pharmacist;
import App.user.Staff;
import App.user.User;


/**
 * Menu class to manage various administrative operations in the Hospital Management System.
 * Allows the administrator to manage appointments, staff, medicines, replenishment requests, and alert levels.
 */
public class AdministratorMenu extends Menu {
    private String adminHospitalId;
    private Administrator administrator;
    private PatientSystem patientContainer;
    private DoctorSystem doctorContainer;
    private PharmacistSystem pharmacistContainer;
    private MedicineSystem medicineContainer;
    private ReplenishmentRequestSystem replenishmentRequestContainer;
    private AppointmentSystem appointmentContainer;
    private AdministratorSystem administratorContainer;

    /**
     * Constructs an AdministratorMenu with all the required containers for functionality.
     * 
     * @param hospitalId Administrator's hospital ID.
     * @param administratorContainer System for administrators.
     * @param patientContainer System for patients.
     * @param doctorContainer System for doctors.
     * @param pharmacistContainer System for pharmacists.
     * @param medicineContainer System for medicines.
     * @param replenishmentRequestContainer System for replenishment requests.
     * @param appointmentContainer System for appointments.
     */
    public AdministratorMenu(String hospitalId, AdministratorSystem administratorContainer, PatientSystem patientContainer, DoctorSystem doctorContainer, PharmacistSystem pharmacistContainer, MedicineSystem medicineContainer, ReplenishmentRequestSystem replenishmentRequestContainer, AppointmentSystem appointmentContainer) {
        this.adminHospitalId = hospitalId;
        administrator = (Administrator) administratorContainer.getUserByHospitalId(adminHospitalId);
        this.patientContainer = patientContainer;
        this.doctorContainer = doctorContainer;
        this.pharmacistContainer = pharmacistContainer;
        this.medicineContainer = medicineContainer;
        this.replenishmentRequestContainer = replenishmentRequestContainer;
        this.appointmentContainer = appointmentContainer;
        this.administratorContainer = administratorContainer;
    }
    

    /**
     * Runs the administrator menu and prompts the user for choices to manage the system.
     */
    @Override
    public void run() {
        Scanner sc=new Scanner(java.lang.System.in);
        int choice;
        do {
            java.lang.System.out.println("----------------------------------------");
            java.lang.System.out.println("Administrator Menu");
            java.lang.System.out.println("0. Log out");
            java.lang.System.out.println("1. View Appointment");
            java.lang.System.out.println("2. Manage Staff");
            java.lang.System.out.println("3. Manage Medicine");
            java.lang.System.out.println("4. Manage Replenishment Request");
            java.lang.System.out.println("5. Change Medicine Alert Level");
            
            java.lang.System.out.print("Enter your choice: ");
            choice=sc.nextInt();
            switch (choice) {
                case 0:
                    java.lang.System.out.println("Logging out..."); break;
                case 1:
                    viewAppointment(); break;
                case 2:
                    manageStaff(); break;
                case 3:
                    manageMedicine(); break;
                case 4:
                    manageReplenishmentRequest(); break;
                case 5:
                    changeAlertLevel(); break; // missed function
                default:
                    java.lang.System.out.println("Invalid choice");
            }
        } while (choice!=0);
    }

    /**
     * Changes the alert level for a specific medicine.
     * Displays the list of medicines and allows the administrator to update the alert threshold.
     */
    private void changeAlertLevel(){
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("----------------------------------------");
        java.lang.System.out.println("All Medicines:");
        java.lang.System.out.printf("%-5s %-20s %-15s %-15s %-15s%n", "No.", "Medicine Name", "Current Stock", "Alert Threshold", "Stock Level");
        java.lang.System.out.println("------------------------------------------------------------");
    
        List<Medicine> medicines = medicineContainer.getAllMedicines();
    
        if (medicines.isEmpty()) {
            java.lang.System.out.println("No medicines in the inventory.");
        } else {
            for (int i = 0; i < medicines.size(); i++) {
                Medicine medicine = medicines.get(i);
                String stockLevel = (medicine.getCurrentStock() <= medicine.getAlertThreshold()) ? "Low Stock" : "Normal";
                java.lang.System.out.printf("%-5d %-20s %-15d %-15d %-15s%n",
                    (i + 1),
                    medicine.getMedicineName(),
                    medicine.getCurrentStock(),
                    medicine.getAlertThreshold(),
                    stockLevel
                );
            }
    
            // Prompt the user to select a medicine to update
            java.lang.System.out.println("------------------------------------------------------------");
            java.lang.System.out.print("Enter the number of the medicine you want to update stock alert level (or 0 to go back): ");
            int choice;
            while (true) {
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine(); // Clear buffer
                    if (choice >= 0 && choice <= medicines.size()) {
                        break;
                    } else {
                        java.lang.System.out.println("Invalid choice. Please enter a number between 0 and " + medicines.size());
                    }
                } else {
                    java.lang.System.out.println("Invalid input. Please enter a number.");
                    sc.next(); // Clear invalid input
                }
            }
    
            // Go back if user chooses 0
            if (choice == 0) {
                java.lang.System.out.println("Returning to previous menu...");
                return;
            }
    
            // Get the selected medicine
            Medicine selectedMedicine = medicines.get(choice - 1);
            java.lang.System.out.println("You selected: " + selectedMedicine.getMedicineName());
            
            // Prompt for the new stock level
            java.lang.System.out.print("Enter the new stock alert level: ");
            int newAlertLevel;
            while (true) {
                if (sc.hasNextInt()) {
                    newAlertLevel = sc.nextInt();
                    sc.nextLine(); // Clear buffer
                    if (newAlertLevel >= 0) {
                        break;
                    } else {
                        java.lang.System.out.println("Please enter a valid positive integer for alert stock level.");
                    }
                } else {
                    java.lang.System.out.println("Invalid input. Please enter an integer.");
                    sc.next(); // Clear invalid input
                }
            }
    
            // Update the stock level
            selectedMedicine.setAlertThreshold(newAlertLevel);
            java.lang.System.out.println("Alert stock level for " + selectedMedicine.getMedicineName() + " updated to " + newAlertLevel + ".");
        }
    }

    /**
     * Manages the medicines in the system.
     * Allows the administrator to update medicine stock levels.
     */
    private void manageMedicine() {
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("----------------------------------------");
        java.lang.System.out.println("All Medicines:");
        java.lang.System.out.printf("%-5s %-20s %-15s %-15s %-15s%n", "No.", "Medicine Name", "Current Stock", "Alert Threshold", "Stock Level");
        java.lang.System.out.println("------------------------------------------------------------");
    
        List<Medicine> medicines = medicineContainer.getAllMedicines();
    
        if (medicines.isEmpty()) {
            java.lang.System.out.println("No medicines in the inventory.");
        } else {
            for (int i = 0; i < medicines.size(); i++) {
                Medicine medicine = medicines.get(i);
                String stockLevel = (medicine.getCurrentStock() <= medicine.getAlertThreshold()) ? "Low Stock" : "Normal";
                java.lang.System.out.printf("%-5d %-20s %-15d %-15d %-15s%n",
                    (i + 1),
                    medicine.getMedicineName(),
                    medicine.getCurrentStock(),
                    medicine.getAlertThreshold(),
                    stockLevel
                );
            }
    
            // Prompt the user to select a medicine to update
            java.lang.System.out.println("------------------------------------------------------------");
            java.lang.System.out.print("Enter the number of the medicine you want to update (or 0 to go back): ");
            int choice;
            while (true) {
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine(); // Clear buffer
                    if (choice >= 0 && choice <= medicines.size()) {
                        break;
                    } else {
                        java.lang.System.out.println("Invalid choice. Please enter a number between 0 and " + medicines.size());
                    }
                } else {
                    java.lang.System.out.println("Invalid input. Please enter a number.");
                    sc.next(); // Clear invalid input
                }
            }
    
            // Go back if user chooses 0
            if (choice == 0) {
                java.lang.System.out.println("Returning to previous menu...");
                return;
            }
    
            // Get the selected medicine
            Medicine selectedMedicine = medicines.get(choice - 1);
            java.lang.System.out.println("You selected: " + selectedMedicine.getMedicineName());
            
            // Prompt for the new stock level
            java.lang.System.out.print("Enter the new stock level: ");
            int newStock;
            while (true) {
                if (sc.hasNextInt()) {
                    newStock = sc.nextInt();
                    sc.nextLine(); // Clear buffer
                    if (newStock >= 0) {
                        break;
                    } else {
                        java.lang.System.out.println("Please enter a valid positive integer for the stock level.");
                    }
                } else {
                    java.lang.System.out.println("Invalid input. Please enter an integer.");
                    sc.next(); // Clear invalid input
                }
            }
    
            // Update the stock level
            selectedMedicine.setCurrentStock(newStock);
            java.lang.System.out.println("Stock level for " + selectedMedicine.getMedicineName() + " updated to " + newStock + ".");
        }
    }
    
    /**
     * Manages the replenishment requests in the system.
     * Allows the administrator to approve or reject requests for replenishing medicine stock.
     */
    private void manageReplenishmentRequest() {
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("----------------------------------------");
        java.lang.System.out.println("All Replenishment Requests:");
        java.lang.System.out.printf("%-5s %-20s %-15s %-15s%n", "No.", "Medicine Name", "Quantity", "Status");
        java.lang.System.out.println("------------------------------------------------------------");
    
        List<StockReplenishmentRequest> requests = replenishmentRequestContainer.getAllReplenishmentRequests();
    
        if (requests.isEmpty()) {
            java.lang.System.out.println("No replenishment requests found.");
        } else {
            for (int i = 0; i < requests.size(); i++) {
                StockReplenishmentRequest request = requests.get(i);
                java.lang.System.out.printf("%-5d %-20s %-15d %-15s%n",
                    (i + 1),
                    request.getMedicineName(),
                    request.getQuantity(),
                    request.getStatus()
                );
            }
    
            // Prompt the user to select a request
            java.lang.System.out.println("------------------------------------------------------------");
            java.lang.System.out.print("Enter the index of the request you want to approve/reject (or 0 to go back): ");
            int choice;
            while (true) {
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine(); // Clear buffer
                    if (choice >= 0 && choice <= requests.size()) {
                        break;
                    } else {
                        java.lang.System.out.println("Invalid choice. Please enter a number between 0 and " + requests.size());
                    }
                } else {
                    java.lang.System.out.println("Invalid input. Please enter a number.");
                    sc.next(); // Clear invalid input
                }
            }
    
            // Go back if user chooses 0
            if (choice == 0) {
                java.lang.System.out.println("Returning to previous menu...");
                return;
            }
    
            // Get the selected request
            StockReplenishmentRequest selectedRequest = requests.get(choice - 1);
            java.lang.System.out.println("You selected the request for: " + selectedRequest.getMedicineName());
    
            // Prompt to approve or reject the request
            java.lang.System.out.println("----------------------------------------");
            java.lang.System.out.println("Select an action:");
            java.lang.System.out.println("1. Approve and add medicine");
            java.lang.System.out.println("2. Reject and delete request");
            java.lang.System.out.print("Enter your choice: ");
            
            int action;
            while (true) {
                if (sc.hasNextInt()) {
                    action = sc.nextInt();
                    sc.nextLine(); // Clear buffer
                    if (action == 1 || action == 2) {
                        break;
                    } else {
                        java.lang.System.out.println("Invalid choice. Please enter 1 to approve or 2 to reject.");
                    }
                } else {
                    java.lang.System.out.println("Invalid input. Please enter a number.");
                    sc.next(); // Clear invalid input
                }
            }
    
            if (action == 1) {
                // Approve: update status and inventory
                selectedRequest.setStatus("approved");
                Medicine medicine = medicineContainer.getMedicineByName(selectedRequest.getMedicineName());
                if (medicine != null) {
                    int newStock = medicine.getCurrentStock() + selectedRequest.getQuantity();
                    medicine.setCurrentStock(newStock);
                    java.lang.System.out.println("Request approved. Inventory updated for " + selectedRequest.getMedicineName() + ".");
                } else {
                    java.lang.System.out.println("Error: Medicine not found in inventory.");
                }
            } else if (action == 2) {
                // Reject: remove the request
                replenishmentRequestContainer.removeReplenishmentRequest(selectedRequest.getRecordId());
                java.lang.System.out.println("Request rejected and removed.");
            }
        }
    }
    

    /**
     * Displays all the appointments in the system.
     * Provides an overview of appointments including their status and the associated doctor and patient.
     */
    public void viewAppointment() {
        java.lang.System.out.println("----------------------------------------");
        java.lang.System.out.println("All Appointments:");
        java.lang.System.out.printf("%-5s %-15s %-15s %-15s %-15s%n", "No.", "Date & Time", "Patient ID", "Doctor ID", "Status");
        java.lang.System.out.println("------------------------------------------------------------");

        List<Appointment> appointments = appointmentContainer.getAllAppointments();

        if (appointments.isEmpty()) {
            java.lang.System.out.println("No appointments found.");
        } else {
            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);
                java.lang.System.out.printf("%-5d %-15s %-15s %-15s %-15s%n",
                    (i + 1),
                    appointment.getTime(),
                    appointment.getpatientHospitalId(),
                    appointment.getdoctorHospitalId(),
                    appointment.getStatus()
                );
            }
        }
    }

    /**
     * Manages the staff in the hospital.
     * Provides options to add, edit, or delete staff members.
     */
    private void manageStaff() {
        Scanner sc = new Scanner(java.lang.System.in);
        int choice;
        
        do {
            java.lang.System.out.println("----------------------------------------");
            java.lang.System.out.println("Manage Staff");
            java.lang.System.out.println("0. Back");
            java.lang.System.out.println("1. View All Staff");
            java.lang.System.out.println("2. Add Staff");
            java.lang.System.out.println("3. Edit Staff");
            java.lang.System.out.println("4. Delete Staff");
            java.lang.System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 0:
                    java.lang.System.out.println("Returning to previous menu..."); break;
                case 1:
                    printAllStaff(); break;
                case 2:
                    addStaff(); break;
                case 3:
                    editStaff(); break;
                case 4:
                    deleteStaff(); break;
                default:
                    java.lang.System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 0);
    }

    /**
     * Adds a new staff member to the system.
     * Prompts the administrator for staff details including role, ID, name, and other details.
     */
    private void addStaff() {
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("Add Staff");
        
        // Collect staff details
        java.lang.System.out.print("Enter staff type (Doctor/Pharmacist/Administrator): ");
        String userType = sc.nextLine().trim();
        if (!userType.equalsIgnoreCase("Doctor") && !userType.equalsIgnoreCase("Pharmacist") && !userType.equalsIgnoreCase("Administrator")) {
            java.lang.System.out.println("Invalid staff type entered.");
        }
        
        java.lang.System.out.print("Enter Hospital ID: ");
        String hospitalId = sc.nextLine().trim();
        java.lang.System.out.print("Enter Password: ");
        String password = sc.nextLine().trim();
        java.lang.System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        java.lang.System.out.print("Enter Gender (Male/Female): ");
        String gender = sc.nextLine().trim();
        java.lang.System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();
        java.lang.System.out.print("Enter Age: ");
        int age = sc.nextInt();
        sc.nextLine(); // Clear buffer after reading integer input

        // Add staff to the appropriate container based on type
        ((StaffSystem) getContainerByType(userType)).addStaff(hospitalId, password, name, gender, userType, email, age);

        java.lang.System.out.println("Staff added successfully.");
    }

    // Helper function to get the correct container based on user type
    private StaffSystem getContainerByType(String userType) {
        switch (userType.toLowerCase()) {
            case "doctor":
                return doctorContainer;
            case "pharmacist":
                return pharmacistContainer;
            case "administrator":
                return administratorContainer;
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }

    /**
     * Edits the details of an existing staff member.
     * Prompts the administrator to enter new values for specific fields.
     */
    private void editStaff() {
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("Edit Staff");

        // Ask for the staff type
        java.lang.System.out.print("Enter staff type to edit (Doctor/Pharmacist/Administrator): ");
        String userType = sc.nextLine().trim();
        Staff staff = null;

        if (userType.equalsIgnoreCase("Doctor")) {
            java.lang.System.out.print("Enter Doctor's Hospital ID: ");
            String hospitalId = sc.nextLine().trim();
            staff = doctorContainer.getDoctorByHospitalId(hospitalId);

        } else if (userType.equalsIgnoreCase("Pharmacist")) {
            java.lang.System.out.print("Enter Pharmacist's Hospital ID: ");
            String hospitalId = sc.nextLine().trim();
            staff = pharmacistContainer.getPharmacistByHospitalId(hospitalId);

        } else if (userType.equalsIgnoreCase("Administrator")) {
            java.lang.System.out.print("Enter Administrator's Hospital ID: ");
            String hospitalId = sc.nextLine().trim();
            staff = administratorContainer.getAdministratorByHospitalId(hospitalId);

        } else {
            java.lang.System.out.println("Invalid staff type entered.");
            return;
        }

        // Check if staff was found
        if (staff == null) {
            java.lang.System.out.println("No staff found with the provided Hospital ID.");
            return;
        }

        // Display current details and prompt for updates
        java.lang.System.out.println("Editing details for " + userType + " ID: " + staff.getHospitalId());
        java.lang.System.out.println("Current Name: " + staff.getName());
        java.lang.System.out.print("Enter new Name (or press Enter to keep current): ");
        String newName = sc.nextLine().trim();
        if (!newName.isEmpty()) {
            staff.setName(newName);
        }

        java.lang.System.out.println("Current Gender: " + staff.getGender());
        java.lang.System.out.print("Enter new Gender (or press Enter to keep current): ");
        String newGender = sc.nextLine().trim();
        if (!newGender.isEmpty()) {
            staff.setGender(newGender);
        }

        java.lang.System.out.println("Current Email: " + staff.getEmail());
        java.lang.System.out.print("Enter new Email (or press Enter to keep current): ");
        String newEmail = sc.nextLine().trim();
        if (!newEmail.isEmpty()) {
            staff.setEmail(newEmail);
        }

        java.lang.System.out.println("Current Age: " + staff.getAge());
        java.lang.System.out.print("Enter new Age (or press Enter to keep current): ");
        String ageInput = sc.nextLine().trim();
        if (!ageInput.isEmpty()) {
            try {
                int newAge = Integer.parseInt(ageInput);
                staff.setAge(newAge);
            } catch (NumberFormatException e) {
                java.lang.System.out.println("Invalid age entered. Age not updated.");
            }
        }

        java.lang.System.out.println(userType + " details updated successfully.");
    }


    /**
     * Deletes a staff member from the system.
     * Removes the staff member identified by their hospital ID.
     */
    private void deleteStaff() {
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("Delete Staff");
    
        // Ask for the staff type
        java.lang.System.out.print("Enter staff type to delete (Doctor/Pharmacist/Administrator): ");
        String userType = sc.nextLine().trim();
        boolean success = false;
    
        if (userType.equalsIgnoreCase("Doctor")) {
            java.lang.System.out.print("Enter Doctor's Hospital ID: ");
            String hospitalId = sc.nextLine().trim();
            Doctor doctor = doctorContainer.getDoctorByHospitalId(hospitalId);
            
            if (doctor != null) {
                doctorContainer.removeUser(hospitalId);
                success = true;
                java.lang.System.out.println("Doctor with Hospital ID " + hospitalId + " has been deleted.");
            } else {
                java.lang.System.out.println("No Doctor found with the provided Hospital ID.");
            }
    
        } else if (userType.equalsIgnoreCase("Pharmacist")) {
            java.lang.System.out.print("Enter Pharmacist's Hospital ID: ");
            String hospitalId = sc.nextLine().trim();
            Pharmacist pharmacist = pharmacistContainer.getPharmacistByHospitalId(hospitalId);
            
            if (pharmacist != null) {
                pharmacistContainer.removeUser(hospitalId);
                success = true;
                java.lang.System.out.println("Pharmacist with Hospital ID " + hospitalId + " has been deleted.");
            } else {
                java.lang.System.out.println("No Pharmacist found with the provided Hospital ID.");
            }
    
        } else if (userType.equalsIgnoreCase("Administrator")) {
            java.lang.System.out.print("Enter Administrator's Hospital ID: ");
            String hospitalId = sc.nextLine().trim();
            Administrator administrator = administratorContainer.getAdministratorByHospitalId(hospitalId);
            
            if (administrator != null) {
                administratorContainer.removeUser(hospitalId);
                success = true;
                java.lang.System.out.println("Administrator with Hospital ID " + hospitalId + " has been deleted.");
            } else {
                java.lang.System.out.println("No Administrator found with the provided Hospital ID.");
            }
    
        } else {
            java.lang.System.out.println("Invalid staff type entered.");
            return;
        }
    
        if (!success) {
            java.lang.System.out.println("Deletion failed. Please check the Hospital ID and try again.");
        }
    }    

    /**
     * Displays all the staff in the system.
     * Provides options to filter staff by gender, age, or role.
     */
    private void printAllStaff() {
        Scanner sc = new Scanner(java.lang.System.in);
        int choice;
        do {
            java.lang.System.out.println("----------------------------------------");
            java.lang.System.out.println("View Staff");
            java.lang.System.out.println("0. Back");
            java.lang.System.out.println("1. Filtered by gender");
            java.lang.System.out.println("2. Filtered by age");
            java.lang.System.out.println("3. Filtered by role");
            java.lang.System.out.println("4. No Filter");
            java.lang.System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 0:
                    java.lang.System.out.println("Returning to previous menu..."); break;
                case 1:
                    genderFilteredView(); break;
                case 2:
                    ageFilteredView(); break;
                case 3:
                    roleFilterView(); break;
                case 4:
                    noFilterView();break;
                default:
                    java.lang.System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 0);
    }


    /**
     * Displays staff filtered by gender.
     * Allows the administrator to view only male or female staff.
     */
    public void genderFilteredView() {
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("Select Gender to View:");
        java.lang.System.out.println("1. Male");
        java.lang.System.out.println("2. Female");
        java.lang.System.out.print("Enter the number corresponding to the gender: ");
        
        int choice = sc.nextInt();
        sc.nextLine(); 
        String gender;
        switch (choice) {
            case 1:
                gender = "Male";
                break;
            case 2:
                gender = "Female";
                break;
            default:
                java.lang.System.out.println("Invalid choice. Returning to previous menu.");
                return;
        }
        java.lang.System.out.println("Staff - Gender: " + gender);
        java.lang.System.out.printf("%-5s %-15s %-20s %-15s %-5s%n", "No.", "Hospital ID", "Name", "Role", "Age");
        java.lang.System.out.println("-----------------------------------------------------------------------");
        int counter = 1;
    
        List<User> doctors = doctorContainer.getAllDoctors().values().stream().filter(user -> user.getGender().equalsIgnoreCase(gender)).toList();
        for (User user : doctors) {
            Doctor doctor = (Doctor) user;
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-5d%n", counter++, doctor.getHospitalId(), doctor.getName(), "Doctor", doctor.getAge());
        }
    
        List<User> pharmacists = pharmacistContainer.getAllPharmacists().values().stream().filter(user -> user.getGender().equalsIgnoreCase(gender)).toList();
        for (User user : pharmacists) {
            Pharmacist pharmacist = (Pharmacist) user;
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-5d%n", counter++, pharmacist.getHospitalId(), pharmacist.getName(), "Pharmacist", pharmacist.getAge());
        }
    
        List<User> admins = administratorContainer.getAllAdministrators().values().stream().filter(user -> user.getGender().equalsIgnoreCase(gender)).toList();
        for (User user : admins) {
            Administrator admin = (Administrator) user;
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-5d%n", counter++, admin.getHospitalId(), admin.getName(), "Administrator", admin.getAge());
        }
    
        if (counter == 1) {
            java.lang.System.out.println("No staff members found with the gender: " + gender);
        }
    }
    

    /**
     * Displays staff filtered by age range.
     * Allows the administrator to specify a lower and upper limit for age.
     */
    public void ageFilteredView() {
        Scanner sc = new Scanner(java.lang.System.in);
        
        java.lang.System.out.println("Enter Age Range:");
        java.lang.System.out.print("Enter lower age limit: ");
        int lowerLimit = sc.nextInt();
        
        java.lang.System.out.print("Enter upper age limit: ");
        int upperLimit = sc.nextInt();
        sc.nextLine();
    
        if (lowerLimit > upperLimit) {
            java.lang.System.out.println("Invalid range. The lower limit should be less than or equal to the upper limit.");
            return;
        }
    
        java.lang.System.out.println("Staff - Age Range: " + lowerLimit + " to " + upperLimit);
        java.lang.System.out.printf("%-5s %-15s %-20s %-15s %-10s%n", "No.", "Hospital ID", "Name", "Role", "Age");
        java.lang.System.out.println("-----------------------------------------------------------------------");
    
        int counter = 1;
    
        List<Doctor> doctors = doctorContainer.getAllDoctors().values().stream()
            .filter(user -> {
                Doctor doctor = (Doctor) user;
                return doctor.getAge() >= lowerLimit && doctor.getAge() <= upperLimit;
            })
            .map(user -> (Doctor) user)
            .toList();
        
        for (Doctor doctor : doctors) {
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10d%n", counter++, doctor.getHospitalId(), doctor.getName(), "Doctor", doctor.getAge());
        }
    
        List<Pharmacist> pharmacists = pharmacistContainer.getAllPharmacists().values().stream()
            .filter(user -> {
                Pharmacist pharmacist = (Pharmacist) user;
                return pharmacist.getAge() >= lowerLimit && pharmacist.getAge() <= upperLimit;
            })
            .map(user -> (Pharmacist) user)
            .toList();
        
        for (Pharmacist pharmacist : pharmacists) {
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10d%n", counter++, pharmacist.getHospitalId(), pharmacist.getName(), "Pharmacist", pharmacist.getAge());
        }
    
        List<Administrator> admins = administratorContainer.getAllAdministrators().values().stream()
            .filter(user -> {
                Administrator admin = (Administrator) user;
                return admin.getAge() >= lowerLimit && admin.getAge() <= upperLimit;
            })
            .map(user -> (Administrator) user)
            .toList();
        
        for (Administrator admin : admins) {
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10d%n", counter++, admin.getHospitalId(), admin.getName(), "Administrator", admin.getAge());
        }
    
        if (counter == 1) {
            java.lang.System.out.println("No staff members found within the age range: " + lowerLimit + " to " + upperLimit);
        }
    }
    
    

    /**
     * Displays staff filtered by role.
     * Allows the administrator to view staff by specific roles such as Doctor, Pharmacist, or Administrator.
     */
    public void roleFilterView(){
        Scanner sc = new Scanner(java.lang.System.in);
        java.lang.System.out.println("Select Role to View:");
        java.lang.System.out.println("1. Doctor");
        java.lang.System.out.println("2. Pharmacist");
        java.lang.System.out.println("3. Administrator");
        java.lang.System.out.print("Enter the number corresponding to the role: ");
        
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                java.lang.System.out.printf("%-5s %-15s %-20s %-15s %-10s %-5s%n", "No.", "Hospital ID", "Name", "Role", "Gender", "Age");
                java.lang.System.out.println("-----------------------------------------------------------------------");
                List<User> doctors = doctorContainer.getAllDoctors().values().stream().toList();
                for (int i = 0; i < doctors.size(); i++) {
                    Doctor doctor = (Doctor) doctors.get(i);
                    java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10s %-5d%n", (i + 1), doctor.getHospitalId(), doctor.getName(), "Doctor", doctor.getGender(), doctor.getAge());
                }
                break;
            case 2:
                java.lang.System.out.printf("%-5s %-15s %-20s %-15s %-10s %-5s%n", "No.", "Hospital ID", "Name", "Role", "Gender", "Age");
                java.lang.System.out.println("-----------------------------------------------------------------------");
                List<User> pharmacists = pharmacistContainer.getAllPharmacists().values().stream().toList();
                for (int i = 0; i < pharmacists.size(); i++) {
                    Pharmacist pharmacist = (Pharmacist) pharmacists.get(i);
                    java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10s %-5d%n", (i + 1), pharmacist.getHospitalId(), pharmacist.getName(), "Pharmacist", pharmacist.getGender(), pharmacist.getAge());
                }
                break;
            case 3:
                java.lang.System.out.printf("%-5s %-15s %-20s %-15s %-10s %-5s%n", "No.", "Hospital ID", "Name", "Role", "Gender", "Age");
                java.lang.System.out.println("-----------------------------------------------------------------------");
                List<User> admins = administratorContainer.getAllAdministrators().values().stream().toList();
                for (int i = 0; i < admins.size(); i++) {
                    Administrator admin = (Administrator) admins.get(i);
                    java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10s %-5d%n", (i + 1), admin.getHospitalId(), admin.getName(), "Administrator", admin.getGender(), admin.getAge());
                }
                break;
            default:
                java.lang.System.out.println("Invalid choice. Returning to previous menu.");
                return;
        }
    }

    /**
     * Displays all staff members without any filters.
     */
    public void noFilterView(){
        java.lang.System.out.println("All Staff:");
        java.lang.System.out.printf("%-5s %-15s %-20s %-15s %-10s %-5s%n", "No.", "Hospital ID", "Name", "Role", "Gender", "Age");
        java.lang.System.out.println("-----------------------------------------------------------------------");
        // Display Doctors
        List<User> doctors = doctorContainer.getAllDoctors().values().stream().toList();
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = (Doctor) doctors.get(i);
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10s %-5d%n", (i + 1), doctor.getHospitalId(), doctor.getName(), "Doctor", doctor.getGender(), doctor.getAge());
        }
        // Display Pharmacists
        List<User> pharmacists = pharmacistContainer.getAllPharmacists().values().stream().toList();
        for (int i = 0; i < pharmacists.size(); i++) {
            Pharmacist pharmacist = (Pharmacist) pharmacists.get(i);
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10s %-5d%n", (i + 1), pharmacist.getHospitalId(), pharmacist.getName(), "Pharmacist", pharmacist.getGender(), pharmacist.getAge());
        }
        // Display Administrators
        List<User> admins = administratorContainer.getAllAdministrators().values().stream().toList();
        for (int i = 0; i < admins.size(); i++) {
            Administrator admin = (Administrator) admins.get(i);
            java.lang.System.out.printf("%-5d %-15s %-20s %-15s %-10s %-5d%n", (i + 1), admin.getHospitalId(), admin.getName(), "Administrator", admin.getGender(), admin.getAge());
        }
    }
}