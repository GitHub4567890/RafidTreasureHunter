/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private int generatedTreasure;
    private boolean foundTreasure;
    private String mode;

    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param s The town's shoppe.
     * @param t The surrounding terrain.
     */
    public Town(Shop shop, double toughness, String mode)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.mode = mode;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        generateTreasure();
        foundTreasure = false;

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews()
    {
        return printMessage;
    }
    public void setLatestNews(String news) {
        printMessage = news;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param h The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     * When the player looses all their gold in a brawl, the game ends with a "Game Over" message.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff;
            if (mode.equals("easy")) {
                goldDiff = (int)(Math.random() * 20) + 5;
            } else if (mode.equals("medium")) {
                goldDiff = (int)(Math.random() * 15) + 1;
            } else {
                goldDiff = (int)(Math.random() * 10) + 1;
            }

            if (Math.random() > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                if (mode.equals("easy")) {
                    goldDiff = (int)(Math.random() * 10) + 1;
                }
                printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
                if (hunter.getGold() == 0) {
                    System.out.println("You lost all your gold in the brawl!\nGame Over");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Generates a random number from 1-4.
     */
    public void generateTreasure() {
        generatedTreasure = (int) ((Math.random() * 4) + 1);
    }

    /**
     * Allows the user to "hunt" for treasure/an item.
     * 1 = Banana
     * 2 = Twig
     * 3 = Spoon
     * 4 = Nothing
     * If the user already has the item in their inventory, they "discard" the item.
     * buyItem() is used to access addItem() without making addItem() public.
     */
    public void huntForTreasure() {
        if (!foundTreasure) {
            String treasure = "";
            String action = "";
            if (generatedTreasure == 1) {
                treasure = "Banana";
                if (hunter.buyItem("Banana", 1)) {
                    action = "eat";
                    hunter.changeGold(1);
                    hunter.addToHuntedItemTotal(1);
                }
            } else if (generatedTreasure == 2) {
                treasure = "Twig";
                if (hunter.buyItem("Twig", 1)) {
                    action = "beat";
                    hunter.changeGold(1);
                    hunter.addToHuntedItemTotal(2);
                }
            } else if (generatedTreasure == 3) {
                treasure = "Spoon";
                if (hunter.buyItem("Spoon", 1)) {
                    action = "dig";
                    hunter.changeGold(1);
                    hunter.addToHuntedItemTotal(3);
                }
            } else if (generatedTreasure == 4) {
                printMessage = "You found nothing! Congrats!";
            }

            if (!action.equals("")) {
                printMessage = "You found a " + treasure + "! This will be a good tool to " + action + " with.";
            } else if (generatedTreasure != 4) {
                printMessage = "You already have a " + treasure + " so you discard it.";
            }

            if (hunter.getHuntedItemTotal() == 6) {
                System.out.println(printMessage + "\nYou beat the game! Congrats!");
                System.exit(0);
            }

            foundTreasure = true;
        } else {
            printMessage = ("You already searched for treasure in this town!");
        }
    }


    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }
}