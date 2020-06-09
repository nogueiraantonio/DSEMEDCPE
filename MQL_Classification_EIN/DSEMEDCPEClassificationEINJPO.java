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
    private String ADDINTERFACE_ACTION_MSG="addinterfaceAction";  
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
    * Assigns a Enterprise Item Number based on a Classification interface.
    *
    * SETUP
    * a) Define this as a Java deferred program JPO in the 3DEXPERIENCE database.
    * b) Link this to an addinterface action trigger against the VPMReference type.
    *
    * This function checks first if the interface being added is a Classification interface.
    * If it is a Classification interface then it checks if the VPMReference already has an EnterpriseExtension.
    * It attaches an EnterpriseExtension if it is not attached.
    * It increments a sequence counter unique for that Classification interface
    * Uses a specific logic to generate the item number (to be defined)
    * Attaches the item number to the VPMReference.
    * 
    * @param  ctx the session context
    * @param  args trigger arguments
    * @return      0 success, anything else failure
    */	
    public int addinterfaceAction (Context ctx, String[] args)
    {
        final String ENTERPRISE_EXTENSION = "EnterpriseExtension";
        final String PART_NUMBER = ENTERPRISE_EXTENSION + ".V_PartNumber";
        final String CLASSIFIED_ITEM = "Classified Item";
    
        String objectID     = args[0];  //OBJECTID;
	String relType      = args[6];  //RELTYPE;
	String fromType     = args[7];  //FROMTYPE;
	String fromName     = args[8];  //FROMNAME;
	String fromRevision = args[9]; //FROMREVISION 
	String newInterface = args[10]; //NEWINTERFACE
	
	//this should be the same as the mxsysInterface attribute value;
	log(ctx,  "addInterfaceAction ( " + objectID + ")" );

	if ((objectID == null) || (relType == null) || (fromType == null) || (fromName == null) || (fromRevision == null) || (newInterface == null)) return 0;
	
	if (!relType.equalsIgnoreCase(CLASSIFIED_ITEM)) return 0;
	
	if (!IsClassificationInterface(ctx, newInterface)) return 0;
   
        try {
        
	    BusinessObject businessObject = new BusinessObject(objectID);
	    businessObject.open(ctx);

            BusinessInterfaceList interfaces = businessObject.getBusinessInterfaces(ctx, false);

            //Assuming that interfaces will always be != null even if there are no interfaces attached to the object
            //Test this assumption
            if (interfaces != null)
            {		
                BusinessInterface enterpriseExtensionInterface = interfaces.find(ENTERPRISE_EXTENSION);

                if (enterpriseExtensionInterface == null)
                {		  
                    // Unfortunately calling the addBusinessInterface method below issues a trigger. 
                    // This is not needed. For better performance we issue a straight mql command with triggers off.
                    //...
                    //BusinessInterface enterpriseExtension = new BusinessInterface(ENTERPRISE_EXTENSION, null);
                    //businessObject.addBusinessInterface(ctx,enterpriseExtension );
                    
                    //Add Enterprise Extension interface
                    String addEnterpriseExtension = "mod bus " + businessObject.getTypeName() + " \"" + businessObject.getName() + "\" " + businessObject.getRevision() + " add interface \"" + ENTERPRISE_EXTENSION +  "\";";
             
                    log(ctx, addEnterpriseExtension);
	   
                    mqlNoTrigger(ctx, addEnterpriseExtension);
                }

                AttributeList enterprisePartNumberList = businessObject.getAttributes(ctx, StringList.asList(PART_NUMBER));

                if (enterprisePartNumberList.size() != 1)
                  throw new Exception("Number of Enterprise Part Number attributes is not one");

                Attribute enterprisePartNumberAtt = enterprisePartNumberList.get(0);
                if (enterprisePartNumberAtt == null)
                  throw new Exception("Enterprise Part Number Attribute is null");

                String enterprisePartNumber = enterprisePartNumberAtt.getValue();
               
		int sequenceId = DSEMEDCPEGetUniqueKeyFromStringJPO(ctx, fromRevision);		
                log(ctx, "sequenceId = " + sequenceId);

		String partNumber = GetEnterprisePartNumber(fromType, fromName, fromRevision, sequenceId);
	        log(ctx, "partNumber = " + partNumber);
	
		//Update Attribute value
		
		//3DSpace does not support nested transactions
		//ctx.start(true);
		
		AttributeType partNumberAttributeType = new AttributeType(PART_NUMBER);
		Attribute partNumberAttribute = new Attribute(partNumberAttributeType, partNumber);
		AttributeList newAttributeList = new AttributeList();
		newAttributeList.addElement(partNumberAttribute);
		
		businessObject.setAttributes(ctx, newAttributeList);
		
		//3DSpace does not support nested transactions
		//ctx.commit();               
            }
        } 
        catch (MatrixException e) 
        {
            e.printStackTrace();
            return 1;
        } 
        catch (Exception e) {
           e.printStackTrace();
           return 1;
        }

	return 0;
    }
    
    /**
    * Gets the final Item Number based on the Classification attributes and on a unique sequence id for the classification interface. 
    *
    * Note that this is just an example and changing the logic in this function might be sufficient for most situations
    * This can be further extended to be more flexible and read from a table or file.
    *
    * @param  classType  the Classification type (e.g. General Class)
    * @param  className  the Classification name in the UI (e.g. Engineering Template)
    * @param  classRevision the Classification revision
    * @param  sequenceId a unique sequence Id for the class type, name and revision
    * @return      the item number string
    */
    public String GetEnterprisePartNumber(String classType, String className,  String classRevision, int sequenceId )
    {
        final String GENERAL_CLASS = "General Class";
        final String ENG_TEMPLATE = "Engineering Template";

        if (classType.equals(GENERAL_CLASS))
        {
            if (className.equals(ENG_TEMPLATE))
            {
                return "GC.ET-" + sequenceId;
            }
            else
            {
                return "GC-" + sequenceId;
            }
        }
        else
        {
            return "-" + sequenceId;
        }
    }
    
    /**
    *
    * Checks if the interface name is a Classification interface.
    * 
    * Compares the interface name against the mxsysInterface attribute of all Classification business objects.
    *
    * @param  ctx the session context
    * @param  interfaceName the interface id to check
    * @return      returns true if the classification interface exists
    */
    public boolean IsClassificationInterface(Context ctx, String interfaceName)
    {
        String classificationInterfaceQuery = "temp query bus Classification * * where attribute[mxsysInterface]==\"" + interfaceName + "\" select attribute[mxsysInterface] dump |"; //

        try
        {
            log(ctx, classificationInterfaceQuery);
            String classificationInterfaceResult = mql(ctx, classificationInterfaceQuery);

            log(ctx, classificationInterfaceResult);
            String[] classificationInterfaceResultParser = classificationInterfaceResult.split("\\\\|");

            if ((classificationInterfaceResultParser != null) && (classificationInterfaceResultParser.length == 4) &&
                    classificationInterfaceResultParser[3].equals(interfaceName))
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
    * Increments and returns the value of a named counter.
    *
    * Similar in behaviour to the EKL GetUniqueKeyFromString function.
    *
    * TODO : Validate keyName before using it. E.g. if it has spaces or any other conflictin strange characters before using it
    *
    * DISCLAIMER: This function was not tested together with the EKL GetUniqueKeyFromString function.
    * It is not clear if there would be conflicts in using both functions on the same keyname so
    * this function should only modify keyNames that have been created by this function.
    *
    * @param  ctx the session context
    * @param  keyName  the Counter name
    * @return      the counter value
    */
    private int DSEMEDCPEGetUniqueKeyFromStringJPO(Context ctx, String keyName) throws Exception {

        final String KEY_NAME_PREFIX = "DSEMEDCPE_";
        final String KEY_NAME = KEY_NAME_PREFIX + keyName;

        int counterValueReturn = -1;
        int counterInitialValue = 1;

	//3DSpace does not support nested transactions.
	//if (ContextUtil.startTransaction(ctx, true))
	//{
	try {
	
	    String getCounterValueMQL = "print bus VPLMCounter \"PLMCORE/PLMReference##" + KEY_NAME + "\" --- select attribute[VPLMsys/AutoNamingValue] dump;";

	    // Setting nowait is not required. The incrementing processing should be very fast
	    // and I am expecting that the underlying mechanism correctly handles the queue of
	    // processes that require exclusive access to the resources
	    //MQLCommandUtil.Execute(ctx, "set transaction nowait;");
	    if (!ExistsCounter(ctx, KEY_NAME)) {                    
		//Create it it doesn't exist
		String createCounterMQL = "add bus VPLMCounter \"PLMCORE/PLMReference##" + KEY_NAME + "\" --- policy VPLMAutoNaming_Policy VPLMsys/AutoNamingValue" + " " + counterInitialValue + ";";
		mql(ctx, createCounterMQL);                    
	    }
						    
	    //The first thing I try to do is to modify the description of the field
	    //This acts as an immediate lock on the whole object.
	    Date date = new Date();

	    String updateTempDescriptionMQL = "mod bus VPLMCounter \"PLMCORE/PLMReference##" + KEY_NAME + "\" --- Description \"lock " + date.toString() + "\" ;";
	    mql(ctx, updateTempDescriptionMQL);                           
		    
	    //Update if if exists
	    String getCounterValueResponse = mql(ctx, getCounterValueMQL);
	    int counterValue = Integer.parseInt(getCounterValueResponse);
	    counterValue++;
	      
	    String updateCounterValueMQL = "mod bus VPLMCounter \"PLMCORE/PLMReference##" + KEY_NAME + "\" --- VPLMsys/AutoNamingValue " + counterValue + ";";
	    mql(ctx, updateCounterValueMQL);
			    
	    //TODO: Set Description back to empty
	    //String updateTempDescriptionMQL = "mod bus VPLMCounter \"PLMCORE/PLMReference##" + keyName + "\" --- Description \"" + date.toString() + "\" ;";
	    //MQLCommandUtil.Execute(ctx, updateTempDescriptionMQL);
	    String getReturnCounterValueResponse = mql(ctx, getCounterValueMQL);
	    counterValueReturn = Integer.parseInt(getReturnCounterValueResponse);
	  
	  //3DSpace does not support nested transactions
	  //ContextUtil.commitTransaction(ctx);

	} 
	catch (Exception ex) 
	{
	    //3DSpace does not support nested transactions
	    //ContextUtil.abortTransaction(ctx);
	    ex.printStackTrace();
	    
	    throw ex;
	}            

       return counterValueReturn;
    }

    /**
    * Checks if a named counter exists.
    *
    * @param  ctx  the session context
    * @param  keyName the name of the counter
    * @return      true if the counter exists false otherwise
    */
    private boolean ExistsCounter(Context ctx, String keyName) throws Exception 
    {
        String checkCounterExistsMQL = "temp query bus VPLMCounter \"PLMCORE/PLMReference##" + keyName + "\" --- select dump |;";
        String checkCounterExistsResponse = mql(ctx, checkCounterExistsMQL);

        if (checkCounterExistsResponse == null) return false;

        //Please note the escaping pipe preceded by 4 backslash (linux - in Windows it might be different)
        String[] checkCounterExistsParser = checkCounterExistsResponse.split("\\\\|");
       
        if (!checkCounterExistsParser[0].equals("VPLMCounter")) return false;
        if (!checkCounterExistsParser[1].equals("PLMCORE/PLMReference##" + keyName)) return false;
        if (!checkCounterExistsParser[2].equals("---")) return false;

        return true;
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
    * Invokes an MQL command without firing triggers.
    *
    * @param  ctx the session context
    * @param  command the actual MQL command string
    * @return      the MQL command output
    */
    public String mqlNoTrigger(Context ctx, String command) throws Exception
    {
        return _mql(ctx, command, true, false, true); 
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
      
	String strFileName        = "DSEMEDCPE_EIN_Demo.log";
	String strLogtype         = "";
	boolean bAllFlag          = false;
	return new MatrixLogWriter(ctx,strFileName,strLogtype,bAllFlag);
      
    }
}
