package App.menu;

/**
 * Abstract class representing a menu in the system.
 * <p>
 * Each subclass must implement the run method to provide the menu functionality.
 */
public abstract class Menu {
    /**
     * Abstract method to start the menu.
     * Subclasses should implement this method to define the menu options and behavior.
     */
    public abstract void run();
}
