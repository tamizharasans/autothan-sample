<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				version="1.0">
	<xsl:template match="/">
		<html>
			<head><title>REST Automation Test Report</title></head>
			<body>
				<h2 ><font color="blue">REST Automation Test Report</font></h2>

				<table border="1" style="width:100%">

					<tr bgcolor="#FFA500">
						<th width="5%" >Movie Id</th>
						<th width="15%" >Movie Name</th>
						<th width="20%" >Wikipedia URL</th>
						<th width="10%" >Wikipedia Director Name</th>
						<th width="10%" >IMDB URL</th>
						<th width="10%" >IMDB Director Name</th>
						<th width="10%" >Error Status</th>
					</tr>


					<xsl:for-each select="report/topic">
						<tr>
							<td ALIGN="CENTER" ><xsl:value-of select="movieId"/></td>
							<td><xsl:value-of select="movieName"/></td>
							<td><xsl:value-of select="wikiUrl"/></td>
							<td><xsl:value-of select="wikiDirName"/></td>
							<td><xsl:value-of select="imdbUrl"/></td>
							<td><xsl:value-of select="imdbDirName"/></td>
							<td><xsl:value-of select="errorStatus"/></td>
						</tr>
					</xsl:for-each>

				</table>
			</body></html>
	</xsl:template>
</xsl:stylesheet>