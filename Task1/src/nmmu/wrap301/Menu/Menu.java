package nmmu.wrap301.Menu;


import java.util.Scanner;

/**
 * Created by s2133 on 2017/02/20.
 */
public class Menu implements Runnable {

    private String title;
    private Pairs<String,Runnable> menuOptions = new Pairs<>();
    Boolean run = true;
    public Boolean isSubMenu = false;

    public Menu(String Title, boolean isSubMenu) {
        this.title = Title;
        menuOptions = new Pairs<>();
        this.isSubMenu = isSubMenu;
    }

    /**
     * Gets the text needed to display
     *
     * @return A string is returned with the text for the menu
     */
    public String getText() {
        return title;
    }

    /**
     * The menu is run from here. The choices are displayed and the user can select a choice
     * and then the choice can be processed.
     */
    public void run() {
        run = true;
        while (run) {
            displayChoices();
            int chosenChoice = obtainChoice();
            processChoice(chosenChoice);
        }

    }

    /**
     * A menu option is added to the menu
     *
     * @param choice The option added to the menu
     */
    public void addChoice(Pair<String,Runnable> choice) {
        menuOptions.set(choice.getKey(),choice.getValue());

    }

    /**
     * Displays the menu
     */
    protected void displayChoices() {

        String choiceText;
        int counter = 0;
        System.out.println(getText());
        for (int ii = 0; ii < menuOptions.size(); ii++) {
            Pair choice = menuOptions.get(ii);
            choiceText = (String) choice.getKey();
            System.out.println(ii + 1 + ": " + choiceText);
            counter++;
        }
        if (isSubMenu)
            System.out.println(counter + 1 + ": Back");
        else System.out.println(counter + 1 + ": Exit");
    }

    /**
     * The choice can be obtained from the user here.
     *
     * @return The option the user chooses is returned as an int.
     */
    protected int obtainChoice() {
        int option;

        while (true) {//could be replaced with true, option<=0||option>choices.size()
            System.out.println("Enter option number: ");
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
            if (option > 0 && option <= menuOptions.size() + 1)
                return option;

            System.out.println("Invalid choice ");
        }
    }

    /**
     * The choice that the user chose is processed here
     *
     * @param chosenChoice The option that the user chose.
     */
    protected void processChoice(int chosenChoice) {
        if (chosenChoice <= menuOptions.size()) {
            Pair temp = menuOptions.get(chosenChoice-1);
            ((Runnable) temp.getValue()).run();

        } else
            run = false; // Exits the current Menu

    }
}
