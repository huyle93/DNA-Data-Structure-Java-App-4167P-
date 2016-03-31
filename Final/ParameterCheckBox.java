/** 
 * ParameterCheckbox.java:
 * An abstract class representing a checkbox that displays a label 
 * and simplifies access to the checkbox.
 * 
 * Child classes must override the valueChanged method.
 * 
 * @author Matthew Plumlee
 * CS416 Spring 2008
 */

import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

abstract public class ParameterCheckBox extends JCheckBox 
{
   //------------------- instance variables ------------------------
   //------------------- constructor --------------------------------
   /**
    * Accepts name for box and default state.
    */
   public ParameterCheckBox ( String name, boolean defaultVal ) 
   {
      super( name, defaultVal );
      
      addChangeListener( new ParameterListener() );
      updateUI();
   }

   //-------------------- valueChanged( boolean ) ---------------------
   abstract protected void valueChanged( boolean newValue );
   
   //+++++++++++++++++++ ParameterListener inner class +++++++++++++++++
   /**
    * A CheckBox listener
    */
   private class ParameterListener implements ChangeListener 
   {
      // no constructor needed -- super default works
      //------------------------ stateChanged ----------------------------
      public void stateChanged( ChangeEvent e ) {
         valueChanged( ((JCheckBox)e.getSource()).isSelected() );
      }
   }
}