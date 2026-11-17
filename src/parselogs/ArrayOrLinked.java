package parselogs;

/**
 * Тип коллекции, используемой в бенчмарке для конкретного лога.
 */
public enum ArrayOrLinked {

    /** ArrayList. */
    ARRAY("ArrayList"),

    /** LinkedList. */
    LINKED("LinkedList");

    private final String collectionName;

    ArrayOrLinked(String collectionName) {
        this.collectionName = collectionName;
    }

    /** @return имя коллекции: "ArrayList" или "LinkedList" */
    public String getCollectionName() {
        return collectionName;
    }

    public boolean isArray() {
        return this == ARRAY;
    }

    public boolean isLinked() {
        return this == LINKED;
    }
}