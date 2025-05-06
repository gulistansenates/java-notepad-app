import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotepadApp extends JFrame {
    private DefaultListModel<String> noteModel;
    private final JList<String> noteList;
    private final JTextArea noteArea;

    public NotepadApp() {
        setTitle("Simple Notepad");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        noteModel = new DefaultListModel<>();
        noteList = new JList<>(noteModel);
        JScrollPane listScrollPane = new JScrollPane(noteList);

        noteArea = new JTextArea();
        JScrollPane areaScrollPane = new JScrollPane(noteArea);
        areaScrollPane.setPreferredSize(new Dimension(200, 100));

        JButton addButton = new JButton("Add Note");
        JButton deleteButton = new JButton("Delete Selected Note");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newNote = noteArea.getText().trim();
                if (!newNote.isEmpty()) {
                    noteModel.addElement(newNote);
                    noteArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(NotepadApp.this, "Note cannot be empty.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = noteList.getSelectedIndex();
                if (selectedIndex != -1) {
                    noteModel.remove(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(NotepadApp.this, "Please select a note to delete.");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(areaScrollPane, BorderLayout.NORTH);
        panel.add(addButton, BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.SOUTH);

        add(listScrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotepadApp::new);
    }
}

