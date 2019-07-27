import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JScrollPane;

public class UnitWindow {

    private JPanel contentPane;
    /**
     * Create the frame.
     */
    public UnitWindow(Unit foundUnit) 
    {
        JFrame frame = new JFrame(foundUnit.getTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(100, 100, 639, 485);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);
        
        //Shows unit information from object's toString()
        JTextPane textPane = new JTextPane();
        textPane.setBounds(5, 7, 611, 426);
        textPane.setBorder(UIManager.getBorder("TextArea.border"));
        textPane.setDoubleBuffered(true);
        textPane.setEditable(false);
        textPane.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
        contentPane.add(textPane);
        textPane.setText(foundUnit.toString());
        
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBounds(5, 7, 611, 426);
        contentPane.add(scrollPane);
        frame.setVisible(true);
    }
}