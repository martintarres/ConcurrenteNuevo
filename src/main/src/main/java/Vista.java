import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class Vista extends JFrame {
    private Politica p;

    private JPanel contentPane;
    JLabel cantPiezasA;
    JLabel cantPiezasB;
    JLabel cantPiezasC;


    /**
     * Launch the application.
     */


    /**
     * Create the frame.
     */
    public Vista(Politica p) {
        this.p = p;

        setResizable(false);
        setAlwaysOnTop(true);
        setTitle("Sistema de Manufacturacion Computarizada");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 213, 361);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblCantidadDePiezas = new JLabel("Cantidad de Piezas Producidas:");
        lblCantidadDePiezas.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblCantidadDePiezas.setBounds(10, 40, 186, 38);
        contentPane.add(lblCantidadDePiezas);

        JPanel panel = new JPanel();
        panel.setBounds(10, 126, 186, 29);
        contentPane.add(panel);
        panel.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel lblPiezasA = new JLabel("Piezas A :");
        lblPiezasA.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblPiezasA);

        cantPiezasA = new JLabel(""+p.PiezaA);
        cantPiezasA.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(cantPiezasA);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(10, 188, 186, 29);
        contentPane.add(panel_1);
        panel_1.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel lblPiezasB = new JLabel("Piezas B :");
        lblPiezasB.setHorizontalAlignment(SwingConstants.CENTER);
        panel_1.add(lblPiezasB);

        cantPiezasB = new JLabel(""+p.PiezaB);
        cantPiezasB.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_1.add(cantPiezasB);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 255, 186, 29);
        contentPane.add(panel_2);
        panel_2.setLayout(new GridLayout(0, 2, 0, 0));

        JLabel lblPiezasC = new JLabel("Piezas C");
        lblPiezasC.setHorizontalAlignment(SwingConstants.CENTER);
        panel_2.add(lblPiezasC);

        cantPiezasC = new JLabel(""+p.PiezaC);
        cantPiezasC.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_2.add(cantPiezasC);
    }

    public void repintar(){
        cantPiezasA.setText(""+p.PiezaA);
        cantPiezasB.setText(""+p.PiezaB);
        cantPiezasC.setText(""+p.PiezaC);


    }
}