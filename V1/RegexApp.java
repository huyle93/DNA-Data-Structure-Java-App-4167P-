/**
 * RegexApp - the main class for controlling the application code
 *             for the Regular Expression DNA Exploration Tool.
 * CS416
 * Spring 2013
 * rdb 
 */
import javax.swing.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.text.*;

public class RegexApp
{
    //---------------------  class variables -------------------------------
    static  boolean          plainText = false;
    
    //--------------------- instance variables ----------------------------
    private RegexPanel       _display;          // edit pane display area
    private GUI              _gui;         // not much used
    private JFileChooser     _chooser   = null; // for opening files
    private Scanner          _dnaIn     = null; // for reading sequence data
    private String           _seqHeader = null; // dna sequence header info
    
    private Pattern          _pattern   = null; // pattern corr. to r.e.
    
    private String           _regex;          // the currently active reg expr
    private String           _input;          // the current input data
    private int              _seqLineLen = -1;     // size of input sequence lines
                                              // used to make output the same
    private Vector<String>   _patterns;       // pre-defined patterns from file
    private int              _nxtPattern = 0; // next pre-defined pattern to use
    
    private Regex            _regex1 = null;
    
    //---------------------- constructor ----------------------------------
    /**
     * The app needs the display reference only so it can update the 
     * data for the edit panes.
     */
    public RegexApp( RegexPanel display, GUI gui, 
                    String patFileName, String dnaFileName )
    {
        _display   = display;
        _gui  = gui;
        _regex     = "";       
        
        // Define a File chooser dialog box with its current directory at the
        // current directory of the user. 
        _chooser = new JFileChooser( "." );
                
        readPatternFile( patFileName );
        readDNAfile( dnaFileName );
    }

    //---------------------- readDNAfile() -----------------------------
    /**
     * opens a dna multi-fasta file; and calls nextDNAseq to read the first
     * sequence in the file to become the input test data.
     * Called from GUI.  Uses the JFileChooser class.
     */
    public void readDNAfile( )
    {
        //////////////////// 9P      code ////////////////////////////////
        // This is similar to code for the pattern file
        /////////////////////////////////////////////////////////////////
        int returnval = _chooser.showOpenDialog( null );
        if ( returnval == JFileChooser.APPROVE_OPTION )
        {
            File newIn = _chooser.getSelectedFile();
            readDNAfile( newIn );
        }
    }
    //---------------------- readDNAfile( String ) -------------------------
    /**
     * opens a dna multi-fasta file; and calls nextDNAseq to read the first
     * sequence in the file to become the input test data.
     * Maps a String filename to an File object
     */
    public void readDNAfile( String dnaFileName )
    {
        //////////////////// 9P      code ////////////////////////////////
        // This is similar to code for the pattern file
        /////////////////////////////////////////////////////////////////
        if ( dnaFileName != null )
        {
            try 
            {
               _dnaIn = new Scanner( new File( dnaFileName ) );
                if ( _dnaIn.hasNextLine() )
                {
                    _seqHeader = _dnaIn.nextLine(); //get first header and save it
                    _display.setInputTitle( _seqHeader ); //set header
                    nextDNAseq(); //get first sequence
                }
            }
            catch ( IOException ioe )
            {
                System.err.println( "IO Exception trying to read pattern file: "
                                       + ioe.getMessage() );
            }
        }
    }
    //---------------------- readDNAfile( File ) -------------------------
    /**
     * opens a dna multi-fasta file; and calls nextDNAseq to read the first
     * sequence in the file to become the input test data.
     * This is the actual reading code.
     */
    public void readDNAfile( File dnaFile )
    {
        //////////////////// 9P code //////////////////////////////// 
        // Be sure to break the task into multiple methods.
        ///////////////////////////////////////////////////////////////
        if ( dnaFile != null )
        {
            try 
            {
                _dnaIn = new Scanner( dnaFile );
                if ( _dnaIn.hasNextLine() )
                {
                    _seqHeader = _dnaIn.nextLine(); //get first header and save it
                    _display.setInputTitle( _seqHeader ); //set header
                    nextDNAseq(); //get first sequence
                }
            }
            catch ( IOException ioe )
            {
                System.err.println( "IO Exception trying to read pattern file: "
                                       + ioe.getMessage() );
            }
        }
    }
   //---------------------- nextDNAseq() -----------------------------
    /**
     * Read the next dna sequence from the file -- if it is there. 
     * This method will make all the data lower case.
     *
     * It generates 2 versions of the input data:
     *    displayContents: line-preserved version for display in the
     *                     JEditorPane window
     *    contents: has all eol data removed so the sequence has no interior
     *              "white" space. Need this for proper testing of patterns;
     *              line feeds are not part of real DNA.
     * It also (must) read "ahead" 1 line too many, since it keeps reading
     *   lines of input that represent the dna data until it reads a
     *   line representing the header for the next sequence of dna.
     * So, it "saves" the next header in an instance variable, _seqHeader
     *   and stops reading.
     */
    public void nextDNAseq( )
    {  
        //////////////////// 9P code ////////////////////////////////
        String in = "";
        String dispContents = "";
        String contents = ""; 
        boolean moreToRead = _dnaIn.hasNextLine();
        
        while( moreToRead )
        {
            in = _dnaIn.nextLine();
            if( in.startsWith( ">" ) )
            {
                _seqHeader = in; //save next header
                moreToRead = false; // stop reading
            }
            else
            {                
                if( _seqHeader != null ) //set next header
                {
                    _display.setInputTitle( _seqHeader );
                }
                
                if( plainText ) 
                {
                    //displayText on the left side
                    if( _seqLineLen < 0 )
                    {
                        _seqLineLen = in.length();
                    }
                    dispContents += in + "\n";
                    _display.setInput( dispContents );
                    
                    //text to match on right
                     contents += in + "\n";
                    _input = contents;
                    
                    if( _input != null && _regex1 != null )
                    {
                        _regex1.setInput( _input );
                        System.out.println( "Refreshed plaintext ");
                    }   
                }
                else
                {
                    if( _seqLineLen < 0 )
                    {
                        _seqLineLen = in.length();
                    }
                    
                    //displayText on the left side
                    //_seqLineLen = in.length();
                    dispContents += in + "\n";
                    _display.setInput( dispContents.toLowerCase() );
                    
                    //text to match on right
                    contents += in;
                    _input  = contents.toLowerCase();
                    
                    if( _input != null && _regex1 != null )
                    {
                        _regex1.setInput( _input );
                        System.out.println( "Refreshed uppercase ");
                    }
                } 
                
                moreToRead = _dnaIn.hasNextLine(); //read nextLine
            }
            System.out.println(" input = " + _regex1._inputString22);
        }    
    }
    //------------------ findAll() ------------------------
    /**
     * Given the current r.e. pattern, try to find that pattern in the
     * input data; if successful, try again and again until it fails.
     * 
     * Each region of match should be capitalized in the display.
     * For each match, you should print all the matches to all the groups 
     * of the pattern to System.out.
     */
    public void findAll()
    {      
        //////////////////// 9P code //////////////////////////////// 
        // Be sure to break the task into multiple methods.
        /////////////////////////////////////////////////////////////              
        String results = _regex1.findAll();
        String temp = "";
        
        System.out.println( results );
         if ( results.length() == 0 )
         {
            //no matches
             System.out.println( "No Matches! ");
         }
        else
        {
            if( !plainText )
            {
                for( int i = 0; ; i = _seqLineLen + i )
                {
                    if( results.length() - i < _seqLineLen )
                    {
                        //System.out.println( "Results length " + results.length() );
                        //System.out.println( "i = " + i );
                        temp += results.substring( i, results.length() ) + "\n";
                        break;
                    }
                    else
                    {
                        temp += results.substring( i, _seqLineLen + i ) + "\n";
                    }
                    
                }
                _display.setMatch( temp );
            }
            else
            {
                _display.setMatch( results );
            }
            
        }   
    }
     ////////////////////////////////////////////////////////////////////////
    // Code below here are all custom methods to break up tasks
    ///////////////////////////////////////////////////////////////////////

    
    
    
    ////////////////////////////////////////////////////////////////////////
    // Code below here is all related to getting and managing the Pattern 
    //    file and its contents. 
    // You may change it if you want and/or use it to help develop the
    //    sequence file handling code.
    ///////////////////////////////////////////////////////////////////////
    

    //-------------------- readPatternFile( String ) -----------------------
    /**
     * Open and read the pattern file specified by the parameter
     */
    private void readPatternFile( String patFileName )
    {
        if ( patFileName == null )
            return;
        File patternFile = new File( patFileName );
        if ( patternFile != null && patternFile.exists() )
            readPatternFile( patternFile );
        else
            System.out.println( "Pattern file does not exist: " + patFileName );
    }
    //-------------------- readPatternFile( File ) --------------------------
    /**
     * open and read the pattern file specified by the parameter
     */
    private void readPatternFile( File patternFile )
    {
        /////////////////////////////////////////////////////////////////////
        // 9P code needed
        ////////////////////////////////////////////////////////////////////
        _patterns  = new Vector<String>();
        //-------------- read the predefined patterns from the file -- if there
        if ( patternFile != null )
        {
            try 
            {
                Scanner patScan = new Scanner( patternFile );
                while ( patScan.hasNextLine() )
                    _patterns.add( patScan.nextLine() );
            }
            catch ( IOException ioe )
            {
                System.out.println( "IO Exception trying to read pattern file: "
                                       + ioe.getMessage() );
            }
        }
        nextRegex();
    }
    //+++++++++++++++++++++ methods invoked by GUI buttons +++++++++++++++++++
    //-------------------- readPatternFile() -----------------------------
    /**
     * Get pattern file name from user, open it and read it
     */
    public void readPatternFile()
    {
        int returnval = _chooser.showOpenDialog( null );
        if ( returnval == JFileChooser.APPROVE_OPTION )
        {
            File newIn = _chooser.getSelectedFile();
            readPatternFile( newIn );
        }
    }
    //---------------------- nextRegex() -----------------------------
    /**
     * access the next regex in the pattern array; re-cycles when gets to end
     */
    public void nextRegex( )
    { 
        if ( _patterns != null && _patterns.size() > 0 )   // are there any?
        {
            String newRegex = _patterns.get( _nxtPattern++ );
            if ( _nxtPattern >= _patterns.size() )
            {
                _nxtPattern = 0;
                System.out.println( "*** Regex file finished, starting over ***" );
            }
            setNewRegex( newRegex );
        }
    }
    //------------------ setNewRegex( String ) --------------------------------
    /**
     * define a new regular expression -- a private utility function
     */
    public void setNewRegex( String newRE )
    {
        _regex = newRE;
        
        if( _regex1 == null )
        {
            _regex1 = new Regex();
        }
        _regex1.setRegex( _regex );
        
        //_pattern = Pattern.compile( _regex );
        _gui.setRegexLabel( _regex );
        _display.setMatch( " " ); 
    }
    //---------------------- newRegex() -----------------------------
    /**
     * User enters a new regular expression via a popup dialog
     */
    public void newRegex( )
    {  
        String newRegex = JOptionPane.showInputDialog( "Regular expression", _regex ); 
        
        if ( newRegex != null )
            setNewRegex( newRegex );
    }    //--------------------- main -----------------------------------------
    public static void main( String[] args )
    {
        String patternFileName = "patternsDNA.txt";
        String dnaFileName = "inputDNA.txt";
        if ( args.length > 0 )
            patternFileName = args[ 0 ];
        if ( args.length > 1 )
            dnaFileName = args[ 1 ];
        
        new DNAregex( "DNA Regex", patternFileName, dnaFileName );
    }
}