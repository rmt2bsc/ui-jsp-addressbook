<table width="90%" border="0">
	<tr> 
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">Address 1:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl type="text" size="50" tabIndex="9" name="Addr1" value="#record.Addr1"/>
		</td>
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">City:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl value="#record.ZipCity"/>   
		</td>
	</tr>
	<tr>
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">Address 2:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl type="text" size="50"  tabIndex="10" name="Addr2" value="#record.Addr2"/>
		</td>
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">State:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl value="#record.ZipState"/> 
		</td>								
	</tr>
	<tr>
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">Address 3:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl type="text" size="50"  tabIndex="11" name="Addr3" value="#record.Addr3"/>
		</td>
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">Main Phone:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl type="text" size="20"  tabIndex="15" name="AddrPhoneMain" value="#record.AddrPhoneMain"/>
		</td>
	</tr>              
	<tr> 
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">Address 4:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl type="text" size="50"  tabIndex="12" name="Addr4" value="#record.Addr4"/>
		</td>
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">Fax Phone:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl type="text" size="20"  tabIndex="16" name="AddrPhoneFax" value="#record.AddrPhoneFax"/>
		</td>
	</tr>
	<tr> 
		<td bgcolor="#FFCC00" width="10%"> 
			<div align="right"><b><font size="2">Zip:</font></b></div>
		</td>
		<td width="24%">
			<beanlib:InputControl type="text" size="10"  tabIndex="13" name="AddrZip" value="#record.AddrZip"/>
			<beanlib:InputControl type="text" size="5"  tabIndex="14" name="ddrZipext" value="#record.AddrZipext"/>
		</td>
		<td width="10%">&nbsp; </td>
		<td width="24%">&nbsp;</td>
	</tr>
</table>