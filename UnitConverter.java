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

    enum Category { LENGTH, WEIGHT, TEMPERATURE, VOLUME }

    static class Option {
        final String label; final Runnable action;
        Option(String label, Runnable action) { this.label = label; this.action = action; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        println("🇬🇧  Java Unit Converter (UK + US friendly)\n");

        Map<Integer, Option> main = new LinkedHashMap<>();
        main.put(1, new Option("Length (mi/km, ft/m, in/cm)", () -> lengthMenu(sc)));
        main.put(2, new Option("Weight (lb/kg, stone/kg)",  () -> weightMenu(sc)));
        main.put(3, new Option("Temperature (°C/°F)",       () -> temperatureMenu(sc)));
        main.put(4, new Option("Volume (L, US/Imp gal & pint)", () -> volumeMenu(sc)));
        main.put(0, new Option("Exit", () -> { println("Thank you! Happy travels!"); System.exit(0); }));

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
        m.put(1, new Option("Miles -> Kilometers", () -> convert(sc, "miles", "kilometers", MILES_TO_KM)));
        m.put(2, new Option("Kilometers -> Miles", () -> convert(sc, "kilometers", "miles", 1.0 / MILES_TO_KM)));
        m.put(3, new Option("Feet -> Meters",      () -> convert(sc, "feet", "meters", FOOT_TO_M)));
        m.put(4, new Option("Meters -> Feet",      () -> convert(sc, "meters", "feet", 1.0 / FOOT_TO_M)));
        m.put(5, new Option("Inches -> Centimeters", () -> convert(sc, "inches", "centimeters", INCH_TO_CM)));
        m.put(6, new Option("Centimeters -> Inches", () -> convert(sc, "centimeters", "inches", 1.0 / INCH_TO_CM)));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Length", m);
    }

    private static void weightMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Pounds -> Kilograms", () -> convert(sc, "pounds", "kilograms", LB_TO_KG)));
        m.put(2, new Option("Kilograms -> Pounds", () -> convert(sc, "kilograms", "pounds", 1.0 / LB_TO_KG)));
        m.put(3, new Option("Stone -> Kilograms",  () -> {
            double stone = readDouble(sc, "Enter stone: ");
            double lbs = stone * STONE_TO_LB;
            double kg = lbs * LB_TO_KG;
            println(fmt(stone) + " stone = " + fmt(kg) + " kilograms");
        }));
        m.put(4, new Option("Kilograms -> Stone",  () -> {
            double kg = readDouble(sc, "Enter kilograms: ");
            double lbs = kg / LB_TO_KG;
            double stone = lbs / STONE_TO_LB;
            println(fmt(kg) + " kilograms = " + fmt(stone) + " stone");
        }));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Weight", m);
    }

    private static void temperatureMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Celsius -> Fahrenheit", () -> {
            double c = readDouble(sc, "Enter °C: ");
            double f = c * 9.0/5.0 + 32.0;
            println(fmt(c) + " °C = " + fmt(f) + " °F");
        }));
        m.put(2, new Option("Fahrenheit -> Celsius", () -> {
            double f = readDouble(sc, "Enter °F: ");
            double c = (f - 32.0) * 5.0/9.0;
            println(fmt(f) + " °F = " + fmt(c) + " °C");
        }));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Temperature", m);
    }

    private static void volumeMenu(Scanner sc) {
        Map<Integer, Option> m = new LinkedHashMap<>();
        m.put(1, new Option("Liters -> US Gallons",    () -> convert(sc, "liters", "US gallons", LITER_TO_US_GALLON)));
        m.put(2, new Option("US Gallons -> Liters",    () -> convert(sc, "US gallons", "liters", 1.0 / LITER_TO_US_GALLON)));
        m.put(3, new Option("Liters -> Imperial Gallons", () -> convert(sc, "liters", "Imperial gallons", LITER_TO_IMP_GALLON)));
        m.put(4, new Option("Imperial Gallons -> Liters", () -> convert(sc, "Imperial gallons", "liters", 1.0 / LITER_TO_IMP_GALLON)));
        m.put(5, new Option("Liters -> US Pints",      () -> convert(sc, "liters", "US pints", LITER_TO_US_PINT)));
        m.put(6, new Option("US Pints -> Liters",      () -> convert(sc, "US pints", "liters", 1.0 / LITER_TO_US_PINT)));
        m.put(7, new Option("Liters -> Imperial Pints", () -> convert(sc, "liters", "Imperial pints", LITER_TO_IMP_PINT)));
        m.put(8, new Option("Imperial Pints -> Liters", () -> convert(sc, "Imperial pints", "liters", 1.0 / LITER_TO_IMP_PINT)));
        m.put(0, new Option("Back", () -> {}));
        subLoop(sc, "Volume", m);
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
        println("- " + title + " -");
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

    // friendly formatting w/o scientific notation for typical ranges
    private static String fmt(double d) {
        return String.format(Locale.US, "%,.6f", d).replaceAll("(\\.\\d*?)0+$", "$1").replaceAll("\\.$", "");
    }

    private static void println(String s) { System.out.println(s); }
}
