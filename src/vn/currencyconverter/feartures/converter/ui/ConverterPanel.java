package vn.currencyconverter.feartures.converter.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import vn.currencyconverter.core.theme.AppTheme;

public class ConverterPanel extends JPanel {

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
            new JLabel("Nguồn: Vietcombank • Chưa tải tỷ giá"),
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