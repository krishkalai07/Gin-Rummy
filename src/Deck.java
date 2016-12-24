import java.util.ArrayList;

/**
 * Abstract class containing a generic deck.
 */
abstract class Deck {
    protected ArrayList<Card> deck;

    Deck() {
        deck = new ArrayList<>();
    }

    /**
     * Adds a card to the deck
     * @param card The card to be added.
     */
    void addCard(Card card) {
        deck.add(card);
    }

    /**
     * Removes and returns the first (front) card in the deck.
     * @return the card that is removed.
     */
    Card removeFirstCard() {
        return deck.remove(0);
    }

    /**
     * Removes and returns the last (back) card from the deck
     * @return the card that is removed.
     */
    Card removeLastCard() {
        Card card_to_remove = deck.get(deck.size()-1);
        deck.remove(deck.size()-1);
        return card_to_remove;
    }

    /**
     * Returns the last card in the deck. If the deck is empty, then return null.
     * @return the last card in the deck. If the deck is empty, then return null.
     */
    Card getLastCard() {
        if (deck.size() > 0) {
            return deck.get(deck.size() - 1);
        }
        else {
            return null;
        }
    }

    /**
     * Return the number of cards in the deck.
     * @return the number of cards in the deck as an integer.
     */
    int length() {
        return deck.size();
    }

    @Override
    public String toString() {
        return deck.toString();
    }
}
