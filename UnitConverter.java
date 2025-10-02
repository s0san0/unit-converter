import java.util.*;

public class UnitConverter {
    // Exact standard constants
    private static final double MILES_TO_KM = 1.609344;
    private static final double FOOT_TO_M = 0.3048;
    private static final double INCH_TO_CM = 2.54;
    private static final double LB_TO_KG = 0.4539237;
    private static final double STONE_TO_LB = 14.0;
    private static final double LITER_TO_US_GALLON = 1.0 / 3.785411784;
    private static final double LITER_TO_IMP_GALLON = 1.0 / 4.54609;
    private static final double LITER_TO_US_PINT = 1.0 / 0.473176473;
    private static final double LITER_TO_IMP_PINT = 1.0 / 0.56826125;
    private static final double MPH_TO_KMH = 1.609344;
    private static final double YARD_TO_M = 0.9144;
    private static final double OZ_TO_G = 28.349523125;

    enum Category { LENGTH, WEIGHT, TEMPERATURE, VOLUME, CURRENCY, SPEED }

    static class Option {
        final String label; final Runnable action;
        Option(String label, Runnable action) { this.label = label; this.action = action; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        println("🇬🇧  Java Unit Converter (UK + US friendly)\n");

        Map<Integer, Option> main = new LinkedHashMap<>();
        main.put(1, new Option("Length (mi/km, ft/m, in/cm, yd/m)", () -> lengthMenu(sc)));
        main.put(2, new Option("Weight (lb/kg, stone/kg, oz/g)",  () -> weightMenu(sc)));
        main.put(3, new Option("Temperature (°C/°F)",       () -> temperatureMenu(sc)));
        main.put(4, new Option("Volume (L, US/Imp gal & pint)", () -> volumeMenu(sc)));
        main.put(5, new Option("Currency (USD ↔ GBP)", () -> currencyMenu(sc)));
        main.put(6, new Option("Speed (mph ↔ km/h)", () -> speedMenu(sc)));
        main.put(0, new Option("Exit", () -> { println("Thank you! Happy travels! ✈️"); System.exit(0); }));

        while (true) {
            printMenu("Main Menu", main);
            int choice = readInt(sc, "Choose an option: ");
            runChoice(main, choice);
            println("");
        }
    }

    // Menus
    private static void lengthMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Miles → Kilometers", () ->
            convertLoop(sc, "miles", "kilometers", v -> v * MILES_TO_KM)
        ));
        m.put(2, new Option("Kilometers → Miles", () ->
            convertLoop(sc, "kilometers", "miles", v -> v / MILES_TO_KM)
        ));
        m.put(3, new Option("Feet → Meters",      () ->
            convertLoop(sc, "feet", "meters", v -> v * FOOT_TO_M)
        ));
        m.put(4, new Option("Meters → Feet",      () ->
            convertLoop(sc, "meters", "feet", v -> v / FOOT_TO_M)
        ));
        m.put(5, new Option("Inches → Centimeters", () ->
            convertLoop(sc, "inches", "centimeters", v -> v * INCH_TO_CM)
        ));
        m.put(6, new Option("Centimeters → Inches", () ->
            convertLoop(sc, "centimeters", "inches", v -> v / INCH_TO_CM)
        ));
        m.put(7, new Option("Yards → Meters", () ->
            convertLoop(sc, "yards", "meters", v -> v * YARD_TO_M)
        ));
        m.put(8, new Option("Meters → Yards", () ->
            convertLoop(sc, "meters", "yards", v -> v / YARD_TO_M)
        ));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Length", m);
    }

    private static void weightMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Pounds → Kilograms", () ->
            convertLoop(sc, "pounds", "kilograms", v -> v * LB_TO_KG)
        ));
        m.put(2, new Option("Kilograms → Pounds", () ->
            convertLoop(sc, "kilograms", "pounds", v -> v / LB_TO_KG)
        ));
        m.put(3, new Option("Stone → Kilograms",  () ->
            convertLoop(sc, "stone", "kilograms", v -> (v * STONE_TO_LB) * LB_TO_KG)
        ));
        m.put(4, new Option("Kilograms → Stone",  () ->
            convertLoop(sc, "kilograms", "stone", v -> (v / LB_TO_KG) / STONE_TO_LB)
        ));
        m.put(5, new Option("Ounces(oz) → Grams(g)", () ->
            convertLoop(sc, "ounces", "grams", v -> v * OZ_TO_G)
        ));
        m.put(6, new Option("Grams(g) → Ounces(oz)", () ->
            convertLoop(sc, "grams", "ounces", v -> v / OZ_TO_G)
        ));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Weight", m);
    }

    private static void temperatureMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Celsius(°C) → Fahrenheit(°F)", () -> {
            while (true) {
                String raw = readLine(sc, "Enter °C (or 'b' to go back): ");
                if (raw.equalsIgnoreCase("b")) return;
                try {
                    double c = Double.parseDouble(raw);
                    double f = c * 9.0/5.0 + 32.0;
                    println(fmt(c) + " °C = " + fmt(f) + " °F");
                } catch (NumberFormatException e) {
                    println("Please enter a valid number or 'b' to go back.");
                }
            }
        }));
        m.put(2, new Option("Fahrenheit(°F) → Celsius(°C)", () -> {
            while (true) {
                String raw = readLine(sc, "Enter °F (or 'b' to go back): ");
                if (raw.equalsIgnoreCase("b")) return;
                try {
                    double f = Double.parseDouble(raw);
                    double c = (f - 32.0) * 5.0/9.0;
                    println(fmt(f) + " °F = " + fmt(c) + " °C");
                } catch (NumberFormatException e) {
                    println("Please enter a valid number or 'b' to go back.");
                }
            }
        }));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Temperature", m);
    }

    private static void volumeMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Liters → US Gallons",    () ->
            convertLoop(sc, "liters", "US gallons", v -> v * LITER_TO_US_GALLON)
        ));
        m.put(2, new Option("US Gallons → Liters",    () ->
            convertLoop(sc, "US gallons", "liters", v -> v / LITER_TO_US_GALLON)
        ));
        m.put(3, new Option("Liters → Imperial Gallons", () ->
            convertLoop(sc, "liters", "Imperial gallons", v -> v * LITER_TO_IMP_GALLON)
        ));
        m.put(4, new Option("Imperial Gallons → Liters", () ->
            convertLoop(sc, "Imperial gallons", "liters", v -> v / LITER_TO_IMP_GALLON)
        ));
        m.put(5, new Option("Liters → US Pints",      () ->
            convertLoop(sc, "liters", "US pints", v -> v * LITER_TO_US_PINT)
        ));
        m.put(6, new Option("US Pints → Liters",      () ->
            convertLoop(sc, "US pints", "liters", v -> v / LITER_TO_US_PINT)
        ));
        m.put(7, new Option("Liters → Imperial Pints", () ->
            convertLoop(sc, "liters", "Imperial pints", v -> v * LITER_TO_IMP_PINT)
        ));
        m.put(8, new Option("Imperial Pints → Liters", () ->
            convertLoop(sc, "Imperial pints", "liters", v -> v / LITER_TO_IMP_PINT)
        ));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Volume", m);
    }

    private static void currencyMenu(Scanner sc) {
        println("\n- Currency -");
        double rate = readDouble(sc, "Enter current exchange rate (1 USD = ? GBP): ");

        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("USD($) → GBP(£)", () ->
            convertLoop(sc, "USD", "GBP", v -> v * rate)
        ));
        m.put(2, new Option("GBP(£) → USD($)", () ->
            convertLoop(sc, "GBP", "USD", v -> v / rate)
        ));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Currency", m);
    }

    private static void speedMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Miles/hour(mph) → Kilometers/hour(km/h)", () ->
            convertLoop(sc, "mph", "km/h", v -> v * MPH_TO_KMH)
        ));
        m.put(2, new Option("Kilometers/hour(km/h) → Miles/hour(mph)", () ->
            convertLoop(sc, "km/h", "mph", v -> v / MPH_TO_KMH)
        ));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Speed", m);
    }

    // Helpers
    private static void subLoop(Scanner sc, String title, Map<Integer, Option> menu) {
        while (true) {
            printMenu(title, menu);
            int choice = readInt(sc, "Choose an option: ");
            if (choice == 0) return;
            runChoice(menu, choice);
            println("");
        }
    }

    private static void convert(Scanner sc, String from, String to, double factor) {
        double v = readDouble(sc, "Enter " + from + ": ");
        double out = v * factor;
        println(fmt(v) + " " + from + " = " + fmt(out) + " " + to);
    }

    private static void printMenu(String title, Map<Integer, Option> items) {
        println("\n- " + title + " -");
        for (Map.Entry<Integer, Option> e : items.entrySet()) {
            System.out.printf("[%d] %s%n", e.getKey(), e.getValue().label);
        }
    }

    private static void runChoice(Map<Integer, Option> menu, int choice) {
        Option opt = menu.get(choice);
        if (opt != null) opt.action.run();
        else println("Invalid choice. Please try again.");
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { println("Please enter a whole number."); }
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try { return Double.parseDouble(s); }
            catch (NumberFormatException e) { println("Please enter a valid number."); }
        }
    }

    private static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    // friendly formatting w/o scientific notation for typical ranges
    private static String fmt(double d) {
        return String.format(Locale.US, "%,.6f", d).replaceAll("(\\.\\d*?)0+$", "$1").replaceAll("\\.$", "");
    }

    private static void println(String s) { System.out.println(s); }

    private static void convertLoop(Scanner sc, String from, String to, java.util.function.DoubleUnaryOperator op) {
        while (true) {
            String raw = readLine(sc, "Enter " + from + " (or 'b' to go back): ");
            if (raw.equalsIgnoreCase("b")) return;
            try {
                double v = Double.parseDouble(raw);
                double out = op.applyAsDouble(v);
                println(fmt(v) + " " + from + " → " + fmt(out) + " " + to);
            } catch (NumberFormatException e) {
                println("Please enter a valid number or 'b' to go back.");
            }
        }
    }
}
