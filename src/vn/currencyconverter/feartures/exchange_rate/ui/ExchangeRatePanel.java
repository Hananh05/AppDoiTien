package vn.currencyconverter.feartures.exchange_rate.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import vn.currencyconverter.core.theme.AppTheme;

public class ExchangeRatePanel extends JPanel {

    public ExchangeRatePanel() {
        setLayout(new BorderLayout(20, 20));
        AppTheme.stylePage(this);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        header.add(
            AppTheme.title("Bảng tỷ giá"),
            BorderLayout.CENTER
        );

        JButton refreshButton = AppTheme.button("Cập nhật");
        refreshButton.setEnabled(false);
        refreshButton.setToolTipText("Chưa kết nối API");

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

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        add(
            new JLabel(
                "Chưa tải tỷ giá • Đơn vị: VND / 1 đơn vị ngoại tệ"
            ),
            BorderLayout.SOUTH
        );
    }
}