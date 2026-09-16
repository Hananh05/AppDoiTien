package vn.currencyconverter.feartures.home.ui;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import vn.currencyconverter.core.theme.AppTheme;
import vn.currencyconverter.feartures.converter.ui.ConverterPanel;
import vn.currencyconverter.feartures.exchange_rate.ui.ExchangeRatePanel;
import vn.currencyconverter.feartures.history.ui.HistoryPanel;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);

    private final Map<String, JButton> menuButtons =
        new LinkedHashMap<>();

    public MainFrame() {
        setTitle("NHÓM 4 – ỨNG DỤNG QUY ĐỔI TIỀN TỆ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);

        content.add(new ConverterPanel(), "converter");
        content.add(new ExchangeRatePanel(), "rates");
        content.add(new HistoryPanel(), "history");

        add(content, BorderLayout.CENTER);

        showScreen("converter");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.SIDEBAR);
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel(
            "NHÓM 4 – ỨNG DỤNG QUY ĐỔI TIỀN TỆ"
        );

        title.setForeground(Color.WHITE);
        title.setFont(
            new Font(Font.SANS_SERIF, Font.BOLD, 22)
        );

        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(
            new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBackground(AppTheme.SIDEBAR);
        sidebar.setBorder(new EmptyBorder(24, 12, 24, 12));

        JLabel group = new JLabel("NHÓM 4");
        group.setForeground(Color.WHITE);
        group.setFont(
            new Font(Font.SANS_SERIF, Font.BOLD, 26)
        );
        group.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(group);
        sidebar.add(Box.createVerticalStrut(30));

        addMenu(sidebar, "Quy đổi", "converter");
        addMenu(sidebar, "Bảng tỷ giá", "rates");
        addMenu(sidebar, "Lịch sử", "history");

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private void addMenu(
            JPanel sidebar, String text, String screen
    ) {
        JButton button = AppTheme.button(text);
        button.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 48)
        );
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(event -> showScreen(screen));

        menuButtons.put(screen, button);

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(12));
    }

    private void showScreen(String screen) {
        cardLayout.show(content, screen);

        menuButtons.forEach((name, button) -> {
            button.setBackground(
                name.equals(screen)
                    ? AppTheme.PRIMARY
                    : AppTheme.SIDEBAR
            );
        });
    }
}