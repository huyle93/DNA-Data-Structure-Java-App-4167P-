/** 
 * GUI.java:
 * A JPanel to control the application's user interface and its 
 * connection to the main application code.
 *
 * @author rdb
 * CS416 Spring 2008
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel 
{
    //---------------- instance variables ---------------------------
    private RegexPanel _display;    // the display area for input and output
    private RegexApp   _app;        // the main regex code
    private JTextField _regexLabel;
    private String     _labelPrefix;
    
    //------------------- constructor -------------------------------
    /**
     * Container parent of the control panel is passed as an argument
     * along with the application object.
     */
    public GUI( String patternFileName, String dnaFileName ) 
    {
        super ( new BorderLayout() );
        _display = new RegexPanel();
        
        JScrollPane sPane = new JScrollPane( _display,
                                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
                                           );     
        this.add( sPane, BorderLayout.CENTER );
        
        //create ControlPanel in the South
        JPanel southPanel = makeSouthPanel();
        this.add( southPanel, BorderLayout.SOUTH );
        
        // create the main application object
        _app = new RegexApp( _display, this, patternFileName, dnaFileName );
        
        //create Buttons in the North
        Component buttonMenu = makeButtonMenu();
        this.add( buttonMenu, BorderLayout.NORTH );
        
        int width = _display.getPreferredSize().width;     
        sPane.setPreferredSize( new Dimension( width + 100, 700 ));           
    }
    //-------------- makeCheckBoxes() --------------------------
    private Component makeCheckBoxes()
    {
        JPanel checkPanel = new JPanel();
        
        // create check box for color
        ParameterCheckBox plainTextCheck = new ParameterCheckBox( "Plain text", false )
        {
            public void valueChanged( boolean newValue )
            {
                //_display.showColor( newValue );
                RegexApp.plainText = newValue;
            }
        };
        checkPanel.add( plainTextCheck );
        
        return checkPanel;
    }   
    //------------------- makeSouthPanel --------------------------------
    private JPanel makeSouthPanel()
    {
        JPanel panel = new JPanel();
        panel.add( makeRegexTextArea() );
        panel.add( makeRegexLabel() );
        panel.add( makeCheckBoxes() );
        return panel;
    }
    
    //-------------- makeRegexTextArea() --------------------------
    private Component makeRegexTextArea()
    {
        JPanel labelPanel = new JPanel();
        
        labelPanel.add( new JLabel( "New regex: " ));
        JTextField regexText = new JTextField( "  " );
        regexText.setFont( getFont().deriveFont( 12.0f )); // make the font big
        regexText.setColumns( 20 );
        
        regexText.setBorder( new LineBorder( Color.BLACK ));
        regexText.addActionListener( new ActionListener()
                                        { public void actionPerformed( ActionEvent ev ) 
            {
                                            JTextField field = (JTextField) ev.getSource();
                                            _app.setNewRegex( field.getText() );
                                        }
        });
        
        labelPanel.add( regexText );           
        return labelPanel;
    }
    //-------------- makeRegexLabel() --------------------------
    private Component makeRegexLabel()
    {
        JPanel labelPanel = new JPanel();
        
        labelPanel.add( new JLabel( "Regex: " ));
        _regexLabel = new JTextField( "     " );
        _regexLabel.setFont( getFont().deriveFont( 12.0f )); // make the font big
        _regexLabel.setFont( getFont().deriveFont( 12.0f )); // make the font big
        _regexLabel.setColumns( 30 );
        //_regexLabel.setSize( 200, 6 );
        
        _regexLabel.setBorder( new LineBorder( Color.BLACK ));
        labelPanel.add( _regexLabel );
        
        return labelPanel;
    }
//--------------------- setRegexLabel( String ) ----------------------
    /**
     * update the label field showing the current regular expression
     */
    public void setRegexLabel( String newText )
    {
        _regexLabel.setText( newText );
    }
    //------------------- makeButtonMenu --------------------------------
    private Component makeButtonMenu()
    {
        // JPanel defaults to FlowLayout
        String[] labels = { "Pattern file", "Next pattern",  
            "DNA file", "Next DNA seq", "FindAll" };
        
        JPanel bMenu = new JPanel( new GridLayout( 1, 0 )); 
        JButton button;
        for ( int i = 0; i < labels.length; i++ )
        {
            button = new JButton( labels[ i ] );
            //button.setFont( getFont().deriveFont( 11.0f ));
            bMenu.add( button );
            button.addActionListener( new ButtonListener( i ));
        }      
        return bMenu;
    }
    //+++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++++
    /**
     * ButtonListener handles all button events and passes them along
     * to methods of the ListPanel class.
     */
    public class ButtonListener implements ActionListener
    {
        int _buttonId;
        public ButtonListener( int buttonId )
        {
            _buttonId = buttonId;
        }
        public void actionPerformed( ActionEvent ev )
        {
            switch ( _buttonId )
            {
                case 0:
                    _app.readPatternFile();
                    break;
                case 1:
                    _app.nextRegex();
                    break;
                case 2:
                    _app.readDNAfile();
                    break;
                case 3:
                    _app.nextDNAseq();
                    break;
                case 4:
                    _app.findAll();
                    break;
            }               
        }
    }         
}
