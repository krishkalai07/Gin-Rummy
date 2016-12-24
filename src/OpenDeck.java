
class OpenDeck extends Deck {

    @Override
    public String toString() {
        if (deck.size() > 0) {
            return "[" + deck.get(deck.size() - 1).toString() + "]";
        }
        else {
            return "[]";
        }
    }
}
