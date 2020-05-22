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
  private String BATCHCLONE_CHECK_MSG="batchcloneCheck";
  private String BATCHCLONE_ACTION_MSG="batchcloneAction";
  private String BATCHREVISE_CHECK_MSG="batchreviseCheck";
  private String BATCHREVISE_ACTION_MSG="batchreviseAction";
  private String CHANGENAME_CHECK_MSG="changenameCheck";
  private String CHANGENAME_ACTION_MSG="changenameAction";
  private String CHANGEOWNER_CHECK_MSG="changeownerCheck";
  private String CHANGEOWNER_ACTION_MSG="changeownerAction";
  private String CHANGEPOLICY_CHECK_MSG="changepolicyCheck";
  private String CHANGEPOLICY_ACTION_MSG="changepolicyAction";
  private String CHANGETYPE_CHECK_MSG="changetypeCheck";
  private String CHANGETYPE_ACTION_MSG="changetypeAction";
  private String CHANGEVAULT_CHECK_MSG="changevaultCheck";
  private String CHANGEVAULT_ACTION_MSG="changevaultAction";
  private String CHECKIN_CHECK_MSG="checkinCheck";
  private String CHECKIN_ACTION_MSG="checkinAction";
  private String CHECKOUT_CHECK_MSG="checkoutCheck";
  private String CHECKOUT_ACTION_MSG="checkoutAction";
  private String CONNECT_CHECK_MSG="connectCheck";
  private String CONNECT_ACTION_MSG="connectAction";
  private String COPY_CHECK_MSG="copyCheck";
  private String COPY_ACTION_MSG="copyAction";
  private String CREATE_CHECK_MSG="createCheck";
  private String CREATE_ACTION_MSG="createAction";
  private String DELETE_CHECK_MSG="deleteCheck";
  private String DELETE_ACTION_MSG="deleteAction";
  private String DISCONNECT_CHECK_MSG="disconnectCheck";
  private String DISCONNECT_ACTION_MSG="disconnectAction";
  private String GRANT_CHECK_MSG="grantCheck";
  private String GRANT_ACTION_MSG="grantAction";
  private String IMPLICITCOPY_CHECK_MSG="implicitcopyCheck";
  private String IMPLICITCOPY_ACTION_MSG="implicitcopyAction";
  private String IMPLICITDELETE_CHECK_MSG="implicitdeleteCheck";
  private String IMPLICITDELETE_ACTION_MSG="implicitdeleteAction";
  private String IMPLICITMAJORREVISION_CHECK_MSG="implicitmajorrevisionCheck";
  private String IMPLICITMAJORREVISION_ACTION_MSG="implicitmajorrevisionAction";
  private String IMPLICITMINORREVISION_CHECK_MSG="implicitminorrevisionCheck";
  private String IMPLICITMINORREVISION_ACTION_MSG="implicitminorrevisionAction";
  private String LOCK_CHECK_MSG="lockCheck";
  private String LOCK_ACTION_MSG="lockAction";
  private String MAJORREVISION_CHECK_MSG="majorrevisionCheck";
  private String MAJORREVISION_ACTION_MSG="majorrevisionAction";
  private String MINORREVISION_CHECK_MSG="minorrevisionCheck";
  private String MINORREVISION_ACTION_MSG="minorrevisionAction";
  private String MODIFY_CHECK_MSG="modifyCheck";
  private String MODIFY_ACTION_MSG="modifyAction";
  private String MODIFYATTRIBUTE_CHECK_MSG="modifyattributeCheck";
  private String MODIFYATTRIBUTE_ACTION_MSG="modifyattributeAction";
  private String MODIFYDESCRIPTION_CHECK_MSG="modifydescriptionCheck";
  private String MODIFYDESCRIPTION_ACTION_MSG="modifydescriptionAction";
  private String REMOVEFILE_CHECK_MSG="removefileCheck";
  private String REMOVEFILE_ACTION_MSG="removefileAction";
  private String REMOVEINTERFACE_CHECK_MSG="removeinterfaceCheck";
  private String REMOVEINTERFACE_ACTION_MSG="removeinterfaceAction";
  private String REVOKE_CHECK_MSG="revokeCheck";
  private String REVOKE_ACTION_MSG="revokeAction";
  private String RESERVE_CHECK_MSG="reserveCheck";
  private String RESERVE_ACTION_MSG="reserveAction";
  private String TRANSACTION_CHECK_MSG="transactionCheck";
  private String TRANSACTION_ACTION_MSG="transactionAction";
  private String UNLOCK_CHECK_MSG="unlockCheck";
  private String UNLOCK_ACTION_MSG="unlockAction";
  private String UNRESERVE_CHECK_MSG="unreserveCheck";
  private String UNRESERVE_ACTION_MSG="unreserveAction";

 
  //It "seems" that physical ID, logical ID, major ID and version ID are the same?
  private String EXCLUSION_ARRAY[] = {"ACCESSFLAG", "ALTOWNER1", "ALTOWNER2", "APPLICATION", 
  "FROMALLFLAG", "FROMALLRELFLAG", "FROMCARD", "TOALLFLAG", "TOALLRELFLAG", "TOCARD",
  "CfgDynFiltering", "ConfigFilterFactory", "CHECKACCESSFLAG", "CHECKRANGEFLAG", "ENFORCEDLOCKINGFLAG", "HOME", "HOST", "LATTICE", "LOCKFLAG", "LOGICALID", "MAJORID", "MATRIXHOME", "MX_LOGGED_IN_USER_NAME", "OBJECT", "ORGANIZATION", "PATH", "TRIGGER_VAULT", "VAULT", "VERSIONID", "WORKSPACEPATH" };

    private String STRICT_EXCLUSION_ARRAY[] = {"ATTRTYPE", "TYPE", "NAME", "PHYSICALID", "ATTRTYPEKIND", "INVOCATION", "ORIGINAL_INVOCATION", "POLICY", "REVISION", "TIMESTAMP", "OBJECTID", "OWNER", "USER", "TRANSACTION", "PROJECT"};

      
    private MatrixLogWriter m_logWriter = null;
  
      private MatrixLogWriter InitLogWriter(Context context){
	
	  String strFileName        = "DSEMEDCPE_Demo.log";
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
	printArgs("ctor", ctx);	
    }

		

	
 	public int addinterfaceCheck (Context ctx, String[] args)
	{
		printArgs1(ADDINTERFACE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int batchcloneCheck (Context ctx, String[] args)
	{
		printArgs1(BATCHCLONE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int batchreviseCheck (Context ctx, String[] args)
	{
		printArgs1(BATCHREVISE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int changenameCheck (Context ctx, String[] args)
	{
		printArgs1(CHANGENAME_CHECK_MSG, ctx, args);
		return 0;
	}
	public int changeownerCheck (Context ctx, String[] args)
	{
		printArgs1(CHANGEOWNER_CHECK_MSG, ctx, args);
		return 0;
	}
	public int changepolicyCheck (Context ctx, String[] args)
	{
		printArgs1(CHANGEPOLICY_CHECK_MSG, ctx, args);
		return 0;
	}
	public int changetypeCheck (Context ctx, String[] args)
	{
		printArgs1(CHANGETYPE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int changevaultCheck (Context ctx, String[] args)
	{
		printArgs1(CHANGEVAULT_CHECK_MSG, ctx, args);
		return 0;
	}
	public int checkinCheck (Context ctx, String[] args)
	{
		printArgs1(CHECKIN_CHECK_MSG, ctx, args);
		return 0;
	}
	public int checkoutCheck (Context ctx, String[] args)
	{
		printArgs1(CHECKOUT_CHECK_MSG, ctx, args);
		return 0;
	}
	public int connectCheck (Context ctx, String[] args)
	{
		printArgs1(CONNECT_CHECK_MSG, ctx, args);
		return 0;
	}
	public int copyCheck (Context ctx, String[] args)
	{
		printArgs1(COPY_CHECK_MSG, ctx, args);
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
	public int disconnectCheck (Context ctx, String[] args)
	{
		printArgs1(DISCONNECT_CHECK_MSG, ctx, args);
		return 0;
	}
	public int grantCheck (Context ctx, String[] args)
	{
		printArgs1(GRANT_CHECK_MSG, ctx, args);
		return 0;
	}
	public int implicitcopyCheck (Context ctx, String[] args)
	{
		printArgs1(IMPLICITCOPY_CHECK_MSG, ctx, args);
		return 0;
	}
	public int implicitdeleteCheck (Context ctx, String[] args)
	{
		printArgs1(IMPLICITDELETE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int implicitmajorrevisionCheck (Context ctx, String[] args)
	{
		printArgs1(IMPLICITMAJORREVISION_CHECK_MSG, ctx, args);
		return 0;
	}
	public int implicitminorrevisionCheck (Context ctx, String[] args)
	{
		printArgs1(IMPLICITMINORREVISION_CHECK_MSG, ctx, args);
		return 0;
	}
	public int lockCheck (Context ctx, String[] args)
	{
		printArgs1(LOCK_CHECK_MSG, ctx, args);
		return 0;
	}
	public int majorrevisionCheck (Context ctx, String[] args)
	{
		printArgs1(MAJORREVISION_CHECK_MSG, ctx, args);
		return 0;
	}
	public int minorrevisionCheck (Context ctx, String[] args)
	{
		printArgs1(MINORREVISION_CHECK_MSG, ctx, args);
		return 0;
	}
	public int modifyCheck (Context ctx, String[] args)
	{
		printArgs1(MODIFY_CHECK_MSG, ctx, args);
		return 0;
	}
	public int modifyattributeCheck (Context ctx, String[] args)
	{
		printArgsX(MODIFYATTRIBUTE_CHECK_MSG, ctx, "OBJECT");
		return 0;
	}
	public int modifydescriptionCheck (Context ctx, String[] args)
	{
		printArgs1(MODIFYDESCRIPTION_CHECK_MSG, ctx, args);
		return 0;
	}
	public int removefileCheck (Context ctx, String[] args)
	{
		printArgs1(REMOVEFILE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int removeinterfaceCheck (Context ctx, String[] args)
	{
		printArgs1(REMOVEINTERFACE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int revokeCheck (Context ctx, String[] args)
	{
		printArgs1(REVOKE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int reserveCheck (Context ctx, String[] args)
	{
		printArgs1(RESERVE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int transactionCheck (Context ctx, String[] args)
	{
		printArgs1(TRANSACTION_CHECK_MSG, ctx, args);
		return 0;
	}
	public int unlockCheck (Context ctx, String[] args)
	{
		printArgs1(UNLOCK_CHECK_MSG, ctx, args);
		return 0;
	}
	public int unreserveCheck (Context ctx, String[] args)
	{
		printArgs1(UNRESERVE_CHECK_MSG, ctx, args);
		return 0;
	}
	public int addinterfaceAction (Context ctx, String[] args)
	{
		printArgs1(ADDINTERFACE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int batchcloneAction (Context ctx, String[] args)
	{
		printArgs1(BATCHCLONE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int batchreviseAction (Context ctx, String[] args)
	{
		printArgs1(BATCHREVISE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int changenameAction (Context ctx, String[] args)
	{
		printArgs1(CHANGENAME_ACTION_MSG, ctx, args);
		return 0;
	}
	public int changeownerAction (Context ctx, String[] args)
	{
		printArgs1(CHANGEOWNER_ACTION_MSG, ctx, args);
		return 0;
	}
	public int changepolicyAction (Context ctx, String[] args)
	{
		printArgs1(CHANGEPOLICY_ACTION_MSG, ctx, args);
		return 0;
	}
	public int changetypeAction (Context ctx, String[] args)
	{
		printArgs1(CHANGETYPE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int changevaultAction (Context ctx, String[] args)
	{
		printArgs1(CHANGEVAULT_ACTION_MSG, ctx, args);
		return 0;
	}
	public int checkinAction (Context ctx, String[] args)
	{
		printArgs1(CHECKIN_ACTION_MSG, ctx, args);
		return 0;
	}
	public int checkoutAction (Context ctx, String[] args)
	{
		printArgs1(CHECKOUT_ACTION_MSG, ctx, args);
		return 0;
	}
	public int connectAction (Context ctx, String[] args)
	{
		printArgs1(CONNECT_ACTION_MSG, ctx, args);
		return 0;
	}
	public int copyAction (Context ctx, String[] args)
	{
		printArgs1(COPY_ACTION_MSG, ctx, args);
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
	public int disconnectAction (Context ctx, String[] args)
	{
		printArgs1(DISCONNECT_ACTION_MSG, ctx, args);
		return 0;
	}
	public int grantAction (Context ctx, String[] args)
	{
		printArgs1(GRANT_ACTION_MSG, ctx, args);
		return 0;
	}
	public int implicitcopyAction (Context ctx, String[] args)
	{
		printArgs1(IMPLICITCOPY_ACTION_MSG, ctx, args);
		return 0;
	}
	public int implicitdeleteAction (Context ctx, String[] args)
	{
		printArgs1(IMPLICITDELETE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int implicitmajorrevisionAction (Context ctx, String[] args)
	{
		printArgs1(IMPLICITMAJORREVISION_ACTION_MSG, ctx, args);
		return 0;
	}
	public int implicitminorrevisionAction (Context ctx, String[] args)
	{
		printArgs1(IMPLICITMINORREVISION_ACTION_MSG, ctx, args);
		return 0;
	}
	public int lockAction (Context ctx, String[] args)
	{
		printArgs1(LOCK_ACTION_MSG, ctx, args);
		return 0;
	}
	public int majorrevisionAction (Context ctx, String[] args)
	{
		printArgs1(MAJORREVISION_ACTION_MSG, ctx, args);
		return 0;
	}
	public int minorrevisionAction (Context ctx, String[] args)
	{
		printArgs1(MINORREVISION_ACTION_MSG, ctx, args);
		return 0;
	}
	public int modifyAction (Context ctx, String[] args)
	{
		printArgs1(MODIFY_ACTION_MSG, ctx, args);
		return 0;
	}
	public int modifyattributeAction (Context ctx, String[] args)
	{
		printArgsX(MODIFYATTRIBUTE_ACTION_MSG, ctx, "OBJECT");
		return 0;
	}
	public int modifydescriptionAction (Context ctx, String[] args)
	{
		printArgs1(MODIFYDESCRIPTION_ACTION_MSG, ctx, args);
		return 0;
	}
	public int removefileAction (Context ctx, String[] args)
	{
		printArgs1(REMOVEFILE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int removeinterfaceAction (Context ctx, String[] args)
	{
		printArgs1(REMOVEINTERFACE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int revokeAction (Context ctx, String[] args)
	{
		printArgs1(REVOKE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int reserveAction (Context ctx, String[] args)
	{
		printArgs1(RESERVE_ACTION_MSG, ctx, args);
		return 0;
	}
	public int transactionAction (Context ctx, String[] args)
	{
		printArgs1(TRANSACTION_ACTION_MSG, ctx, args);
		return 0;
	}
	public int unlockAction (Context ctx, String[] args)
	{
		printArgs1(UNLOCK_ACTION_MSG, ctx, args);
		return 0;
	}
	public int unreserveAction (Context ctx, String[] args)
	{
		printArgs1(UNRESERVE_ACTION_MSG, ctx, args);
		return 0;
	}
			
}
