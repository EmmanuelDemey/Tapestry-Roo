package org.got5.tapestry5.roo;

public class TapestryUtils {
	
	 public final static String createEmptyTemplateContent() {
		 	StringBuffer buffer = new StringBuffer();
		 	buffer.append("<html t:type=\"layout\" xmlns:t=\"http://tapestry.apache.org/schema/tapestry_5_1_0.xsd\" " +
		 			" xmlns:p=\"tapestry:parameter\">");
		 	
		 	buffer.append("</html>");
		 	
		 	return buffer.toString();
	 }
}	
