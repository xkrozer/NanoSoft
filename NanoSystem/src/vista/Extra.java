package vista;

import javax.swing.table.DefaultTableModel;

public class Extra {
    public void borrarTabla (DefaultTableModel dtm){
        if(dtm.getRowCount()==1){
            dtm.removeRow(0);
            return;
        }
        int i = 0, total = dtm.getRowCount()-1;
        while (i<total){
            if(dtm.getRowCount()==0)
                return;
            dtm.removeRow(i);
        }
    }
    
}
