package App.models;

/**
 * Represents an abstract record with a unique identifier.
 * 
 * This class serves as a base for all records within the system,
 * providing a globally unique identifier (ID) for each instance.
 */
public abstract class BaseModel {
    private static int globalRecordCounter = 0; // Shared counter for all records
    private final int recordId;

    /**
     * Constructs a new BaseModel instance with a unique record ID.
     * The record ID is generated automatically and incremented globally.
     */
    public BaseModel() {
        this.recordId = globalRecordCounter++; // Increment counter for each new instance
    }

    /**
     * Gets the unique record ID for this BaseModel.
     * 
     * @return The unique identifier for this record.
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * Returns a string representation of the record.
     * This implementation returns the record ID as a string.
     * 
     * @return The string representation of the record ID.
     */
    @Override
    public String toString() {
        return "" + recordId;
    }
}
