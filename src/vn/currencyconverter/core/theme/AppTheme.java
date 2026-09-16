package vn.currencyconverter.core.theme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public final class AppTheme {

    public static final Color PRIMARY = Color.decode("#DB2777");
    public static final Color SIDEBAR = Color.decode("#9D174D");
    public static final Color BACKGROUND = Color.decode("#FFF1F5");
    public static final Color LIGHT_PINK = Color.decode("#FCE7F3");
    public static final Color TEXT = Color.decode("#4A1630");

    private AppTheme() {
    }

    public static void apply() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 15);

        String[] names = {
            "Label", "Button", "TextField", "ComboBox",
            "RadioButton", "Table", "TableHeader"
        };

        for (String name : names) {
            UIManager.put(name + ".font", font);
        }

        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Table.selectionBackground", LIGHT_PINK);
        UIManager.put("Table.selectionForeground", TEXT);
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        return label;
    }

    public static JButton button(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(12, 18, 12, 18));
        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        return button;
    }

    public static void stylePage(JPanel panel) {
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
    }
}