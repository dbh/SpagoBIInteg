# SpagoBIInteg
SpagoBI Tools for Doc Deployment, etc.

## Important Node
This repo is abandonware. I no longer work with SpagoBI (renamed to Knowage) and the code here is horribly out of date. 
The code here is for SpagoBI 5.2.

## Info

I hate manual deployments and repetitive, error-prone configurations. Why can't some of that be automated?
Tools in this project work with the SpagoBI SDK to automate some processes such as deployment of SpagoBI Documents to multiple environments.


The initial deploying of the report is performed via SpagoBI Studio. 
Then, the report is configured in SpagoBI Server and exported as a zip file.
The zip file artifact can now be deployed via the mechanism here to multiple SpagoBI Servers/Environments, in an automated fashion.

## Details

For example, the below command will deploy a report document (extracted from SpagoBI as a zip file) to other SpagoBI environments, eliminating the need to log into SpagoBI Web user-interface in carefully perform a series of import steps.

DeploySpagoBIReport ExportedReports01.zip dev2,dev3,qa1

## Note
The code in this repo was last tested on SpagoBI 5.2. 
The code is completely unsupported and unknown to work with later versions (aka Knowage).
