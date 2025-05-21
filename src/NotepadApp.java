import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.stream.Collectors;

public class NotepadApp extends JFrame {
    private final DefaultListModel<String> noteModel;
    private final JList<String> noteList;
    private final JTextArea noteArea;
    private boolean darkMode = false;
    private final JTextField searchField;
    private final String filePath = "notes.txt";

    public NotepadApp() {
        setTitle("Modern Notepad");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        noteModel = new DefaultListModel<>();
        noteList = new JList<>(noteModel);
        noteList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane listScrollPane = new JScrollPane(noteList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Saved Notes"));

        noteArea = new JTextArea(5, 20);
        noteArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(noteArea);
        areaScrollPane.setBorder(BorderFactory.createTitledBorder("Write a Note"));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setToolTipText("Search notes...");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterNotes(searchField.getText().trim().toLowerCase());
            }
        });

        JButton addButton = new JButton("Add Note");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear All Notes");
        JButton toggleThemeButton = new JButton("Toggle Theme");

        JButton[] buttons = {addButton, deleteButton, clearButton, toggleThemeButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Segoe UI", Font.BOLD, 13));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        }

        addButton.addActionListener(e -> {
            String newNote = noteArea.getText().trim();
            if (!newNote.isEmpty()) {
                noteModel.addElement(newNote);
                noteArea.setText("");
                saveNotesToFile();
            } else {
                JOptionPane.showMessageDialog(this, "Note cannot be empty.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = noteList.getSelectedIndex();
            if (selectedIndex != -1) {
                noteModel.remove(selectedIndex);
                saveNotesToFile();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a note to delete.");
            }
        });

        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear all notes?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                noteModel.clear();
                saveNotesToFile();
            }
        });

        toggleThemeButton.addActionListener(e -> {
            darkMode = !darkMode;
            applyTheme();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(areaScrollPane, BorderLayout.NORTH);
        topPanel.add(searchField, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(toggleThemeButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        add(listScrollPane, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        loadNotesFromFile();
        applyTheme();

        setVisible(true);
    }

    private void applyTheme() {
        Color bg = darkMode ? new Color(40, 40, 40) : new Color(250, 250, 255);
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        Color panelBg = darkMode ? new Color(55, 55, 55) : new Color(230, 240, 255);

        getContentPane().setBackground(bg);
        noteList.setBackground(panelBg);
        noteList.setForeground(fg);
        noteArea.setBackground(panelBg);
        noteArea.setForeground(fg);
        searchField.setBackground(panelBg);
        searchField.setForeground(fg);
        repaint();
    }

    private void saveNotesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < noteModel.size(); i++) {
                writer.write(noteModel.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save notes.");
        }
    }

    private void loadNotesFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                noteModel.addElement(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load notes.");
        }
    }

    private void filterNotes(String keyword) {
        noteList.clearSelection();
        noteModel.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            noteModel.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    noteModel.addElement(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotepadApp::new);
    }
}
