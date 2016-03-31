/**
 * Regex - this class encapsulates some of Java's regular expression behavior
 *         into a single class.
 * @author rdb
 * Last Modified: April 2014
 */
import java.util.regex.*;
import java.io.*;
import java.text.*;

public class Regex
{
    //--------------------- instance variables ----------------------------
    
    private Pattern          _pattern;
    private Matcher          _matcher;
    
    private String           _regexString;    // currently active reg expr
    public String           _inputString;
    private StringBuilder _strBuilder;
    
    //////////////////////////////////////////////////////////////////
    // Step 1a. Initialize _flags to Pattern.MULTILINE
    //////////////////////////////////////////////////////////////////
    private int _flags = Pattern.MULTILINE;
    
    
    //---------------------- constructor ------------------------------
    /**
     * Build a Regex object; none of require information is available yet.
     */
    public Regex()
    {
        _regexString = null;
        _inputString   = null;
        _pattern     = null;
        _matcher     = null;
    }
    //---------------------- setRegex( String) ------------------------
    /**
     * Given a regular expression as a string, create a Pattern object
     * and also a Matcher if we have an input string already.
     */
    public void setRegex( String regex )
    {  
        ///////////////////////////////////////////////////////////////
        // Step 1b.
        //   If the argument is not null and has length > 0, 
        //      - save it in the instance variable _regexString
        //      - invoke compile static method of Pattern with the
        //        regex string and _flags as arguments.
        //      - assign the returned pattern object to the instance
        //        variable _pattern.
        //      - call the private method "makeMatcher" to make a Matcher
        //        object if it can.
        ///////////////////////////////////////////////////////////////
        if( regex.length() > 0 )
        {
            _regexString = regex;
            Pattern pat = Pattern.compile( _regexString, _flags );
            _pattern = pat;
            makeMatcher();
        }
        
    }
    //------------------ setInput() ------------------------
    /**
     * Save the new input, create a Matcher if you have a pattern 
     */
    public void setInput( String testString )
    {
        /////////////////////////////////////////////////////////
        // Step 1c.
        //   - Save the parameter in the instance variable _inputString
        //   - Call the private makeMatcher method to create a Matcher
        //     object if it can.
        /////////////////////////////////////////////////////////
        _inputString = testString;
        makeMatcher();
//        if( _pattern != null )
//        {
//            Matcher m = _pattern.matcher( testString );
//        }
       
            
    }
    
    //------------------ makeMatcher() ------------------------
    /**
     * If an input string and pattern have been defined, use them
     *    to create a Matcher object.
     */
    public void makeMatcher(  )
    {
        /////////////////////////////////////////////////////////////
        // Step 1d.
        //   - if _pattern has been created and an _inputString specified
        //     make a Matcher object by calling the "matcher" method using
        //     _pattern. Assign it to the _matcher instance variable.
        //////////////////////////////////////////////////////////////
        if( _pattern != null && _inputString != null )
        {
            _matcher = _pattern.matcher( _inputString );
        }
    
    }
    //---------------------- find() -----------------------------
    /**
     * return the next match (not all of them)
     */
    public String find( )
    {  
        /////////////////////////////////////////////////////////////
        // Step 1e.
        //     IF the matcher is not defined print an error message to 
        //        System.err.
        //     else
        //        call the find method of the Matcher object
        //        if it returns true
        //            call the group() method of the matcher object
        //            and return the results.
        //        else 
        //            return null
        /////////////////////////////////////////////////////////////
        String results = null;
         if( _matcher == null )
        {
          System.err.println( "Error" );
        }
        else
        {
          
          if( _matcher.find() )
          {
              results = _matcher.group( ) + "\n";
              return results;
          }
          
          else
          {
            return null;
          }
        }
        
        
        
        return results;
    }
    
    //------------------ findAll() ------------------------
    /**
     * Find all matches in the input string to the pattern
     *  Return all matches in one string separated by line feeds.
     */
    public String findAll()
    {
        ///////////////////////////////////////////////////////////////////
        // Step 2.
        //    IF matcher has not been created, 
        //       print an error message to System.err 
        //       return null
        //    else
        //      keep calling find method of matcher until it returns false.
        //      For each true match, 
        //        -call the group() method of matcher
        //           (it  returns the complete string matched by the pattern)
        //        -add string returned from group() plus \n with previous
        //    return string of all matches (which could be null)
        //////////////////////////////////////////////////////////////////
        String results = "";
        StringBuffer _strBuffer = new StringBuffer();

        //System.out.println( _pattern + "< and > " + _inputString );
        if ( _matcher == null )
        {
            System.err.println( "ERROR: _matcher not define!");
        }
        else
        {
          
            while( _matcher.find() )
            {
                results = _matcher.group();
                _matcher.appendReplacement( _strBuffer, results.toUpperCase() );
                results = _strBuffer.toString();
                //System.out.println( "Findall = " + results );
                
            }
            
            System.out.println( "Findall: _inputString = " + _inputString );
            results +=  _inputString.substring( results.length() );
        }

        return results;
    }
    //------------------ split() ------------------------
    /**
     * split the input string according to the r.e.
     */
    public String split()
    {
        /////////////////////////////////////////////////////////////
        // Step 3:
        //   IF the pattern is defined and the inputString is defined
        //       Use the split method on the pattern 
        //           it returns an array of strings.
        //       You need to concatenate them together  (with \n) into a 
        //       single string and return it.
        /////////////////////////////////////////////////////////////
        String results = "";

        if( _pattern != null && _inputString != null )
        {
          String[] boo = _pattern.split( _inputString );
          
          for( int i = 0; i < boo.length; i++ )
          {
            results += boo[ i ] + "\n";
          }
        }
        
        
        
        return results;
    }
    //---------------------- group() -----------------------------
    /**
     * return the entire match string from previous match
     */
    public String group( )
    {  
        //////////////////////////////////////////////////////
        // Step 4a
        // IF matcher not defined
        //    print an error message to System.err
        // ELSE need to return the results of calling the Matcher
        //      method, group(), 
        //
        // EXCEPT need to be sure that a match has occurred.
        // 2 choices:
        //   1. Make an instance variable that "remembers" whether
        //      the last match operation succeeded or not and set
        //      that everywhere in the program
        //   2. Put the group() call in a try - catch that will
        //      catch the IllegalStateException.
        // Return null if previous match failed
        //////////////////////////////////////////////////////
            
        String results = null;

        
        if ( _matcher == null )
        {
            System.err.println( "ERROR: Matcher not defined! ");
        }
        else
        {
            try 
            {
                results = _matcher.group();
            } 
            catch ( IllegalStateException e )
            {
                return null;
            }
        }

        return results;
    }
    //---------------------- group( int ) -----------------------------
    /**
     * return i-th group from previous match
     */
    public String group( int i )
    {  
        //////////////////////////////////////////////////////////////
        // Step 4b. 
        // IF matcher is not defined
        //    return null
        // ELSE
        //    Need to return the results from calling the 
        //         Matcher method, group( i ). 
        //
        //    if there is no group i, return null.
        //
        // The problem is to avoid an IndexOutOfBoundsException. This
        // is thrown if the i-th group doesn't exist or if it did not 
        // match anything in the current test string.
        //
        // There are 2 ways to do this: 
        // 1. Use the groupCount() method.
        //    Note: although group( 0 ) returns the ENTIRE match, this match
        //          is not included in groupCount.
        // 2. Put your invocation of group( i ) in a try block and catch the
        //    IndexOutOfBound exception if it is thrown.
        //
        // You also have to worry about whether the previous match failed
        //    as described in the group() method above, in which case an
        //    IllegalStateException is thrown.
        ///////////////////////////////////////////////////////////////////
        
        String results = null;
 
        if ( _matcher == null )
        {
            return null;
        }
        else
        {
            try 
            {
                if( _matcher.group( i )  != null )
                {
                    results = _matcher.group( i );
                } 
                else
                {
                    return null;
                }
            } 
            catch ( IndexOutOfBoundsException  e )
            {
                System.err.println( "ERROR: IndexOutOfBoundsException " );
            } 
        }
        
        
        return results;
    }
    
    
    
    //---------------------- setFlags( int ) ---------------------------
    /**
     * some flags have changed, need to recompile the pattern and reset
     */
    public void setFlags( int flags )
    {
        ////////////////////////////////////////////////////////////////
        // Step 6.  
        //    - assign the "flags" parameter to the _flags instance variable
        //    - call setRegex with the existing _regexString
        //
        // Hint: if your flags don't seem to work
        ////////////////////////////////////////////////////////////////   
 
        _flags = flags;
        setRegex( _regexString );
        
        
        
    }
    //---------------------- reset() -----------------------------
    /**
     * reset the regular expression processing on the current input string.
     * So, next matching will start over again on the same input string.
     */
    public void reset( )
    {  
        if ( _matcher == null )
            System.err.println( "*** reset Error *** matcher is not defined." );
        else
            _matcher.reset();
    }
    //--------------------- main -----------------------------------------
    public static void main( String[] args )
    {
        //new RegexInJava( "Regex in Java", args );
    }
}
