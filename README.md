# KotlinJS_XMLTV_Parser
Parse XMLTV file on KotlinJS

# Using
```
XMLTV.parseXMLTV(xmltvSrc, epgID, fun(xmltv: XMLTV){
    //Do Something
    
    //Get XMLTV data
    //e.g.
    val start = xmltv.programmes[0].start
    val stop = xmltv.programmes[0].stop
    val titles = xmltv.programmes[0].titles
    val subTitles = xmltv.programmes[0].subTitles
    val descs = xmltv.programmes[0].descs
    val redits = xmltv.programmes[0].credits
    ////
})
```

# Reference
https://salsa.debian.org/nickm-guest/xmltv/blob/master/xmltv.dtd

# License
The source code is licensed under GPL v3. License is available [here](/LICENSE).
