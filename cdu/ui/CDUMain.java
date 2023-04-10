package cdu.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 *
 * @author cristopher
 */
public class CDUMain extends JFrame {

    public CDUMain() throws HeadlessException {
        initUI();
    }
    
    public final void initUI() {
        this.setSize(new Dimension(1000, 630));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
}
