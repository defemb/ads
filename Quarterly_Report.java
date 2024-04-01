import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;

public class Quarterly_Report {

	public static void main(String[] args) {
		// Welcome message
		System.out.println("*** Welcome to the Quarterly Report App ***");
		System.out.println("\nFirstly, we require the sales data.");
		
		// Create a double array for sales: Departments, Months
		double[][] sales = new double[5][12];

		// Call the enterSales method to input sales data
		enterSales(sales);
		
		// Main menu
        int choice;
        do {
        	// Display the main menu options
        	System.out.println("\n*** Menu: What do you want to display? ***");
            System.out.println("1. Total sales per department per quarter");
            System.out.println("2. Best and worst department per quarter");
            System.out.println("3. Most effective month for department within a quarter");
            System.out.println("4. Tax that needs to be paid for quarter");
            System.out.println("5. New sales target for each department");
            System.out.println("6. Exit");
            
            // Get user's choice
            choice = getUserChoice();

            // Execute the chosen option
            switch (choice) {
	            case 1:
	                totalSales(sales);
	                break;
	            case 2:
	                bestAndWorst(sales);
	                break;
	            case 3:
	                effectiveMonth(sales);
	                break;
	            case 4:
	                taxForQuarter(sales);
	                break;
	            case 5:
	                newSalesTarget(sales);
	                break;
	            case 6:
	                System.out.println("\nGoodbye!");
	                break;
	            default:
	                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
	        }
	    } while (choice != 6);
	}
	
	// Method to input sales data either randomly or through user input
	static void enterSales(double[][] salesIn) {
		// Create a Scanner object to read user input
        Scanner keyboard = new Scanner(System.in);
        
        // Create a Random object for generating random sales data
        Random random = new Random();
        
        // Variable to store user response
    	String response;
    	
        do {
        	// Ask the user if they want to generate sales randomly
        	System.out.print("Would you like to generate sales randomly? (yes/no): ");
            response = keyboard.next().toLowerCase();
        } while (!response.equals("yes") && !response.equals("no"));
        
        // Loop through each department and month to input sales data
        for (int dept = 0; dept < salesIn.length; dept++) {
            System.out.println("\n*** Monthly sales for " + deptName(dept) + " department ***");

            for (int month = 0; month < salesIn[dept].length; month++) {
                if (response.equals("yes")) {
                    // Generate random sales between 0 and 100000
                    salesIn[dept][month] = random.nextDouble() * 100000;
                    System.out.printf("Sales for %s: £%s%n", monthName(month), NumberFormatter.format(salesIn[dept][month]));
                } else {
                    // Ask the user to input sales
                    System.out.print("Enter sales for " + monthName(month) + " (in £1,000): ");
                    salesIn[dept][month] = (keyboard.nextDouble() * 1000);
                }
            }
        }
    }

	// Method to get the user choice and ensure it is valid
    static int getUserChoice() {
    	// Create a Scanner object to read user input
        Scanner keyboard = new Scanner(System.in);
        
        // Prompt the user to enter their choice
        System.out.print("\nEnter your choice (1-6): ");
        
        // Validate user input, ensuring it is an integer
        while (!keyboard.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            keyboard.next();
        }
        return keyboard.nextInt();
    }

    // Method to display the total sales per department per quarter
    static void totalSales(double[][] salesIn) {
        System.out.println("\n*** Option 1: Total sales per department per quarter ***");

        // Loop through each quarter
        for (int quarter = 0; quarter < 4; quarter++) {
        	System.out.println(); // Add a new line for formatting
        	
        	// Loop through each department
        	for (int dept = 0; dept < 5; dept++) {
        		// Calculate total sales for the current quarter and department
                double [] deptSales = calculateTotalSales(salesIn, quarter);
                
                // Display the total sales for the department in the specified quarter
                System.out.printf("%s Quarter totals for %s: £%s%n", 
                		quarterName(quarter), deptName(dept), NumberFormatter.format(deptSales[dept]));
            }
        }
    }
    
    // Method to calculate total sales per quarter
    static double[] calculateTotalSales(double[][] salesIn, int quarter) {
    	// Initialize an array to store total sales for each department
        double[] departmentTotalSales = new double[5];

        // Loop through each department
        for (int dept = 0; dept < 5; dept++) 
        {
            double totalSales = 0;
            
            // Loop through the months in the specified quarter
            for (int month = quarter * 3; month < (quarter + 1) * 3; month++) 
            {
            	// Accumulate the sales for the current department in the specified quarter
                totalSales += salesIn[dept][month];
            }
            
            // Store the total sales for the current department
            departmentTotalSales[dept] = totalSales;
        }
        
        // Return the array of total sales for each department
        return departmentTotalSales;
    }

    // Method to display the best and worst department per quarter
    static void bestAndWorst(double[][] salesIn) {
        System.out.println("\n*** Option 2: Best and worst department per quarter ***");

        // Loop through each quarter
        for (int quarter = 0; quarter < 4; quarter++) {
        	// Calculate total sales for the current quarter
        	double[] departmentTotalSales = calculateTotalSales(salesIn, quarter);

        	// Find the index of the best and worst departments based on total sales
	        int bestDept = findMaxIndex(departmentTotalSales, 0, 4);
	        int worstDept = findMinIndex(departmentTotalSales);
	        
	        // Output the best department for the current quarter
	        System.out.printf("\n%s Quarter best: %s", quarterName(quarter), deptName(bestDept));
	        
	        // Display monthly sales for the best department
	        displayMonthlySales(salesIn, bestDept, quarter);
	        
	        // Output the worst department for the current quarter
	        System.out.printf("\n%s Quarter worst: %s", quarterName(quarter), deptName(worstDept));
	        
	        // Display monthly sales for the worst department
	        displayMonthlySales(salesIn, worstDept, quarter);
	        
	        System.out.println(); // Add a new line for formatting
        }
    }
    
    // Method to display the monthly sales    
    static void displayMonthlySales(double[][] salesIn, int dept, int quarter) {
    	// Loop through each month in the specified quarter
        for (int month = quarter * 3; month < (quarter + 1) * 3; month++) {
        	// Display the monthly sales for the specified department
            System.out.printf(", £%s", NumberFormatter.format(salesIn[dept][month]));
        }
    }

    // Method to find the index of the maximum value in an array within a specified range
    static int findMaxIndex(double[] array, int startIndex, int endIndex) {
        double max = array[startIndex];
        int maxIndex = startIndex;

        // Loop through the array to find the maximum value and its index within the specified range
        for (int i = startIndex + 1; i < endIndex; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

     // Method to find the index of the minimum value in an array
     static int findMinIndex(double[] array) {
        double min = array[0];
        int minIndex = 0;

        // Loop through the array to find the minimum value and its index
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    // Method to display the most effective month for each department per quarter
    static void effectiveMonth(double[][] salesIn) {
        System.out.println("\n*** Option 3: Most effective month for each department per quarter ***");
        
        // Loop through each quarter
        for (int quarter = 0; quarter < 4; quarter++) {
        	System.out.printf("\n%s Quarter: ", quarterName(quarter));
        	
            // Loop through each department
        	for (int dept = 0; dept < 5; dept++) {
                // Find the index of the most effective month for the current department
        		int mostEffectiveMonth = findMaxIndex(salesIn[dept], quarter * 3, (quarter + 1) * 3);
        		
                // Get the sales for the most effective month
        		double sales = salesIn[dept][mostEffectiveMonth];
        		
                // Display department name, most effective month, and corresponding sales
        		System.out.printf("%s, %s, £%s", deptName(dept), monthName(mostEffectiveMonth), NumberFormatter.format(sales));

                // Formatting for user-friendliness
        		if (dept == 2) {
                    System.out.print("\n");
                }
        		else if (dept < 4) {
                    System.out.print("; ");
                }
        	}
        	System.out.println(); // Add a new line for formatting
        }
    }
    
    // Method to calculate and report tax at 17% for each quarter
    static void taxForQuarter(double[][] salesIn) {
        System.out.println("\n*** Option 4: Tax that needs to be paid for each quarter ***\n");

        // Loop through each quarter
        for (int quarter = 0; quarter < 4; quarter++) {
            // Calculate total sales for each department in the specified quarter
            double[] departmentTotalSales = calculateTotalSales(salesIn, quarter);
            double totalSales = 0;

            // Accumulate total sales for the quarter
            for (double deptSales : departmentTotalSales) {
                totalSales += deptSales;
            }
            
            // Calculate tax assuming a fixed rate of 17%
            double tax = totalSales * 0.17;
            
            // Output the tax to be paid for the specified quarter
            System.out.printf("Tax to be paid for %s quarter: £%s%n", quarterName(quarter), NumberFormatter.format(tax));
        }
    }
    
    // Method to calculate and display new sales targets for each department
    static void newSalesTarget(double[][] salesIn) {
        System.out.println("\n*** Option 5: New sales target for each department ***\n");
        
        // Loop through each department
        for (int dept = 0; dept < 5; dept++) {
            // Get the sales from the last quarter for the current department
            double currentSales = calculateTotalSales(salesIn, 3)[dept];
            
            // Calculate the new sales target as 12% more than the current sales
            double newTarget = currentSales * (1 + 0.12);
            
            // Output the new sales target for the current department
            System.out.printf("New sales target for %s: £%s%n", deptName(dept), NumberFormatter.format(newTarget));
        }
    }
    
    // Method to get the department name based on the department index
    static String deptName(int dept) {
        switch (dept) {
            case 0:
                return "Electrical";
            case 1:
                return "Kitchen";
            case 2:
                return "Bathroom";
            case 3:
                return "Soft Furnishing";
            case 4:
                return "Accessories";
            default:
                return "Unknown";
        }
	}
    
    // Method to get the quarter name based on the quarter index
	static String quarterName(int quarter) {
        switch (quarter) {
            case 0:
                return "1st";
            case 1:
                return "2nd";
            case 2:
                return "3rd";
            case 3:
                return "4th";
            default:
                return "Unknown";
        }
	}
	
	// Method to get the month name based on the month index
    static String monthName(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Unknown Month";
        }
    }
    
    // Utility class for formatting numbers
    public class NumberFormatter {
        // Create a decimal format with the pattern "#,###.00"
        private static final DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

        // Method to format a double number and return it as a string
        public static String format(double number) {
            return decimalFormat.format(number);
        }
    }
}
