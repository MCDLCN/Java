package persistence.itemdata;

/**
 * Persisted state for scroll instances.
 */
public class ScrollInstanceData implements ItemInstanceData {

    private final int usesRemaining;

    /**
     * Creates persisted data for a scroll instance.
     *
     * @param usesRemaining remaining uses on the scroll
     */
    public ScrollInstanceData(int usesRemaining) {
        this.usesRemaining = usesRemaining;
    }

    /**
     * Returns remaining uses.
     *
     * @return remaining uses
     */
    public int getUsesRemaining() {
        return usesRemaining;
    }
}