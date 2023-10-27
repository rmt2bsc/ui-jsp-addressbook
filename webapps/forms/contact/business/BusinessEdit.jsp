<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ taglib uri="/rmt2-beantaglib" prefix="beanlib" %>
<%@ page import="com.api.util.RMT2Utility" %>
<%@ page import="com.action.contacts.ContactsConst" %>
<%@ page import="com.api.constants.GeneralConst" %>
<%@ page import="com.api.constants.RMT2ServletConst" %>

<gen:InitAppRoot id="APP_ROOT"/>
<gen:InitSessionBean id="SESSION_BEAN"/>

<%
  String pageTitle = "Business Contact Edit";
%>   
<html>
	<head>
		<title><%=pageTitle%></title>
		<meta http-equiv="Pragma" content="no-cache">
	   	<meta http-equiv="Expires" content="-1">
	    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2Table.css">
		<link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2General.css">
		<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2General.js"></script>
		<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2Menu.js"></script>
	</head>
 
	<body bgcolor="#FFFFFF" text="#000000">
		<form name="DataForm" method="post" action="<%=APP_ROOT%>/unsecureRequestProcessor/Business.Edit">
				<table width="90%" border="0">
					<caption align="left"> <strong><%=pageTitle%></strong> </caption>
						<tr> 
							<td width="10%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Business Id:</font></b></div>
							</td>
							<td width="21%"> 
								<div id="bus_id" align="left"> 
									<beanlib:InputControl type="hidden" name="BusinessId" value="#record.BusinessId"/>
									<beanlib:InputControl type="hidden"  name="AddrId" value="#record.AddrId"/>
									<beanlib:InputControl value="#record.BusinessId"/> 
								</div>
							</td>
							<td width="12%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Contact First Name:</font></b></div>
							</td>
							<td width="24%"> 
								<div align="left"> 
									<beanlib:InputControl type="text" size="20" tabIndex="5" name="ContactFirstname" value="#record.BusContactFirstname"/>
								</div>
							</td>
						</tr>	
						<tr> 
							<td width="10%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Business Name:</font></b></div>
							</td>
							<td width="21%"> 
								<div align="left"> 
									<beanlib:InputControl type="text" size="60" tabIndex= "1" name="Longname" value="#record.BusLongname"/>
								</div>
							</td>
							<td width="12%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Contact Last Name:</font></b></div>
							</td>
							<td width="21%"> 
								<div align="left"> 
									<beanlib:InputControl type="text" size="20" tabIndex= "6" name="ContactLastname" value="#record.BusContactLastname"/>
								</div>
							</td>
						</tr>
						<tr> 
							<td width="12%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Business Type:</font></b></div>
							</td>
							<td width="24%"> 
								<div align="left"> 
									<db:InputControl dataSource="busTypeDso"
															   type="select"
															   name="EntityTypeId"
															   codeProperty="CodeId"
															   displayProperty="Longdesc"
															   selectedValue="#record.BusEntityTypeId"/>
								</div>
							</td>
							<td width="10%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Contact Phone:</font></b></div>
							</td>
							<td width="21%"> 
								<div align="left"> 
									<beanlib:InputControl type="text" size="20" tabIndex= "7" name="ContactPhone" value="#record.BusContactPhone"/>
								</div>
							</td>							
						</tr>
						<tr> 
							<td width="10%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Service Type:</font></b></div>
							</td>
							<td width="30%">
								<div align="left"> 
									 <db:InputControl dataSource="busServTypeDso"
															   type="select"
															   name="ServTypeId"
															   codeProperty="CodeId"
															   displayProperty="Longdesc"
															   selectedValue="#record.BusServTypeId"/>								
								</div>
							</td>
							<td width="12%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Contact Phone Ext.</font></b></div>
							</td>
							<td width="24%"> 
								<div align="left"> 
									<beanlib:InputControl type="text" size="20" tabIndex="8" name="ContactExt" value="#record.BusContactExt"/>
								</div>
							</td>
						</tr>
						<tr>
							<td width="12%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Web Site:</font></b></div>
							</td>
							<td width="21%"> 
								<div align="left"> 
									<beanlib:InputControl type="text" size="50" tabIndex="4" name="Website" value="#record.BusWebsite"/>
								</div>
							</td>
							<td width="12%" bgcolor="#FFCC00"> 
								<div align="right"><b><font size="2">Tax Id:</font></b></div>
							</td>
							<td width="24%"> 
								<div align="left"> 
									<beanlib:InputControl type="text" size="20" tabIndex="9" name="TaxId" value="#record.BusTaxId"/>
								</div>
							</td>
						</tr>
					</table>
					<br>
					
					<!--  Include address edit section -->
                    <%@include file="../address/AddressEdit.jsp"%>

  				<p>&nbsp;</p>
	  			 <!-- Display any messages -->
		  		 <table>
			  		 <tr>
			  		   <td>
					     <font color="red">
						     <gen:ShowPageMessages dataSource="<%=RMT2ServletConst.REQUEST_MSG_MESSAGES%>"/>
					     </font>
				 	   </td>
					 </tr>
				  </table>				
				<br>				
				<input type="button" name="<%=GeneralConst.REQ_SAVE%>" value="Save" style=width:90  onClick="handleAction('_self', document.DataForm, this.name)">
				<input type="button" name="<%=GeneralConst.REQ_DELETE%>" value="Delete" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
				<input type="button" name="<%=GeneralConst.REQ_BACK%>" value="Back" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
        <input name="clientAction" type="hidden">
		</form>
		<db:Dispose/>
	</body>
</html>
