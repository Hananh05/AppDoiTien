package vn.currencyconverter.features.exchange_rate.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import vn.currencyconverter.core.theme.AppTheme;

public class ExchangeRatePanel extends JPanel {
    private final JButton refreshButton = AppTheme.button("Cập nhật trực tuyến");
    private DefaultTableModel tableModel;
    private final JLabel status = new JLabel("Chưa tải dữ liệu");
    public void onRefresh(Runnable action) { refreshButton.addActionListener(e -> action.run()); }
    public void setBusy(boolean busy) { refreshButton.setEnabled(!busy); }
    public void setStatus(String value) { status.setText(value); }
    public void setRates(java.util.List<vn.currencyconverter.features.exchange_rate.model.ExchangeRate> rates) {
        tableModel.setRowCount(0);
        var allowed = java.util.Set.of("USD", "JPY", "GBP", "CAD", "AUD", "SGD");
        for (var r : rates) if (allowed.contains(r.getCurrencyCode())) tableModel.addRow(new Object[]{r.getCurrencyCode(), r.getCurrencyName(), r.getBuyRate(), r.getTransferRate(), r.getSellRate()});
    }


    public ExchangeRatePanel() {
        setLayout(new BorderLayout(20, 20));
        AppTheme.stylePage(this);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        header.add(
            AppTheme.title("Bảng tỷ giá"),
            BorderLayout.CENTER
        );

        
        refreshButton.setEnabled(true);
        refreshButton.setToolTipText("Lấy tỷ giá trực tiếp từ Vietcombank");

        header.add(refreshButton, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] columns = {
            "Mã", "Ngoại tệ",
            "Mua tiền mặt", "Mua chuyển khoản", "Bán"
        };

        Object[][] data = {
            {"USD", "Đô la Mỹ", "—", "—", "—"},
            {"JPY", "Yên Nhật", "—", "—", "—"},
            {"GBP", "Bảng Anh", "—", "—", "—"},
            {"CAD", "Đô la Canada", "—", "—", "—"},
            {"AUD", "Đô la Úc", "—", "—", "—"},
            {"SGD", "Đô la Singapore", "—", "—", "—"}
        };

        DefaultTableModel model =
            new DefaultTableModel(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

        tableModel = model;
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        add(
            status,
            BorderLayout.SOUTH
        );
    }
}