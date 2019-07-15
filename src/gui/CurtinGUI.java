import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class CurtinGUI {

    //Big ol hashmap
    private HashMap<String,Unit> unitsHM = new HashMap<String,Unit>();
	private JFrame frame;
	private JTextField txtSearch;
	private JButton btnSearch;
	private JList list;
	private JScrollPane scrollPane;
	private JLabel labelClick;
	private JLabel lblUpdatedthJuly;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CurtinGUI window = new CurtinGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CurtinGUI() {
		initialize();
		createHashMap();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Curtin Unit Search");
		frame.setBounds(100, 100, 610, 485);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel labelCurtin = new JLabel("Curtin Unit Search");
		labelCurtin.setBounds(163, 27, 278, 45);
		labelCurtin.setFont(new Font("Britannic Bold", Font.PLAIN, 31));
		frame.getContentPane().add(labelCurtin);
		
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			//Double click to open window of information
			@Override
			public void mouseClicked(MouseEvent click) {
				if (click.getClickCount() == 2) {
					String selectedResult = String.valueOf(list.getModel().getElementAt(list.locationToIndex(click.getPoint())));
					//Get unit object from hashmap based on selected value in JList
					Unit foundUnit = unitsHM.get(selectedResult);
					
					//Open new window with unit information
					UnitWindow uw = new UnitWindow(foundUnit);
				}
								
			}
		});
		
		labelClick = new JLabel("Results will show here:");
		labelClick.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 10));
		labelClick.setBounds(41, 133, 304, 16);
		frame.getContentPane().add(labelClick);
		
		list.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		list.setBounds(40, 158, 512, 260);
		frame.getContentPane().add(list);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 158, 512, 260);
		frame.getContentPane().add(scrollPane);
		
		txtSearch = new JTextField();
		txtSearch.setBounds(81, 85, 316, 25);
		txtSearch.addActionListener(new ActionListener() {
			//When Enter is pressed
			public void actionPerformed(ActionEvent e) {
				String text = txtSearch.getText();
				String[] foundArr;
				DefaultListModel<String> listModel = new DefaultListModel<String>();
				foundArr = searchResults(text);

				int i = 0;
				while (foundArr[i] != null) {
					listModel.addElement(foundArr[i]);
					i++;
				}
				list.setModel(listModel);
				scrollPane.setViewportView(list);
				labelClick.setText("Double-click a result to display its information!");
			}
		});
		frame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		
		btnSearch = new JButton("Search");
		btnSearch.setFocusPainted(false);
		btnSearch.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 13));
		btnSearch.addActionListener(new ActionListener() {
			//When Search is pressed
			public void actionPerformed(ActionEvent e) {
				String text = txtSearch.getText();
				String[] foundArr;
				DefaultListModel<String> listModel = new DefaultListModel<String>();
				foundArr = searchResults(text);

				int i = 0;
				while (foundArr[i] != null) {
					listModel.addElement(foundArr[i]);
					i++;
				}
				list.setModel(listModel);
				scrollPane.setViewportView(list);
				labelClick.setText("Double-click a result to display its information!");
			}
		});
		btnSearch.setBounds(420, 85, 97, 25);
		frame.getContentPane().add(btnSearch);
		
		JLabel labelCredit = new JLabel("by Tom Di Pietro");
		labelCredit.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 7));
		labelCredit.setBounds(359, 56, 62, 16);
		frame.getContentPane().add(labelCredit);
		
		lblUpdatedthJuly = new JLabel("Updated: 16th July 2019");
		lblUpdatedthJuly.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 10));
		lblUpdatedthJuly.setBounds(218, 424, 165, 16);
		frame.getContentPane().add(lblUpdatedthJuly);
	}

	private String[] searchResults(String query)
	{
        int i = 0;
        String[] foundArr = new String[4000];//buffer

        //a way to search partial strings in hashmap
        //creates foundArr - array of results found
        for (String val : unitsHM.keySet())
        {
            if (val.toUpperCase().contains(query.toUpperCase()))
            {
                foundArr[i] = val;
                i++;
            }
        }
        return foundArr;
	}
    //Creates Hash map from Serialized objects
    private void createHashMap()
    {
        String dir = System.getProperty("user.dir");
        System.out.println(dir);
        String os = System.getProperty("os.name");
        int FILES;

        if (os.contains("Windows"))
            FILES = new File(dir+"\\src\\SerializedData\\").listFiles().length;
        else
            FILES = new File(dir+"/src/SerializedData/").listFiles().length;

        for(int i = 1; i <= FILES; i++)
        {
            try 
            {
                FileInputStream fi;
                if (os.contains("Windows"))
                    fi = new FileInputStream(new File(dir+"\\src\\SerializedData\\"+i+".curtin"));
                else
                    fi = new FileInputStream(new File(dir+"/src/SerializedData/"+i+".curtin"));
                
                ObjectInputStream oi = new ObjectInputStream(fi);

                // Read objects
                Unit unit = (Unit) oi.readObject();

                //put each obj in hashmap
                unitsHM.put(unit.getTitle(), unit);

                oi.close();
                fi.close(); 
            }
            catch (FileNotFoundException e) 
            {
                System.out.println("File not found");
            } 
            catch (IOException e) 
            {
                System.out.println("Error initializing stream");
            } 
            catch (ClassNotFoundException e) 
            {
                e.printStackTrace();
            }
        }       
    }	
}
