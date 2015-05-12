# SpagoBIInteg
SpagoBI Tools for Doc Deployment, etc.

Tools in this project work with the SpagoBI SDK to automate some processes such as deployment of SpagoBI Documents to multiple environments

For example, the below command will deploy a report document (extracted from SpagoBI as a zip file) to other SpagoBI environments, eliminating the need to log into SpagoBI Web user-interface in carefully perform a series of import steps.

  java com.psionline.spagobi.DeploySpagoBIReport TA-171-App-Flow.zip "['dev1','qa1','prod1']"
