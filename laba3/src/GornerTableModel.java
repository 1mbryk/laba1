import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel
{
    Double[] coeffs = null;
    double begin = 0.0;
    double end = 0.0;
    double step= 0.0;

    public GornerTableModel(Double[] coeffs, double begin, double end, double step)
    {
        this.coeffs = coeffs;
        this.begin = begin;
        this.end = end;
        this.step = step;
    }

    public double getBegin() { return begin; }
    public double getEnd() { return end; }
    public double getStep() {return step; }


    @Override
    public int getColumnCount()
    {
        return 3;
    }

    @Override
    public int getRowCount()
    {
        return 0;
    }

    @Override
    public Object getValueAt(int row, int col)
    {
        double x = begin + step*row;
        if(col==0)
            return x;
        else
        {
            double result = 0d;
            // Если запрашивается значение 2-го столбца, то это значение многочлена
            if (col == 1)
            {
                result = coeffs[0];
                for (int i = 0; i < coeffs.length - 1; i++) {
                    result = result * x + coeffs[i + 1];
                }
                return result;
            }
            else
            {
                // 3-й столбец
                result = coeffs[0];
                Boolean resultBoolean;
                for (int i = 0; i < coeffs.length - 1; i++) {
                    result = result * x + coeffs[i + 1];
                }
                String temp = Double.toString(result);
                char[] arr = temp.toCharArray();

                if ( arr[2] == '0') {
                    resultBoolean = true;
                } else {
                    resultBoolean = false;
                }
                return resultBoolean;
            }
        }

    }

    @Override
    public Class<?> getColumnClass(int arg0)
    {
        return Double.class;
    }

    @Override
    public String getColumnName(int arg0)
    {
        if(arg0 == 0)
        {
            return "Argument value";
        }
        else
        {
            return "Polynomial value";
        }
    }

}
