package View.MainWindowMenu;

import View.Handlers.MainWindowEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

public class SubjectMenu extends JMenu {

    HashMap<String,JRadioButtonMenuItem> subjects;
    ButtonGroup buttons;


    public void removeSubject(MainWindowEventHandler handler,String subject){
        JRadioButtonMenuItem item = subjects.remove(subject);
        this.remove(item);
        buttons.remove(item);
    }

    public void addSubject(String subject){
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(subject);
        super.add( item );
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
        this.subjects = new HashMap<String,JRadioButtonMenuItem>();
        buttons = new ButtonGroup();
        this.addSubject("All");
        for(String subject: subjects){
            this.addSubject(subject);
        }
    }

}