package View;

import Model.Idea;
import Model.Note;
import Model.Topic;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * 
 * View.ManageIdeasPage:
 * Page for editing & deleting ideas and building & removing Topics
 * 		- Combine button
 * 		- Disband button
 * 		- Edit button
 * 		- Delete button
 * 		- Model.Idea tree
 * @author kimathi nyota
 */
public class ManageIdeasPage extends JPanel {

	private JTree ideaTree;
	private DefaultMutableTreeNode rootNode;
	private Topic root;
	
	public void refresh() {
		DefaultTreeModel model = (DefaultTreeModel) this.ideaTree.getModel();
		model.reload();	
	}


	
	private void setUpPage(Topic root) {
		this.root = root;
		// COMBINE and DISBAND buttons

		JPanel manageIdeasPanel = new JPanel(new BorderLayout());

		DefaultMutableTreeNode[] rootPassByReference= new DefaultMutableTreeNode[1];
			
		this.buildIdeaTree(this.root, null, rootPassByReference);
		this.ideaTree = new JTree(rootPassByReference[0]);
		this.rootNode = rootPassByReference[0];
		ideaTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
		JScrollPane idea = new JScrollPane(ideaTree);
		idea.setBorder(BorderFactory.createEmptyBorder());
		manageIdeasPanel.add(idea, BorderLayout.CENTER);
		manageIdeasPanel.setPreferredSize(new Dimension(500,500));
		
		this.add(manageIdeasPanel);
		this.setLayout( new GridBagLayout() );
	}
	
	/**
	 * Constructor for View.ManageIdeasPage
	 * 	It sets up all of the layouts, buttons and tree
	 * 	It builds the Tree from the input root Model.Topic
	 * @param root - a Model.Topic object used to buld the tree from, typically an allTopic object
	 */
	public ManageIdeasPage(Topic root) {
		this.setUpPage(root);
	}
	
	/**
	 * Recursive method used to build Tree from root:
	 * @param root
	 * @param parent
	 * @param stores reference of root node of created Tree
	 */
	private void buildIdeaTree(Topic root, DefaultMutableTreeNode parent, DefaultMutableTreeNode[] referenceToRootNode) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(root);
		
		if(parent==null) {
			referenceToRootNode[0] = top;
			/*
			 * Array is chosen as a work around to pass by reference in Java
			 */
		}else 
			parent.add(top);

		for(Idea i: root.getIdeas()) {
			DefaultMutableTreeNode idea = new DefaultMutableTreeNode(i);
			top.add(idea);
		}
			
		for(Topic subTopic: root.getSubTopics()) {
			this.buildIdeaTree(subTopic, top, null);
			
		}
			
	}
	
	public void moveNode(List<DefaultMutableTreeNode> nodes ) {
		/*
		 * Constrainsts:
		 * At least two nodes selected
		 * Model.Topic can't be moved to its descendants
		 * Model.Idea/Model.Topic can only be moved into a Model.Topic
		 */
		
		if(nodes.size()>1) {
			DefaultMutableTreeNode firstNode = nodes.get(0);
			DefaultMutableTreeNode secondNode = nodes.get(1);
			
			if(!firstNode.isNodeDescendant(secondNode) && secondNode.getUserObject() instanceof Topic) {
				Topic moveToTopic = (Topic) secondNode.getUserObject();
				moveToTopic.add(firstNode.getUserObject());
				DefaultMutableTreeNode firstNodeParent = (DefaultMutableTreeNode) firstNode.getParent();
				((Topic) firstNodeParent.getUserObject()).delete(firstNode.getUserObject());
				firstNodeParent.remove(firstNode);
				secondNode.add(firstNode);
				this.refresh();
			}else {
				JOptionPane.showMessageDialog(this, "You can't move " + firstNode + " to " + secondNode);
			}
		}
	}
	

	
	
	/**
	 * Adds Model.Idea to the root Tree
	 * Also add idea to the Model.Topic attached to the root node
	 * @param idea
	 */
	public void addToRoot(Idea idea) {
		this.root.add(idea);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(idea);
		rootNode.add( node );
		this.refresh();
	}
	
	/**
	 * Adds Model.Topic to the root Tree
	 * Also add Model.Topic to the Model.Topic attached to the root node
	 * @param idea
	 */
	public void addToRoot(Topic topic) {
		this.root.add(topic);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(topic);
		rootNode.add( node );
		this.refresh();
	}
	
	/**
	 * Checks if all nodes in the input list share the same parent
	 * @param ideaTopic
	 * @return true: all nodes share same parent: false: if they don't
	 */
	private boolean allShareSameParent(List<DefaultMutableTreeNode>ideaTopic) {
		boolean allShareSameParent = true;
		
		for(int i=0;i<ideaTopic.size()-1; i++) {
			DefaultMutableTreeNode current = ideaTopic.get(i);
			DefaultMutableTreeNode next = ideaTopic.get(i+1);
			if(!current.getParent().equals(next.getParent())){
				allShareSameParent = false;
			}
		}
		return allShareSameParent;
	}
	
	/**
	 * Nodes can only be combined:
	 *  -> Node isn't the root node (root integrity must be maintained)
	 *  -> All nodes share the same parent
	 *  -> Input list isn't empty
	 * @param ideaTopic
	 * @return
	 */
	private boolean canCombine(List<DefaultMutableTreeNode>ideaTopic) {
		return !ideaTopic.contains(rootNode) && allShareSameParent(ideaTopic)  && ideaTopic.size()>0;
	}
	
	/**
	 * Will prune the input tree by: removing all nodes with object (Model.Idea or Model.Topic),
	 * which don't contain any of the notes within the input Model.Note collection
	 * @param rootNode - root of input tree
	 * @param notes - all nodes with zero associating to this collection will be pruned from the tree
	 */
	private void removeAllUnassociatedNodes(DefaultMutableTreeNode rootNode,Collection<Note>notes, Collection<Topic>topicExemptions) {
		List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		if(topicExemptions.contains((Topic) rootNode.getUserObject()))
			return;
		for(int i=0; i<rootNode.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(i);
			Object childObject = child.getUserObject();
			if(childObject instanceof Idea) {
				if(!((Idea) childObject).containsAny(notes)) {
					nodes.add(child);
				}
			}else {
				if(!topicExemptions.contains((Topic) childObject) && !((Topic) childObject).containsAny(notes)) {
					nodes.add(child);
				}else {
					removeAllUnassociatedNodes(child,notes,topicExemptions);
				}
			}
		}
		for(DefaultMutableTreeNode n: nodes) {
			n.removeFromParent();
		}
	}
	
	/**
	 * Only displays nodes with object (Model.Idea or Model.Topic) attached that contains any of the
	 * notes within the input note collection
	 * @param notes
	 */
	public void hideAllTopicsOrIdeasNotAssociatedWithNotes(Collection<Note>notes, Collection<Topic>topicExemptions) {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		//below array will be used to pass by reference 
		DefaultMutableTreeNode[] rootPassByReference= new DefaultMutableTreeNode[1];
		//below will build a new tree from Model.Topic root
		this.buildIdeaTree(root, null, rootPassByReference);
		rootNode = rootPassByReference[0]; //root of just created tree
		this.removeAllUnassociatedNodes(rootNode, notes, (topicExemptions==null ? new ArrayList<Topic>() : topicExemptions)); //prunes created tree
		//by removing all unassociated nodes
		this.rootNode.removeAllChildren();
		/* Adds all of the children of the pruned tree to the root node of the JTree
		 * This now makes the previously pruned tree visible
		 */
		for(int i=0; i<rootNode.getChildCount(); i++) {
			this.rootNode.add( (DefaultMutableTreeNode) rootNode.getChildAt(i) );
			i-=1;
		}
		//refresh view of model
		DefaultTreeModel model = (DefaultTreeModel) this.ideaTree.getModel();
		model.reload();
	}
	
	public List<Topic> getSelectedTopics(){
		List<DefaultMutableTreeNode> nodes =  this.getSelected();
		ArrayList<Topic> topics = new ArrayList<Topic>();
		for(DefaultMutableTreeNode n: nodes) {
			if(n.getUserObject() instanceof Topic) {
				topics.add((Topic) n.getUserObject());
			}
		}
		return topics;
	}
	
	
	
	/**
	 * Combine: Lets user input a name for Model.Topic and creates and displays one
	 * using all of the input nodes.
	 * @param ideaTopic - list of nodes
	 */
	public void combineNodes(List<DefaultMutableTreeNode> ideaTopic) {
		/*
		 * Combine 
		 * Prereq: share same direct parent
		 * Action: create new Model.Topic as parent
		 */

		/*
		 * Example of combine:
		 * Model.Topic: A, B
		 * Model.Topic: Model.Topic: A, B
		 * Process:
		 *  -> Get topic name as input from user
		 *  -> Remove every child object (Model.Idea or Model.Topic) from parent object (Ideas, SubTopics)
		 *  -> Create new Model.Topic (name=topic name) and add all objects of children of the parent to this topic
		 *  -> Create new node and attach above Model.Topic object to it
		 *  -> Set parent of all children of previous parent to the created node
		 *  -> Add created node as a child to the previous parent
		 *  -> Reload model (refresh view)
		 */
		
		if(canCombine(ideaTopic) ) {
			// Get topic name as input from user
			String topicName = JOptionPane.showInputDialog("Enter Topic: ");
			if(topicName!=null) {
				//Create new Model.Topic (name=topic name)
				Topic generalTopic = new Topic(topicName);
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) ideaTopic.get(0).getParent();
				Topic parentTopic = (Topic) parent.getUserObject();
				//Create new node and attach above Model.Topic object to it
				DefaultMutableTreeNode current = new DefaultMutableTreeNode(generalTopic);
				//Add created node as a child to the previous parent
				parent.add(current);
				parentTopic.add(current.getUserObject());
				for(DefaultMutableTreeNode n: ideaTopic) {
					//add all objects of children of the parent to this topic
					generalTopic.add(n.getUserObject());
					//remove every child object (Model.Idea or Model.Topic) from parent object (Ideas, SubTopics)
					parentTopic.delete(n.getUserObject());
					//Set parent of all children of previous parent to the created node
					current.add(n);
				}
				//Reload model (refresh view)
				DefaultTreeModel model = (DefaultTreeModel) this.ideaTree.getModel();
				model.reload();
			}
		}else {
			JOptionPane.showMessageDialog(this, "You can't select the root node and you can only select Ideas/Topics within the same Model.Topic ");
		}
	
	}
	
	/**
	 * 
	 * @return RootTopic.xml
	 */
	public String getHeirachyAsXML() {
		Topic root = (Topic) this.rootNode.getUserObject();
		return root.toXML();
	}
	
	/**
	 * Gets selected nodes
	 * @return selected nodes
	 */
	public List<DefaultMutableTreeNode> getSelected(){
		List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		if(ideaTree.getSelectionPaths()==null)
			return nodes;
		
		for(TreePath path: this.ideaTree.getSelectionPaths()) {
			nodes.add( (DefaultMutableTreeNode)  path.getLastPathComponent()  );
		}
		return nodes;
		
	}
	
	/**
	 * Disband: 
	 * Removes each Model.Topic and adds children of each Model.Topic back to the parent of Model.Topic
	 * @param ideaTopic
	 */
	public void disbandNodes(List<DefaultMutableTreeNode> ideaTopic) {
		//This topic is removed: all children are moved to parent
		/*
		 * For each selected node: 
		 * Prereq: Selected node has Model.Topic attached
		 * Actions: 
		 * 	-> Move each child node of selected node to parent node (of input nodes) 
		 *  -> Move each object of child node to topic of parent
		 *  -> Delete topic of selected node
		 *  -> Remove parent node from parent
		 */
		
		for(DefaultMutableTreeNode top: ideaTopic) {
			//Prereq: Selected node has Model.Topic attached
			if(top.getUserObject() instanceof Topic) {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) top.getParent();
				if(parent!=null) {
					Topic parentTopic = (Topic) parent.getUserObject();
					for(int i=0; i<top.getChildCount(); i++) {
						DefaultMutableTreeNode child = (DefaultMutableTreeNode) top.getChildAt(i);
						//Move each child node of selected node to parent node (of selected ..) 
						parent.add(child);
						i-=1; //since one child has been removed
						//Move each object of child node to topic of parent
						parentTopic.add(child.getUserObject());
					}
					//Delete topic of selected node
					parentTopic.delete(top.getUserObject());
					//Remove parent node from parent
					top.removeFromParent();
					//Refresh Tree View
					DefaultTreeModel model = (DefaultTreeModel) this.ideaTree.getModel();
					model.reload();
				}else {
					JOptionPane.showMessageDialog(this, "This topic cannot be disbanded");
				}
			}
		}
	}
	
	/**
	 * Removes the node and attached Model.Idea or Model.Topic and all of the children (if Model.Topic)
	 * Returns the object attached to the node
	 * @param ideaTopic
	 * @return Object
	 */
	public Object deleteNode(DefaultMutableTreeNode ideaTopic) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) ideaTopic.getParent();
		Object deletedObject = ideaTopic.getUserObject();
		if(parent==null) {
			JOptionPane.showMessageDialog(this, "This topic cannot be deleted");
		}else {
			Topic parentTopic = (Topic) parent.getUserObject();
			parentTopic.delete(ideaTopic.getUserObject());
			ideaTopic.removeFromParent();
			this.refresh();
	
		}
		return deletedObject;
	}
	
	/**
	 * Gets first idea attached to selected nodes
	 * @return Model.Idea
	 */
	public Idea getSelectedIdea() {
		for(DefaultMutableTreeNode n: this.getSelected()) {
			if(n.getUserObject() instanceof Idea)
				return (Idea) n.getUserObject();
		}
		return null;
	}


	public void addRightClickListener(MouseListener listener){
		this.ideaTree.addMouseListener(listener);
	}


}
