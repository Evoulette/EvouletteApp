package com.example.evouletteapp.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketContent {
    public static final List<TicketItem> ITEMS = new ArrayList<TicketItem>();


    public static final Map<String, TicketItem> ITEM_MAP = new HashMap<String, TicketItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createTicketItem(i));
        }
    }

    private static void addItem(TicketItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static TicketItem createTicketItem(int position) {
        return new TicketItem(String.valueOf(position), "Item " + position, "Date" +position);
    }

   /* private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

    /**
     * A dummy item representing a piece of content.
     */
    public static class TicketItem {
        public final String id;
        public final String content;
        public final String date;

        public TicketItem(String id, String content, String date) {
            this.id = id;
            this.content = content;
            this.date = date;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
