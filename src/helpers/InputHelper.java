package helpers;

import exceptions.InputCancelledException;
import models.Tool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class InputHelper {
    public static String getStringFromInput(String prompt) throws InputCancelledException {
        Scanner in = new Scanner(System.in);
        System.out.println();
        System.out.printf(prompt);
        String input = in.nextLine();

        if (input.equals("q"))
        {
            throw new InputCancelledException();
        }

        return input;
    }

    public static String getToolCodeFromInput(String prompt, List<Tool> tools) throws InputCancelledException {
        Scanner in = new Scanner(System.in);

        while(true)
        {
            System.out.println();
            System.out.printf(prompt);
            String input = in.nextLine();

            if (input.equals("q"))
            {
                throw new InputCancelledException();
            }

            boolean isValidToolCode = tools.stream()
                    .anyMatch((t) -> t.getCode().equals(input));

            if (isValidToolCode)
            {
                return input;
            }
            else
            {
                System.out.println("Invalid tool code. Please enter an existing tool code or q to cancel.");
            }
        }
    }

    public static int getNumberFromInput(String prompt) throws InputCancelledException {
        Scanner in = new Scanner(System.in);

        while (true)
        {
            System.out.println();
            System.out.printf(prompt);
            String input = in.nextLine();

            if (input.equals("q"))
            {
                throw new InputCancelledException();
            }

            try {
                int inputInt = Integer.parseInt(input);
                return inputInt;
            } catch (Exception e) {
                System.out.println("Invalid number. Please enter a number or q to cancel.");
            }
        }
    }

    public static LocalDate getDateFromInput(String prompt) throws InputCancelledException {
        Scanner in = new Scanner(System.in);

        while (true)
        {
            System.out.println();
            System.out.printf(prompt);
            String input = in.nextLine();

            if (input.equals("q"))
            {
                throw new InputCancelledException();
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
                LocalDate inputDate = LocalDate.parse(input, formatter);
                return inputDate;
            } catch (Exception e) {
                System.out.println("Invalid date. Please enter a date in the format mm/dd/yy or q to cancel.");
            }
        }
    }

    public static boolean getBooleanFromInput(String prompt) throws InputCancelledException {
        Scanner in = new Scanner(System.in);

        while (true)
        {
            System.out.println();
            System.out.printf(prompt);
            String input = in.nextLine();

            if (input.equals("q"))
            {
                throw new InputCancelledException();
            }

            if (input.equals("y"))
                return true;
            if (input.equals("n"))
                return false;

            System.out.println("Invalid response. Please enter y or n.");
        }
    }
}
