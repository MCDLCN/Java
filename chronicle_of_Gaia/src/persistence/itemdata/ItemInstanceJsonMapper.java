package persistence.itemdata;

import main_logic.enums.ItemCode;
import model.items.Item;
import model.items.scrolls.Scroll;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts runtime item instances to persisted JSON and back through DTOs.
 */
public class ItemInstanceJsonMapper {

    private static final Pattern USES_REMAINING_PATTERN =
            Pattern.compile("\"usesRemaining\"\\s*:\\s*(\\d+)");

    /**
     * Converts a runtime item into JSON for persistence.
     *
     * @param item runtime item
     * @return JSON payload for unique items, or null when no instance state is needed
     */
    public String toJson(Item item) {
        if (item == null || item.isStackable()) {
            return null;
        }

        ItemInstanceData data = toData(item);

        if (data == null) {
            return "{}";
        }

        if (data instanceof ScrollInstanceData scrollData) {
            return """
                    {"usesRemaining":%d}
                    """.formatted(scrollData.getUsesRemaining()).trim();
        }

        return "{}";
    }

    /**
     * Converts persisted JSON into typed instance data for a given item code.
     *
     * @param code item code
     * @param json persisted JSON
     * @return typed instance data, or null when no instance state is present
     */
    public ItemInstanceData fromJson(ItemCode code, String json) {
        if (json == null || json.isBlank() || "{}".equals(json.trim())) {
            return null;
        }

        return switch (code) {
            case FIREBALL_SCROLL, LIGHTNING_BOLT_SCROLL ->
                    new ScrollInstanceData(extractInt(json, "usesRemaining"));
            default -> null;
        };
    }

    /**
     * Converts a runtime item into a typed persistence DTO.
     *
     * @param item runtime item
     * @return typed persistence data, or null when not needed
     */
    private ItemInstanceData toData(Item item) {
        if (item instanceof Scroll scroll) {
            return new ScrollInstanceData(scroll.getUsesRemaining());
        }

        return null;
    }

    /**
     * Extracts an integer field from a small JSON payload.
     *
     * @param json JSON payload
     * @param fieldName field to extract
     * @return parsed integer value
     */
    private int extractInt(String json, String fieldName) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Missing integer field '%s' in JSON: %s"
                    .formatted(fieldName, json));
        }

        return Integer.parseInt(matcher.group(1));
    }
}