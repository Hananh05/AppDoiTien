package vn.currencyconverter.features.converter.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import vn.currencyconverter.core.theme.AppTheme;

public class ConverterPanel extends JPanel {
    private final JLabel sourceStatus = new JLabel("Chưa có tỷ giá trực tuyến");
    public void setSourceStatus(String text) { sourceStatus.setText(text); }
    private java.util.Map<String, vn.currencyconverter.features.exchange_rate.model.ExchangeRate> rates = new java.util.HashMap<>();
    private java.util.function.Consumer<vn.currencyconverter.features.history.model.ConversionRecord> onConverted = record -> {};
    public void onConverted(java.util.function.Consumer<vn.currencyconverter.features.history.model.ConversionRecord> action) { onConverted = action; }
    public void setRates(java.util.List<vn.currencyconverter.features.exchange_rate.model.ExchangeRate> values) {
        rates.clear();
        for (var value : values) rates.put(value.getCurrencyCode(), value);
        convertButton.setEnabled(!rates.isEmpty());
        resultLabel.setText("— VND");
    }
    private void convert() {
        try {
            if (!convertButton.isEnabled()) return;
            String input = amountField.getText().trim();
            if (!input.matches("[0-9]+([.,][0-9]+)?")) throw new IllegalArgumentException("Nhập số tiền, ví dụ 100 hoặc 100,50; không dùng dấu phân cách hàng nghìn.");
            var amount = new java.math.BigDecimal(input.replace(',', '.'));
            String code = currencyBox.getSelectedItem().toString().substring(0,3);
            var rate = rates.get(code);
            if (rate == null) throw new IllegalArgumentException("Nguồn chưa có tỷ giá " + code + ". Hãy cập nhật hoặc chọn ngoại tệ khác.");
            var price = transferRadio.isSelected() ? rate.getTransferRate() : rate.getBuyRate();
            if (price == null) throw new IllegalArgumentException("Không có tỷ giá cho hình thức đã chọn.");
            var value = new vn.currencyconverter.features.converter.service.CurrencyConverterService().convertToVnd(amount, price);
            resultLabel.setText(java.text.NumberFormat.getNumberInstance(java.util.Locale.forLanguageTag("vi-VN")).format(value) + " VND");
            var type = transferRadio.isSelected() ? vn.currencyconverter.features.exchange_rate.model.RateType.TRANSFER_BUY : vn.currencyconverter.features.exchange_rate.model.RateType.BUY;
            onConverted.accept(new vn.currencyconverter.features.history.model.ConversionRecord(java.time.LocalDateTime.now(), code, amount, price, value, type));
        } catch (IllegalArgumentException e) {
            resultLabel.setText("— VND");
            JOptionPane.showMessageDialog(this, e.getMessage(), "Kiểm tra dữ liệu", JOptionPane.WARNING_MESSAGE);
        }
    }


    private final JTextField amountField = new JTextField();

    private final JComboBox<String> currencyBox =
        new JComboBox<>(new String[] {
            "USD – Đô la Mỹ",
            "JPY – Yên Nhật",
            "GBP – Bảng Anh",
            "CAD – Đô la Canada",
            "AUD – Đô la Úc",
            "SGD – Đô la Singapore"
        });

    private final JRadioButton transferRadio =
        new JRadioButton("Chuyển khoản", true);

    private final JRadioButton cashRadio =
        new JRadioButton("Tiền mặt");

    private final JButton convertButton =
        AppTheme.button("QUY ĐỔI SANG VND");

    private final JLabel resultLabel =
        new JLabel("— VND", SwingConstants.CENTER);

    public ConverterPanel() {
        convertButton.addActionListener(e -> convert());
        amountField.addActionListener(e -> convert());
        currencyBox.addActionListener(e -> resultLabel.setText("— VND"));
        transferRadio.addActionListener(e -> resultLabel.setText("— VND"));
        cashRadio.addActionListener(e -> resultLabel.setText("— VND"));
        amountField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { resultLabel.setText("— VND"); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { resultLabel.setText("— VND"); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { resultLabel.setText("— VND"); }
        });
        setLayout(new BorderLayout(20, 20));
        AppTheme.stylePage(this);

        add(
            AppTheme.title("Quy đổi ngoại tệ"),
            BorderLayout.NORTH
        );

        JPanel body = new JPanel(new GridLayout(1, 2, 20, 0));
        body.setOpaque(false);
        body.add(createForm());
        body.add(createResult());

        add(body, BorderLayout.CENTER);

        add(
            sourceStatus,
            BorderLayout.SOUTH
        );
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        amountField.setPreferredSize(new Dimension(200, 42));
        currencyBox.setPreferredSize(new Dimension(200, 42));

        JTextField destination =
            new JTextField("VND – Việt Nam đồng");

        destination.setEditable(false);
        destination.setPreferredSize(new Dimension(200, 42));

        ButtonGroup group = new ButtonGroup();
        group.add(transferRadio);
        group.add(cashRadio);

        transferRadio.setOpaque(false);
        cashRadio.setOpaque(false);

        JPanel options = new JPanel(new GridLayout(2, 1));
        options.setOpaque(false);
        options.add(transferRadio);
        options.add(cashRadio);

        convertButton.setEnabled(false);
        convertButton.setToolTipText(
            "Cần tải tỷ giá trước khi quy đổi"
        );

        Component[] components = {
            new JLabel("Số tiền cần đổi"),
            amountField,
            new JLabel("Từ ngoại tệ"),
            currencyBox,
            new JLabel("Đổi sang"),
            destination,
            new JLabel("Hình thức bán ngoại tệ"),
            options,
            convertButton
        };

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(6, 0, 6, 0);

        for (int i = 0; i < components.length; i++) {
            constraints.gridy = i;
            form.add(components[i], constraints);
        }

        return form;
    }

    private JPanel createResult() {
        JPanel result = new JPanel(new GridBagLayout());
        result.setBackground(AppTheme.LIGHT_PINK);
        result.setBorder(new EmptyBorder(20, 20, 20, 20));

        resultLabel.setFont(
            new Font(Font.SANS_SERIF, Font.BOLD, 36)
        );

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(12, 0, 12, 0);

        constraints.gridy = 0;
        result.add(
            new JLabel("SỐ TIỀN VND ƯỚC TÍNH"),
            constraints
        );

        constraints.gridy = 1;
        result.add(resultLabel, constraints);

        constraints.gridy = 2;
        result.add(
            new JLabel("Kết quả sẽ hiển thị tại đây"),
            constraints
        );

        return result;
    }
}