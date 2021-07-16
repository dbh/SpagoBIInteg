# SpagoBIInteg
SpagoBI Tools for Doc Deployment, etc.

## Note
The code in this repo was last tested on SpagoBI 5.2. 
The code is completely unsupported and unknown to work with later versions (aka Knowage).


## Info

Tools in this project work with the SpagoBI SDK to automate some processes such as deployment of SpagoBI Documents to multiple environments.

The initial deploying of the report is performed via SpagoBI Studio. 
Then, the report is configured in SpagoBI Server and exported as a zip file.
The zip file artifact can now be deployed via the mechanism here to multiple SpagoBI Servers/Environments, in an automated fashion.

## Details

For example, the below command will deploy a report document (extracted from SpagoBI as a zip file) to other SpagoBI environments, eliminating the need to log into SpagoBI Web user-interface in carefully perform a series of import steps.

DeploySpagoBIReport ExportedReports01.zip dev2,dev3,qa1
