<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
version="1.0">
<xsl:template match="/">
<html>
<head><title>Automation Test Report</title></head>
<body>
<h2 ><font color="blue">Test Autothon Report</font></h2>

<table border="1" style="width:100%">

<tr bgcolor="#FFA500">
	<th width="5%" >Movie No</th>
	<th width="15%" >Movie Name</th>
</tr>


<xsl:for-each select="report/topic">
<tr>
<!-- Test Case Id -->
<td ALIGN="CENTER" ><xsl:value-of select="movieNo"/></td>
<!-- Test Case Name -->
<td><xsl:value-of select="movieName"/></td>


</tr>
</xsl:for-each>

</table>
</body></html>
</xsl:template>
</xsl:stylesheet>