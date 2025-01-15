import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class MenuUtils {
    public void showMenu() {
        // Opciones de monedas
        String[] currencies = APIHandler.getAvailableCurrencies(); // Obtiene monedas desde la API
        Arrays.sort(currencies); // Ordena las monedas alfabéticamente

        // Menús desplegables
        JComboBox<String> fromCurrency = new JComboBox<>(currencies);
        JComboBox<String> toCurrency = new JComboBox<>(currencies);

        // Campo de texto para la cantidad
        JTextField amountField = new JTextField(10);

        // Paneles
        JPanel openingPanel = createPanel();
        JPanel currencyFromPanel = createCurrencyPanel("De: ", fromCurrency);
        JPanel currencyToPanel = createCurrencyPanel("A:   ", toCurrency);
        JPanel amountPanel = createAmountPanel(amountField);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(openingPanel);
        mainPanel.add(currencyFromPanel);
        mainPanel.add(currencyToPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(amountPanel);

        // Mostrar el pop-up inicial
        int result = JOptionPane.showConfirmDialog(
                null,
                mainPanel,
                "Conversor de Monedas",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();
            String amountText = amountField.getText();

            // Verificación para evitar valores nulos
            if (from == null || to == null) {
                JOptionPane.showMessageDialog(null, "Por favor, verifica las monedas seleccionadas.", "Error", JOptionPane.ERROR_MESSAGE);
                showMenu(); // Reinicia el menú
                return;
            }

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(null, "No puedes convertir la misma moneda.", "Error", JOptionPane.ERROR_MESSAGE);
                showMenu(); // Reinicia el menú
                return;
            }

            try {
                double amount = Double.parseDouble(amountText.replace(",", "."));
                Converter converter = new Converter();
                double resultAmount = converter.convert(from, to, amount);

                // Formatea el resultado
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                symbols.setDecimalSeparator('.');
                DecimalFormat df = new DecimalFormat("#.##", symbols);
                df.format(resultAmount);

                // Ventana de resultados
                showResultWindow(amount, from, resultAmount, to);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                showMenu(); // Reiniciar el menú
            }
        } else {
            System.out.println("Operación cancelada o finalizada.");
        }
    }

    private void showResultWindow(double amount, String from, double resultAmount, String to) {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String message = String.format("Convertiste %.2f %s a %.2f %s.", amount, from, resultAmount, to);
        JLabel resultLabel = new JLabel("<html>" + message.replaceAll("\n", "<br>") + "</html>");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultPanel.add(resultLabel);
        resultPanel.add(Box.createVerticalStrut(10));

        JOptionPane.showMessageDialog(
                null,
                resultPanel,
                "Resultado de la Conversión",
                JOptionPane.PLAIN_MESSAGE
        );

        showMenu(); // Reinicia el menú después de mostrar el resultado
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("  " + "Selecciona el tipo de moneda:"));
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private JPanel createCurrencyPanel(String label, JComboBox<String> currencyComboBox) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel(label));
        panel.add(currencyComboBox);
        panel.add(Box.createVerticalStrut(20));
        return panel;
    }

    private JPanel createAmountPanel(JTextField amountField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 30, 10, 30));
        panel.add(new JLabel("Cantidad a convertir:"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(amountField);
        amountField.setPreferredSize(new Dimension(200, 40));
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                amountField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return panel;
    }
}
