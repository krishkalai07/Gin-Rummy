import java.util.Scanner;

public class Game {
    private ClosedDeck closed_deck;
    private OpenDeck open_deck;
    private PlayerHand player_hand;
    private PlayerHand computer_hand;
    private String player_name;
    private String computer_name;

    /**
     * Initialized the closed deck, open deck, player1 and computer_hand.
     */
    private Game() {
        closed_deck = new ClosedDeck();
        open_deck = new OpenDeck();
        player_hand = new PlayerHand();
        computer_hand = new PlayerHand();
        player_name = "Player";
        computer_name = "CPU";
    }

    /**
     * Does the pre-game preparation: Deal 10 cards to each player, and one card to the open deck.
     */
    private void initGame () {
        // Distribute cards...
        for (int i = 0; i < 10; i++) {
            // ... to player 1
            Card card = closed_deck.removeFirstCard();
            player_hand.addCard(card);

            // ... to player 2
            card = closed_deck.removeFirstCard();
            computer_hand.addCard(card);
        }

        player_hand.meld();
        computer_hand.meld();

        // Add a card to open deck.
        Card card = closed_deck.removeFirstCard();
        open_deck.addCard(card);
    }

    /**
     * Displays the intro screen.
     */
    private void displayIntro () {
        System.out.println("------------------------------");
        System.out.println("Simple Gin Rummy!");
        System.out.println("by Krish Kalai");
        System.out.println("------------------------------\n");
    }

    /**
     * Displays the draw screen.
     */
    private void displayDrawScreen () {
        System.out.println("Closed Deck: " + "[X]" + "\t\tOpen Deck: " + open_deck);
        System.out.println("Deadwood: " + player_hand.calculateDeadwood());
        System.out.println(player_name + " Hand: " + player_hand + "\n");

        System.out.println("Your options are:");
        System.out.println("(1) to draw from the open deck");
        System.out.println("(2) to draw from the closed deck");
        System.out.println();
        System.out.println("(q) to quit");
    }

    /**
     * Displays the drop screen.
     */
    private void displayDropScreen () {
        System.out.println("Closed Deck: " + "[X]" + "\t\tOpen Deck: " + open_deck);
        System.out.println("Deadwood: " + player_hand.calculateDeadwood());
        System.out.println(player_name + " Hand: " + player_hand + "\n");

        System.out.println("Your options are:");
        System.out.println("(1) to drop your last card");
        System.out.println();
        System.out.println("(q) to quit");
    }

    /**
     * Displays the knock screen.
     */
    private void displayKnockScreen () {
        System.out.println("Closed Deck: " + "[X]" + "\t\tOpen Deck: " + open_deck);
        System.out.println("Deadwood: " + player_hand.calculateDeadwood());
        System.out.println(player_name + " Hand: " + player_hand + "\n");

        System.out.println("(1) to Knock");
    }

    /**
     * Displays the winning screen.
     * @param is_player_win A boolean: true if the player won the game; false if the CPU won.
     */
    private void displayAfterKnockScreen (boolean is_player_win) {
        System.out.println("Closed Deck: " + "[X]" + "\t\tOpen Deck: " + open_deck);
        System.out.println("Deadwood: " + player_hand.calculateDeadwood());
        System.out.println(player_name + " Hand: " + player_hand + "\n");

        System.out.println("Deadwood: " + computer_hand.calculateDeadwood());
        System.out.println(computer_name + " Hand: " + computer_hand + "\n");

        if (is_player_win) {
            System.out.println("Congratulations, you won the game.");
        }
        else {
            System.out.println("Sorry, you lost the game.");
        }
    }

    /**
     * Displays the CPU moving screen.
     */
    private void displayCPUScreen () {
        System.out.println("CPU Player moved...");
        System.out.println();
    }

    /**
     * Returns the first character of an user input without validating.
     * @return the first character of the input.
     *         If the user types "100101", then the function will return '1'.
     */
    private char getInput() {
        Scanner cin = new Scanner(System.in);
        String buffer;

        buffer = cin.next();
        cin.nextLine();

        return buffer.charAt(0);
    }

    /**
     * This function contains the logic for the CPU move. The CPU does:
     * 1) pseudo-draw a card from the open deck, meld, and record the deadwood.
     * 2) pseudo-draw a card from the closed deck, meld, and record the deadwood.
     * 3) Compare the deadwood, and draw from the deck which yields the least deadwood.
     * 4) Check for knock. If possible, then knock immediately.
     * 5) Drop a card to the open deck, and the turn is over.
     */
    private boolean performCPUPlayerMove() {
        Card card;
        int open_deadwood, closed_deadwood;
        boolean avoid_open_deck = false;

        card = open_deck.getLastCard();
        computer_hand.addCard(card);
        computer_hand.meld();
        open_deadwood = computer_hand.calculateDeadwood();
        if (card.getCardID() == computer_hand.getLastCard().getCardID()) {
            avoid_open_deck = true;
        }
        computer_hand.removeCardFromHand(card);

        if (avoid_open_deck) {
            card = closed_deck.removeLastCard();
        }
        else {
            if (closed_deck.length() <= 1) {
                System.out.println("Its a draw");
                System.exit(0);
            }
            card = closed_deck.getLastCard();
            computer_hand.addCard(card);
            computer_hand.meld();
            closed_deadwood = computer_hand.calculateDeadwood();
            computer_hand.removeCardFromHand(card);

            if (open_deadwood <= closed_deadwood) {
                card = open_deck.removeLastCard();
            } else {
                card = closed_deck.removeLastCard();
            }
        }
        computer_hand.addCard(card);
        computer_hand.meld();

        card = computer_hand.removeLastCard();
        open_deck.addCard(card);

        if (computer_hand.calculateDeadwood() <= 10) {
            displayAfterKnockScreen(false);
            return true;
        }
        return false;
    }

    /**
     * Main driver for game
     * @param args Unused default string array.
     */
    public static void main(String[] args) {
        Game game = new Game();
        Card card = null, open_card;
        char input;

        // Start the game
        game.displayIntro();

        // Populate player1's hand, computer_hand's hand and the open deck.
        game.initGame();

        while (true) {
            game.displayDrawScreen();

            open_card = null;

            // Input validation for draw screen
            do {
                System.out.printf(">>> ");
                input = game.getInput();
                if (input == '1' || input == '2' || input == 'q') {
                    break;
                }
            } while (true);

            System.out.println("");
            // Perform the requested operation
            switch (input) {
                case '1':
                    // Draw from the open deck
                    card = game.open_deck.removeLastCard();
                    open_card = card;
                    break;
                case '2':
                    // Draw from the closed deck
                    if (game.closed_deck.length() <= 1) {
                        System.out.println("Its a draw");
                        System.exit(0);
                    }
                    card = game.closed_deck.removeLastCard();
                    break;
                case 'q':
                    // Quit the game
                    System.exit(0);
            }
            game.player_hand.addCard(card);
            game.player_hand.meld();

            if (game.player_hand.calculateDeadwood() <= 10) {
                game.displayKnockScreen();

                // Input validation for knock screen
                do {
                    System.out.printf(">>> ");
                    input = game.getInput();
                    if (input == '1') {
                        System.out.println();
                        break;
                    }
                } while (true);

                // Perform the requested operation
                switch (input) {
                    case '1':
                        // Drop the player's last card and knock
                        game.player_hand.removeLastCard();
                        game.displayAfterKnockScreen(true);
                        System.exit(0);
                }
            }

            game.displayDropScreen();

            // Input validation for drop screen
            do {
                System.out.printf(">>> ");
                input = game.getInput();
                if (input == '1' || input == 'q') {
                    System.out.println();
                    break;
                }
            } while (true);

            // Perform the requested operation
            switch (input) {
                case '1':
                    // If the last card == open card, remove second to last card
                    if (open_card != null && open_card.getCardID() == game.player_hand.getLastCard().getCardID()) {
                        card = game.player_hand.removeSecondToLastCard();
                    }
                    else {
                        // Drop the player's last card
                        card = game.player_hand.removeLastCard();
                    }

                    game.open_deck.addCard(card);
                    break;
                case 'q':
                    // Quit the game
                    System.exit(0);
            }

            game.displayCPUScreen();
            // CPU player turn
            if (game.performCPUPlayerMove()) {
                return;
            }
        }
    }
}
