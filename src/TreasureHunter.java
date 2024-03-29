/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all of the display based on the messages it receives from the Town object.
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
import java.util.Locale;
import java.util.Scanner;

public class TreasureHunter
{
    //Instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean normalMode;
    private boolean cheatMode;

    //Constructor
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter()
    {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
    }

    // starts the game; this is the only public method
    public void play()
    {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();

        boolean cont = false;
        while (!cont) {
            System.out.print("Hard, Easy, or Normal mode? (h/e/n): ");
            String hard = scanner.nextLine();
            if (hard.equals("H") || hard.equals("h")) {
                hardMode = true;
                cont = true;
                System.out.println("Game set to hard mode.");
            } else if ((hard.toLowerCase()).equals("e")) {
                easyMode = true;
                cont = true;
                System.out.println("Game set to easy mode.");
            } else if ((hard.toLowerCase()).equals("n")) {
                normalMode = true;
                cont = true;
                System.out.println("Game set to normal mode.");
            } else if (hard.equals("ch34t")) {
                cheatMode = true;
                cont = true;
            } else {
                System.out.println("Error: please enter h, e, or n.");
            }
        }

        // set hunter instance variable
        if (easyMode) {
            hunter = new Hunter(name, 20);
        } else {
            hunter = new Hunter(name, 10);
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown()
    {
        double markdown = 0;
        double toughness = 0;
        String mode = "";

        if (hardMode)
        {
            // in hard mode, you get less money back when you sell items
            markdown = 0.17;

            // and the town is "tougher"
            toughness = 0.75;

            // To determine store prices
            mode = "hard";
        } else if (easyMode) {
            // More money back for reselling in easy mode
            markdown = 0.5;
            // easier town
            toughness = 0.20;
            mode = "easy";
        } else if (normalMode) {
            markdown = 0.27;
            toughness = 0.4;
            mode = "normal";
        } else if (cheatMode) {
            mode = "ch34t";
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, cheatMode, easyMode);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, mode);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String choice = "";

        while (!(choice.equals("X") || choice.equals("x")))
        {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            currentTown.setLatestNews("No new news.");
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            boolean cont = false;
            while (!cont) {
                System.out.println("Would you like to visit the (S)hop, execute an (A)ction, or give up the hunt and (E)xit? (S/A/E):");
                choice = scanner.nextLine();
                if ((choice.toLowerCase()).equals("s")) {
                    System.out.println("*** Shop ***");
                    System.out.println("(B)uy something at the shop.");
                    System.out.println("(S)ell something at the shop.");
                    System.out.println("(R)eturn to main menu.");
                    System.out.println("Give up the hunt and (e)xit.");
                    System.out.print("What's your next move? ");
                    choice = scanner.nextLine();
                } else if ((choice.toLowerCase()).equals("a")) {
                    System.out.println("*** Actions ***");
                    System.out.println("(M)ove on to a different town.");
                    System.out.println("(L)ook for trouble!");
                    System.out.println("(H)unt for treasure!");
                    System.out.println("(R)return to main menu.");
                    System.out.println("Give up the hunt and (e)xit.");
                    System.out.print("What's your next move? ");
                    choice = scanner.nextLine();
                }
                if (!(choice.toLowerCase()).equals("r")) {
                    System.out.println();
                    cont = true;
                }
            }
            System.out.println();
            processChoice(choice);
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice)
    {
        if (choice.equals("B") || choice.equals("b") || choice.equals("S") || choice.equals("s"))
        {
            currentTown.enterShop(choice);
        }
        else if (choice.equals("M") || choice.equals("m"))
        {
            if (currentTown.leaveTown())
            {
                //This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        }
        else if (choice.equals("L") || choice.equals("l"))
        {
            currentTown.lookForTrouble();
        }
        else if ((choice.toLowerCase()).equals("h")) {
            currentTown.huntForTreasure();
        }
        else if (choice.equals("E") || choice.equals("e"))
        {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
            System.exit(0);
        }
        else
        {
            System.out.println("Yikes! That's an invalid option! Try again.");
            showMenu();
        }
    }
}