package View;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class SearchList<E> extends JPanel {
    private JList<E> displayList;
    private DefaultListModel<E> listModel;
    private JTextField searchField;


    private List<E> list;

    public void displayValuesContaining(String word){
        listModel.removeAllElements();
        for(E elem: list){
            if(elem!=null && elem.toString().toLowerCase().contains(word.toLowerCase())){
                listModel.addElement(elem);
            }
        }
    }

    public void addMouseListenerForList(MouseListener mouseListener){
        this.displayList.addMouseListener(mouseListener);
    }

    public void addElement(E elem){
        if(!listModel.contains(elem))
            listModel.addElement(elem);
        if(!list.contains(elem))
            list.add(elem);
    }

    public void removeElement(E elem){
        listModel.removeElement(elem);
        list.remove(elem);
    }

    public List<E> getSelectedValuesList(){
        return displayList.getSelectedValuesList();
    }


    public SearchList(List<E>lst){
        this.list = lst;
        listModel = new DefaultListModel<>();
        displayList = new JList<>(listModel);

        for(E elem: lst){
            this.listModel.addElement(elem);
        }

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        displayValuesContaining(searchField.getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        displayValuesContaining(searchField.getText());
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        displayValuesContaining(searchField.getText());
                    }
                }
        );
        this.setLayout(new BorderLayout());
        this.add(searchField, BorderLayout.NORTH);
        this.add(new JScrollPane(displayList), BorderLayout.CENTER);





    }
}
