/**
 * @Author: NGUYEN VAN NGAN
 * @Unit: KHOA CONG NGHE THONG TINH
 * 		TRUONG DH KINH TE QUOC DAN
 * @version 1.00 2009/6/4
 */
package Editor_Class;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import javax.swing.text.Document;
import javax.swing.text.rtf.*;
import javax.swing.border.TitledBorder;

import java.awt.Event;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Color;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.Adjustable;
import java.awt.ComponentOrientation;
import java.awt.print.*;

import java.net.URL;
import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;


public class Text_Editor extends JFrame implements ActionListener,ItemListener
{
	public void setStyle(int style)
	{
		try{
			LookAndFeelInfo[] gui=UIManager.getInstalledLookAndFeels();
			UIManager.setLookAndFeel(gui[style].getClassName());
			SwingUtilities.updateComponentTreeUI(this);
		}
		catch(Exception ecp)
		{}

	}

    public Text_Editor()
    {
    		MainFrame();
    }
    public void MainFrame()
    {
//first
	setStyle(1);
		regionEditor=new JTextArea();
		undoAction=new UndoManager();
    	menubar=new JMenuBar();
    	fileMenu=new JMenu("File");
    	editMenu=new JMenu("Edit");
    	insertMenu=new JMenu("Insert");
    	formatMenu=new JMenu("Format");
    	helpMenu=new JMenu("Help");

    	newMenuItem=new JMenuItem("New");
    	openMenuItem=new JMenuItem("Open");
    	saveMenuItem=new JMenuItem("Save");
    	saveasMenuItem=new JMenuItem("Save As...");
    	printMenuItem=new JMenuItem("Print");
    	pageFormatMenuItem=new JMenuItem("Page Setup");
    	exitMenuItem=new JMenuItem("Exit");

    	undoMenuItem=new JMenuItem("Undo");
    	copyMenuItem=new JMenuItem("Copy");
    	cutMenuItem=new JMenuItem("Cut");
    	pasteMenuItem=new JMenuItem("Paste");
    	deleteMenuItem=new JMenuItem("Delete");
    	findMenuItem=new JMenuItem("Find");
    	selectallMenuItem=new JMenuItem("Select All");
    	clearallMenuItem=new JMenuItem("Clear All");


	   	fileInsertMenuItem=new JMenuItem("File...");
	   	dateInsertMenuItem=new JMenuItem("Date/Times...");

    	boldMenuItem=new JCheckBoxMenuItem("Bold");
    	italicMenuItem=new JCheckBoxMenuItem("Italic");
    	normalMenuItem=new JMenuItem("Normal");

    	changeFont=new JMenuItem("Font...");
    	changeBgColor=new JMenuItem("Background");
    	changeTextColor=new JMenuItem("Text Color");
    	resetMenuItem=new JMenuItem("Reset All");

    	aboutMenuItem=new JMenuItem("About...");

    	copyMenuItemPopup=new JMenuItem("Copy");
    	deleteMenuItemPopup=new JMenuItem("Delete");


    	times=new JLabel();
    	status=new JPanel();
    	status.setLayout (new BorderLayout());
    	statusLbl=new JLabel("  | "+fileType+" : |   1 Line       |0 Byte");
    	status.add(statusLbl,BorderLayout.CENTER);
    	times.hide ();
    	status.add(ShowTime(),BorderLayout.EAST);
    	status.setToolTipText(info);

    	fileChooser=new JFileChooser();
//Add Content Main Frame
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				Exit();
			}
		});
		screenSize=Toolkit.getDefaultToolkit ().getScreenSize ();
		int x=screenSize.width;
		int y=screenSize.height-35;
    	setSize(x,y);
    	setTitle("JNotepad - "+fileName);
    	setJMenuBar(getjMenuBar());
    	add(status,BorderLayout.SOUTH);
    	add(Toolbar (),BorderLayout.NORTH);
    	scroll=new JScrollPane();
    	scroll.setName("scroll");
    	this.add(getRegionEditor(),BorderLayout.CENTER);

    	//phuong thuc Undo
    	Document regionEditorDoc=regionEditor.getDocument();
    	regionEditorDoc.addUndoableEditListener(new UndoableEditListener()
    	{
    		public void undoableEditHappened(UndoableEditEvent evt)
    		{
				undoAction.addEdit(evt.getEdit());
    		}
    	});

    	//end-----------
    }
//=================
/*private JSplitPane JSP()
{
	jsp=new JSplitPane();
	jsp.setRightComponent (getRegionEditor ());

	return jsp;
}*/
public JSlider ZoomText()
{
	zoom=new JSlider();
	zoom.setToolTipText ("Zoom Text");
	zoom.hide();
	zoom.setValue (fontSize);
	zoom.setMaximum (200);
	zoom.setMinimum (8);
	zoom.addChangeListener (new ChangeListener()
	{
	public void stateChanged(ChangeEvent e)
	{
		fontSize=zoom.getValue ();
		if(zoom.getValue ()==zoom.getMaximum ())
		{
			zoominB.setEnabled (false);
			zoominMI.setEnabled (false);
			}
		else
		{
			zoominB.setEnabled (true);
			zoominMI.setEnabled (true)	;
		}
		if(zoom.getValue ()==zoom.getMinimum ())
		{
			zoomoutB.setEnabled (false);
			zoomoutMI.setEnabled (false);
			}
		else
		{
			zoomoutB.setEnabled (true);
			zoomoutMI.setEnabled (true)	;
		}
		UpdateFont ();
	}
	});
	return zoom;
}
    ///Editor
private JScrollPane getRegionEditor()
    {

    	fontDefault=new Font(fontName,fontStyle,fontSize);
    	regionEditor.setFont(fontDefault);

    	regionEditor.setWrapStyleWord (true);
    	regionEditor.setTabSize (4);
    	regionEditor.setSelectedTextColor(new Color(255,50,50));//mau text Selected
    	regionEditor.setCaretColor(new Color(255,0,0) );//Mau cua con tro tren nen Editor
    	regionEditor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
	    	    	"Text Document - JNotePad",TitledBorder.LEADING,TitledBorder.CENTER,
	    	    	new Font("Courier New",Font.ITALIC,16),new Color(0,0,255)));
    	regionEditor.setToolTipText("JNotePad - Text Document");

		regionEditor.addKeyListener(new KeyAdapter()
			{
				public void keyTyped(KeyEvent e) {
					if (!docSaved)
					{
						saveMenuItem.setEnabled (true);
						undoMenuItem.setEnabled (true);
						findMenuItem.setEnabled (true);
						undoB.setEnabled (true);
						findB.setEnabled (true);
						saveB.setEnabled (true);
						zoominB.setEnabled (true);
						zoomoutB.setEnabled (true);
						zoomoutMI.setEnabled (true);
						zoominMI.setEnabled (true);

						docSaved = true;
					}
					if(fileName.equals("New Document"))
					{
						setTitle("JNotepad - "+fileName+" ***");
					}
					else
					{
						File file=fileChooser.getSelectedFile();
						String path=file.getPath();
						setTitle(("JNotepad - "+path+" ***"));
					}


			}
		});
		regionEditor.addCaretListener (new CaretListener()
		{public void caretUpdate(CaretEvent e)
		{
			int oi=regionEditor.getSelectionEnd ()-regionEditor.getSelectionStart ()	;
			if(oi>0)
			{
				cutMenuItem.setEnabled (true);
				cutB.setEnabled (true);
				copyB.setEnabled (true);
				copyMenuItem.setEnabled (true);
				deleteMenuItem.setEnabled (true);
				deleteB.setEnabled (true)	;
			}
			else
			{
				cutMenuItem.setEnabled (false);
				cutB.setEnabled (false);
				copyB.setEnabled (false);
				copyMenuItem.setEnabled (false);
				deleteB.setEnabled (false)	;
				deleteMenuItem.setEnabled (false);
			}
			line=regionEditor.getLineCount ();

			String sobyte="  | "+fileType+" : |   "+String.valueOf(line)+" Line       |  "+String.valueOf (regionEditor.getText ().length ())+" Byte";
			String local="   |  Location : "+String.valueOf (regionEditor.getCaretPosition ());

			statusLbl.setText (sobyte+local);
			if(regionEditor.getText ().length ()==0)
			{
				zoominB.setEnabled (false);
				zoominMI.setEnabled (false);
				zoomoutB.setEnabled (false);
				zoomoutMI.setEnabled (false);
				findB.setEnabled (false);
			}
			else
			{
				zoominB.setEnabled (true);
				zoominMI.setEnabled (true);
				zoomoutB.setEnabled (true);
				zoomoutMI.setEnabled (true);
				findB.setEnabled (true);
			}
		}
		});
    	regionEditor.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(e.isPopupTrigger())
				{
					getPopupMenu().show(regionEditor,e.getX(),e.getY());
					e.consume();
				}
			}
			public void mouseReleased(MouseEvent e)
			{
				if(e.isPopupTrigger())
				{
					getPopupMenu().show(regionEditor,e.getX(),e.getY());

					int oi=regionEditor.getSelectionEnd ()-regionEditor.getSelectionStart ();
					if(oi>0)
					{
						copyMenuItemPopup.setEnabled (true);
						cutMenuItemPopup.setEnabled (true);
						deleteMenuItemPopup.setEnabled (true);
						upperMenuItem.setEnabled (true);
						lowerMenuItem.setEnabled (true);
					}
					else
					{
						copyMenuItemPopup.setEnabled (false);
						cutMenuItemPopup.setEnabled (false);
						deleteMenuItemPopup.setEnabled (false);
						upperMenuItem.setEnabled (false);
						lowerMenuItem.setEnabled (false);
					}
					e.consume();
				}
			}
		});
    	scroll.setViewportView(regionEditor);
    	return scroll;
    }

//MenuBar
protected JMenuBar getjMenuBar()
	 {

    	menubar.add(fileMenu);
    	menubar.setToolTipText (info);
    		fileMenu.setMnemonic(KeyEvent.VK_F);
    		fileMenu.add(getNewMenuItem());
	    	fileMenu.add(getOpenMenuItem());
	    	fileMenu.addSeparator();
	    	fileMenu.add(getSaveMenuItem());
	    	fileMenu.add(getSaveAsMenuItem());
	    	fileMenu.addSeparator();
	    	fileMenu.add(getPrintMenuItem());
	    	fileMenu.add(PageFormat());
	    	fileMenu.addSeparator();
	    	fileMenu.add(getExitMenuItem());
    	menubar.add(editMenu);
    		editMenu.setMnemonic(KeyEvent.VK_E);
    		editMenu.add(getUndoMenuItem());
			editMenu.addSeparator ();
    		editMenu.add(getCopyMenuItem());
    		editMenu.add(getCutMenuItem());
    		editMenu.add(getPasteMenuItem());
    		editMenu.add(getDeleteMenuItem());
    		editMenu.addSeparator();
    		editMenu.add(getFindMenuItem());
    		editMenu.addSeparator();
    		editMenu.add(getSelectAllMenuItem());
    		editMenu.add(getClearAllMenuItem());
    	menubar.add(getViewMenu());
    	menubar.add(insertMenu);
	    	insertMenu.setMnemonic(KeyEvent.VK_I);
	   		insertMenu.add(getFileInsertMenuItem());
	   		insertMenu.add(getDateInsertMenuItem());
    	menubar.add(formatMenu);
    		formatMenu.setMnemonic(KeyEvent.VK_F);
	    	formatMenu.add(getChangeFont());
	    	formatMenu.add(getChangeBgColor());
    		formatMenu.add(getChangeTextColor());
	    	formatMenu.addSeparator();
	    	formatMenu.add (getBoldMenuItem());
	    	formatMenu.add(getItalicMenuItem ());
	    	formatMenu.addSeparator ();
	    	formatMenu.add (getNormalMenuItem ());
    		formatMenu.add(getResetMenuItem());
    	menubar.add(helpMenu);
    		helpMenu.setMnemonic(KeyEvent.VK_H);
    		helpMenu.add(getHelp ());
    		helpMenu.add(getAboutMenuItem());
    	return menubar;
   }
//====================
private JToolBar Toolbar()
{
	toolbar=new JToolBar();
	toolbar.setFloatable (false);
	toolbar.setName ("Standard");
	openB=new JButton();
	newB=new JButton();
	undoB=new JButton();
	saveB=new JButton();
	cutB=new JButton();
	copyB=new JButton();
	deleteB=new JButton();
	pasteB=new JButton();
	findB=new JButton();
	zoominB=new JButton();
	zoomoutB=new JButton();
	infoB=new JButton();
	exitB=new JButton();
	try{
		openB.setIcon(new ImageIcon(getClass().getResource("open.png")));
	}
	catch(Exception exp){
		openB.setText("Open");
	}
	try{
		infoB.setIcon(new ImageIcon(getClass().getResource("info.png")));
	}
	catch(Exception exp){
		infoB.setText("Info");
	}
	try{
		zoomoutB.setIcon(new ImageIcon(getClass().getResource("zoomout.png")));
	}
	catch(Exception exp){
		zoomoutB.setText("Zoomout");
	}
	try{
		zoominB.setIcon(new ImageIcon(getClass().getResource("zoomin.png")));
	}
	catch(Exception exp){
		zoominB.setText("Zoomin");
	}
	try{
		findB.setIcon(new ImageIcon(getClass().getResource("find.png")));
	}
	catch(Exception exp){
		findB.setText("Find");
	}
	try{
		pasteB.setIcon(new ImageIcon(getClass().getResource("paste.png")));
	}
	catch(Exception exp){
		pasteB.setText("Paste");
	}
	try{
		deleteB.setIcon(new ImageIcon(getClass().getResource("delete.png")));
	}
	catch(Exception exp){
		deleteB.setText("Delete");
	}
	try{
		copyB.setIcon(new ImageIcon(getClass().getResource("copy.png")));
	}
	catch(Exception exp){
		copyB.setText("Copy");
	}
	try{
		cutB.setIcon(new ImageIcon(getClass().getResource("cut.png")));
	}
	catch(Exception exp){
		cutB.setText("Cut");
	}
	try{
		saveB.setIcon(new ImageIcon(getClass().getResource("save.png")));
	}
	catch(Exception exp){
		saveB.setText("Save");
	}
	try{
		undoB.setIcon(new ImageIcon(getClass().getResource("undo.png")));
	}
	catch(Exception exp){
		undoB.setText("Undo");
	}
	try{
		newB.setIcon(new ImageIcon(getClass().getResource("new.png")));
	}
	catch(Exception exp){
		newB.setText("New");
	}
	try{
		exitB.setIcon(new ImageIcon(getClass().getResource("exit.png")));
	}
	catch(Exception e){
		exitB.setText("Exit");
	}
	openB.setToolTipText ("Open File");
	openB.addActionListener (this);


	newB.setToolTipText ("New File");
	newB.addActionListener (this);


	saveB.setToolTipText ("Save File");
	saveB.setEnabled (false);
	saveB.addActionListener (this);


	cutB.setToolTipText ("Cut");
	cutB.setEnabled (false);
	cutB.addActionListener (this);


	copyB.setToolTipText ("Copy");
	copyB.setEnabled (false);
	copyB.addActionListener (this);


	pasteB.setToolTipText ("Paste");
	pasteB.addActionListener (this);


	deleteB.setToolTipText ("Delete");
	deleteB.setEnabled (false);
	deleteB.addActionListener (this);

	findB.setToolTipText ("Find text");
	findB.setEnabled (false);
	findB.addActionListener (this);

	undoB.setToolTipText ("Undo");
	undoB.setEnabled (false);
	undoB.addActionListener (this);

	zoominB.setToolTipText ("Zoom In +");
	zoominB.setEnabled (false);
	zoominB.addActionListener (this);

	zoomoutB.setToolTipText ("Zoom Out -");
	zoomoutB.setEnabled (false);
	zoomoutB.addActionListener (this);

	infoB.setToolTipText ("Info");
	infoB.addActionListener (this);


	exitB.setToolTipText ("Exit");
	exitB.addActionListener (this);

	toolbar.add(newB);
	toolbar.addSeparator ();
	toolbar.add (openB);
	toolbar.add(saveB);
	toolbar.addSeparator ();
	toolbar.add (undoB);
	toolbar.addSeparator ();
	toolbar.add (copyB);
	toolbar.add (cutB);
	toolbar.add(pasteB);
	toolbar.add(deleteB);
	toolbar.addSeparator ();
	toolbar.add (findB);
	toolbar.addSeparator ();
	toolbar.add (zoominB);
	toolbar.add (zoomoutB);
	toolbar.addSeparator ();
	toolbar.add (ZoomText ());
	toolbar.addSeparator ();
	toolbar.add (infoB);
	toolbar.addSeparator ();
	toolbar.add (exitB);
	return toolbar;
}
	private JMenu getViewMenu()
	{
		viewMenu=new JMenu("View");
		viewMenu.setMnemonic((KeyEvent.VK_V));

		titleView=new JCheckBoxMenuItem("Title");
		titleView.setSelected (true);
		titleView.addItemListener (this);

		styleMenu = new JMenu("Style");
    	windowStyleCheckBoxMenuItem = new JCheckBoxMenuItem("Window");
    	windowStyleCheckBoxMenuItem.setSelected(true);
		windowStyleCheckBoxMenuItem.addItemListener(this);
    	buttonGroup.add(windowStyleCheckBoxMenuItem);
    	styleMenu.add(windowStyleCheckBoxMenuItem);

    	windowClassicCheckBoxMenuItem = new JCheckBoxMenuItem("Window Classic");
    	windowClassicCheckBoxMenuItem.addItemListener(this);
    	buttonGroup.add(windowClassicCheckBoxMenuItem);
    	styleMenu.add(windowClassicCheckBoxMenuItem);

    	metaStyleCheckBoxMenuItem = new JCheckBoxMenuItem("Default");
    	metaStyleCheckBoxMenuItem.addItemListener(this);
    	buttonGroup.add(metaStyleCheckBoxMenuItem);
    	styleMenu.add(metaStyleCheckBoxMenuItem);

		nimbusStyleCheckBoxMenuItem=new JCheckBoxMenuItem("Nimbus");
		nimbusStyleCheckBoxMenuItem.addItemListener(this);
		buttonGroup.add(nimbusStyleCheckBoxMenuItem);
		styleMenu.add(nimbusStyleCheckBoxMenuItem);

    	otherCheckBoxMenuItem = new JCheckBoxMenuItem("Other");
    	otherCheckBoxMenuItem.addItemListener(this);
    	buttonGroup.add(otherCheckBoxMenuItem);
    	styleMenu.add(otherCheckBoxMenuItem);
    	viewMenu.add(styleMenu);


		toolbarView=new JCheckBoxMenuItem("Toolbar");
		JCheckBoxMenuItem lineNumber=new JCheckBoxMenuItem("Line Number");
		toolbarView.setSelected (true);
		toolbarView.addItemListener(this);

		lineNumber.setEnabled (false);




		zoominMI=new JMenuItem("Zoom In");
		zoominMI.setEnabled (false);
		zoominMI.setMnemonic ('N');
		zoominMI.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_ADD,Event.CTRL_MASK));
		zoominMI.addActionListener (this);

		zoomoutMI=new JMenuItem("Zomm Out");
		zoomoutMI.setEnabled (false);
		zoomoutMI.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_SUBTRACT,Event.CTRL_MASK));
		zoomoutMI.addActionListener (this);

		zoomView=new JCheckBoxMenuItem("Zoom Tool");
		zoomView.setMnemonic ('T');
		zoomView.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_T,Event.CTRL_MASK));
		zoomView.addItemListener (this);

		timesView=new JCheckBoxMenuItem("Times/Date");
		timesView.addItemListener(this);

		viewMenu.add(titleView);
		viewMenu.add (toolbarView);
		viewMenu.add(lineNumber);
		viewMenu.add(zoomView);
		viewMenu.addSeparator ();
		viewMenu.add (zoominMI);
		viewMenu.add (zoomoutMI);
		viewMenu.addSeparator ();
		viewMenu.add(timesView);


		return viewMenu;
	}

   //==========
   	private JMenuItem getFileInsertMenuItem()
   	{
   		fileInsertMenuItem.setEnabled (false);
   		fileInsertMenuItem.addActionListener (this);
   	return fileInsertMenuItem;
   	}
   	private JMenuItem getDateInsertMenuItem()
   	{
   		dateInsertMenuItem.setMnemonic (KeyEvent.VK_T);
   		dateInsertMenuItem.addActionListener (this);
	return dateInsertMenuItem;
   	}

//========================
//popup
	protected JPopupMenu getPopupMenu()
	{
		popupMenu=new JPopupMenu();
		popupMenu.add (getUndoPopup ());
		popupMenu.addSeparator ();
    	popupMenu.add(getCutPopup());
    	popupMenu.add(getCopyPopup());
    	popupMenu.add(getPastePopup());
    	popupMenu.add(getDeletePopup());
    	popupMenu.addSeparator();
    	popupMenu.add(getFindMenuItem ());
    	popupMenu.addSeparator();
    	popupMenu.add(ToUpperMenuItem());
    	popupMenu.add(ToLowerMenuItem());
    	popupMenu.addSeparator();
    	popupMenu.add(getMarkText());
    	popupMenu.add(getClearAllMenuItem());
    	popupMenu.addSeparator();
    	popupMenu.add(FontPopup ());
    	return popupMenu;
	}
//=============
public void Undo()
{
	try
	{
		if(undoAction.canUndo())
		{
			undoAction.undo();
		}
	}
	catch(CannotUndoException evt)
	{}
}
//=============
private JMenuItem getUndoPopup()
{
	undoMenuItemPopup=new JMenuItem("Undo");
	undoMenuItemPopup.setMnemonic (KeyEvent.VK_U);
	undoMenuItemPopup.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_Z,Event.CTRL_MASK));
	undoMenuItemPopup.setEnabled (true);
	undoMenuItemPopup.addActionListener (this);
	return undoMenuItemPopup;
}
//============
private JMenuItem getMarkText()
{
	markTextMenuItem=new JMenuItem("Mark");
	markTextMenuItem.setMnemonic(KeyEvent.VK_M);
	markTextMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,Event.CTRL_MASK));
	markTextMenuItem.setEnabled (false);
	markTextMenuItem.addActionListener(this);
return markTextMenuItem;
}

//=============
private JMenuItem getCopyPopup()
{
	copyMenuItemPopup.setMnemonic(KeyEvent.VK_C);
	copyMenuItemPopup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK));
	copyMenuItemPopup.setEnabled (false);
	copyMenuItemPopup.addActionListener(this);
return copyMenuItemPopup;
}
//==============
private JMenuItem getCutPopup()
{
	cutMenuItemPopup=new JMenuItem("Cut");
	cutMenuItemPopup.setMnemonic(KeyEvent.VK_T);
	cutMenuItemPopup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
	cutMenuItemPopup.setEnabled (false);
	cutMenuItemPopup.addActionListener(this);
return cutMenuItemPopup;
}
//==============
private JMenuItem getPastePopup()
{
	pasteMenuItemPopup=new JMenuItem("Paste");
	pasteMenuItemPopup.setMnemonic(KeyEvent.VK_E);
	pasteMenuItemPopup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK));
	pasteMenuItemPopup.addActionListener(this);
return pasteMenuItemPopup;
}
///===================
private JMenuItem getDeletePopup()
{
	deleteMenuItemPopup.setMnemonic(KeyEvent.VK_D);
	deleteMenuItemPopup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,Event.CTRL_MASK));
	deleteMenuItemPopup.setEnabled (false);
	deleteMenuItemPopup.addActionListener(this);
return deleteMenuItemPopup;
}
//===============
private JMenuItem FontPopup()
{
	fontPopup=new JMenuItem("Font Option...");
	fontPopup.setMnemonic (KeyEvent.VK_N);
	fontPopup.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_F,Event.CTRL_MASK+Event.SHIFT_MASK));
	fontPopup.addActionListener (this);
return fontPopup;
}
//==============
private JMenuItem getNewMenuItem()
{
	newMenuItem.setMnemonic(KeyEvent.VK_N);
	newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK));
	newMenuItem.addActionListener(this);
return newMenuItem;
}
//getOpenMenuItem
private JMenuItem getOpenMenuItem()
{
    openMenuItem.setMnemonic(KeyEvent.VK_O);
    openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK));
    openMenuItem.addActionListener(this);
return openMenuItem;
}
//End OpenMenuItem
private JMenuItem getSaveMenuItem()
{
    saveMenuItem.setMnemonic(KeyEvent.VK_S);
    saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK));
    saveMenuItem.setEnabled (false);
    saveMenuItem.addActionListener(this);
return saveMenuItem;
}
//getSaveMenuItem
private JMenuItem getSaveAsMenuItem()
{
    saveasMenuItem.setMnemonic(KeyEvent.VK_A);
    saveasMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK+Event.SHIFT_MASK));
    saveasMenuItem.addActionListener(this);
    return saveasMenuItem;
}
//End SaveMenuItem
//==============
public void New()
{
	String text="";
	regionEditor.setText(text);
	fileType=fileType;
	statusLbl.setText ("  | "+fileType+" : |   0 Byte");
	fileName="New Document";
	setTitle("JNotepad - "+fileName);
	docSaved=false;/*
	Text_Editor a=new Text_Editor ();
	a.setSize(500,450);
	a.setTitle("New Document");
	a.setVisible(true);*/

}
/**
  *OpenFile();
  **/
public void Open()
{
	fileChooser.setDialogTitle("Open File");
	int option=fileChooser.showOpenDialog(this);
	if(option==JFileChooser.CANCEL_OPTION)
	{
	  	return;
	}
	try
	{
		File file=fileChooser.getSelectedFile();
		String path=file.getPath();
		fileType=fileChooser.getTypeDescription(file);
		fileType=fileType.toUpperCase ();
		//lay phan duoi mo rong cua file
		for(i=file.getName ().length ()-1;i>=0;i--)
		{
			if(file.getName ().substring (i,i+1).equals ("."))
			{
				break;
			}
		}
		String extension=file.getName ().substring (i+1).toUpperCase ();
		//======end

		if(extension.equals("EXE")||extension.equals ("DOC")||extension.equals ("DOCX")
						||extension.equals ("MP3")||extension.equals ("MP4")||extension.equals ("AVI")
						||extension.equals ("WAV")||extension.equals ("WMV")||extension.equals ("BMP")
						||extension.equals ("ZIP")||extension.equals ("ISO")||extension.equals ("MOV")
						||extension.equals ("FLV")||extension.equals ("VCD")||extension.equals ("TIF")
						||extension.equals ("ICO")||extension.equals ("PPS")||extension.equals ("XLS")
						||extension.equals ("JAR")||extension.equals ("MSI")||extension.equals ("PNG")
						||extension.equals ("CHM")||extension.equals ("PDF")||extension.equals ("GIF")
						||extension.equals ("PRC")||extension.equals ("MDB")||extension.equals ("PPT")
						||extension.equals ("DLL")||extension.equals ("CHK")||extension.equals ("CER")
						||extension.equals ("DAT")||extension.equals ("DER")
						||extension.equals ("SYS")||extension.equals ("ACCDB")||extension.equals ("PPTX")
						/*||extension.equals ("H")||extension.equals ("XML")||extension.equals ("HTML")
						||extension.equals ("HPP")||extension.equals ("NFO")||extension.equals ("INI")
						||extension.equals ("PRG")||extension.equals ("PHP3")||extension.equals ("CSPROJ")
						||extension.equals ("PHP4")||extension.equals ("PHP5")||extension.equals ("RTF")
						||extension.equals ("SCP")||extension.equals ("VBE")||extension.equals ("JSP")
						||extension.equals ("SHTML")||extension.equals ("XHTML")||extension.equals ("MHTML")
						||extension.equals ("WSF")||extension.equals ("WSH")||extension.equals ("SLN")
						||extension.equals ("TT")||extension.equals ("VBPROJ")||extension.equals ("PROPERTIES")
						*/
						||fileType.equals ("WINRAR ARCHIVE")||fileType.equals ("MTEG TS FILE")
						||fileType.equals ("JPEG IMAGE")||fileType.equals ("MPEG FILE"))
					{
		String mesTitle="Cannot open: \""+path;
		JOptionPane.showMessageDialog(null,mesTitle+"\n\nB\u1ea1n kh\u00f4ng th\u1ec3 m\u1edf file \u0111\u1ecbnh d\u1ea1ng : '."+extension
							+ "'  -  "+fileType+"\nB\u1ea1n h\u00e3y ch\u1ecdn file c\u00f3 \u0111\u1ecbnh d\u1ea1ng kh\u00e1c",mesTitle,JOptionPane.INFORMATION_MESSAGE);
					}
		else
		{

			fileName=fileChooser.getName(file);
			String si=String.valueOf (file.length ());
			setTitle("JNotepad -  \""+path+"       : "+fileType);
			statusLbl.setText ("  | "+fileType+" : |   "+si+" Byte");
			BufferedReader nhap=new BufferedReader(new FileReader(file));
			regionEditor.read(nhap,null);
			nhap.close();
			docSaved=false;
		}
		}
		catch(FileNotFoundException n)
		{
 	 		JOptionPane.showMessageDialog(this,n.getMessage(),"Kh\u00f4ng t\u00ecm th\u1ea5y file",JOptionPane.ERROR_MESSAGE);
 	 	}
		catch(IOException e)
		{
		 	JOptionPane.showMessageDialog(this,e.getMessage(),"C\u00f3 l\u1ed7i",JOptionPane.ERROR_MESSAGE);
		}
		}
//============
public void Save()
	{

		if (fileName.equals("New Document"))
		{
			SaveAs();
			fileName=fileChooser.getName(file);
			fileType=fileChooser.getTypeDescription (file);
		}
		else{
	    	try
    	{
    		file=fileChooser.getSelectedFile();
			fileName=fileChooser.getName(file);
			fileType=fileChooser.getTypeDescription (file);
			String gh=file.getPath ();
			setTitle("JNotepad - "+gh);
    		BufferedWriter write=new BufferedWriter(new FileWriter(file));
    		write.write(regionEditor.getText());
    		write.close();
    		docSaved=false;
    	}
    	catch(IOException mes)
    	{
    		JOptionPane.showMessageDialog(this,mes.getMessage(),"File b\u1ecb l\u1ed7i",JOptionPane.ERROR_MESSAGE);
    	}
		}
	}
/**
  *SaveFile()
  */
     public void SaveAs()
    {
    	fileChooser.setDialogTitle("New File...");

    	file = new File(fileChooser.getCurrentDirectory(),fileName);
    	int option=fileChooser.showSaveDialog(this);
    	if(option==JFileChooser.CANCEL_OPTION)
    	{
    		return;
    	}
    	try
    	{
    		file=fileChooser.getSelectedFile();
			fileName=fileChooser.getName(file);
			String gh=file.getPath ();
			setTitle("JNotepad - "+gh);
    		BufferedWriter write=new BufferedWriter(new FileWriter(file));
    		write.write(regionEditor.getText());
    		write.close();
    		docSaved=false;
    	}
    	catch(IOException mes)
    	{
    		JOptionPane.showMessageDialog(this,mes.getMessage(),"File b\u1ecb l\u1ed7i",JOptionPane.ERROR_MESSAGE);
    	}
    }
    //End SaveFile//
 public void Exit()
    {
    	if(docSaved==false)//cai nay hoi nguoc doi, hehe
	    {
			System.exit(0);
		}
		else if((docSaved==true))
		{

			String mess="<html><font color='blue' size='3'>D\u1eef li\u1ec7u c\u1ee7a b\u1ea1n \u0111\u00e3 \u0111\u01b0\u1ee3c thay \u0111\u1ed5i.<br>B\u1ea1n c\u00f3 mu\u1ed1n l\u01b0u l\u1ea1i kh\u00f4ng ?<br><br>Your data has changed.<br>Do you want to save ?</font></html>";
			int k=JOptionPane.showConfirmDialog(null,mess,"Are you sure ?",JOptionPane.INFORMATION_MESSAGE);
			switch(k)
			{
				case JOptionPane.YES_OPTION:
				{
					Save() ; System.exit(0); break;
				}
				case JOptionPane.NO_OPTION: System.exit(0);break;
				case JOptionPane.CANCEL_OPTION: return;
			}
		}
    }
    //End
    //Print
    private JMenuItem getPrintMenuItem()
    {
    	printMenuItem.setMnemonic(KeyEvent.VK_P);
    	printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,Event.CTRL_MASK));
    	printMenuItem.setEnabled (false);
    	return printMenuItem;
    }
///===============
    private JMenuItem PageFormat()
    {
    	pageFormatMenuItem.setMnemonic(KeyEvent.VK_V);
    	pageFormatMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,Event.CTRL_MASK+Event.SHIFT_MASK));
    	pageFormatMenuItem.setEnabled (true);
    	pageFormatMenuItem.addActionListener (this);

    	return pageFormatMenuItem;
    }
//==================
    protected JMenuItem getExitMenuItem()
    {

    	exitMenuItem.setMnemonic(KeyEvent.VK_X);
    	exitMenuItem.setAccelerator(
    		KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
    	exitMenuItem.addActionListener(this);

    return exitMenuItem;
    }
   //============
	private JMenuItem getUndoMenuItem()
	{
		undoMenuItem.setMnemonic(KeyEvent.VK_U);
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,Event.CTRL_MASK));
		undoMenuItem.setEnabled (false);
		undoMenuItem.addActionListener(this);
	return undoMenuItem;
	}
//==============
	private JMenuItem getCopyMenuItem()
	{
		copyMenuItem.setMnemonic(KeyEvent.VK_C);
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK));
		copyMenuItem.setEnabled (false);
		copyMenuItem.addActionListener(this);
	return copyMenuItem;
	}
//=====================
	private JMenuItem getCutMenuItem()
	{
		cutMenuItem.setMnemonic(KeyEvent.VK_T);
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
		cutMenuItem.setEnabled (false);
		cutMenuItem.addActionListener(this);

		return cutMenuItem;
	}
//===========
	private JMenuItem getPasteMenuItem()
	{
		pasteMenuItem.setMnemonic(KeyEvent.VK_P);
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK));
		pasteMenuItem.addActionListener(this);
		return pasteMenuItem;
	}
//===========
	private JMenuItem getDeleteMenuItem()
	{
		deleteMenuItem.setMnemonic(KeyEvent.VK_D);
		deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,Event.CTRL_MASK));
		deleteMenuItem.setEnabled (false);
		deleteMenuItem.addActionListener(this);
	return deleteMenuItem;
	}
///=============
	private JMenuItem getFindMenuItem()
	{
		findMenuItem=new JMenuItem("Find");
		findMenuItem.setMnemonic(KeyEvent.VK_F);
		findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,Event.CTRL_MASK));

		findMenuItem.addActionListener(this);
	return findMenuItem;
	}
//==============
	private JMenuItem getSelectAllMenuItem()
	{
		selectallMenuItem.setMnemonic(KeyEvent.VK_A);
		selectallMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,Event.CTRL_MASK));
		selectallMenuItem.addActionListener(this);
		return selectallMenuItem;
	}
//===============
	private JCheckBoxMenuItem getBoldMenuItem()
	{
		boldMenuItem.setMnemonic(KeyEvent.VK_B);
		boldMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,Event.CTRL_MASK));
		boldMenuItem.addItemListener(this);
		return boldMenuItem;
	}
//===============
	private JCheckBoxMenuItem getItalicMenuItem()
	{
		italicMenuItem.setMnemonic(KeyEvent.VK_I);
		italicMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,Event.CTRL_MASK));
		italicMenuItem.addItemListener(this);
		return italicMenuItem;
	}
//==============
	private JMenuItem getNormalMenuItem()
	{
		normalMenuItem.setMnemonic(KeyEvent.VK_L);
		normalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,Event.CTRL_MASK));
		normalMenuItem.addActionListener(this);
		return normalMenuItem;
	}
//===============
	private JMenuItem ToUpperMenuItem()
	{
		upperMenuItem=new JMenuItem("To Upper");
		upperMenuItem.setMnemonic(KeyEvent.VK_U);
		upperMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,Event.CTRL_MASK));
		upperMenuItem.setEnabled (false);
		upperMenuItem.addActionListener(this);

		return upperMenuItem;
	}
//==============
	private JMenuItem ToLowerMenuItem()
	{
		lowerMenuItem=new JMenuItem("To Lower");
		lowerMenuItem.setMnemonic(KeyEvent.VK_W);
		lowerMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,Event.CTRL_MASK));
		lowerMenuItem.setEnabled (false);
		lowerMenuItem.addActionListener(this);
		return lowerMenuItem;
	}
//ChangeFont.f1
   protected JMenuItem getChangeFont()
   {
	   	changeFont.setMnemonic(KeyEvent.VK_F);
	   	changeFont.setAccelerator(
	   		KeyStroke.getKeyStroke(KeyEvent.VK_F,Event.CTRL_MASK+Event.SHIFT_MASK));
	   	changeFont.addActionListener (this);
	   	/*changeFont.addActionListener(new ActionListener()
   		{
	   		public void actionPerformed(ActionEvent e)
	   		{

	   			String tit="Ban hay chon font ban thich";
	   			//----------object2
	   			GraphicsEnvironment ga=GraphicsEnvironment.getLocalGraphicsEnvironment ();
	   			String[] fonts=ga.getAvailableFontFamilyNames ();
	   			fontList=new JList(fonts);
				showf=new JTextField("List Font",3);
				fontview=new JLabel();
				fontview.setText("     aA bB cC dD eE fF gG hH iI jJ kK");
				String vach="_________________________________________\n";

	   			fontList.addListSelectionListener(new ListSelectionListener()
	   			{
	   				public void valueChanged(ListSelectionEvent e)
	   				{
	   					fontName=(String)fontList.getSelectedValue();
	   					showf.setText(fontName);
	   					newfont=new Font(fontName,fontStyle,17);
	   					fontview.setFont(newfont);
	   				}
	   			});
   			scroll=new JScrollPane();

   			scroll.setViewportView(fontList);
   			///-----end Object2---------------
   			int k= JOptionPane.showOptionDialog(null,
   			new Object[]{tit,vach,showf,vach,scroll,vach,fontview,vach},"Font Chooser",JOptionPane.OK_CANCEL_OPTION,JOptionPane.DEFAULT_OPTION,
   			null,null,null);
   			switch(k)
   			{
   				case JOptionPane.OK_OPTION :
   				{
   					UpdateFont();
   					break;
   				}
   				default :break;
   			}
   		}
   	});*/
   	return changeFont;
   }
//==========
//ChangeBgColor
	private JMenuItem getChangeBgColor()
	{
		changeBgColor.setText("Background...");
		changeBgColor.setMnemonic(KeyEvent.VK_G);
		changeBgColor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,Event.CTRL_MASK));
		changeBgColor.addActionListener(this);

	return changeBgColor;
	}
//ChangeTextColor
	private JMenuItem getChangeTextColor()
	{
		changeTextColor.setText("Text Color...");
		changeTextColor.setMnemonic(KeyEvent.VK_T);
		changeTextColor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,Event.CTRL_MASK));
		changeTextColor.addActionListener(this);

	return changeTextColor;
	}
//=================
    private JMenuItem getClearAllMenuItem()
    {
    	clearallMenuItem.setText("Clear All");
    	clearallMenuItem.setMnemonic(KeyEvent.VK_R);
    	clearallMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,Event.CTRL_MASK));
    	clearallMenuItem.addActionListener(this);
    return clearallMenuItem;
    }
//================
	private JMenuItem getResetMenuItem()
	{
		resetMenuItem.setText("Reset All");
		resetMenuItem.setMnemonic(KeyEvent.VK_E);
		resetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,Event.CTRL_MASK));
		resetMenuItem.addActionListener(this);
	return resetMenuItem;
	}

//About============
	private JMenuItem getHelp()
	{
		help=new JMenuItem("Topic");
		help.setMnemonic(KeyEvent.VK_H);
    	help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,Event.CTRL_MASK));
		help.addActionListener (this);
	return help;
	}
//==================
    private JMenuItem getAboutMenuItem()
    {
    	aboutMenuItem.setMnemonic(KeyEvent.VK_A);
    	aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,Event.CTRL_MASK));
    	aboutMenuItem.addActionListener(this);
	return aboutMenuItem;
    }
//================
   private JLabel ShowTime()
   {
   	javax.swing.Timer timer = new javax.swing.Timer(1000, new ShowClock());
    timer.start();
    return times;
    }
    class ShowClock implements ActionListener
    {
   		public void actionPerformed(ActionEvent e)
    	{

            Calendar now = Calendar.getInstance();
            h = now.get(Calendar.HOUR_OF_DAY);
            m = now.get(Calendar.MINUTE);
            s = now.get(Calendar.SECOND);
            thu=now.get(Calendar.DAY_OF_WEEK);
            ngay=now.get(Calendar.DAY_OF_MONTH);
            thang=now.get(Calendar.MONTH)+1;
            nam=now.get(Calendar.YEAR);
            time=h + " : " + m + " : " + s+"     "+thu+" - "+ngay+"/"+thang+"/"+nam +"  ";
           	times.setText("  Now is : "+time);
    	}
	}
//==============
	public void UpdateFont()
	{
		regionEditor.setFont(new Font (fontName,fontStyle,fontSize));
	}
//==========
//========================Class FontDialog=====================
public class FontFrm extends JDialog implements ActionListener,ListSelectionListener{

    public FontFrm()
    {
    	setTitle("Font Chooser_JNotepad");
    	setSize(460,360);
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	Dimension screenSize=Toolkit.getDefaultToolkit ().getScreenSize ();
		int x=(screenSize.width-this.getSize ().width)/2;
		int y=(screenSize.height-this.getSize ().height)/2;
		this.setLocation (x,y);
    	setLayout(null);
    	setResizable(false);
    	fontLbl=new JLabel("Font:");

    	scroll1=new JScrollPane();
    	scroll2=new JScrollPane();

    	styleLbl=new JLabel("Font Style:");
    	sizeLbl=new JLabel("Size:");
    	fontTxt=new JTextField();
    	fontTxt.setText("Times New Roman");
    	styleTxt=new JTextField();
    	styleTxt.setText("Regular");
    	sizeTxt=new JTextField();
    	sizeTxt.setText("12");
    	//component
    	fontLbl.setBounds(10,10,50,15);
    	add(fontLbl);
    	fontTxt.setBounds(10,30,140,22);
    	add(fontTxt);
    	styleLbl.setBounds(160,10,100,15);
    	add(styleLbl);
    	styleTxt.setBounds(160,30,100,22);
    	add(styleTxt);
    	sizeLbl.setBounds(270,10,100,15);
    	add(sizeLbl);
    	sizeTxt.setBounds(270,30,80,22);
    	add(sizeTxt);

    	ok.setBounds(360,30,80,20);
    	ok.addActionListener (this);
    	add(ok);

    	cancel.setBounds(360,55,80,20);
    	cancel.addActionListener(this);
    	add(cancel);
    	view.setText("Aa Bb Gg uU tT vV sS Yy zoom");
    	view.setEditable(false);
    	view.setBounds(120,260,180,50);
    	add(view);
    	add(FontList());
    	add(FontStyle());
    	add(FontSize());
    }

//================
    private JScrollPane FontList()
    {

	   	GraphicsEnvironment ga=GraphicsEnvironment.getLocalGraphicsEnvironment ();
	   	String[] fontFamily=ga.getAvailableFontFamilyNames ();
	   	fontList=new JList(fontFamily);
	   	fontList.addListSelectionListener(this);
    	scroll1.setBounds(10,52,140,180);
    	scroll1.setViewportView (fontList);
    	//
    	return scroll1;
   	}
//====================
   	private JScrollPane FontStyle()
   	{
		String style[]={"Regular","Bold","Italic","Bold Italic"};
   		styleList=new JList(style);
   		fontStyle=Font.PLAIN;
   		styleList.addListSelectionListener(this);

   		scroll2.setBounds(160,52,100,180);
   		scroll2.setViewportView (styleList);
   		return scroll2;
   	}
//===================
   	private JScrollPane FontSize()
   	{
   		fontSize=12;
   		scroll3=new JScrollPane();
   		String si[]={"8","10","12","14","16","18","20","22","24"
	   		,"26","28","30","32","34","36","38","40","42","44","46","48","50"
	   			,"52","54","56","58","60","62","64","66","68","70","72"};
	   	sizeList=new JList(si);
	   	sizeList.addListSelectionListener(this);
   		scroll3.setBounds(270,52,80,180);
    	scroll3.setViewportView (sizeList);
   		return scroll3;
   	}
//=================ActionEvent
public void actionPerformed(ActionEvent e)
{
   Object obj=e.getSource ();
   if(obj==ok)
   {
    	UpdateFont ();
    	zoom.setValue (fontSize);
    	dispose ();
   }
   if(obj==cancel)
   {
   	dispose ();
   }

}
//==============ListSelectionEvent
public void valueChanged(ListSelectionEvent e)
{
	Object obj=e.getSource ();
	if(obj==sizeList)
	{
	 String size=(String)sizeList.getSelectedValue();
	 fontSize=Integer.parseInt (sizeTxt.getText ());
	 sizeTxt.setText(size);
	 FontView();
	}
	//====
	if(obj==styleList)
	{

   		String font=(String)styleList.getSelectedValue();
   		styleTxt.setText(font);
   		if(styleTxt.getText ().equals ("Bold"))
   		{
   			fontStyle=Font.BOLD;
   			FontView();
   		}
   		else if(styleTxt.getText ().equals ("Italic"))
   		{
   			fontStyle=Font.ITALIC;
   			FontView();
   		}
   		else if(styleTxt.getText ().equals ("Bold Italic"))
   		{
   			fontStyle=Font.ITALIC+Font.BOLD;
   			FontView();
   		}
	   	else
	   	{
	   		fontStyle=Font.PLAIN;
	   		FontView();
   		}
	}
	//==
	if(obj==fontList)
	{
		fontName=(String)fontList.getSelectedValue();
   		fontTxt.setText(fontName);
   		FontView();
	}
}
public void FontView()
{
	view.setFont (new Font(fontName,fontStyle,fontSize));
}
}
//====================End FontDialog=======================
public class FindFrm extends JDialog implements ActionListener,ItemListener
{
    public FindFrm()
	{
		setTitle("Find and Replace");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		screenSize=Toolkit.getDefaultToolkit ().getScreenSize ();
		int x=(screenSize.width-this.getSize ().width)/3;
		int y=(screenSize.height-this.getSize ().height)/3;
		this.setLocation (x,y);
		setLayout(null);
		findLbl1=new JLabel("Find what: ");
		findLbl1.setBounds(4,20,80,20);
		add(findLbl1);
		add(FindTxt());
		replaceLbl=new JLabel("Replace: ");
		replaceLbl.setBounds (4,50,80,20);

		add(replaceLbl);
		add(ReplaceTxt());
		add(OptionFind ());
		add(getAllCheck());
		add(getMarkCheck());
		add(getMatchCaseCheck());
		add(FindButton());
		add(replaceButton());
		add(CancelButton());
		setSize(420,200);
		setResizable(false);
	}
//==============
	private JTextField FindTxt()
	{
		findTxt=new JTextField(20);
		findTxt.setBounds(68,20,250,22);
		findTxt.addCaretListener (new CaretListener()
		{
			public void caretUpdate(CaretEvent e)
			{
				findButton.setEnabled (true);
				replaceButton.setEnabled (true);

				if(findTxt.getText().equals(""))
				{
					replaceButton.setEnabled (false);
					findButton.setEnabled (false);
				}
			}
		});
		return findTxt;
	}
//=============
	private JTextField ReplaceTxt()
	{
		replaceTxt=new JTextField(20);
		replaceTxt.setBounds (68,50,250,22);


	return replaceTxt;
	}
//=============
public void Found()
{
	String text=regionEditor.getText ();
	text=text.toUpperCase ();//dua van ban ve dang chu Hoa phuc vu cho viec so sanh tim kiem
	StringBuffer bf=new StringBuffer(text);
	k=findTxt.getText ().length ();
	k1=bf.indexOf (findTxt.getText ().toUpperCase ());
	if(k1!=-1)
	{
		regionEditor.select (k1,k1+k);
	}
	else if (k1==-1)
	{
		JOptionPane.showMessageDialog (null,
		"Kh\u00f4ng t\u00ecm th\u1ea5y: \""+findTxt.getText ()+"\"",
		"Cannot find",JOptionPane.INFORMATION_MESSAGE);
		return ;
	}

}
//================
	private JButton FindButton()
	{
		findButton=new JButton("Find Next");
		findButton.setBounds(325,20,80,22);
		findButton.setEnabled (false);
		findButton.addActionListener (this);
	return findButton;
	}
//=============
	private JButton replaceButton()
	{
		replaceButton=new JButton("Replace");
		replaceButton.setBounds(325,50,80,22);
		replaceButton.setEnabled (false);
		replaceButton.addActionListener (this);
	return replaceButton;
	}
//============
	private JButton CancelButton()
	{
		cancelButton=new JButton("Cancel");
		cancelButton.setBounds(325,80,80,22);
		cancelButton.addActionListener(this);
	return cancelButton;
	}
//==========
private JCheckBox OptionFind()
{
	optionFind=new JCheckBox("Option");
	optionFind.setBounds(5,80,75,20);
	optionFind.addItemListener (this);
	return optionFind;
}
	private JCheckBox getAllCheck()
	{
		allCheck=new JCheckBox("All Files");
		allCheck.setBounds(20,100,75,20);
		allCheck.setEnabled (false);
		return allCheck;
	}
//==========
	private JCheckBox getMatchCaseCheck()
	{
		matchCaseCheck=new JCheckBox("Match Case");
		matchCaseCheck.setBounds(20,120,100,20);
		matchCaseCheck.setEnabled (false);
		return matchCaseCheck;
	}
//=========
	private JCheckBox getMarkCheck()
	{
		markCheck=new JCheckBox("Mark Line");
		markCheck.setBounds(20,140,100,20);
		markCheck.setEnabled (false);
		return markCheck;
	}
//===ActionEvent
public void actionPerformed(ActionEvent e)
	{
		Object obj=e.getSource ();
		if(obj==findButton)
		{Found ();}
		if(obj==cancelButton)
		{dispose();}
		if(obj==replaceButton)
		{
			Found ();
			if(k1==-1)//new khong tim thay
			{
				return ;
			}
			else
			{
				regionEditor.replaceSelection (replaceTxt.getText ());
			}
		}
	}
//========ItemEvent
public void itemStateChanged(ItemEvent e)
	{
		Object obj=e.getSource ();
		if(obj==optionFind)
		{
			boolean enable=(e.getStateChange()==e.SELECTED);
			allCheck.setEnabled (enable);
			allCheck.setSelected (false);
			markCheck.setEnabled (enable);
			markCheck.setSelected (false);
			matchCaseCheck.setEnabled (enable);
			matchCaseCheck.setSelected (false);
		}
	}
	private JCheckBox optionFind;
}
//=================End Class FindFrm======
//info
public void Info()
	{
		String info="<html><div align='center'><font color='blue' size='5'><em>JNotepad v1.0.0 !</em></font></div></html>"+
		    	"\n<html><font color='blue'>====================================================</font></html>"+
		    	"\n<html><font color='black' size='3'>Build by :</font></html> :\n<html><font color='#FF0000' size='4'>Nguy\u1ec5n V\u0103n Ng\u00e2n</font></html>"+
		    	"\n<html><font color='green' size='4'>Khoa C\u00f4ng Ngh\u1ec7 Th\u00f4ng Tin - K49<br>Tr\u01b0\u1eddng \u0110\u1ea1i H\u1ecdc Kinh T\u1ebf Qu\u1ed1c D\u00e2n</font></html>"+
		    	"\n<html><hr><font color='blue' size='3'><a href='mailto:ngankt2@gmail.com'>Mail to: ngankt2@gmail.com</a><br> ÐT: <html>0975377280<br>Thanh Mi\u1ec7n _ H\u1ea3i D\u01b0\u01a1ng</html>"+
		    	"\n<html><font color='blue'>====================================================</font></html>";
		 try
		 {
			URL url=new Text_Editor().getClass ().getResource ("author.jpg");
			ImageIcon image=new ImageIcon(url);
			Image im=image.getImage ();
			JOptionPane.showOptionDialog(null,
		    new Object[]{image,info},"JNotepad Version 1.0.0 - Beta 1.0.2",
		    	JOptionPane.CLOSED_OPTION,JOptionPane.PLAIN_MESSAGE,
		    		null,null,null);
		 }
		 catch(Exception e)
		 {
		    JOptionPane.showOptionDialog(null,
			info,"JNotepad Version 1.0.0 - Beta 1.0.2",
			JOptionPane.CLOSED_OPTION,JOptionPane.PLAIN_MESSAGE,
			null,null,null);
		 }
	}
//====================
	public void  OpenConf()
	{
		if(docSaved==false)
					//khong biet cho nay nhamlan o dau
				{
					Open();
				}
				else
				{
					String mess="D\u1eef li\u1ec7u c\u1ee7a b\u1ea1n \u0111\u00e3 \u0111\u01b0\u1ee3c thay \u0111\u1ed5i."+
								"\nB\u1ea1n c\u00f3 mu\u1ed1n l\u01b0u l\u1ea1i  tr\u01b0\u1edbc khi m\u1edf file m\u1edbi kh\u00f4ng ?";
					int k=JOptionPane.showConfirmDialog(null,mess);
					switch(k)
					{
						case JOptionPane.YES_OPTION:
						{
							Save();
							Open();
							break;
						}
						case JOptionPane.NO_OPTION:
						{
							Open();
							break;
						}
						case JOptionPane.CANCEL_OPTION: return;
					}
				}
		}
//===================
	public void NewConf()
	{
		if(docSaved==false)
			{
				New();
			}
			else
			{
				String mess="D\u1eef li\u1ec7u c\u1ee7a b\u1ea1n \u0111\u00e3 \u0111\u01b0\u1ee3c thay \u0111\u1ed5i."+
							"\nB\u1ea1n c\u00f3 mu\u1ed1n l\u01b0u l\u1ea1i  tr\u01b0\u1edbc khi t\u1ea1o file m\u1edbi kh\u00f4ng ?";
				int k=JOptionPane.showConfirmDialog(null,mess);
				switch(k)
				{
					case JOptionPane.YES_OPTION:
					{
						Save();
						New();
						break;
					}
					case JOptionPane.NO_OPTION:
					{
						New();
						break;
					}
					case JOptionPane.CANCEL_OPTION: return;
				}
			}
	}
//==========ActionListener
public void actionPerformed(ActionEvent e)
{
	Object obj=e.getSource ();
	if(obj==newMenuItem || obj==newB)
	{NewConf ();}
	//=open
	if(obj==openMenuItem || obj==openB)
	{OpenConf();}
	//===Save
	if(obj==saveB || obj==saveMenuItem)
	{Save();
	saveB.setEnabled (false);
	saveMenuItem.setEnabled (false);}
	//===saveas
	if(obj==saveasMenuItem)
	{SaveAs();}
	//=====print
	if(obj==pageFormatMenuItem)
	{
			PrinterJob pjob = PrinterJob.getPrinterJob();
			PageFormat pformat = pjob.defaultPage();
			pformat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
			pformat = pjob.pageDialog(pformat);
		}
	if(obj==exitMenuItem || obj==exitB)
	{Exit();}
//==================
	if(obj==undoMenuItem || obj==undoMenuItemPopup || obj==undoB)
	{Undo();}
	if(obj==cutMenuItem || obj==cutMenuItemPopup || obj==cutB)
	{regionEditor.cut();}
	if(obj==copyMenuItem || obj==copyMenuItemPopup || obj==copyB)
	{regionEditor.copy ();}
	if(obj==pasteMenuItem || obj==pasteMenuItemPopup || obj==pasteB)
	{regionEditor.paste ();}
	if(obj==deleteMenuItem || obj==deleteMenuItemPopup || obj==deleteB)
	{regionEditor.replaceSelection ("");}
	if(obj==selectallMenuItem)
	{regionEditor.selectAll();}
	if(obj==dateInsertMenuItem)
	{
		String timeInsert="Times is : " +time;
   		regionEditor.replaceSelection (timeInsert);
   	}
   	if(obj==fontPopup || obj==changeFont)
   	{
   		//new FontFrm().setVisible (true);
   		FontFrm diaF=new FontFrm();
   		diaF.show(true);
   	}
   	if(obj==findMenuItem || obj==findMenuItemPopup || obj==findB)
   	{new FindFrm().show (true);}
   	if(obj==zoominB || obj==zoominMI)
   	{
   		fontSize=fontSize+2;
   		zoom.setValue (fontSize);
   		UpdateFont ();
   	}
   	if(obj==zoomoutB || obj==zoomoutMI)
   	{	fontSize=fontSize-1;
   		zoom.setValue (fontSize);
   		UpdateFont ();

   	}
//=============
	if(obj==help)
	{
		try
		{

			Runtime.getRuntime().exec("hhu.exe help.chm");
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog (null,
			"<html><marquee loop='-1' direction='left'><div style=' color:#0000FF; font-size:10px;'>Xin l\u1ed7i! kh\u00f4ng t\u00ecm th\u1ea5y File<br>Sorry! File not found</div></marquee></html>");
		return;
		}
	}
	if(obj==aboutMenuItem || obj==infoB)
	{Info();}
   	if(obj==normalMenuItem)
   	{
   		fontStyle=Font.PLAIN;
		UpdateFont();
	}
	if(obj==upperMenuItem)
	{
		String text=regionEditor.getSelectedText();
		text=text.toUpperCase();
		regionEditor.replaceSelection(text);
	}
	if(obj==lowerMenuItem)
	{
		String text=regionEditor.getSelectedText();
		text=text.toLowerCase ();
		regionEditor.replaceSelection(text);
	}
	if(obj==clearallMenuItem)
	{regionEditor.setText("");}
	if(obj==resetMenuItem)
	{	regionEditor.setFont(fontDefault);
		regionEditor.setBackground(new Color(255,255,255));//mau trang (White)
		regionEditor.setForeground(new Color(0,0,0));//mau den (Black)
		windowStyleCheckBoxMenuItem.setSelected(true);
		setStyle(3);
		times.hide ();
	}
	if(obj==changeTextColor)
	{
		Color colorText=JColorChooser.showDialog(null,"Hay chon mau cua van ban",Color.black);
		regionEditor.setForeground(colorText);//mau cua font chu
	}
	if(obj==changeBgColor)
	{
		Color colorBg=JColorChooser.showDialog(null,"Bang chon mau nen _ Background",Color.white);
		regionEditor.setBackground(colorBg);
	}
}
//======ItemListener======
public void itemStateChanged(ItemEvent e)
{
	Object obj=e.getSource ();
	if(obj==titleView)
	{
		if(titleView.getState ())
			{
				regionEditor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
		    	"Text Document - JNotePad",TitledBorder.LEADING,TitledBorder.CENTER,
		    	new Font("Courier New",Font.ITALIC,16),new Color(0,0,255)));
			}
			else
			{
				regionEditor.setBorder (null);
			}
	}
	if(obj==toolbarView)
	{
		toolbar.show (toolbarView.isSelected ());
	}
	if(obj==timesView)
	{
		times.show (timesView.isSelected ());

	}
	if(obj==zoomView)
	{
		zoom.show(zoomView.isSelected ());
	}
	if(obj==italicMenuItem)
	{
		fontStyle=Font.PLAIN;
		if(boldMenuItem.getState())
		{
			fontStyle=fontStyle+Font.BOLD;
		}
		if(italicMenuItem.getState())
		{
			fontStyle=fontStyle+Font.ITALIC;
		}
		UpdateFont();
	}
	if(obj==boldMenuItem)
	{
		fontStyle=Font.PLAIN;
		if(boldMenuItem.getState())
		{
			fontStyle=fontStyle+Font.BOLD;
		}
		if(italicMenuItem.getState())
		{
				fontStyle=fontStyle+Font.ITALIC;
		}
		UpdateFont ();
	}
	if(obj==windowStyleCheckBoxMenuItem){
		if(windowStyleCheckBoxMenuItem.isSelected())
		{
			setStyle(3);
		}
	}
	if(obj==windowClassicCheckBoxMenuItem)
	{
		if(windowClassicCheckBoxMenuItem.isSelected())
		{
			setStyle(4);
		}
	}
	if(obj==metaStyleCheckBoxMenuItem){
		if(metaStyleCheckBoxMenuItem.isSelected()){
			setStyle(0);
		}

	}
	if(obj==otherCheckBoxMenuItem){
		if(otherCheckBoxMenuItem.isSelected())
		{setStyle(2);}
	}
	if(obj==nimbusStyleCheckBoxMenuItem)
	{
		if(nimbusStyleCheckBoxMenuItem.isSelected()){
			setStyle(1);
		}

	}
}
/*
 *main
 **/
    public static void main (String[] args)
    {
    	Text_Editor jnotepad=new Text_Editor();
    	jnotepad.setVisible(true);
    }
/*  Variable  */
	/*Class FontDialog vriable*/
	public JScrollPane scroll1,scroll2,scroll3,treeScroll;
    public JLabel fontLbl,styleLbl,sizeLbl;
    public JTextField fontTxt,styleTxt,sizeTxt,view=new JTextField();
    public JButton ok=new JButton("OK"),cancel=new JButton("Cancel");
    public JList fontList,styleList;
    public JList sizeList;
/*End FontDialog Vriable*/
	/*Class FindFrm*/
	public JButton findButton,cancelButton,replaceButton;
    public JTextField findTxt,replaceTxt;
    public JLabel findLbl1,findLbl2,replaceLbl;
    public JCheckBox allCheck,markCheck,matchCaseCheck;
   /*======End=======*/
   	protected JMenuBar menubar;
    public JButton findTextButton,findCancelButton,
    openB,newB,saveB,undoB,cutB,copyB,deleteB,exitB,pasteB,infoB,
    	zoomoutB,zoominB,findB;
    private JLabel times,statusLbl;
    public String time,fontName="Times New Roman",textSelect,g,
    info="<html><font color='red' size='4'>Author: Nguy\u1ec5n V\u0103n Ng\u00e2n _ Sinh Vi\u00ean  \u0110\u1ea1i H\u1ecdc Kinh T\u1ebf Qu\u1ed1c D\u00e2n - K49</font><html>";
    private JPopupMenu popupMenu;
    protected JMenu fileMenu,editMenu,insertMenu,viewMenu,formatMenu,helpMenu;
    private JMenuItem undoMenuItemPopup,copyMenuItemPopup,cutMenuItemPopup,fontPopup
    		,pasteMenuItemPopup,findMenuItemPopup,deleteMenuItemPopup;
    private JMenuItem changeBgColor,changeTextColor,changeFont,resetMenuItem,normalMenuItem;
    private JMenuItem aboutMenuItem,markTextMenuItem,zoominMI,zoomoutMI;
    private JMenuItem openMenuItem,saveMenuItem,saveasMenuItem
    		,newMenuItem,printMenuItem,pageFormatMenuItem,exitMenuItem;
    private JMenuItem copyMenuItem,cutMenuItem,pasteMenuItem
    		,undoMenuItem,deleteMenuItem,selectallMenuItem
    		,clearallMenuItem,findMenuItem,help;
    private JMenuItem fileInsertMenuItem,dateInsertMenuItem,upperMenuItem,lowerMenuItem;
    private JCheckBoxMenuItem titleView,timesView,languageView,
    		zoomView,toolbarView,boldMenuItem,italicMenuItem,metaStyleCheckBoxMenuItem,otherCheckBoxMenuItem,
    		windowClassicCheckBoxMenuItem,windowStyleCheckBoxMenuItem,nimbusStyleCheckBoxMenuItem;
    private JPanel status;
    private JFileChooser fileChooser;
    private JTextArea mes,editor;
    public JTextArea regionEditor;
    private Font fontDefault,newfont;
	public int sizeChooser,fontStyle=Font.PLAIN,fontSize=17
		,h,m,s,thu,ngay,thang,nam,i,k,k1,x,y,line;
    public File file,root;
    private String fileName="New Document",fileType="Text Document";
 	protected boolean doc,docSaved=false;
 	protected StyledEditorKit edt;
 	private UIManager.LookAndFeelInfo xx[];
 	private JScrollPane scroll;
 	public JSlider zoom;
 	private JToolBar toolbar;
 	private Document regionEditorDoc;
 	private UndoManager undoAction;
 	public Dimension screenSize;
 	private JMenu styleMenu;
 	private final ButtonGroup buttonGroup = new ButtonGroup();
}