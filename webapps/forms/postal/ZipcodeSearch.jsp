<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ taglib uri="/rmt2-taglib" prefix="db" %>
<%@ taglib uri="/rmt2-beantaglib" prefix="beanlib" %>
<%@ page import="com.api.util.RMT2Utility" %>
<%@ page import="com.api.constants.GeneralConst" %>
<%@ page import="com.action.postal.PostalConst" %>
<%@ page import="com.api.constants.RMT2ServletConst" %>

<gen:InitAppRoot id="APP_ROOT"/>
<gen:InitSessionBean id="SESSION_BEAN"/>
    
<%
  String pageTitle = "Postal Zip Code Search";
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
	    <h2><strong><%=pageTitle%></strong></h2>
			<form name="DataForm" method="post" action="<%=APP_ROOT%>/unsecureRequestProcessor/Zipcode.Search">
			    <font size="4" style="color:blue">Search Criteria</font>
				<table width="90%" border="0">
					<tr>
						<td bgcolor="#FFCC00" width="12%">
							<div align="right"><font size="2"><b>Zip Code:</b></font></div>
						</td>
						<td width="19%">
						  <beanlib:InputControl type="text" name="qry_Zip" value="#QUERY_BEAN.CustomObj.qry_Zip" size="10"/>
						</td>
						<td bgcolor="#FFCC00" width="12%">
							<div align="right"><font size="2"><b>City:</b></font></div>
						</td>
						<td width="24%">
						    <beanlib:InputControl type="text" name="qry_City" value="#QUERY_BEAN.CustomObj.qry_City" size="10"/>
						</td>
						<td bgcolor="#FFCC00" width="12%">
							<div align="right"><font size="2"><b>State (Abbreviated):</b></font></div>
						</td>
						<td width="21%">
						    <beanlib:InputControl type="text" name="qry_State" value="#QUERY_BEAN.CustomObj.qry_State" size="10"/>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFCC00" width="12%">
							<div align="right"><font size="2"><b>Area Code:</b></font></div>
						</td>
						<td width="19%">
						    <beanlib:InputControl type="text" name="qry_AreaCode" value="#QUERY_BEAN.CustomObj.qry_AreaCode" size="10"/>
						</td>
						<td bgcolor="#FFCC00" width="12%">
							<div align="right"><b><font size="2">County Name:</font></b></div>
						</td>
						<td width="24%">
							<beanlib:InputControl type="text" name="qry_CountyName" value="#QUERY_BEAN.CustomObj.qry_CountyName" size="10"/>
						</td>
						<td bgcolor="#FFCC00" width="12%">
							<div align="right"><font size="2"><b>Time Zone:</b></font></div>
						</td>
						<td width="21%">
						    <beanlib:InputControl dataSource="<%=PostalConst.CLIENT_DATA_TIMEZONES %>"
												  type="select"
												  name="qry_TimeZone"
												  codeProperty="TimeZoneId"
											 	  displayProperty="Descr"
												  selectedValue="#QUERY_BEAN.CustomObj.Qry_TimeZone"/>			
						</td>
					</tr>
				</table>
				<br>
				
				<br>
				<font size="4" style="color:blue">Search Results&nbsp;&nbsp; <i>Sorted by State, County Name, City, City Alias, and Zip code</i></font>
          <div style="border-style:groove; border-color:#999999; background-color:buttonface; width:85%; height:520px; overflow:auto">
						<table width="100%" border="0" cellspacing="0">
							<tr bgcolor="#FFCC00">
							  <th width="3%" class="clsTableListHeader">&nbsp;</th>
							  <th width="5%"> 
									<div align="left">
									   <font color="#000000"><b>Id</b></font>
									</div>
							  </th>
							  <th width="7%" bgcolor="#FFCC00"> 
									<div align="left"> 
									   <font color="#000000"><b>Zipcode</b></font> 
									</div>
							  </th>
							  <th width="5%"> 
									<div align="left">
									  <font color="#000000"><b>State</b></font>
									</div>
							  </th>
							  <th width="18%"> 
									<div align="left">
									  <font color="#000000"><b>County</b></font>
									</div>
							  </th>
	 						  <th width="22%"> 
									<div align="left">
									  <font color="#000000"><b>City</b></font>
									</div>
							  </th>
	 						  <th width="22%"> 
									<div align="left">
									  <font color="#000000"><b>City Alias</b></font>
									</div>
							  </th>						  
	 						  <th width="10%"> 
									<div align="left">
									  <font color="#000000"><b>Area Code</b></font>
									</div>
							  </th>						  
	  						<th width="8%"> 
									<div align="left">
									  <font color="#000000"><b>Time Zone</b></font>
									</div>
							  </th>						  						  
							</tr>
			
							<beanlib:LoopRows bean="zipitem" list="<%=GeneralConst.CLIENT_DATA_LIST%>">
							    <gen:ColorBarLines evenColor="#CCFFCC" oddColor="#FFFFFF"/>
									<td valign="top" align="center" bgcolor="#FFCC00">
									 	<beanlib:InputControl type="radio" name="<%=GeneralConst.CLIENTROW_PROPERTY%>" value="rowid"/>
									 	<beanlib:InputControl type="hidden" name="Elevation" value="#zipitem.Elevation" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="CitytypeDescr" value="#zipitem.CitytypeDescr" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="DayLightSaving" value="#zipitem.DayLightSaving" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="Latitude" value="#zipitem.Latitude" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="Longitude" value="#zipitem.Longitude" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="Msa" value="#zipitem.Msa" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="Pmsa" value="#zipitem.Pmsa" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="Cbsa" value="#zipitem.Cbsa" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="CbsaDiv" value="#zipitem.CbsaDiv" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="PersonsPerHousehold" value="#zipitem.PersonsPerHousehold" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="ZipcodePopulation" value="#zipitem.ZipcodePopulation" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="HouseholdsPerZipcode" value="#zipitem.HouseholdsPerZipcode" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="WhitePopulation" value="#zipitem.WhitePopulation" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="BlackPopulation" value="#zipitem.BlackPopulation" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="HispanicPopulation" value="#zipitem.HispanicPopulation" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="IncomePerHousehold" value="#zipitem.IncomePerHousehold" uniqueName="yes"/>
									 	<beanlib:InputControl type="hidden" name="AverageHouseValue" value="#zipitem.AverageHouseValue" uniqueName="yes"/>
									</td>
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.ZipId"/> 
									   <beanlib:InputControl type="hidden" name="ZipId" value="#zipitem.ZipId" uniqueName="yes"/>
								    </td>
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.Zip"/> 
									   <beanlib:InputControl type="hidden" name="Zip" value="#zipitem.Zip" uniqueName="yes"/>
								    </td>
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.State"/> 
									   <beanlib:InputControl type="hidden" name="State" value="#zipitem.State" uniqueName="yes"/>
								    </td>
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.CountyName"/> 
									   <beanlib:InputControl type="hidden" name="CountyName" value="#zipitem.CountyName" uniqueName="yes"/>
								    </td>
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.City"/> 
									   <beanlib:InputControl type="hidden" name="City" value="#zipitem.City" uniqueName="yes"/>
								    </td>
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.CityAliasName"/>
									   <beanlib:InputControl type="hidden" name="CityAliasName" value="#zipitem.CityAliasName" uniqueName="yes"/> 
								    </td>							    
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.AreaCode"/> 
									   <beanlib:InputControl type="hidden" name="AreaCode" value="#zipitem.AreaCode" uniqueName="yes"/>
								    </td>							    
								    <td valign="top"> 
									   <beanlib:InputControl value="#zipitem.TimezoneDescr"/> 
									   <beanlib:InputControl type="hidden" name="TimezoneDescr" value="#zipitem.TimezoneDescr" uniqueName="yes"/>
								    </td>							    							    							    							    							    
							    </tr>
			          </beanlib:LoopRows>
		         </table>                
          </div>
				
				  <br>
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
			  	<input type="button" name="<%=GeneralConst.REQ_SEARCH%>" value="Search" style=width:90  onClick="handleAction('_self', document.DataForm, this.name)">
				  <input type="button" name="<%=GeneralConst.REQ_VIEW%>" value="View" style=width:90  onClick="handleAction('_self', document.DataForm, this.name)">
				  <input type="button" name="<%=GeneralConst.REQ_RESET%>" value="Reset" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
				  <input type="button" name="<%=GeneralConst.REQ_BACK%>" value="Back" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
   	      <input name="clientAction" type="hidden">
			</form>
			
	</body>
</html>
