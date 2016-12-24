
/**
 * Constructs a card with a suit and face value.
 */
class Card {
    final static short SPADES = 0;
    final static short HEARTS = 1;
    final static short CLUBS = 2;
    final static short DIAMOND = 3;

    //Face values
    final static short ACE = 1;
    final static short JACK = 11;
    final static short QUEEN = 12;
    final static short KING = 13;

    // Magic number: Default value for marker.
    private final int defualt_marker = 13;

    private int suit;
    private int face_value;
    private int weight;
    private int card_id;
    private int seq_group;
    private int collate_seq_grp;

    /**
     * Initializes the card to a new card with given values
     * @param suit The suit of the card, represented as an integer.
     * @param face_value The face value of the card, repersented as an integer
     */
    Card (int suit, int face_value) {
        this.suit = suit;
        this.face_value = face_value;
        card_id = face_value + suit * 13;

        this.weight = face_value;
        if (face_value >= 10) {
            this.weight = 10;
        }

        seq_group = defualt_marker;

        collate_seq_grp = 0;
    }

        int getSuit() {
            return suit;
        }

        int getFaceValue() {
            return face_value;
        }

        int getWeight() {
            return weight;
        }

        int getSeqGrp() {
            return seq_group;
        }

        int getCardID () { return card_id; }

        int getCollateSeqGrp () { return  collate_seq_grp; }

        void setSeqGrp(int state) {
            seq_group = state;
        }

        void setCollateSeqGrp (int value) { collate_seq_grp = value; }

    public String toString() {
        String ret_str = "";

        // Add face value to string
        switch (face_value) {
            case ACE:
                ret_str += "A";
                break;
            case JACK:
                ret_str += "J";
                break;
            case QUEEN:
                ret_str += "Q";
                break;
            case KING:
                ret_str += "K";
                break;
            default:
                ret_str += face_value;
        }

        // Add suit to string
        switch (suit) {
            case SPADES:
                ret_str += "S";
                break;
            case DIAMOND:
                ret_str += "D";
                break;
            case CLUBS:
                ret_str += "C";
                break;
            case HEARTS:
                ret_str += "H";
                break;
            default:
                return "*";
        }

        return ret_str;
    }
}
