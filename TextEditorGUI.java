import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextEditorGUI extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;
    private TextEditor editor;

    private boolean isBold = false;
    private boolean isItalic = false;

    public TextEditorGUI() {
        editor = new TextEditor();
        textArea = new JTextArea(15, 50);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        inputField = new JTextField(30);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(scrollPane);

        JMenuBar menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        menubar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutMenuItem = new JMenuItem("cut");
        JMenuItem copyMenuItem = new JMenuItem("copy");
        JMenuItem pasteMenuItem = new JMenuItem("paste");
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        menubar.add(editMenu);

        JMenu viewMenu = new JMenu("View");
        JMenuItem zoomMenuItem = new JMenuItem("zoom");
        JMenuItem statusBarMenuItem = new JMenuItem("statusBar");
        JMenuItem wordWrapMenuItem = new JMenuItem("wordWrap");
        viewMenu.add(zoomMenuItem);
        viewMenu.add(statusBarMenuItem);
        viewMenu.add(wordWrapMenuItem);
        menubar.add(viewMenu);

        setJMenuBar(menubar);

        JButton insertButton, deleteButton, undoButton, redoButton, findButton, underlineButton, italicButton,
                boldButton;
        insertButton = new JButton("Insert");
        insertButton.setToolTipText("Insert the given text");

        deleteButton = new JButton("Delete");
        deleteButton.setToolTipText("Delete the given text");

        undoButton = new JButton("Undo");
        undoButton.setToolTipText("Reverse the action");

        redoButton = new JButton("Redo");
        redoButton.setToolTipText("Redoing the action reversed");

        findButton = new JButton("Find");
        findButton.setToolTipText("Find the given word");

        boldButton = new JButton("B");
        boldButton.setToolTipText("Make Text Bold");

        italicButton = new JButton("I");
        italicButton.setToolTipText("Make Text Italic");

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> fontComboBox = new JComboBox<>(fontNames);

        String[] fontSizes = { "12", "14", "16", "18", "20", "30", "40", "60", "70", "80" };
        JComboBox<String> fontSizeComboBox = new JComboBox<>(fontSizes);
        JButton applyFontButton = new JButton("Apply Font");
        applyFontButton.addActionListener(e -> {
            String selectedFont = (String) fontComboBox.getSelectedItem();
            int selectedSize = Integer.parseInt((String) fontSizeComboBox.getSelectedItem());
            Font newFont = new Font(selectedFont, Font.PLAIN, selectedSize);
            textArea.setFont(newFont);
        });

        JButton textColorButton = new JButton("Text Color");
        textColorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Select Text Color", textArea.getForeground());
            if (selectedColor != null) {
                textArea.setForeground(selectedColor);
            }
        });

        JButton bgColorButton = new JButton("Background Color");
        bgColorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Select Background Color", textArea.getBackground());
            if (selectedColor != null) {
                textArea.setBackground(selectedColor);
            }
        });

        JPanel controlPanel1 = new JPanel();
        controlPanel1.add(inputField);
        controlPanel1.add(insertButton);
        controlPanel1.add(deleteButton);
        controlPanel1.add(undoButton);
        controlPanel1.add(redoButton);
        controlPanel1.add(findButton);
        add(controlPanel1, BorderLayout.SOUTH);

        JPanel controlPanel2 = new JPanel();
        controlPanel2.add(boldButton);
        controlPanel2.add(italicButton);
        controlPanel2.add(fontComboBox);
        controlPanel2.add(fontSizeComboBox);
        controlPanel2.add(applyFontButton);
        controlPanel2.add(textColorButton);
        controlPanel2.add(bgColorButton);
        add(controlPanel2, BorderLayout.NORTH);

        insertButton.addActionListener(e -> {
            String input = inputField.getText();
            if (!input.isEmpty()) {
                editor.addText(input);
                textArea.setText(editor.getText());
                inputField.setText("");
            } else {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "No text entered");
            }
        });

        deleteButton.addActionListener(e -> {
            String input = inputField.getText();
            if (!input.isEmpty()) {
                editor.deleteWord(input);
                textArea.setText(editor.getText());
                inputField.setText("");
            } else {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "No text entered.");
            }
        });

        undoButton.addActionListener(e -> {
            if (editor.canUndo()) {
                editor.undo();
                textArea.setText(editor.getText());
            } else {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "Nothing to undo.");
            }
        });

        redoButton.addActionListener(e -> {
            if (editor.canRedo()) {
                editor.redo();
                textArea.setText(editor.getText());
            } else {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "Nothing to redo.");
            }
        });

        findButton.addActionListener(e -> {
            String input = inputField.getText();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "No text entered.");
            } else if (editor.findWord(input)) {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "Word found.");
            } else {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "Word not found.");
            }
            inputField.setText("");
        });

        boldButton.addActionListener(e -> {
            if (textArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "No text found");
            } else {
                isBold = !isBold;
                setBoldAndItalicFont(isBold, isItalic);
            }
        });

        italicButton.addActionListener(e -> {
            if (textArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(TextEditorGUI.this, "No text found");
            } else {
                isItalic = !isItalic;
                setBoldAndItalicFont(isBold, isItalic);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Text Editor GUI");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setBoldFont(boolean bold) {
        Font currentFont = textArea.getFont();
        int style = bold ? Font.BOLD : Font.PLAIN;
        textArea.setFont(new Font(currentFont.getName(), style, currentFont.getSize()));
    }

    private void setItalicFont(boolean italic) {
        Font currentFont = textArea.getFont();
        int style = italic ? Font.ITALIC : Font.PLAIN;
        textArea.setFont(new Font(currentFont.getName(), style, currentFont.getSize()));
    }

    private void setBoldAndItalicFont(boolean bold, boolean italic) {
        Font currentFont = textArea.getFont();
        int style = Font.PLAIN;
        if (bold && italic) {
            style = Font.BOLD | Font.ITALIC;
        } else if (bold) {
            style = Font.BOLD;
        } else if (italic) {
            style = Font.ITALIC;
        }
        textArea.setFont(new Font(currentFont.getName(), style, currentFont.getSize()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TextEditorGUI();
        });
    }
}
