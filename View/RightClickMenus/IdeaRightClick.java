package View.RightClickMenus;

import View.Handlers.RightClickEventHandler;

import javax.swing.*;

public class IdeaRightClick extends JPopupMenu {
    JMenuItem editIdea, combine, disband, move, stopFiltering, removeIdea, refresh;

    public IdeaRightClick(RightClickEventHandler handler){
        this.editIdea = new JMenuItem("Edit");
        this.editIdea.addActionListener(handler::editIdea);
        this.removeIdea = new JMenuItem("Remove selected");
        this.removeIdea.addActionListener(handler::removeIdeaTopic);
        this.combine = new JMenuItem("Create Topic");
        this.combine.addActionListener(handler::combine);
        this.disband = new JMenuItem("Disband");
        this.disband.addActionListener(handler::disband);
        this.move = new JMenuItem("Move ideas to Topic");
        this.move.addActionListener(handler::move);
        this.stopFiltering = new JMenuItem("View hidden items");
        this.stopFiltering.addActionListener(handler::stopFiltering);
        this.refresh = new JMenuItem("Refresh");
        this.refresh.addActionListener(handler::refreshIdeaTree);


        add(editIdea);
        add(removeIdea);
        add(combine);
        add(disband);
        add(move);
        add(stopFiltering);
        add(refresh);
    }
}
