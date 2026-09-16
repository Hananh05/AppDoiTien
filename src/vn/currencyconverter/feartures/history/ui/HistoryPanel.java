package vn.currencyconverter.feartures.history.ui;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import vn.currencyconverter.core.theme.AppTheme;

public class HistoryPanel extends JPanel {

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

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFillsViewportHeight(true);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(
            new JLabel("Chưa có giao dịch quy đổi."),
            BorderLayout.SOUTH
        );
    }
}