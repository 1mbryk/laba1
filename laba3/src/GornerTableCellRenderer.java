import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GornerTableCellRenderer implements TableCellRenderer
{
    String needle = null;
    JPanel panel = null;
    JLabel label = null;
    DecimalFormat formatter = null;

    //////////////////////////
    public GornerTableCellRenderer()
    {
        panel = new JPanel();
        label = new JLabel();
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        formatter = (DecimalFormat) NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
    }


    public void setNeedle(String needle)
    {
        this.needle = needle;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object
            value, boolean isSelected, boolean hasFocus, int row, int col)
    {
        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);
        if (col==1 && needle!=null && needle.equals(formattedDouble)) {
            panel.setBackground(Color.RED);
        } else {
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }
}
