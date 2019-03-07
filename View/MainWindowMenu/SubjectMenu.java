package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

public class SubjectMenu extends JMenu {

    HashMap<String,JRadioButtonMenuItem> subjects;
    ButtonGroup buttons;
    MainWindowEventHandler handler;


    public void selectSubject(String subject){
        ButtonModel model = subjects.get(subject).getModel();
        buttons.clearSelection();
        buttons.setSelected(model,true);
    }


    public void removeSubject(String subject){
        JRadioButtonMenuItem item = subjects.remove(subject);
        this.remove(item);
        buttons.remove(item);
        subjects.remove(subject);
    }

    public void addSubject(String subject){
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(subject);
        item.addItemListener(handler::subjectSelected );
        add(item);
        buttons.add(item);
        subjects.put(subject,item);
    }

    public List<String> getAllSubjects(){
        List<String> subjects = new ArrayList<>();
        subjects.addAll(this.subjects.keySet());
        return subjects;
    }


    public SubjectMenu(MainWindowEventHandler handler,List<String>subjects){
        super("Subjects");
        this.handler = handler;
        this.subjects = new HashMap<String,JRadioButtonMenuItem>();
        buttons = new ButtonGroup();

        JRadioButtonMenuItem item = new JRadioButtonMenuItem("All");
        item.setSelected(true);
        item.addItemListener(handler::subjectSelected );
        this.subjects.put("All",item);
        add(item);
        buttons.add(item);

        for(String subject: subjects){
            this.addSubject(subject);
        }

    }

}