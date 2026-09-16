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

    private final ConverterPanel converterPanel = new ConverterPanel();
    private final ExchangeRatePanel ratePanel = new ExchangeRatePanel();
    private final HistoryPanel historyPanel = new HistoryPanel();
    private final vn.currencyconverter.feartures.history.service.ConversionHistoryService historyService = new vn.currencyconverter.feartures.history.service.ConversionHistoryService();
    private final java.util.concurrent.ExecutorService historyExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
    private void loadHistory() {
        historyExecutor.submit(() -> {
            try {
                var records = historyService.getAllRecords();
                SwingUtilities.invokeLater(() -> historyPanel.setRecords(records));
            } catch (Exception e) { SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Không đọc được lịch sử: " + e.getMessage())); }
        });
    }
    private void reloadRates() {
        ratePanel.setBusy(true);
        ratePanel.setStatus("Đang đọc exrate.xml...");
        converterPanel.setRates(java.util.List.of());
        new SwingWorker<java.util.List<vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate>, Void>() {
            protected java.util.List<vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate> doInBackground() {
                return new vn.currencyconverter.feartures.exchange_rate.service.VietcombankRateService().getLatestRates();
            }
            protected void done() {
                ratePanel.setBusy(false);
                try {
                    var rates = get();
                    converterPanel.setRates(rates);
                    ratePanel.setRates(rates);
                    ratePanel.setStatus("Dữ liệu mẫu từ exrate.xml • CAD/SGD giả định để kiểm thử");
                } catch (Exception e) {
                    ratePanel.setRates(java.util.List.of());
                    ratePanel.setStatus("Tải thất bại. Kiểm tra exrate.xml rồi bấm đọc lại.");
                    JOptionPane.showMessageDialog(MainFrame.this, e.getCause() == null ? e.getMessage() : e.getCause().getMessage(), "Không tải được tỷ giá", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
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

        content.add(converterPanel, "converter");
        content.add(ratePanel, "rates");
        content.add(historyPanel, "history");

        add(content, BorderLayout.CENTER);

        showScreen("converter");
        ratePanel.onRefresh(this::reloadRates);
        converterPanel.onConverted(record -> historyExecutor.submit(() -> {
            try {
                historyService.saveRecord(record);
                var records = historyService.getAllRecords();
                SwingUtilities.invokeLater(() -> historyPanel.setRecords(records));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Đã tính kết quả nhưng không thể lưu/đọc lịch sử: " + e.getMessage()));
            }
        }));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) { historyExecutor.shutdown(); }
        });
        loadHistory();
        reloadRates();
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