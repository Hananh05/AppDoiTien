package vn.currencyconverter;

import javax.swing.SwingUtilities;
import vn.currencyconverter.core.theme.AppTheme;
import vn.currencyconverter.features.home.ui.MainFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppTheme.apply();
            new MainFrame().setVisible(true);
        });
    }
}