<td> 
	<%! String addr = "";%>
	<%! String addr2 = "";%>
	<%! String temp = null;%>
	<%! int intTemp = 0;%>
	<%! String brk = null;%>
	<%! String phoneNums = "";%>
	<%   VwBusinessAddress address = (VwBusinessAddress) pageContext.getAttribute("item");
	     temp = address.getAddr1();
		 if (temp != null && !temp.equals("")) {
		   addr = temp;
		 }
		 temp = address.getAddr2();
		 if (temp != null && !temp.equals("")) {
		   addr += "<br>" + temp;
		 }
		 temp = address.getAddr3();
		 if (temp != null && !temp.equals("")) {
		   addr += "<br>" + temp;
		 }
		 temp = address.getAddr4();
		 if (temp != null && !temp.equals("")) {
		   addr += "<br>" + temp;
		 }
		 temp = address.getZipCity();
		 if (temp != null && !temp.equals("")) {
		   addr2 =  temp;
		 }
		 temp = address.getZipState();
		 if (temp != null && !temp.equals("")) {
		   addr2 += ", " + temp;
		 }
		 
		 // Only include zip if previous address components exist
		 if (!addr2.equals("")) {
		         intTemp = address.getAddrZip();
		 		 if (intTemp != 0) {
		   		  addr2 += " " + intTemp;
		 		 }
		 	 intTemp = address.getAddrZipext();
		     if (intTemp != 0) {
		        addr2 +=  "-" + intTemp;
		     }
		 }
	
		 if (addr.equals("") && addr2.equals("")) {
		   addr = "Not Available";
		 }
		 else if (addr.equals("") && !addr2.equals("")) {
		   addr = addr2;
		 }
		 else if (!addr.equals("") && !addr2.equals("")) {
		   addr += "<br>" + addr2;
		 }
	 %>
	<div align="left">
	<%out.print(addr);%>
	<% temp = null;
	   addr = "";
	   addr2 = "";
	%>
  </div>
</td>

<td valign="top"> 
    <% 
	 temp = address.getAddrPhoneHome();
	  if (temp != null && !temp.equals("")) {
		phoneNums = "Home - " + temp;
	  }
	  temp = address.getAddrPhoneWork();
	  if (temp != null && !temp.equals("")) {
		brk = (phoneNums.length() > 0 ? "<br>" : "");
		phoneNums += brk + "Work - " + temp;
	  }
	  temp = address.getAddrPhoneExt();
	  if (temp != null && !temp.equals("")) {
		brk = (phoneNums.length() > 0 ? "<br>" : "");
		phoneNums += brk + "Work Ext. - " + temp;
	  }
	  temp = address.getAddrPhoneFax();
	  if (temp != null && !temp.equals("")) {
		brk = (phoneNums.length() > 0 ? "<br>" : "");
		phoneNums += brk + "Fax - " + temp;
	  }
	  temp = address.getAddrPhonePager();
	  if (temp != null && !temp.equals("")) {
		brk = (phoneNums.length() > 0 ? "<br>" : "");
		phoneNums += brk + "Pager - " + temp;
	  }
	  temp = address.getAddrPhoneCell();
	  if (temp != null && !temp.equals("")) {
		brk = (phoneNums.length() > 0 ? "<br>" : "");
		phoneNums += brk + "Cellular - " + temp;
	  }
	  temp = address.getAddrPhoneMain();
	  if (temp != null && !temp.equals("")) {
		brk = (phoneNums.length() > 0 ? "<br>" : "");
		phoneNums += brk + "Main - " + temp;
	  }
	  if (phoneNums == null || phoneNums.equals("")) {
		out.println("Not Available");
	  }
	  else {
		out.println(phoneNums);
	  }
	  phoneNums = "";
   %>
 </td>