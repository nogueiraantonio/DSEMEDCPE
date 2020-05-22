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

public class ${CLASSNAME}
{
  private String ADDINTERFACE_CHECK_MSG="addinterfaceCheck";
  private String ADDINTERFACE_ACTION_MSG="addinterfaceAction";
  private String ADDOWNERSHIP_CHECK_MSG="addownershipCheck";
  private String ADDOWNERSHIP_ACTION_MSG="addownershipAction";
  private String CREATE_CHECK_MSG="createCheck";
  private String CREATE_ACTION_MSG="createAction";
  private String DELETE_CHECK_MSG="deleteCheck";
  private String DELETE_ACTION_MSG="deleteAction";
  private String FREEZE_CHECK_MSG="freezeCheck";
  private String FREEZE_ACTION_MSG="freezeAction";
  private String MODIFY_CHECK_MSG="modifyCheck";
  private String MODIFY_ACTION_MSG="modifyAction";
  private String MODIFYATTRIBUTE_CHECK_MSG="modifyattributeCheck";
  private String MODIFYATTRIBUTE_ACTION_MSG="modifyattributeAction";
  private String MODIFYFROM_CHECK_MSG="modifyfromCheck";
  private String MODIFYFROM_ACTION_MSG="modifyfromAction";
  private String MODIFYTO_CHECK_MSG="modifytoCheck";
  private String MODIFYTO_ACTION_MSG="modifytoAction";
  private String REMOVEINTERFACE_CHECK_MSG="removeinterfaceCheck";
  private String REMOVEINTERFACE_ACTION_MSG="removeinterfaceAction";
  private String REMOVEOWNERSHIP_CHECK_MSG="removeownershipCheck";
  private String REMOVEOWNERSHIP_ACTION_MSG="removeownershipAction";
  private String THAW_CHECK_MSG="thawCheck";
  private String THAW_ACTION_MSG="thawAction";
  private String TRANSACTION_CHECK_MSG="transactionCheck";
  private String TRANSACTION_ACTION_MSG="transactionAction";
 
  //It "seems" that physical ID, logical ID, major ID and version ID are the same?
  private String EXCLUSION_ARRAY[] = {"ACCESSFLAG", "ALTOWNER1", "ALTOWNER2", "APPLICATION", 
  "FROMALLFLAG", "FROMALLRELFLAG", "FROMCARD", "TOALLFLAG", "TOALLRELFLAG", "TOCARD",
  "CfgDynFiltering", "ConfigFilterFactory", "CHECKACCESSFLAG", "CHECKRANGEFLAG", "ENFORCEDLOCKINGFLAG", "HOME", "HOST", "LATTICE", "LOCKFLAG", "LOGICALID", "MAJORID", "MATRIXHOME", "MX_LOGGED_IN_USER_NAME", "OBJECT", "ORGANIZATION", "PATH", "TRIGGER_VAULT", "VAULT", "VERSIONID", "WORKSPACEPATH" };

    private String STRICT_EXCLUSION_ARRAY[] = {"ATTRTYPE", "ATTRTYPEKIND", "POLICY", "REVISION", "TIMESTAMP", "OBJECTID", "OWNER", "USER", "TRANSACTION", "PROJECT"};

  private MatrixLogWriter m_logWriter = null;
  
    	private MatrixLogWriter InitLogWriter(Context context){
	  
            String strFileName        = "DSEMEDCPE_Demo.log";
            String strLogtype         = "";
            boolean bAllFlag          = false;
            return new MatrixLogWriter(context,strFileName,strLogtype,bAllFlag);
          
       }
       
       protected void log(Context ctx, String s)  {
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
    
    public ${CLASSNAME}(Context ctx,String[] args) throws MatrixException
    {
	printArgs("ctor", ctx);
		
    }

	public void printArgs(String methodName, Context ctx)
	{
	    log(ctx, "********");
	    log(ctx, methodName);
	    log(ctx, "********");
	    printLocalVariables(ctx, true);
	    
	}
	
	public void printArgs1(String methodName, Context ctx,String[] args)
	{
	     printArgs(methodName, ctx);    
	}
	
	public int addinterfaceCheck (Context ctx, String[] args)
	{
		printArgs1(ADDINTERFACE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int addownershipCheck (Context ctx, String[] args)
	{
		printArgs1(ADDOWNERSHIP_CHECK_MSG, ctx, args);
		return 0;
	}
	public int createCheck (Context ctx, String[] args)
	{
		printArgs1(CREATE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int deleteCheck (Context ctx, String[] args)
	{
		printArgs1(DELETE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int freezeCheck (Context ctx, String[] args)
	{
		printArgs1(FREEZE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int modifyCheck (Context ctx, String[] args)
	{
		printArgs1(MODIFY_CHECK_MSG, ctx, args);
		return 0;
	}
	public int modifyattributeCheck (Context ctx, String[] args)
	{
		printArgs1(MODIFYATTRIBUTE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int modifyfromCheck (Context ctx, String[] args)
	{
		printArgs1(MODIFYFROM_CHECK_MSG, ctx, args);
		return 0;
	}
	public int modifytoCheck (Context ctx, String[] args)
	{
		printArgs1(MODIFYTO_CHECK_MSG, ctx, args);
		return 0;
	}
	public int removeinterfaceCheck (Context ctx, String[] args)
	{
		printArgs1(REMOVEINTERFACE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int removeownershipCheck (Context ctx, String[] args)
	{
		printArgs1(REMOVEOWNERSHIP_CHECK_MSG, ctx, args);
		return 0;
	}
	public int thawCheck (Context ctx, String[] args)
	{
		printArgs1(THAW_CHECK_MSG, ctx, args);
		return 0;
	}
	public int transactionCheck (Context ctx, String[] args)
	{
		printArgs1(TRANSACTION_CHECK_MSG, ctx, args);
		return 0;
	}
	
	//actions
	public int addinterfaceAction (Context ctx, String[] args)
	{
		printArgs1(ADDINTERFACE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int addownershipAction (Context ctx, String[] args)
	{
		printArgs1(ADDOWNERSHIP_ACTION_MSG, ctx, args);
		return 0;
	}
	public int createAction (Context ctx, String[] args)
	{
		printArgs1(CREATE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int deleteAction (Context ctx, String[] args)
	{
		printArgs1(DELETE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int freezeAction (Context ctx, String[] args)
	{
		printArgs1(FREEZE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int modifyAction (Context ctx, String[] args)
	{
		printArgs1(MODIFY_ACTION_MSG, ctx, args);
		return 0;
	}
	public int modifyattributeAction (Context ctx, String[] args)
	{
		printArgs1(MODIFYATTRIBUTE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int modifyfromAction (Context ctx, String[] args)
	{
		printArgs1(MODIFYFROM_ACTION_MSG, ctx, args);
		return 0;
	}
	public int modifytoAction (Context ctx, String[] args)
	{
		printArgs1(MODIFYTO_ACTION_MSG, ctx, args);
		return 0;
	}
	public int removeinterfaceAction (Context ctx, String[] args)
	{
		printArgs1(REMOVEINTERFACE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int removeownershipAction (Context ctx, String[] args)
	{
		printArgs1(REMOVEOWNERSHIP_ACTION_MSG, ctx, args);
		return 0;
	}
	public int thawAction (Context ctx, String[] args)
	{
		printArgs1(THAW_ACTION_MSG, ctx, args);
		return 0;
	}
	public int transactionAction (Context ctx, String[] args)
	{
		printArgs1(TRANSACTION_ACTION_MSG, ctx, args);
		return 0;
	}

}
