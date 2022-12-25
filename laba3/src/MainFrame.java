import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainFrame extends JFrame
{
    // constants screen size
    static final int WIDTH = 700;
    static final int HEIGHT = 500;
    // Массив коэффициентов многочлена
    Double[] coeffs;

    // object dialog menu for choice files
    JFileChooser file_chooser = null;

    // Menu items
    JMenuItem save_to_text_menu_item;
    JMenuItem save_to_graphics_menu_item;
    JMenuItem search_value_menu_item;

    // fields to output/input variables
    JTextField text_field_begin;
    JTextField text_field_end;
    JTextField text_field_step;
    Box hor_box_result;

    GornerTableCellRenderer renderer = new GornerTableCellRenderer();

    GornerTableModel data;

    public MainFrame(Double[] coeffs)
    {
        super("Tabulating a polynomial on a segment according to the Gorner scheme");
        this.coeffs = coeffs;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH) /2,
                (kit.getScreenSize().height - HEIGHT) / 2);

        // set menu as main menu of application
        JMenuBar menu_bar = new JMenuBar();
        setJMenuBar(menu_bar);

        // Create and add menu option "File"
        JMenu file_menu = new JMenu("File");
        menu_bar.add(file_menu);
        // Create and add menu option "Table:
        JMenu table_menu = new JMenu("Table");
        menu_bar.add(table_menu);

        // create new option to save table in text file
        Action save_to_text_action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(file_chooser == null)
                {
                    file_chooser = new JFileChooser();
                    file_chooser.setCurrentDirectory(new File("."));
                }

                // show dialog window
                if(file_chooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION)
                {
                    saveToTextFile(file_chooser.getSelectedFile());
                }
            }
        };
        save_to_text_menu_item = file_menu.add(save_to_text_action);
        save_to_text_menu_item.setEnabled(false);

        Action save_to_graphics_action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (file_chooser == null)
                {
                    file_chooser = new JFileChooser();
                    file_chooser.setCurrentDirectory(new File("."));
                }

                if(file_chooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION)
                {
//                    saveToGraphicFile(file_chooser.getSelectedFile());
                }
            }
        };
        save_to_graphics_menu_item = file_menu.add(save_to_graphics_action);
        save_to_graphics_menu_item.setEnabled(false);

        Action search_value_action = new AbstractAction("Search polynomial value")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String value = JOptionPane.showInputDialog(MainFrame.this,
                        "Input value for search",
                        "Value Search", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }
        };
        search_value_menu_item = table_menu.add(search_value_action);
        search_value_menu_item.setEnabled(false);

        JButton calc_button = new JButton("Calculate");
        calc_button.addActionListener(e -> {
            try
            {
                Double begin = Double.parseDouble(text_field_begin.getText());
                Double end = Double.parseDouble(text_field_end.getText());
                Double step = Double.parseDouble(text_field_step.getText());

                data = new GornerTableModel(MainFrame.this.coeffs, begin, end, step);

                JTable table = new JTable();
                // set as cells renderer for Double developed renderer
                table.setDefaultRenderer(Double.class, renderer);
                table.setRowHeight(30);
                hor_box_result.removeAll();
                hor_box_result.add(new JScrollPane(table));
                getContentPane().validate();
                save_to_text_menu_item.setEnabled(true);
                save_to_graphics_menu_item.setEnabled(true);
                search_value_menu_item.setEnabled(true);

            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Float number format error", "Incorrect format of number"
                , JOptionPane.WARNING_MESSAGE);
            }

        });

        JButton reset_button = new JButton("Clear");
        reset_button.addActionListener(e -> {
            text_field_begin.setText("0.0");
            text_field_end.setText("1.0");
            text_field_step.setText("0,1");
            hor_box_result.removeAll();
            save_to_text_menu_item.setEnabled(false);
            save_to_graphics_menu_item.setEnabled(false);
            search_value_menu_item.setEnabled(false);

            getContentPane().validate();

        });

        Box hor_box_buttons = Box.createHorizontalBox();
        hor_box_buttons.setBorder(BorderFactory.createBevelBorder(1));
        hor_box_buttons.add(Box.createHorizontalGlue());
        hor_box_buttons.add(calc_button);
        hor_box_buttons.add(Box.createHorizontalStrut(30));
        hor_box_buttons.add(Box.createVerticalGlue());
        hor_box_buttons.setPreferredSize(new Dimension(
            Double.valueOf(hor_box_buttons.getMaximumSize().getWidth()).intValue(),
            Double.valueOf(hor_box_buttons.getMinimumSize().getHeight()).intValue()*2));

    }

    protected void saveToTextFile(File selected_file)
    {
        try
        {
            PrintStream out = new PrintStream(selected_file);
            out.println("Result");
            out.println("Polynomial: ");
            for (int i = 0; i < coeffs.length; ++i)
            {
                out.println(coeffs[i] + "*X^" + (coeffs.length -i -1));
                if (i != coeffs.length - 1)
                    out.print("+");
            }
            out.println();
            out.println("Interval from " + data.getBegin() + " to " + data.getEnd()
            + "With step " + data.getStep());
            out.println("================================================");
            for (int i = 0; i<data.getRowCount(); i++)
            {
                out.println("Значение в точке " + data.getValueAt(i,0) +
                        " равно " + data.getValueAt(i,1));
            }
            out.close();
        }
        catch (FileNotFoundException ex) { }

    }

    protected void saveToGraphicsFile(File selectedFile)
    {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i<data.getRowCount(); i++)
            {
                out.writeDouble((Double)data.getValueAt(i,0));
                out.writeDouble((Double)data.getValueAt(i,0));
            }
            out.close();
        } catch (Exception e) {
        }
    }

}