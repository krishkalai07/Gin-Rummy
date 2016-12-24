
class ClosedDeck extends Deck{
    /**
     * Initializes the deck with 52 unique cards and shuffle.
     */
    ClosedDeck() {
        super();

        // Construct deck of cards in order
        for (int suit = 0; suit <= 3; suit++) {
            for (int face_value = 1; face_value <= Card.KING; face_value++) {
                deck.add(new Card(suit, face_value));
            }
        }

        shuffle();
    }

    /**
     * Shuffles the deck with whatever cards is in the deck.
     */
    void shuffle() {
        for (int i = deck.size()-1; i > 1; i--) {
            int random_value = (int)(Math.random() * i - 1);
            swap(i, random_value);
        }
    }

    /**
     * Swaps two cards in the ArrayList.
     * @param index1 the index of the first card
     * @param index2 the index of the second card
     */
    private void swap(int index1, int index2) {
        Card temp = deck.get(index1);
        deck.set(index1, deck.get(index2));
        deck.set(index2, temp);
    }
}
