<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/car">
		<html>
			<head>
				<title><xsl:value-of select="make"/> <xsl:value-of select="model"/></title>
			</head>
			<body>
				<h1><xsl:value-of select="make"/></h1><br />
				<h2><xsl:value-of select="model"/></h2><br />
				<table border="0">
					<tr><td>VIN:</td><td><xsl:value-of select="@vin"/></td></tr>
					<tr><td>Year:</td><td><xsl:value-of select="year"/></td></tr>
					<tr><td>Color:</td><td><xsl:value-of select="color"/></td></tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>