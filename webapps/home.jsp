<%@ taglib uri="/rmt2-securitytaglib" prefix="auth" %>
<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ page import="com.api.util.RMT2Utility" %>
<%@ page import="com.api.security.authentication.web.AuthenticationConst" %>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.api.web.security.RMT2SessionBean" %>

<gen:InitAppRoot id="APP_ROOT"/>
<gen:InitSessionBean id="SESSION_BEAN"/>

<html>
  <head>
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
	  <h1>Contacts Application</h1> 
	  <br><br>
	  <table width="50%" border="1" cellpadding="0" cellspacing="0">
	    <caption align="left">
	       <font color="blue" size="4">
	          <strong>Main Menu</strong>
	       </font>
	    </caption>
	    <tr> 
	      <td>
	      <table width="100%" border="0">
	         <tr>
		      <td width="12%" valign="top" align="center"> 
		          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/GeneralCodeGroup.GeneralCodeGroupList?clientAction=list&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
		            General Code Maintenance
		          </a>
		      </td>
		      <td width="12%" valign="top" align="center"> 
		          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/Person.Search?clientAction=newsearch&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
		            Personal Contacts
		          </a>
		      </td>
		      <td width="12%"  valign="top" align="center"> 
		          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/Business.Search?clientAction=newsearch&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
		            Business Contacts
		          </a>
		      </td>	  			      
		      <td width="12%"  valign="top" align="center"> 
		          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/Zipcode.Search?clientAction=newsearch&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
		            Zip Code Search
		          </a>
		      </td>	  
		      <td width="12%"  valign="top" align="center"> 
		          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/State.Search?clientAction=newsearch&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
		            State/Province Search
		          </a>
		      </td>	  					      
		      <td width="12%"  valign="top" align="center"> 
		          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/Country.Search?clientAction=list&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
		            Country Search
		          </a>
		      </td>	  					      					      
		      <td width="28%"  valign="top" align="center">&nbsp;</td>	  
	        </tr>
	      </table>
	      </td>
	    </tr>  

       		 
        <tr>
           <td>
              <table width="100%" border="0">
                <tr>
			        <td width="10%" valign="top" align="left"> 
				          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/ContactLoader.Add?clientAction=start&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
				            Load Contacts
				          </a>
				      </td>
				      <td width="10%" valign="top" align="left"> 
				          <a href="/ui-jsp-addressbook/unsecureRequestProcessor/Security.Authentication?clientAction=logoff&<%=SESSION_BEAN.getAuthUrlParms()%>"> 
					            Log Off
				          </a>
				      </td>					                         
                </tr>
              </table>
           </td>
		 </tr>		    
	  </table>
  </body>
</html>
