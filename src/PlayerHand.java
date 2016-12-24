import java.util.Collections;
import java.util.List;

class PlayerHand extends Deck{
    private int marker;
    private int deadwood;
    private int collate_seq_grp_value;
    private final int default_marker = 13;

    /**
     * Constructs the player's hand to an array list.
     * The marker (for sequences and groups) is set to 0.
     * The deadwood is set to 0 (because there is no cards).
     */
    PlayerHand() {
        super();
        marker = 1;
        deadwood = 0;
        collate_seq_grp_value = 0;
    }

    /**
     * Calculates and returns the deadwood of the current hand.
     * The deadwood is the sum of the weights of the cards
     * @return an integer >= 0
     */
    int calculateDeadwood () {
        deadwood = 0;
        int start = getUnmarkedCardIndex();

        if (start != -1) {
            for (int i = start; i < 10; i++) {
                deadwood += deck.get(i).getWeight();
            }
        }
        return deadwood;
    }

    /**
     * Auto-melds the player's hand in a way that
     * sequences are to the far left,
     * groups are in the middle, and
     * deadwood is to the right.
     */
    void meld() {
        int start;

        // Reset values and start from fresh.
        collate_seq_grp_value = 0;
        marker = 1;
        for (Card card : deck) {
            card.setSeqGrp(default_marker);
            card.setCollateSeqGrp(0);
        }

        // Find and mark sequence.
        sortByCardID();
        findSequence();
        sortBySeqGrpMarker();

        // Find and mark group
        start = getUnmarkedCardIndex();
        if (start >= 0) {
            List<Card> sublist = sortByFaceValue(start, length());
            findGroup(sublist);

            int index = start;
            for (Card card : sublist) {
                setCard(card, index++);
            }
        }
        sortBySeqGrpMarker();

        //Sort the deadwood cards
        start = getUnmarkedCardIndex();

        if (start >= 0) {
            List<Card> sublist = sortByFaceValue(start, length());

            int index = start;
            for (Card card : sublist) {
                setCard(card, index++);
            }
        }
    }

    /**
     * Sorts the entire hand by the cardID.
     */
    private void sortByCardID() {
        Collections.sort(deck, (Card c1, Card c2)
                -> c1.getCardID() < c2.getCardID() ? -1 : c1.getCardID() == c2.getCardID() ? 0 : 1);
    }

    /**
     * Sorts the specified section of the hand.
     * @param start The index of the first card to be sorted
     * @param end The index of the last card to be sorted
     * @return A sublist with all the cards inside it sorted.
     */
    private List<Card> sortByFaceValue(int start, int end) {
        List<Card> sub_deck = deck.subList(start, end);

        Collections.sort(sub_deck, (Card c1, Card c2)
                -> c1.getFaceValue() < c2.getFaceValue() ? -1 : c1.getFaceValue() == c2.getFaceValue() ? 0 : 1);
        return sub_deck;
    }

    /**
     * Sorts the entire hand by the marker.
     */
    private void sortBySeqGrpMarker() {
        Collections.sort(deck, (Card c1, Card c2)
                -> c1.getSeqGrp() < c2.getSeqGrp() ? -1 : c1.getSeqGrp() == c2.getSeqGrp() ? 0 : 1);
    }

    /**
     * Find and mark the sequences in the hand. A sequence length can be minimum 3 cards, and maximum the hand's size.
     */
    private void findSequence() {
        int start;
        int end;
        boolean sequence;

        start = 0;
        end = start + 2;
        sequence = false;

        while (end < deck.size()) {
            if (isSequence(start, end)) {
                // Bound check before increment
                if (end >= deck.size() - 1) {
                    markSequence(start, end);
                    end++;
                    continue;
                }
                else {
                    end++;
                }
                sequence = true;
                continue;
            }
            else {
                if (sequence) {
                    markSequence(start, end - 1);
                    sequence = false;
                }
                else {
                    start++;
                    end = start + 2;
                    continue;
                }
            }
            start = end;
            end = start + 2;
        }
    }

    /**
     * This goes through the range given and blindly marks the cards as a sequence.
     * Along with this, the collator will also mark cards (for use in the toString()).
     * @param start The start index of the range.
     * @param end The end index of the range. This must be greater than start.
     */
    private void markSequence(int start, int end) {
        if (end >= deck.size() || start > end) {
            return;
        }

        deck.get(start).setCollateSeqGrp(collate_seq_grp_value++);
        for (int i = start; i <= end; i++) {
            deck.get(i).setSeqGrp(marker);
            deck.get(i).setCollateSeqGrp(collate_seq_grp_value);
            marker++;
        }

    }

    /**
     * Test if the cards in the range provided is a sequence.
     * @param start The start index of the range.
     * @param end The end index of the range. end must be at least 2 more than start.
     * @return true if the cards in the range is a sequence. false if otherwise.
     */
    private boolean isSequence(int start, int end) {
        // Return false, collator few cards for a sequence.
        if (end - start < 2 || end >= deck.size()) {
            return false;
        }

        // Get the top card: every card will be compared to this.
        Card top_card = deck.get(start);

        for (int i = start+1; i <= end; i++) {
            Card current_card = deck.get(i);

            // Check for suit, or face value
            if (top_card.getSuit() != current_card.getSuit() || top_card.getFaceValue() != current_card.getFaceValue() - i + start) {
                return false;
            }
        }

        // The cards ranging from start to end is a sequence
        return true;
    }

    /**
     * Finds the group in a sublist of cards.
     * @param sublist The sublist of cards to look at.
     */
    private void findGroup(List<Card> sublist) {
        int start = 0;
        int end = start + 2;

        // Check size because a group must be greater than 2 cards.
        if (sublist.size() <= 2) {
            return;
        }

        while (end < sublist.size()) {
            // Check group for 3 cards
            if (isGroup(sublist, start, end)) {
                // If true, then check 4 cards
                if (isGroup(sublist, start, end+1)){
                    // If both are true, then mark 4 cards
                    markGroup(sublist, start, end+1);
                }
                else {
                    // If outer only is true, then mark 3 cards
                    markGroup(sublist, start, end);
                }
            }
            else {
                // If it is not a group, keep looking.
                start++;
                end = start + 2;
                continue;
            }
            // Set start and end to next 3 cards.
            start = end + 1;
            end = start + 2;
        }
    }

    /**
     * Marks the cards in a range of the sublist as a group.
     * Also marks the collator (for use in toString()).
     * @param sublist The sublist with cards to be processed.
     * @param start The start index of the sublist
     * @param end The end index of the sublist
     */
    private void markGroup(List<Card> sublist, int start, int end) {
        if (end >= sublist.size()) {
            return;
        }

        sublist.get(start).setCollateSeqGrp(collate_seq_grp_value++);
        for (int i = start; i <= end; i++) {
            sublist.get(i).setSeqGrp(marker);
            sublist.get(i).setCollateSeqGrp(collate_seq_grp_value);
            marker++;
        }
    }

    /**
     * Determines if the cards in the specified range of the sublist is a group.
     * @param sublist The sublist of cards to be processed
     * @param start The index of the first card to be processed
     * @param end The index of the last card to be processed. end-start must be > 2 & < 3
     * @return true if the cards in the range is a group, false if otherwise.
     */
    private boolean isGroup(List<Card> sublist, int start, int end) {
        // Return false, too few cards for a group.
        if (end - start < 2 || end - start > 3 || end >= sublist.size()) {

            return false;
        }

        // Get the top card: every card will be compared to this.
        Card top_card = sublist.get(start);
        for (int i = start + 1; i <= end; i++) {
            Card current_card = sublist.get(i);
            if (top_card.getFaceValue() != current_card.getFaceValue()) {
                return false;
            }
        }

        // The cards in range start to end is a group.
        return true;
    }

    /**
     * Finds and returns the first unmarked card's index.
     * @return The index of the first unmarked card.
     */
    private int getUnmarkedCardIndex() {
        for (int i = 0; i < deck.size(); i++) {
            if (deck.get(i).getSeqGrp() == default_marker) {
                return i;
            }
        }
        return -1; //not found
    }

    /**
     * Overwrites the card at a specified position in the deck
     * @param card The card to be inserted in the list.
     * @param index The index of the deck that the card is set in.
     */
    private void setCard(Card card, int index) {
        deck.set(index, card);
    }

    /**
     * Removed a certain card from the hand
     * @param card The Card to be removed from the hand.
     */
    void removeCardFromHand (Card card) {
        deck.remove(card);
    }

    /**
     * Remove second to last card (10th card).
     */
    Card removeSecondToLastCard () {
        return deck.remove(deck.size()-2);
    }

    @Override
    public String toString() {
        String string = "[";

        int collator = 0;

        for (int i = 0; i < deck.size(); i++) {
            Card card = deck.get(i);

            if (card.getCollateSeqGrp() != 0 && collator != card.getCollateSeqGrp()) {
                collator = card.getCollateSeqGrp();
                string += "(";
            }

            if (card.getCollateSeqGrp() == 0) {
                collator = 0;
            }

            string += card;

            if (i < deck.size()-1 && deck.get(i+1).getCollateSeqGrp() != collator) {
                if (collator > 0) {
                    string += ") ";
                }
            }
            else {
                if (i != deck.size() -1) {
                    string += " ";
                }
            }
        }

        string += "]";

        return string;
    }
}
