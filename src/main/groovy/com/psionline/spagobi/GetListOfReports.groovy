package com.psionline.spagobi

import groovy.json.JsonSlurper; /**
 * Created by dharrison on 4/16/2015.
 * David B. Harrison
 *
 * See SpagoBI SDK Documentation at http://wiki.spagobi.org/xwiki/bin/view/spagobi_sdk/SDK_2_3_User_guide
 */


import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;
import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import java.rmi.RemoteException;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class GetListOfReports {
    private static final Logger logger = LoggerFactory.getLogger("GetListOfReports")

    public static void main(String[] args) {
        def envArg = args[0] //Should be spagobi environment name in the config.json file

        def baseDir = "/var/lib/psi/deployer"
        def jsonStr = new File ("${baseDir}/config.json").text
        def slurper  = new JsonSlurper().parseText(jsonStr)
        def envConfig = slurper.environment

        DocumentsServiceProxy proxy = null;

        logger.info("Main");
        try {

            def envData = envConfig.find{it.name==envArg}
            def spagobi = envData.spagobi
            def spagobiBaseURL = "${spagobi.server}/sdk/DocumentsService";

            proxy = new DocumentsServiceProxy(spagobi.user, spagobi.password);
            proxy.setEndpoint(spagobiBaseURL);

            SDKDocument[] documents = proxy.getDocumentsAsList("REPORT", null/*state*/, null/*docPath*/);
            for (SDKDocument doc : documents) {
                logger.info(doc.getId() + ", "+ doc.getLabel()+", "+doc.getDescription());
            }
        }
        catch (RemoteException reE) {
            logger.error(reE.getMessage());
        }
        catch (Exception e) {
            logger.info(e.getMessage());
        }

    }
}
