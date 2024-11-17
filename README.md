# Hospital Management System (HMS)

This repository contains the source code and documentation for the Hospital Management System (HMS). It includes the Java source code, Javadocs, UML class diagram, data files, and project report.

## Table of Contents
1. [Our team](#our-team)
2. [App Source Code](#app-source-code)
3. [Javadocs](#javadocs)
4. [UML Class Diagram](#uml-class-diagram)
5. [Report](#report)

---

## Our team
Our team is Group 2 from Lab Group SCS7 that comprises of:
 - Hui Cheok Shun, Jordan
 - Jerwin Lee Chu Hao
 - Nguyen Pham Minh Quan
 - See Tow Tze Jiet
 - Dallas Ng Zhi Hao

## App Source Code

The `App` folder contains all the source code for the Hospital Management System (HMS) written in Java. This includes classes for managing users (e.g., doctors, patients), appointments, and other essential hospital operations.

### App general Structure:
The project follows a modular structure to separate concerns:

- `Main.java`: Entry point of the application.
- `menu` folder: Contains classes to manage user menus for each role (e.g., DoctorMenu, PatientMenu).
- `user` folder: Classes representing different user roles like Doctor, Patient, and Administrator.
- `record` folder: Handles medical records, appointment records, and prescriptions.
- `container` folder: Manages data containers for users and records (e.g., PatientContainer, AppointmentContainer).
- `data` folder: Contains all the CSV data files required for the project. These files are loaded at runtime to simulate real hospital data, and any updates to the system are saved back into the CSV files upon exit.

### Instructions to Compile and Run the App
1. **Download all the folders including `App` and `data` folder**:
   Download all the relevant folders from the current github repository and place them in a single folder.

2. **Navigate to the outer folder**:
   Open a terminal (or command prompt) and change the directory to where the `App` folder is located.
   For example, if the `App` and `data` folders are placed in a folder named `Project`, navigate to the `Project` folder
   
   ```bash
   cd path/to/Project
   ```

3. **Compile the source code**:
   Run the following command to compile all the `.java` files in the `App` folder:
   
   ```bash
   javac -d App/*.java
   ```

4. **Run the application**:
   After compilation, you can run the Main class with the following command:

   ```bash
   java App/Main
   ```

This will start the Hospital Management System (HMS) via the Command Line Interface (CLI).

---

## Javadocs

The `docs` folder contains the generated documentation for the Java code. It provides an organized view of the classes, methods, and their relationships within the HMS.

### Overview
The Javadocs provide:
- Descriptions of each class and its purpose.
- Details of methods and their parameters.
- Information about data structures used throughout the application.

To view the Javadocs, open the `index.html` file in the `docs` folder with any browser and navigate using the links or search bar.

---

## UML Class Diagram

The UML class diagram illustrates the structure of the Hospital Management System, showing the relationships between different classes implemented.
![UML Class Diagramt](UMLClassDiagram.png)

### Overview
- **Class relationships**: Shows inheritance, associations, and dependencies.
- **Class attributes**: Displays key properties of each class.
- **Methods**: Shows major methods and their interactions.

---

## Report

The `report` folder contains the final project report, which outlines the OOP concepts and principles applications and design decisions made during the implementation of the Hospital Management System.

### Overview
- **Project Scope**: Describes the functionality and features of the system.
- **Design Decisions**: Explains key design patterns and principles applied, limitations of the system, and highlight the additional features added and potential future improvements.
- **Reflection**: Reflect on the journey in building the project, including challenges faced and lessons learnt.

You can read our final report here: [Final Report](Report.pdf)
