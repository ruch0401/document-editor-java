import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Composite Design Pattern. The method is initialised and performed operation on in the PSVM
 */
interface BaseComposite {
    Font courier20 = new Font("Courier", Font.BOLD, 20);
    Font courier36 = new Font("Helvetica", Font.BOLD, 36);
}

class Base implements BaseComposite {
    JFrame frame;
    JPanel north, editPane, status, image, tools, menu, east, west, padding1, padding2, padding_bottom;
    JLabel message;
    JLabel background;
    JLabel title;
    JTextPane editarea2;
    int flagSave = 0;
    File fileSave = null;

    Base() {
        frame = new JFrame("Document Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        background = new JLabel(new ImageIcon("")); // Use this to add custom background
        frame.setContentPane(background);
        frame.setLayout(new BorderLayout());
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public JPanel addComposite() {
        // Creating required JPanels and JLabels and setting text colors and background colors
        north = new JPanel(new BorderLayout());
        editPane = new JPanel();
        status = new JPanel(new BorderLayout());
        image = new JPanel(new FlowLayout());
        tools = new JPanel(new BorderLayout());
        east = new JPanel();
        west = new JPanel();
        padding1 = new JPanel();
        padding2 = new JPanel();
        menu = new JPanel(new BorderLayout());
        message = new JLabel("Status will be displayed here.");
        message.setForeground(Color.BLACK);
        message.setFont(courier20);
        padding_bottom = new JPanel();
        title = new JLabel("Document Editor");
        title.setForeground(Color.BLACK);
        title.setFont(courier36);

        // Adding JPanel, JLabels to the JFrame with the default Border Layout
        frame.add(north, BorderLayout.NORTH);
        frame.add(editPane, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.add(east, BorderLayout.EAST);
        frame.add(west, BorderLayout.WEST);
        north.add(image, BorderLayout.NORTH);
        north.add(menu, BorderLayout.SOUTH);
        image.add(title);
        status.add(padding_bottom, BorderLayout.WEST);
        status.add(message, BorderLayout.CENTER);
        menu.add(padding1, BorderLayout.WEST);
        menu.add(padding2, BorderLayout.EAST);
        menu.add(tools, BorderLayout.CENTER);

        // Setting the layout of the 'tools' JPanel
        tools.setLayout(new BoxLayout(tools, BoxLayout.X_AXIS));

        // Setting preferred size of each component
        message.setPreferredSize(new Dimension(600, 200));
        padding_bottom.setPreferredSize(new Dimension(800, 200));
        image.setPreferredSize(new Dimension(500, 100));
        tools.setPreferredSize(new Dimension(500, 100));
        editPane.setPreferredSize(new Dimension(600, 150));
        status.setPreferredSize(new Dimension(100, 100));
        east.setPreferredSize(new Dimension(200, 100));
        west.setPreferredSize(new Dimension(200, 100));
        padding1.setPreferredSize(new Dimension(200, 100));
        padding2.setPreferredSize(new Dimension(200, 100));

        // Setting opacity of each component
        padding_bottom.setOpaque(false);
        menu.setOpaque(false);
        tools.setOpaque(false);
        east.setOpaque(false);
        west.setOpaque(false);
        status.setOpaque(false);
        padding1.setOpaque(false);
        padding2.setOpaque(false);
        image.setOpaque(false);
        editPane.setOpaque(false);
        north.setOpaque(false);

        return tools;
    }

    public JTextPane addLeaf() {
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        menubar.add(file);
        editarea2 = new JTextPane();
        editarea2.setMargin(new Insets(50, 50, 50, 50));
        editarea2.setPreferredSize(new Dimension(600, 600));
        JScrollPane scroll = new JScrollPane(editarea2);

        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        open.addActionListener(ae -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file1 = fileChooser.getSelectedFile();
                try {
                    FileInputStream fis = new FileInputStream(file1.getAbsolutePath());
                    BufferedReader input = new BufferedReader(new InputStreamReader(new
                            FileInputStream(file1)));
                    editarea2.read(input, "READING FILE :-)");
                    fis.close();
                } catch (Exception ex) {
                    System.out.println("problem accessing file" + file1.getAbsolutePath());
                }
            } else {
                System.out.println("File access cancelled by user.");
            }
        });

        JMenuItem neww = new JMenuItem("New");
        file.add(neww);
        neww.addActionListener(ae -> {
            if (editarea2.getText().isEmpty()) {
                editarea2.setText("");
            } else {
                save();
                editarea2.setText("");
                flagSave = 0;
                fileSave = null;
            }
        });

        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        save.addActionListener(ae -> {
            if (flagSave == 1) {
                BufferedWriter outFile = null;
                try {
                    outFile = new BufferedWriter(new FileWriter(fileSave));
                } catch (IOException ex) {
                    Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    editarea2.write(outFile);
                } catch (IOException ex) {
                    Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (outFile != null) {
                        try {
                            outFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                save();
            }
        });

        JMenuItem saveas = new JMenuItem("Save As");
        file.add(saveas);
        saveas.addActionListener(ae -> save());
        JMenuItem quit = new JMenuItem("Quit");
        file.add(quit);
        quit.addActionListener(ae -> frame.dispose());

        JMenu edit = new JMenu("Edit");
        menubar.add(edit);

        JMenuItem undo = new JMenuItem("Undo");
        edit.add(undo);
        undo.addActionListener(ae -> {
        });
        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());

        cut.setText("Cut");
        edit.add(cut);
        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());

        copy.setText("Copy");
        edit.add(copy);
        JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());

        paste.setText("Paste");
        edit.add(paste);
        JRootPane root = image.getRootPane();
        root.setJMenuBar(menubar);
        JRootPane root2 = editPane.getRootPane();
        root2.getContentPane().add(scroll);
        editPane.setVisible(true);
        return editarea2;
    }

    // Method to perform the save as operation on the text
    public void save() {
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text File", "txt");
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(frame);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            editarea2.write(outFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/*
 * Command Design Pattern. The method is initialised and performed operation on in the main method
 */
interface ButtonsCommand {
    void click();

    Font f = new Font("Courier", Font.BOLD, 20);
    Dimension dimen = new Dimension(200, 200);
}

class FontSize implements ButtonsCommand {
    JButton incr, decr;
    JPanel tools, p1;
    JTextPane pane;
    JLabel l1, label;
    static int a;

    FontSize(JPanel panel, JTextPane pane) {
        p1 = new JPanel();
        p1.setPreferredSize(dimen);
        p1.setOpaque(false);
        this.tools = panel;
        this.pane = pane;
        incr = new JButton("Up");
        incr.setAlignmentX(Component.CENTER_ALIGNMENT);
        decr = new JButton("Down");
        decr.setAlignmentX(Component.CENTER_ALIGNMENT);
        l1 = new JLabel("Size ");
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);
        l1.setForeground(Color.BLACK);
        label = new JLabel("\t\t\t\t");
        StyledDocument doc = (StyledDocument) pane.getDocument();
        Style style = doc.addStyle("StyleName", null);
        a = StyleConstants.getFontSize(style);
    }

    @Override
    public void click() {
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        l1.setFont(f);
        p1.add(l1);
        p1.add(incr, Component.CENTER_ALIGNMENT);
        p1.add(decr, Component.CENTER_ALIGNMENT);
        p1.add(label);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp, BorderLayout.WEST);
        temp.setBackground(Color.ORANGE);
        tools.add(p1);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        p1.setBackground(Color.ORANGE);

        incr.addActionListener(ae -> {
            Font font = pane.getFont();
            float size = font.getSize() + 2.0f;
            pane.setFont(font.deriveFont(size));
        });

        decr.addActionListener(ae -> {
            Font font = pane.getFont();
            float size = font.getSize() - 2.0f;
            pane.setFont(font.deriveFont(size));
        });
    }
}

class FontStyle implements ButtonsCommand {
    JButton bold, italics, underline;
    JPanel tools, p2;
    JTextPane pane;
    JLabel l2;

    FontStyle(JPanel panel, JTextPane pane) {
        p2 = new JPanel();
        p2.setOpaque(false);
        this.tools = panel;
        this.pane = pane;
        bold = new JButton("Bold");
        bold.setAlignmentX(Component.CENTER_ALIGNMENT);
        italics = new JButton("Italics");
        italics.setAlignmentX(Component.CENTER_ALIGNMENT);
        underline = new JButton("Underline");
        underline.setAlignmentX(Component.CENTER_ALIGNMENT);
        l2 = new JLabel("Style ");
        l2.setForeground(Color.BLACK);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        l2.setFont(f);
        p2.add(l2);
        p2.add(bold);
        p2.add(italics);
        p2.add(underline);
        tools.add(p2);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        temp.setBackground(Color.ORANGE);
        tools.add(temp);
        p2.setBackground(Color.ORANGE);

        bold.addActionListener(ae -> {
            StyledDocument doc = (StyledDocument) pane.getDocument();
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setBold(style, true);
            try {
                String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                pane.setText("");
                doc.insertString(0, text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        italics.addActionListener(ae -> {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setItalic(style, true);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        );

        underline.addActionListener( ae -> {
                StyledDocument doc = (StyledDocument) pane.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setUnderline(style, true);
                try {
                    String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                    pane.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        );
    }
}

class FontColor implements ButtonsCommand {
    JPanel tools, p3;
    JTextPane pane;
    JButton red, green, blue;
    JLabel l3;

    FontColor(JPanel tools, JTextPane pane) {
        this.pane = pane;
        this.tools = tools;
        p3 = new JPanel();
        p3.setOpaque(false);

        red = new JButton("Red");
        red.setAlignmentX(Component.CENTER_ALIGNMENT);

        green = new JButton("Green");
        green.setAlignmentX(Component.CENTER_ALIGNMENT);

        blue = new JButton("Blue");
        blue.setAlignmentX(Component.CENTER_ALIGNMENT);

        l3 = new JLabel("Color");
        l3.setForeground(Color.BLACK);
        l3.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        l3.setFont(f);
        p3.add(l3);
        p3.add(red);
        p3.add(green);
        p3.add(blue);
        tools.add(p3);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p3.setBackground(Color.ORANGE);
        red.addActionListener( ae -> {
            StyledDocument doc = (StyledDocument) pane.getDocument();
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setForeground(style, Color.RED);
            try {
                String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                pane.setText("");
                doc.insertString(0, text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        green.addActionListener(ae -> {
            StyledDocument doc = (StyledDocument) pane.getDocument();
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setForeground(style, Color.GREEN);
            try {
                String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                pane.setText("");
                doc.insertString(0, text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        blue.addActionListener(ae -> {
            StyledDocument doc = (StyledDocument) pane.getDocument();
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setForeground(style, Color.BLUE);
            try {
                String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                pane.setText("");
                doc.insertString(0, text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }
}

class FontFamily implements ButtonsCommand {
    JPanel tools, p4;
    JTextPane pane;
    JButton tnr, calibre, sansSerif;
    JLabel l4;

    FontFamily(JPanel tools, JTextPane pane) {
        this.pane = pane;
        this.tools = tools;
        p4 = new JPanel();
        p4.setOpaque(false);
        tnr = new JButton("Times New Roman");
        tnr.setAlignmentX(Component.CENTER_ALIGNMENT);
        calibre = new JButton("Calibre");
        calibre.setAlignmentX(Component.CENTER_ALIGNMENT);
        sansSerif = new JButton("Surprise");
        sansSerif.setAlignmentX(Component.CENTER_ALIGNMENT);
        l4 = new JLabel("Font Family");
        l4.setForeground(Color.BLACK);
        l4.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
        l4.setFont(f);
        p4.add(l4);
        p4.add(tnr);
        p4.add(calibre);
        p4.add(sansSerif);
        tools.add(p4);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p4.setBackground(Color.ORANGE);

        tnr.addActionListener(ae -> {
            StyledDocument doc = (StyledDocument) pane.getDocument();
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setFontFamily(style, "Times New Roman");
            try {
                String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                pane.setText("");
                doc.insertString(0, text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        calibre.addActionListener(ae -> {
            StyledDocument doc = (StyledDocument) pane.getDocument();
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setFontFamily(style, "Calibri");
            try {
                String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                pane.setText("");
                doc.insertString(0, text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });

        sansSerif.addActionListener(ae -> {
            StyledDocument doc = (StyledDocument) pane.getDocument();
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setFontSize(style, 32);
            StyleConstants.setFontFamily(style, "OCR A Extended");
            try {
                String text = pane.getDocument().getText(0, pane.getDocument().getLength());
                pane.setText("");
                doc.insertString(0, text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }
}

// This implementation includes the use of the Decorator Design Pattern as well.
class Decorate implements ButtonsCommand {
    JPanel tools, p5;
    JTextPane pane;
    JButton addscbar, addBorder;
    JLabel l5, label;

    Decorate(JPanel tools, JTextPane pane) {
        this.pane = pane;
        this.tools = tools;
        p5 = new JPanel();
        label = new JLabel("\t\t\t\t");
        p5.setOpaque(false);
        addscbar = new JButton("Add Scroll Bar");
        addscbar.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBorder = new JButton("Add Border");
        addBorder.setAlignmentX(Component.CENTER_ALIGNMENT);
        l5 = new JLabel("Decorate");
        l5.setForeground(Color.BLACK);
        l5.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));
        l5.setFont(f);
        p5.add(l5);
        p5.add(addscbar);
        p5.add(addBorder);
        p5.add(label);
        tools.add(p5);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p5.setBackground(Color.ORANGE);

        addBorder.addActionListener(ae -> {
            // Instantiating the Deorating class in the Decorator[2][3][2][3] Design Pattern
            Decorating d = new AddBorder(pane);
            d.addDecor(0);
        });

        addscbar.addActionListener(ae -> {
            // TODO: Put code for adding scroll bar
        });
    }
}

// This implementation includes the use of the Bridge[4][5] Design Pattern as well.
class Os implements ButtonsCommand {
    JPanel tools, p6;
    JTextPane pane;
    JButton windows, linux, macOS;
    JLabel status, l2;

    Os(JPanel tools, JTextPane pane, JLabel status) {
        this.pane = pane;
        this.tools = tools;
        this.status = status;
        p6 = new JPanel();
        p6.setOpaque(false);
        windows = new JButton("Windows");
        windows.setAlignmentX(Component.CENTER_ALIGNMENT);
        linux = new JButton("Linux");
        linux.setAlignmentX(Component.CENTER_ALIGNMENT);
        macOS = new JButton("MacOS");
        macOS.setAlignmentX(Component.CENTER_ALIGNMENT);
        l2 = new JLabel("Operating System");
        l2.setForeground(Color.BLACK);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    public void click() {
        p6.setLayout(new BoxLayout(p6, BoxLayout.Y_AXIS));
        l2.setFont(f);
        p6.add(l2);
        p6.add(windows);
        p6.add(linux);
        p6.add(macOS);
        tools.add(p6);
        JPanel temp = new JPanel();
        temp.setOpaque(false);
        temp.setSize(new Dimension(50, 50));
        tools.add(temp);
        temp.setBackground(Color.ORANGE);
        p6.setBackground(Color.ORANGE);

        windows.addActionListener(ae -> {
            // Instantiating the abstract Software class and passing the information of the type of OS and Platform in the parameters
            Software windows = new OperatingSystem("Windows", "Platform1", new Windows(), status);
            windows.display();
        });

        linux.addActionListener(ae -> {
            // Instantiating the abstract Software class and passing the information of the type of OS and Platform in the parameters
            Software linux = new OperatingSystem("Linux", "Platform2", new Linux(), status);
            linux.display();
        });

        macOS.addActionListener(ae -> {
            // Instantiating the abstract Software class and passing the information of the type of OS and Platform in the parameters
            Software macOS = new OperatingSystem("MacOS", "Platform3", new Mac(), status);
            macOS.display();
        });
    }
}

// Invoker class for the Command Design Pattern which has been used to add buttons
class Invoke {
    private final java.util.List<ButtonsCommand> orderList = new ArrayList<>();

    public void addButton(ButtonsCommand button) {
        orderList.add(button);
    }

    public void placeButton() {
        for (ButtonsCommand buttonsCommand : orderList) {
            buttonsCommand.click();
        }
    }
}

// Bridge Design Pattern
interface drawOS {
    void displayOS(String os, String platform, JLabel message);
}

class Windows implements drawOS {
    @Override
    public void displayOS(String os, String platform, JLabel status) {
        status.setText("Document made compatible with Windows OS");
    }
}

class Linux implements drawOS {
    @Override
    public void displayOS(String os, String platform, JLabel message) {
        message.setText("Document made compatible with LINUX OS");
    }
}

class Mac implements drawOS {
    @Override
    public void displayOS(String os, String platform, JLabel message) {
        message.setText("Document made compatible with MacOS");
    }
}

abstract class Software {
    Software() {}
    public abstract void display();
}

class OperatingSystem extends Software {
    String os, platform;
    drawOS dos;
    JLabel message;

    public OperatingSystem(String os, drawOS dos, JLabel message) {
        super();
        this.dos = dos;
        this.os = os;
        this.message = message;
    }

    public OperatingSystem(String os, String platform, drawOS dos, JLabel message) {
        super();
        this.dos = dos;
        this.os = os;
        this.platform = platform;
        this.message = message;
    }

    @Override
    public void display() {
        dos.displayOS(os, platform, message);
    }
}

// Decorator Design Pattern
abstract class Decorating {
    JTextPane pane;

    Decorating(JTextPane pane) {
        this.pane = pane;
    }

    abstract void addDecor(int flag);
}

class AddBorder extends Decorating {
    public AddBorder(JTextPane pane) {
        super(pane);
        this.pane = pane;
    }

    @Override
    void addDecor(int flag) {
        if (flag % 2 == 0) {
            pane.setMargin(new Insets(100, 100, 100, 100));
            pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK,
                    25), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        } else {
            pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        }
    }
}

class AddScrollBar extends Decorating {
    public AddScrollBar(JTextPane pane) {
        super(pane);
        this.pane = pane;
    }

    @Override
    void addDecor(int flag) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }
}

public class DocumentEditor {
    public static void main(String[] args) {
        // Instantiating the Base class in the Composite Design Pattern
        Base b = new Base();

        // Collecting objects of JPanel & JTextPane
        JPanel p = b.addComposite();
        JTextPane t = b.addLeaf();

        // Instantiating all classes used to add functionalities using Command Design Pattern
        FontSize fs = new FontSize(p, t);
        FontStyle st = new FontStyle(p, t);
        FontColor clr = new FontColor(p, t);
        FontFamily fam = new FontFamily(p, t);
        Decorate dr = new Decorate(p, t);
        Os os = new Os(p, t, b.message);

        // Instantiating the Invoker class and adding all buttons to the array list
        Invoke iv = new Invoke();
        iv.addButton(fs);
        iv.addButton(st);
        iv.addButton(clr);
        iv.addButton(fam);
        iv.addButton(dr);
        iv.addButton(os);

        // Placing the buttons on the JPanel using placeButton() method
        iv.placeButton();
    }
}