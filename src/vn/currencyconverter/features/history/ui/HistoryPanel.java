package vn.currencyconverter.feartures.history.ui;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import vn.currencyconverter.core.theme.AppTheme;

public class HistoryPanel extends JPanel {
    private DefaultTableModel tableModel;
    private final JLabel status = new JLabel("Chưa có giao dịch quy đổi.");
    public void setRecords(java.util.List<vn.currencyconverter.feartures.history.model.ConversionRecord> records) {
        tableModel.setRowCount(0);
        for (var r : records) tableModel.addRow(new Object[]{r.getTimestamp(), r.getCurrencyCode(), r.getAmount(), r.getType(), r.rateApplied(), r.getResultVnd()});
        status.setText("Đã lưu " + records.size() + " giao dịch • Xem nguồn tỷ giá trước khi sử dụng");
    }


    public HistoryPanel() {
        setLayout(new BorderLayout(20, 20));
        AppTheme.stylePage(this);

        add(
            AppTheme.title("Lịch sử quy đổi"),
            BorderLayout.NORTH
        );

        String[] columns = {
            "Thời gian", "Ngoại tệ", "Số tiền",
            "Hình thức", "Tỷ giá", "Kết quả VND"
        };

        DefaultTableModel model =
            new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

        tableModel = model;
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFillsViewportHeight(true);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(
            status,
            BorderLayout.SOUTH
        );
    }
}