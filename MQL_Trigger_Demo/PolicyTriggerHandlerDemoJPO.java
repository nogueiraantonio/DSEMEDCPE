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
  private String APPROVE_CHECK_MSG="approveCheck";
  private String APPROVE_ACTION_MSG="approveAction";
  private String DEMOTE_CHECK_MSG="demoteCheck";
  private String DEMOTE_ACTION_MSG="demoteAction";
  private String DISABLE_CHECK_MSG="disableCheck";
  private String DISABLE_ACTION_MSG="disableAction";
  private String ENABLE_CHECK_MSG="enableCheck";
  private String ENABLE_ACTION_MSG="enableAction";
  private String IGNORE_CHECK_MSG="ignoreCheck";
  private String IGNORE_ACTION_MSG="ignoreAction";
  private String OVERRIDE_CHECK_MSG="overrideCheck";
  private String OVERRIDE_ACTION_MSG="overrideAction";
  private String PROMOTE_CHECK_MSG="promoteCheck";
  private String PROMOTE_ACTION_MSG="promoteAction";
  private String REJECT_CHECK_MSG="rejectCheck";
  private String REJECT_ACTION_MSG="rejectAction";
  private String SCHEDULE_CHECK_MSG="scheduleCheck";
  private String SCHEDULE_ACTION_MSG="scheduleAction";
  private String UNSIGN_CHECK_MSG="unsignCheck";
  private String UNSIGN_ACTION_MSG="unsignAction";

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
	   
	   if (isStrict && variableName.equalsIgnoreCase(STRICT_EXCLUSION_ARRAY[i]))
	   {
	      return true;
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
	
	public int approveCheck (Context ctx, String[] args)
	{
		printArgs1(APPROVE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int demoteCheck (Context ctx, String[] args)
	{
		printArgs1(DEMOTE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int disableCheck (Context ctx, String[] args)
	{
		printArgs1(DISABLE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int enableCheck (Context ctx, String[] args)
	{
		printArgs1(ENABLE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int ignoreCheck (Context ctx, String[] args)
	{
		printArgs1(IGNORE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int overrideCheck (Context ctx, String[] args)
	{
		printArgs1(OVERRIDE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int promoteCheck (Context ctx, String[] args)
	{
		printArgs1(PROMOTE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int rejectCheck (Context ctx, String[] args)
	{
		printArgs1(REJECT_CHECK_MSG, ctx, args);
		return 0;
	}
	public int scheduleCheck (Context ctx, String[] args)
	{
		printArgs1(SCHEDULE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int unsignCheck (Context ctx, String[] args)
	{
		printArgs1(UNSIGN_CHECK_MSG, ctx, args);
		return 0;
	}
	public int approveAction (Context ctx, String[] args)
	{
		printArgs1(APPROVE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int demoteAction (Context ctx, String[] args)
	{
		printArgs1(DEMOTE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int disableAction (Context ctx, String[] args)
	{
		printArgs1(DISABLE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int enableAction (Context ctx, String[] args)
	{
		printArgs1(ENABLE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int ignoreAction (Context ctx, String[] args)
	{
		printArgs1(IGNORE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int overrideAction (Context ctx, String[] args)
	{
		printArgs1(OVERRIDE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int promoteAction (Context ctx, String[] args)
	{
		printArgs1(PROMOTE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int rejectAction (Context ctx, String[] args)
	{
		printArgs1(REJECT_ACTION_MSG, ctx, args);
		return 0;
	}
	public int scheduleAction (Context ctx, String[] args)
	{
		printArgs1(SCHEDULE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int unsignAction (Context ctx, String[] args)
	{
		printArgs1(UNSIGN_ACTION_MSG, ctx, args);
		return 0;
	}
		
}
