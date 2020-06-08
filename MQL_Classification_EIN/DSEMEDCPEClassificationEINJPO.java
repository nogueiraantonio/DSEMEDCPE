import java.io.File;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.BufferedWriter;
import java.util.*;
import java.util.Set;

import matrix.db.*;
import matrix.util.*;

import com.matrixone.apps.domain.DomainObject;

import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.ContextUtil;

public class ${CLASSNAME}
{ 
  //private String ADDINTERFACE_CHECK_MSG="addinterfaceCheck";
  private String ADDINTERFACE_ACTION_MSG="addinterfaceAction";
  
  //It "seems" that physical ID, logical ID, major ID and version ID are the same?
  private String EXCLUSION_ARRAY[] = {"ACCESSFLAG", "ALTOWNER1", "ALTOWNER2", "APPLICATION", 
  "FROMALLFLAG", "FROMALLRELFLAG", "FROMCARD", "TOALLFLAG", "TOALLRELFLAG", "TOCARD",
  "CfgDynFiltering", "ConfigFilterFactory", "CHECKACCESSFLAG", "CHECKRANGEFLAG", "ENFORCEDLOCKINGFLAG", "HOME", "HOST", "LATTICE", "LOCKFLAG", "LOGICALID", "MAJORID", "MATRIXHOME", "MX_LOGGED_IN_USER_NAME", "OBJECT", "ORGANIZATION", "PATH", "TRIGGER_VAULT", "VAULT", "VERSIONID", "WORKSPACEPATH" };

    private String STRICT_EXCLUSION_ARRAY[] = {"ATTRTYPE", "TYPE", "NAME", "PHYSICALID", "ATTRTYPEKIND", "INVOCATION", "ORIGINAL_INVOCATION", "POLICY", "REVISION", "TIMESTAMP", "OBJECTID", "OWNER", "USER", "TRANSACTION", "PROJECT"};

      
    private MatrixLogWriter m_logWriter = null;
  
      private MatrixLogWriter InitLogWriter(Context context){
	
	  String strFileName        = "DSEMEDCPE_EIN_Demo.log";
	  String strLogtype         = "";
	  boolean bAllFlag          = false;
	  return new MatrixLogWriter(context,strFileName,strLogtype,bAllFlag);
	
      }
       
     protected void log(Context ctx, String s)
     {
        if (m_logWriter == null) {
          m_logWriter = InitLogWriter(ctx);
         }
	  try {
	      m_logWriter.write(": "+s);
	  }catch(Exception ex) {
	      ex.printStackTrace(System.out);
	  }
    }
    
    
   
    private boolean ExcludeEnv(String variableName, boolean isStrict)
    {
	boolean exclude = true;
	
	for (int i = 0; i < EXCLUSION_ARRAY.length; i++)
	{
	   if (variableName.equalsIgnoreCase(EXCLUSION_ARRAY[i]))
	   {
	      return true;
	   }	
	}
	
	if (isStrict)
	{
	  for (int i = 0; i < STRICT_EXCLUSION_ARRAY.length; i++)
	  {
	    if (variableName.equalsIgnoreCase(STRICT_EXCLUSION_ARRAY[i]))
	    {
		return true;
	    }
	  }
	}
	
	return false;
    }

    private String getVariableValue(Context ctx, String variableName)
    {
	String variableValue =  "";
	
	try
	{
	   variableValue = MqlUtil.mqlCommand(ctx, "get env " + variableName);
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
	return variableValue;
    }
    
    private void printLocalVariables(Context ctx, boolean isStrict)
    {
	try
	{
	  String localEnvsRaw = MqlUtil.mqlCommand(ctx, "list env");
	
	  String localEnvs[] = localEnvsRaw.split("\\r?\\n");
	  
	  for (int i = 0; i < localEnvs.length; i++)
	  {
	    String localEnv[] =localEnvs[i].split("=");
	    
	    if (localEnv.length == 2)
	    {
	      if ((localEnv[1] != null) && !ExcludeEnv(localEnv[0], isStrict))
	      {
		  log(ctx, localEnvs[i]);
	      }
	    }
	  }
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    public void printArgs1(String methodName, Context ctx,  String[] args)
    {
	  printArgs(methodName, ctx);
    }
	
    public void printArgs(String methodName, Context ctx)
    {
	log(ctx, "********");
	log(ctx, methodName);
	log(ctx, "********");
	printLocalVariables(ctx, true);
    }
    
    public void printArgsX(String methodName, Context ctx, String variableName)
    {
        String variableValue = getVariableValue(ctx, variableName);
        
        if (variableValue == null)
        {
           variableValue = "";
        }
        
	log(ctx, "********");
	log(ctx, methodName + " [" + variableValue + " ]");
	log(ctx, "********");
	printLocalVariables(ctx, true);
    }
    
    public ${CLASSNAME}(Context ctx,String[] args) throws MatrixException
    {
	//printArgs("ctor", ctx);	
	log(ctx, "ctor");
    }

		
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

		String partNumber = GetClassificationLogic(fromType, fromName, fromRevision, sequenceId);
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
  
        } catch (MatrixException e) {
            e.printStackTrace();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

	return 0;
    }
    
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

   // TODO: Check if keyName has spaces in it or any other strange characters before using it
    // In order to best mimic an "atomic update" and avoid a race condition / concurrency issues this function does the following:
    // Wraps all code in a transaction (with wait as default)
    // 1. Checks if the keyName exists in the VPLMCounter (creates if it doesn't exist)
    // 2. If keyName exists updates the description of the record it wants to use.
    // 2.1 Once the description is set then the record is locked for the entirety of the transaction.
    // 3. The function now reads the current value of the counter and increments it.
    // 4. The transaction is committed (or aborted if there is an error).

    //DISCLAIMER: This function should not be used together with the EKL:GetUniqueKeyFromString function.
    //It is not clear if there would be conflicts in using both functions on the same keyname so
    //this function should only modify keyNames that have been created by this function.
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

    public String GetClassificationLogic(String classType, String className,  String classRevision, int sequenceId )
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

    private boolean ExistsCounter(Context ctx, String keyName) throws Exception {

        //Create if it exists
        String checkCounterExistsMQL = "temp query bus VPLMCounter \"PLMCORE/PLMReference##" + keyName + "\" --- select dump |;";
        String checkCounterExistsResponse = mql(ctx, checkCounterExistsMQL);

        if (checkCounterExistsResponse == null) return false;

        //Please note the escaping pipe preceded by 4 backslash
        String[] checkCounterExistsParser = checkCounterExistsResponse.split("\\\\|");
       
        if (!checkCounterExistsParser[0].equals("VPLMCounter")) return false;
        if (!checkCounterExistsParser[1].equals("PLMCORE/PLMReference##" + keyName)) return false;
        if (!checkCounterExistsParser[2].equals("---")) return false;

        return true;
    }

    public String mqlNoTrigger(Context ctx, String command) throws Exception
    {
        return _mql(ctx, command, true, false, true); 
    }
       
    public String mql(Context ctx, String command) throws Exception
    {
	return _mql(ctx, command, true, false, false);        
    }
    
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
	
}
