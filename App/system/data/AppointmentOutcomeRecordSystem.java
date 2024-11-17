package App.system.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import App.models.Appointment;
import App.models.AppointmentOutcome;
import App.models.Prescription;
import App.models.BaseModel;


/**
 * System class for managing appointment outcome records.
 * Provides functionalities for loading, adding, and exporting appointment outcomes.
 */
public class AppointmentOutcomeRecordSystem extends RecordSystem {

    /**
     * Constructs an AppointmentOutcomeRecordSystem and loads records from the given file.
     *
     * @param filePath Path to the CSV file containing appointment outcome records.
     */
    public AppointmentOutcomeRecordSystem(String filePath) {
        super();
        loadAppointmentOutcomeRecords(filePath);
    }

    /**
     * Loads appointment outcome records from a CSV file into the container.
     *
     * @param filePath Path to the CSV file.
     */
    private void loadAppointmentOutcomeRecords(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header line
                }
                String[] fields = line.split(",");
                String appointmentRecordId = fields[0].trim();
                String patientHospitalId = fields[1].trim();
                String doctorHospitalId = fields[2].trim();
                String serviceType = fields[3].trim();
                String consultationNotes = fields[4].trim();

                // Create AppointmentOutcome object and load prescriptions within the class
                AppointmentOutcome record = new AppointmentOutcome(
                        appointmentRecordId,
                        patientHospitalId,
                        doctorHospitalId,
                        serviceType,
                        consultationNotes
                );

                // The record itself will load prescriptions from CSV if needed
                super.putRecord(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an appointment outcome record for a given appointment.
     *
     * @param appointment       The Appointment object for which the outcome is to be recorded.
     * @param serviceType       The type of service provided during the appointment.
     * @param consultationNotes The consultation notes.
     */
    public void addAppointmentOutcomeRecord(Appointment appointment, String serviceType, String consultationNotes) {
        // Extract details from the Appointment object
        String appointmentRecordId = appointment.getAppointmentIdentifyId();
        String patientHospitalId = appointment.getpatientHospitalId();
        String doctorHospitalId = appointment.getdoctorHospitalId();

        // Create a new AppointmentOutcome with the extracted information
        AppointmentOutcome appointmentOutcomeRecord = new AppointmentOutcome(
                appointmentRecordId,
                patientHospitalId,
                doctorHospitalId,
                serviceType,
                consultationNotes
        );

        // Add the AppointmentOutcome to the container
        super.putRecord(appointmentOutcomeRecord);
    }

    /**
     * Retrieves an appointment outcome record by its appointment record ID.
     *
     * @param appointmentRecordId The appointment record ID.
     * @return The AppointmentOutcome object if found, otherwise null.
     */
    public AppointmentOutcome getAppointmentOutcomeRecordById(String appointmentRecordId) {
        // Iterate through all records in the container
        for (BaseModel baseModel : getRecords().values()) {
            if (baseModel instanceof AppointmentOutcome) {
                AppointmentOutcome outcomeRecord = (AppointmentOutcome) baseModel;
                if (outcomeRecord.getAppointmentRecordId().equals(appointmentRecordId)) {
                    return outcomeRecord; // Return the matching baseModel
                }
            }
        }
        return null; // Return null if no matching record is found
    }

    /**
     * Retrieves all appointment outcome records for a specific patient by their ID.
     *
     * @param patientHospitalId The hospital ID of the patient.
     * @return A list of AppointmentOutcome objects.
     */
    public List<AppointmentOutcome> getAppointmentOutcomeRecordsByPatientId(String patientHospitalId) {
        List<AppointmentOutcome> outcomeRecords = new ArrayList<>();

        // Iterate through all records in the container
        for (BaseModel baseModel : getRecords().values()) {
            if (baseModel instanceof AppointmentOutcome) {
                AppointmentOutcome outcomeRecord = (AppointmentOutcome) baseModel;
                if (outcomeRecord.getPatientHospitalId().equals(patientHospitalId)) {
                    outcomeRecords.add(outcomeRecord); // Add matching baseModel to the list
                }
            }
        }
        return outcomeRecords; // Return the list of matching records (empty if none found)
    }

    /**
     * Retrieves all appointment outcome records in the container.
     *
     * @return A list of all AppointmentOutcome objects.
     */
    public List<AppointmentOutcome> getAllAppointmentOutcomeRecords() {
        List<AppointmentOutcome> outcomeRecords = new ArrayList<>();

        // Iterate through all records in the container
        for (BaseModel baseModel : getRecords().values()) {
            if (baseModel instanceof AppointmentOutcome) {
                AppointmentOutcome outcomeRecord = (AppointmentOutcome) baseModel;
                outcomeRecords.add(outcomeRecord); // Add baseModel to the list
            }
        }
        return outcomeRecords; // Return the list of records
    }

    /**
     * Exports all appointment outcome records to a CSV file.
     *
     * @param filePath The path of the CSV file to write to.
     */
    public void exportAppointmentOutcomeRecordToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.write("AppointmentRecordId,PatientHospitalId,DoctorHospitalId,ServiceType,ConsultationNotes\n");

            // Write each AppointmentOutcome's details
            for (AppointmentOutcome record : getAllAppointmentOutcomeRecords()) {
                writer.write(String.format("%s,%s,%s,%s,%s\n",
                        record.getAppointmentRecordId(),
                        record.getPatientHospitalId(),
                        record.getDoctorHospitalId(),
                        record.getServiceType(),
                        record.getConsultationNotes()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exports prescriptions from appointment outcome records to a CSV file.
     *
     * @param prescriptions_list The path of the CSV file to write prescriptions to.
     */
    public void exportPrescriptionsToCSV(String prescriptions_list) {
        try (FileWriter writer = new FileWriter(prescriptions_list)) {
            // Write header
            writer.write("AppointmentRecordId,Medicine,Amount,Status\n");

            // Write each prescription for each AppointmentOutcome
            for (AppointmentOutcome record : getAllAppointmentOutcomeRecords()) {
                String appointmentRecordId = record.getAppointmentRecordId();

                for (Prescription prescription : record.getPrescriptions()) {
                    writer.write(String.format("%s,%s,%d,%s\n",
                            appointmentRecordId,
                            prescription.getMedicine(),
                            prescription.getAmount(),
                            prescription.getStatus()
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
