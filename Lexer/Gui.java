package compiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import lexer.Lexer;
import lexer.Token;

public class Gui extends JFrame implements ActionListener {
  
  private JTextArea console;
  private JTextArea editor;
  private JTable tokensTable;  
  private JMenuItem menuOpen = new JMenuItem("Open ...");    
  private JMenuItem menuLexer = new JMenuItem("Lexer");  
  
  private void writeConsole(String msg) {
    console.append(msg+"\n");
  }

  private void writeEditor(String msg) {
    editor.append(msg+"\n");
  }
  
  private void writeTokensTable(int line, String token, String word) {
   ((DefaultTableModel)tokensTable.getModel()).addRow(new Object[]{String.format("%04d", line), token, word});  
  }
  
  private boolean loadFile(String file) throws FileNotFoundException, IOException {       
    String line;
    BufferedReader br = new BufferedReader(new FileReader(file));    
    writeConsole("Reading " + file + "");
    line = br.readLine();  
    while (line != null ) {     
      writeEditor(line);       
      line = br.readLine();  
    }
    writeConsole("File loaded.");
    br.close();
    return true;
  }
  
  private void clearTokenTable() {   
    int ta = ((DefaultTableModel)tokensTable.getModel()).getRowCount();
    for(int i=0; i< ta; i++)
     ((DefaultTableModel)tokensTable.getModel()).removeRow(0); 
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (menuOpen.equals(e.getSource())) {
      JFileChooser fc = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
      fc.setFileFilter(filter);    
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();   
        console.setText("");
        editor.setText(""); 
        clearTokenTable();
        try {
          loadFile(file.getAbsolutePath());      
        } catch (IOException ex) {
          writeConsole(ex.toString());
        } 
      }
    } else if (menuLexer.equals(e.getSource())) {
      clearTokenTable();
      console.setText("");
      // lexical analysis
      Lexer lex = new Lexer (editor.getText());     
      lex.run();
      Vector<Token> tokens = lex.getTokens(); 
      // show token in a table
      for (Token token1 : tokens) {
        int line = token1.getLine();
        String token = token1.getToken();
        String word = token1.getWord();
        writeTokensTable(line, token,word );            
      }
      // counting errors
      int errors = 0;
      for (Token token : tokens) {
        if (token.getToken().equals("ERROR")) {
          errors++;      
        }
      }
      // show stats on on the console
      writeConsole(tokens.size() + " strings found in " + tokens.get(tokens.size()-1).getLine() + " lines,");
      writeConsole(errors + " strings do not match any rule");
    }    
  }
  
  private void createMenu() {    
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu("File");
    JMenu menuRun = new JMenu("Run");  
    menuOpen.addActionListener(this);
    menuLexer.addActionListener(this);
    menuFile.add(menuOpen);
    menuRun.add(menuLexer);
    menuBar.add(menuFile);   
    menuBar.add(menuRun);  
    setJMenuBar(menuBar);
  }
  
  private void createGUI() {
    TitledBorder panelTitle;     
    setLayout(new BorderLayout());
    JPanel topPanel = new JPanel(new GridLayout(1,2));
    JPanel downPanel = new JPanel(new GridLayout(1,1));
    JPanel tokenPanel = new JPanel(new GridLayout(1,1));
    JPanel screenPanel = new JPanel(new GridLayout(1,1));
    JPanel consolePanel = new JPanel(new GridLayout(1,1));
    //screen
    panelTitle = BorderFactory.createTitledBorder("Source Code");
    screenPanel.setBorder(panelTitle);  
    editor = new JTextArea();
    editor.setEditable(true);
    JScrollPane scrollScreen = new JScrollPane(editor); 
    screenPanel.add(scrollScreen);
    //Tokens
    panelTitle = BorderFactory.createTitledBorder("Lexical Analysis");
    tokenPanel.setBorder(panelTitle);
    DefaultTableModel modelRegistry = new DefaultTableModel(); 
    tokensTable = new JTable(modelRegistry); 
    tokensTable.setShowGrid(true);
    tokensTable.setGridColor(Color.LIGHT_GRAY);
    tokensTable.setAutoCreateRowSorter(true);
    modelRegistry.addColumn("line"); 
    modelRegistry.addColumn("token");  
    modelRegistry.addColumn("string or word");
    JScrollPane scrollRegistry = new JScrollPane(tokensTable);
    tokensTable.setFillsViewportHeight(true);
    tokenPanel.add(scrollRegistry);
    tokensTable.setEnabled(false);
    //console
    panelTitle = BorderFactory.createTitledBorder("Console");
    consolePanel.setBorder(panelTitle);  
    console = new JTextArea();
    console.setEditable(false);
    console.setBackground(Color.black);
    console.setForeground(Color.white);    
    JScrollPane scrollConsole = new JScrollPane(console); 
    consolePanel.add(scrollConsole);
    //main frame
    topPanel.add(screenPanel);
    topPanel.add(tokenPanel);      
    downPanel.add(consolePanel);
    downPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/4 ));
    add(topPanel, BorderLayout.CENTER);
    add(downPanel, BorderLayout.SOUTH);
  }
  
  public Gui(String title) throws IOException {
    super(title);
    //file = null;
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
    }    
    Dimension dim = getToolkit().getScreenSize();
    setSize(3 * dim.width  / 4, 3 * dim.height / 4);
    setLocation((dim.width - getSize().width)/2, (dim.height - getSize().height)/2);    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    createMenu();
    createGUI();
  }
  
  public static void main(String[] args) throws FileNotFoundException, IOException {
    Gui gui = new Gui("Lexer");
    gui.setVisible(true);
  }
  
}
