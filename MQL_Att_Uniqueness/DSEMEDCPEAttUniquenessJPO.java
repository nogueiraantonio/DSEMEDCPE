// Copyright © 2020 Dassault Systèmes, Euromed, Customer Process Experience
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
// and associated documentation files (the “Software”), to deal in the Software without restriction, 
// including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
// and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all copies 
// or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
//INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
//PURPOSE AND NONINFRINGEMENT. 
//IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR 
//OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT 
//OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

import java.io.File;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.BufferedWriter;
import java.util.*;
import java.util.Set;

import matrix.db.*;
import matrix.util.*;

import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.ContextUtil;

public class ${CLASSNAME}
{ 
    private MatrixLogWriter m_logWriter = null;
  
    /**
    * ctor.
    *
    * @param  ctx the session context
    * @param  args trigger arguments
    * @return
    */
    public ${CLASSNAME}(Context ctx,String[] args) throws MatrixException
    {		
	log(ctx, "ctor");
    }

    /**
    * Checks the uniqueness of a given attribute value.
    *
    * SETUP
    * a) Define this as a Java deferred program JPO in the 3DEXPERIENCE database.
    * b) Link this to an modifyattribute check trigger against the VPMReference type.
    *
    * 
    * @param  ctx the session context
    * @param  args trigger arguments
    * @return      0 success, anything else failure
    */	
    public int modifyattributeCheck (Context ctx, String[] args) throws MatrixException
    {    
		log(ctx, "modifyattributeCheck");
	
        String objectID      = args[0];  //OBJECTID;
        String objectType    = args[1];  //TYPE;
        
		String attrName      = args[6];  //ATTRNAME;
		String attrValue     = args[7];  //ATTRVALUE;
		String newAttrValue  = args[8];  //NEWATTRVALUE;
			
		if ((objectID == null) || (attrName == null)) return 0;
	
		//debugging
		//log(ctx, "objectID=" + objectID );
		//log(ctx, "attrName=" + attrName );
		//log(ctx, "attrValue=" + attrValue );
		//log(ctx, "newAttrValue=" + newAttrValue );

		if (!IsAttributeUniquenessCheckRequired(ctx, objectType, attrName)) return 0;
	 
		//debugging
		//log(ctx,  "AttributeValueMatch ( '" + objectType + "', '" + attrName + "', '" + newAttrValue + "')" );
		
		if (IsAttributeValueMatch(ctx, objectType, attrName, newAttrValue))
		{
          //Can use an NLS resource for the return string. As an example: EnoviaResourceBundle.getProperty(ctx, "emxShopperStringResource", ctx.getLocale(), "emxShopper.Attribute.AssetGondolaSection")
		  throw new MatrixException("The value '" + newAttrValue + "' is not unique for Title");
		}	     

		return 0;
    }
    
    /**
    * Checks if the attribute value, for the specific business type, needs to be checked for uniqueness.
    *
    * Note that this is just an demo and doesn't necessarily reflect production code.
    *  
    * @param  busObjType the business object type (e.g. VPMReference)
    * @param  attrName  the attribute name
    * @return      true if the business logic requires the pair busObjType/attrName to be further checked, false otherwise
    */
    public boolean IsAttributeUniquenessCheckRequired(Context ctx, String busObjType, String attrName)
    {
      if (busObjType.equals("VPMReference") && attrName.equals("PLMEntity.V_Name"))
      {
		//Note: Can also include an additional condition to temporarely override this behaviour for some reason
		return true;      
      }      
      
	  return false;
    }
    
    /**
    * Checks if the attribute value, for the specific business type, matches (smatch) any already existing value.
    *
    * Documentation for 2020X - https://help.3ds.com/2020x/english/dsdoc/mqlreferencemap/mql-r-where-clause.htm?contextscope=all
    *
    * Note that this is just an demo and doesn't necessarily reflect production code.
    *  
    * @param  busObjType the business object type (e.g. VPMReference)
    * @param  attrName  the attribute name (of type attribute[])
    * @param  attrValue the matching value to check  
    * @return      true if there is a match, false otherwise
    */
    public boolean IsAttributeValueMatch(Context ctx, String busObjType, String attrName, String attrValue) 
    {         
        String attrValueMatchQuery = "temp query bus " + busObjType + " * * where 'attribute[" + attrName + "] smatch \"" + attrValue + "\"' select id dump |;";
        
        try
        {
            log(ctx, attrValueMatchQuery);
            String attrValueMatchQueryResult = mql(ctx, attrValueMatchQuery);

            log(ctx, attrValueMatchQueryResult);
            String[] attrValueMatchQueryResultParser = attrValueMatchQueryResult.split("\\\\|");

            if ((attrValueMatchQueryResultParser != null) && (attrValueMatchQueryResultParser.length >= 4))
			{
			  return true;
			}
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return false;
    }
    
    /**
    * Invokes an MQL command with the default behaviour.
    *
    * @param  ctx the session context
    * @param  command the actual MQL command string
    * @return      the MQL command output
    */
    public String mql(Context ctx, String command) throws Exception
    {
	return _mql(ctx, command, true, false, false);        
    }
    
    
    /**
    * Invokes an MQL command with controlled behaviour.
    *
    * @param  ctx the session context
    * @param  command the actual MQL command string
    * @param  allowMultipleOverride 
    * @param  historyOff if true it doesn't include history 
    * @param  triggerOff if true it doesn't fire triggers
    * @return      the MQL command output
    */
    private String _mql(Context ctx, String command, boolean allowMultipleOverride, boolean historyOff, boolean triggerOff) throws Exception
    {
        MQLCommand mqlCommand = new MQLCommand();

        if (!mqlCommand.executeCommand(ctx, command, allowMultipleOverride, historyOff, triggerOff)) {
            
            throw new Exception (mqlCommand.getError());
        }

        /* Parse Result */
        String mqlCommandResult = mqlCommand.getResult();
        if ((mqlCommandResult != null) && mqlCommandResult.endsWith("\n")) {
            mqlCommandResult = mqlCommandResult.substring(0, mqlCommandResult.length() - 1);
        }
        return mqlCommandResult;
    }
        
    /**
    * Logs a message to the log file.
    *
    * @param  ctx the session context
    * @param  s  message
    * @return 
    */
    protected void log(Context ctx, String message)
    {
	if (m_logWriter == null) 
        {
	    m_logWriter = InitLogWriter(ctx);
        }
	
	try 
	{
	    m_logWriter.write(": " + message);
	}
	catch(Exception ex) 
	{
	    ex.printStackTrace(System.out);
	}
    }
    
    /**
    * Initiates the logger.
    *
    * @param  ctx the session context
    * @return the initiated logger
    */
    private MatrixLogWriter InitLogWriter(Context ctx){
      
	String strFileName        = "DSEMEDCPE_ATU_Demo.log";
	String strLogtype         = "";
	boolean bAllFlag          = false;
	return new MatrixLogWriter(ctx,strFileName,strLogtype,bAllFlag);
      
    }
}
