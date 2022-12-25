/**
 * The Shop class controls the cost of the items in the Treasure Hunt game.<p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method.<p>
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
import java.util.Scanner;

public class Shop
{
    // constants
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;
    private static final int BRAVERY_COST = 50;
    private int waterEasy = WATER_COST - 1;
    private int ropeEasy = ROPE_COST - 1;
    private int macheteEasy = MACHETE_COST - 2;
    private int horseEasy = HORSE_COST - 3;
    private int boatEasy = BOAT_COST - 4;
    private int braveryEasy = BRAVERY_COST - 10;

    // instance variables
    private double markdown;
    private Hunter customer;
    private boolean cheatMode;
    private boolean easyMode;

    //Constructor
    public Shop(double markdown, boolean cheatMode, boolean easyMode)
    {
        this.markdown = markdown;
        customer = null;
        this.cheatMode = cheatMode;
        this.easyMode = easyMode;
    }

    /** method for entering the shop
     * @param hunter  the Hunter entering the shop
     * @param buyOrSell  String that determines if hunter is "B"uying or "S"elling
     */
    public void enter(Hunter hunter, String buyOrSell)
    {
        customer = hunter;

        Scanner scanner = new Scanner(System.in);
        if (buyOrSell.equals("B") || buyOrSell.equals("b"))
        {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println("Currently we have the following items:");
            System.out.println(inventory());
            System.out.print("What're you lookin' to buy? ");
            String item = scanner.nextLine();
            int cost = checkMarketPrice(item, true);
            if (cost == 0)
            {
                System.out.println("We ain't got none of those.");
            }
            else
            {
                if (cheatMode) {
                    System.out.print("It'll cost you " + 1 + " gold. Buy it (y/n)? ");
                } else {
                    System.out.print("It'll cost you " + cost + " gold. Buy it (y/n)? ");
                }
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y"))
                {
                    buyItem(item);
                }
            }
        }
        else
        {
            System.out.println("What're you lookin' to sell? ");
            System.out.print("You currently have the following items: " + customer.getInventory());
            String item = scanner.nextLine();
            int cost = checkMarketPrice(item, false);
            if (cost == 0)
            {
                System.out.println("We don't want none of those.");
            }
            else
            {
                System.out.print("It'll get you " + cost + " gold. Sell it (y/n)? ");
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y"))
                {
                    sellItem(item);
                }
            }
        }
    }

    /** A method that returns a string showing the items available in the shop (all shops sell the same items)
     *
     * @return  the string representing the shop's items available for purchase and their prices
     */
    public String inventory()
    {
        String str;
        if (cheatMode) {
            str = "Water: " + 1 + " gold\n";
            str += "Rope: " + 1 + " gold\n";
            str += "Machete: " + 1 + " gold\n";
            str += "Horse: " + 1 + " gold\n";
            str += "Boat: " + 1 + " gold\n";
            str += "Bravery: " + 1 + " gold\n";
        } else if (easyMode) {
            str = "Water: " + waterEasy + " gold\n";
            str += "Rope: " + ropeEasy + " gold\n";
            str += "Machete: " + macheteEasy + " gold\n";
            str += "Horse: " + horseEasy + " gold\n";
            str += "Boat: " + boatEasy + " gold\n";
            str += "Bravery: " + braveryEasy + " gold\n";
        } else {
            str = "Water: " + WATER_COST + " gold\n";
            str += "Rope: " + ROPE_COST + " gold\n";
            str += "Machete: " + MACHETE_COST + " gold\n";
            str += "Horse: " + HORSE_COST + " gold\n";
            str += "Boat: " + BOAT_COST + " gold\n";
            str += "Bravery: " + BRAVERY_COST + " gold\n";
        }
        return str;
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     * @param item The item being bought.
     */
    public void buyItem(String item)
    {
        int costOfItem = checkMarketPrice(item, true);
        if (cheatMode) {
            if (customer.buyItem(item, 1))
            {
                System.out.println("Ye' got yerself a " + item + ". Come again soon.");
            }
            else
            {
                System.out.println("Hmm, either you don't have enough gold or you've already got one of those!");
            }
        } else {
            if (customer.buyItem(item, costOfItem))
            {
                System.out.println("Ye' got yerself a " + item + ". Come again soon.");
            }
            else
            {
                System.out.println("Hmm, either you don't have enough gold or you've already got one of those!");
            }
        }

    }

    /**
     * A pathway method that lets the Hunter sell an item.
     * @param item The item being sold.
     */
    public void sellItem(String item)
    {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice))
        {
            System.out.println("Pleasure doin' business with you.");
        }
        else
        {
            System.out.println("Stop stringin' me along!");
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying)
    {
        if (isBuying)
        {
            return getCostOfItem(item, easyMode);
        }
        else
        {
            return getBuyBackCost(item);
        }
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item, boolean easyMode)
    {
        if (easyMode) {
            if (item.toLowerCase().equals("water"))
            {
                return waterEasy;
            }
            else if (item.toLowerCase().equals("rope"))
            {
                return ropeEasy;
            }
            else if (item.toLowerCase().equals("machete"))
            {
                return macheteEasy;
            }
            else if (item.toLowerCase().equals("horse"))
            {
                return horseEasy;
            }
            else if (item.toLowerCase().equals("boat"))
            {
                return boatEasy;
            }
            else if (item.toLowerCase().equals("bravery")) {
                return braveryEasy;
            }
            else
            {
                return 0;
            }
        } else {
            if (item.toLowerCase().equals("water"))
            {
                return WATER_COST;
            }
            else if (item.toLowerCase().equals("rope"))
            {
                return ROPE_COST;
            }
            else if (item.toLowerCase().equals("machete"))
            {
                return MACHETE_COST;
            }
            else if (item.toLowerCase().equals("horse"))
            {
                return HORSE_COST;
            }
            else if (item.toLowerCase().equals("boat"))
            {
                return BOAT_COST;
            }
            else if (item.toLowerCase().equals("bravery")) {
                return BRAVERY_COST;
            }
            else
            {
                return 0;
            }
        }

    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item)
    {
        int cost = (int)(getCostOfItem(item, easyMode) * markdown);
        return cost;
    }
}