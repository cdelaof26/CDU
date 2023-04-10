package cdu.ui;

import cdu.utils.AppUtils;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author cristopher
 */
public class ColorScrollBarUI extends BasicScrollBarUI {
    private final Color colorDelTrack;
    private final Color colorDeLaBarra;

    public ColorScrollBarUI() {
        this.colorDelTrack = AppUtils.APP_BG_A_COLOR;
        this.colorDeLaBarra = Color.GRAY;

        this.scrollBarWidth = 100;
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = colorDeLaBarra;
        trackColor = colorDelTrack;
        scrollbar.setBorder(null);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return noButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return noButton();
    }

    @Override
    protected void installComponents() {
        scrollBarWidth = 10;
        super.installComponents();
    }

    private JButton noButton() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        b.setMaximumSize(new Dimension(0, 0));

        return b;
    }
}
