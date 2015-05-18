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
        def baseDir = "/var/lib/psi/deployer"
        def jsonStr = new File ("${baseDir}/config.json").text
        def slurper  = new JsonSlurper().parseText(jsonStr)
        def envConfig = slurper.environment
        DocumentsServiceProxy proxy = null;

        if (args.size()!=1) {
            logger.info("GetListOfReports environment[,environment]")
            System.exit(0)
        }

        logger.info("GetListOfReports main");
        def envList = args[0].split(",")
        envList.each() { envArg ->
            try {
                logger.info("Process env arg: ${envArg}")
                def envData = envConfig.find{it.name==envArg}
                def spagobi = envData.spagobi
                def spagobiBaseURL = "${spagobi.server}/sdk/DocumentsService"

                proxy = new DocumentsServiceProxy(spagobi.user, spagobi.password)
                proxy.setEndpoint(spagobiBaseURL)

                SDKDocument[] documents = proxy.getDocumentsAsList("REPORT", null/*state*/, null/*docPath*/)
                for (SDKDocument doc : documents) {
                    logger.info("${envArg} ${doc.getId()} , ${doc.getLabel()}, ${doc.getDescription()}")
                }
            }
            catch (RemoteException reE) {
                logger.error("RemoteException while getting list of documents (getDocumentsAsList)")
                logger.error(reE.getMessage())
            }
            catch (Exception e) {
                logger.error("Unexpected error, but continue trying for other environments")
                logger.info(e.getMessage())
            }
        }
    }
}
