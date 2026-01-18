package CourseWork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/* Main algorithm outline:
 1. Load car data from a file into an array.
 2. Display a menu of options for the user to choose from.
 3. Based on the user's choice:
    a) Rent a car by taking inputs on car type, rental price, and features.
    b) Cancel a rental by entering the car number.
    c) View all car rentals.
    d) Exit and save all changes back to the file.
 Car class represents a car in the rental system */
class Carmain {
    private int carNum;            // Car number (unique identifier)
    private String carType;        // Type of car (e.g., sedan, SUV, etc.)
    private double rentalPrice;    // Rental price per day
    private String hasGPS;         // GPS availability (stored as "yes" or "no")
    private String hasSunroof;     // Sunroof availability (stored as "yes" or "no")
    private String email;          // Email of the renter, "free" if the car is available

    // Constructor to initialize all car attributes
    public Carmain(int carNum, String carType, double rentalPrice, String hasGPS, String hasSunroof, String email) {
        this.carNum = carNum;              // Assign car number
        this.carType = carType;            // Assign car type
        this.rentalPrice = rentalPrice;    // Assign rental price
        this.hasGPS = hasGPS;              // Assign GPS availability
        this.hasSunroof = hasSunroof;      // Assign sunroof availability
        this.email = email;                // Assign renter's email or "free" if available
    }

    // Getter for car number
    public int getCarNum() {
        return carNum;
    }

    // Getter for car type
    public String getCarType() {
        return carType;
    }

    // Getter for rental price
    public double getRentalPrice() {
        return rentalPrice;
    }

    // Getter for GPS availability
    public String hasGPS() {
        return hasGPS;
    }

    // Getter for sunroof availability
    public String hasSunroof() {
        return hasSunroof;
    }

    // Getter for renter's email
    public String getEmail() {
        return email;
    }

    // Setter to update renter's email or mark the car as "free"
    public void setEmail(String email) {
        this.email = email;
    }

    // Checks if the car is currently available for rent
    public boolean isAvailable() {
        return email.equalsIgnoreCase("free"); // Car is available if the email is "free"
    }

    // Returns a string representation of the car details
    public String toString() {
        return carNum + " " + carType + " " + rentalPrice + " " + hasGPS + " " + hasSunroof + " " + email;
    }
}

public class CarRental {
    private static Carmain[] cars;                          // Array to store all car objects
    private static final String txtFile = "C:\\Users\\DELL\\eclipse-workspace\\Final\\src\\cars[1].txt"; // Path to the car data file

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);      // Scanner for reading user input

        listCars();                                    // Load car data from the file into the array

        char choice = 0;                               // Variable to store the user's menu choice
        do {
            // Display the main menu to the user
            System.out.println("\n- - Car Rental System - -");
            System.out.println("1 - Rent Car");
            System.out.println("2 - Cancel Rental");
            System.out.println("3 - View Car Rentals");
            System.out.println("Q - Quit");
            System.out.print("Pick: ");
            choice = scanner.next().toUpperCase().charAt(0); // Read user's choice and convert to uppercase

            switch (choice) {
                case '1': // Rent a car
                    rentCar(scanner);
                    break;
                case '2': // Cancel a rental
                    cancelRental(scanner);
                    break;
                case '3': // View car details
                    viewFile();
                    break;
                case 'Q': // Quit the system
                    storeCars(); // Save data to file before quitting
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default: // Handle invalid input
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        while (choice != 'Q'); // Loop until the user chooses to quit
        scanner.close(); // Close the scanner to free resources
    }

    // Save all car data back to the file
    private static void storeCars() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(txtFile))) // Open file for writing
        { 
            for (Carmain car : cars)
            {
                writer.println(car); // Write each car's details to the file
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error: Unable to save cars to file."); // Handle file writing errors
        }
    }

    // Display details of all cars
    private static void viewFile() {
        System.out.println("\nCurrent Car Rentals:"); // Header for the output
        for (Carmain car : cars) {
            System.out.println(car); // Print details of each car
        }
    }

    // Load car data from a file and store it in the cars array
    private static void listCars() {
        List<Carmain> carList = new ArrayList<>(); // Temporary list to hold car objects
        try (Scanner fileScanner = new Scanner(new File(txtFile))) // Open file for reading
        { 
            while (fileScanner.hasNextLine()) // Read file line by line
            { 
                String line = fileScanner.nextLine();
                try (Scanner lineScanner = new Scanner(line)) // Parse the line
                { 
                    lineScanner.useDelimiter(" "); // Use space as the delimiter
                    int carNum = lineScanner.nextInt();
                    String carType = lineScanner.next();
                    double rentalPrice = lineScanner.nextDouble();
                    String hasGPS = lineScanner.next();
                    String hasSunroof = lineScanner.next();
                    String email = lineScanner.next();
                    carList.add(new Carmain(carNum, carType, rentalPrice, hasGPS, hasSunroof, email)); // Add car to the list
                } 
                catch (Exception e)
                {
                    System.out.println("Skipping invalid line: " + line); // Handle invalid lines in the file
                }
            }
        }
        catch (FileNotFoundException e) 
        {
            System.out.println("Error: File not found."); // Handle missing file
        }
        cars = carList.toArray(new Carmain[0]); // Convert the list to an array
    }

    // Rent a car based on user preferences
    private static void rentCar(Scanner scanner) {
        String carType = "";
        double maxPrice = 0;
        String needGPS = "";
        String needSunroof = "";
        String email = ""; // Email as a string

        // Input validation with retry attempts
        for (int i = 0; i < 3; i++) {
            try {
                // Loop until the user enters a valid car type
                while (true) {
                    System.out.print("Enter car type (PickOne: Economy, Compact, Midsize, Fullsize, SUV): ");
                    carType = scanner.next().toLowerCase(); // Convert input to lowercase for consistency

                    switch (carType) {
                        case "economy":
                        case "compact":
                        case "midsize":
                        case "fullsize":
                        case "suv":
                            break; // Valid car type entered, exit the loop
                        default:
                            System.out.println("Invalid car type. Please choose from: Economy, Compact, Midsize, Fullsize, SUV.");
                            continue; // Invalid input, ask again
                    }
                    break; // Exit the loop if a valid car type is entered
                }

                System.out.print("Enter maximum rental price: ");
                maxPrice = scanner.nextDouble();

                System.out.print("Do you need GPS (yes/no or y/n): ");
                needGPS = scanner.next().toLowerCase(); // Convert input to lowercase for consistency
                if (!needGPS.equals("yes") && !needGPS.equals("no") && !needGPS.equals("y") && !needGPS.equals("n")) {
                    System.out.println("Invalid input for GPS. Please enter yes, no, y, or n.");
                    continue; // Retry input
                }

                System.out.print("Do you need a Sunroof (yes/no or y/n): ");
                needSunroof = scanner.next().toLowerCase(); // Convert input to lowercase for consistency
                if (!needSunroof.equals("yes") && !needSunroof.equals("no") && !needSunroof.equals("y") && !needSunroof.equals("n")) {
                    System.out.println("Invalid input for Sunroof. Please enter yes, no, y, or n.");
                    continue; // Retry input
                }

                // Email validation: Check if the email contains the '@' symbol
                boolean validEmail = false;
                for (int j = 0; j < 3; j++) { // Loop to retry email input if invalid
                    System.out.print("Enter your email to rent the car: ");
                    email = scanner.next();
                    if (email.contains("@")) {
                        validEmail = true;
                        break; // Break if email is valid
                    } else {
                        System.out.println("Invalid email. Please enter again.");
                    }
                }
                if (!validEmail) {
                    System.out.println("Maximum attempts reached for email validation. Try again later.");
                    return; // Exit if email is invalid after 3 attempts
                }

                break; // If all inputs are correct, break the loop
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Clear the buffer
            }
            if (i == 2) {
                System.out.println("Maximum attempts reached. Try again later.");
                return; // Exit if 3 invalid attempts
            }
        }

        // Convert "y" and "n" to "yes" and "no" for consistency
        if (needGPS.equals("y")) needGPS = "yes";
        if (needGPS.equals("n")) needGPS = "no";
        if (needSunroof.equals("y")) needSunroof = "yes";
        if (needSunroof.equals("n")) needSunroof = "no";

        Carmain bestMatch = null; // Holds the exact match
        Carmain partialMatch = null; // Holds a partial match

        // Search for the best match or partial match
        for (Carmain car : cars) {
            if (car.isAvailable() && car.getCarType().equalsIgnoreCase(carType)) {
                if (car.getRentalPrice() <= maxPrice) {
                    if (car.hasGPS().equalsIgnoreCase(needGPS) && car.hasSunroof().equalsIgnoreCase(needSunroof)) {
                        bestMatch = car; // Exact match found
                        break;
                    } else if (partialMatch == null) {
                        partialMatch = car; // First partial match found
                    }
                }
            }
        }

        if (bestMatch != null) {
            // Exact match found, rent the car
            bestMatch.setEmail(email); // Assign the car to the user
            System.out.println("Car rented successfully: " + bestMatch);
        } else if (partialMatch != null) {
        	// Partial match found, offer to the user
        	System.out.println("Exact match not found. Next best match: " + partialMatch);
        	System.out.print("Would you like to rent this car instead? (yes/no or y/n): ");
        	String response = scanner.next().toLowerCase(); // Convert input to lowercase for consistency

        	// Check for different valid responses
        	if (response.equals("yes") || response.equals("y")) {
        	    partialMatch.setEmail(email); // Assign the car to the user
        	    System.out.println("Car rented successfully: " + partialMatch);
        	} else if (response.equals("no") || response.equals("n")) {
        	    System.out.println("Rental canceled. No car rented.");
        	} else {
        	    System.out.println("Invalid response. Please enter 'yes', 'no', 'y', or 'n'.");
        	}
        }
    }



    // Cancel a car rental based on car number
 // Cancel a car rental based on email
    private static void cancelRental(Scanner scanner) {
        String email = "";

        // Input validation with retry attempts for email
        for (int i = 0; i < 3; i++) {
            try {
                System.out.print("Enter your email to cancel the rental: ");
                email = scanner.next(); // Read email
                if (email.contains("@")) {
                    break; // If email is valid, exit the loop
                } else {
                    System.out.println("Invalid email. Please enter a valid email address.");
                }
            } catch (Exception e) {
                System.out.println("Error occurred while processing email input.");
            }
            if (i == 2) {
                System.out.println("Maximum attempts reached. Try again later.");
                return; // Exit if 3 invalid attempts
            }
        }

        // Search for the car based on the email
        boolean carFound = false;
        for (Carmain car : cars) {
            if (car.getEmail().equalsIgnoreCase(email)) { // Check if the email matches
                if (!car.isAvailable()) { // If the car is rented
                    car.setEmail("free"); // Mark the car as available
                    System.out.println("Rental canceled for car: " + car);
                    carFound = true;
                } else {
                    System.out.println("The car is not currently rented.");
                    carFound = true;
                }
                break;
            }
        }

        if (!carFound) {
            System.out.println("No car found rented with the specified email.");
        }
    }
}
