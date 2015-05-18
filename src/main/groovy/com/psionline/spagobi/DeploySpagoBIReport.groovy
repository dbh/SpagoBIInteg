package com.psionline.spagobi
/**
 * Created by dharrison on 4/20/2015.
 * David B. Harrison
 */


import groovy.json.JsonSlurper
import it.eng.spagobi.sdk.proxy.ImportExportSDKServiceProxy
import it.eng.spagobi.sdk.importexport.bo.SDKFile

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path

import javax.activation.DataHandler
import javax.activation.FileDataSource
import java.rmi.RemoteException


public class DeploySpagoBIReport {
    private static final Logger logger = LoggerFactory.getLogger("DeploySpagoBIReport")

    public static void main(String[] args) {
        def baseDir = "/var/lib/psi/deployer"
        def reportDir = "${baseDir}/reports"
        def logDir = "${baseDir}/log"
        def jsonStr = new File ("${baseDir}/config.json").text
        def slurper  = new JsonSlurper().parseText(jsonStr)
        def envConfig = slurper.environment

        def importSvc = '/services/ImportExportSDKService'

        if (args.size()!=2) {
            logger.info("DeploySpagoBIReport exportDocumentName.zip environment[,environment]")
            System.exit(0)
        }

        logger.info("DeploySpagoBIReport main")
        def startTime = System.currentTimeMillis()

        String exportFileName = args[0]
        String associationsFileName = "associations.xml"

        def envList = args[1].split(",")
        envList.each() { env->
            logger.info("---------------------")
            logger.info("Processing for ${env}")
            logger.info( "Env: ${env}, Deploying ${exportFileName}")

            def envData = envConfig.find{it.name==env}
            def spagobi = envData.spagobi

            def dstSpagoBIBaseURL = spagobi.server
            def importURL = "${spagobi.server}${importSvc}"
            def spagobiUser = spagobi.user
            def spagobiPassword = spagobi.password

            ImportExportSDKServiceProxy dstProxy = null
            SDKFile response = null

            dstProxy = new ImportExportSDKServiceProxy(spagobiUser, spagobiPassword)
            dstProxy.setEndpoint(importURL)

            try {
                // defines the export SDKFile using the export file produced by Export administration web interface
                FileDataSource exportFileDataSource = new FileDataSource("${reportDir}/${exportFileName}");
                DataHandler exportFileDataHandler = new DataHandler(exportFileDataSource);
                SDKFile exportSDKFile = new SDKFile();
                exportSDKFile.setFileName(exportFileName);
                exportSDKFile.setContent(exportFileDataHandler);

                // defines the associations SDKFile using the associations file produced by a previous import operated by the Import administration web interface
                // this is not mandatory: in case it is not specified, associations will be performed automatically by items' identifiers
                FileDataSource associationsFileDataSource = new FileDataSource("${baseDir }/${associationsFileName}");
                DataHandler associationsFileDataHandler = new DataHandler(associationsFileDataSource);
                SDKFile associationsSDKFile = new SDKFile();
                associationsSDKFile.setFileName(associationsFileName);
                associationsSDKFile.setContent(associationsFileDataHandler);

                response = dstProxy.importDocuments(exportSDKFile, associationsSDKFile, true);

                logger.info( "importDocuments response: ${response}" )

                def tmpLog = response.getContent().getDataSource().getName();
                Path p = new File(tmpLog).toPath();

                def dstLog = "${logDir}/${env}_${p.getFileName()}"
                logger.info( "Writing log file: ${dstLog}" )

                def f = new File(dstLog).toPath()
                Files.copy(p, f)
                displayLog(env, f.toAbsolutePath().toString())
            }
            catch (RemoteException reE) {
                logger.error("RemoteException while deploying documents (getDocumentsAsList)")
                logger.error(reE.getMessage());
                reE.printStackTrace();
            }
            catch (Exception e) {
                logger.error("Exception while deploying report")
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        def now = System.currentTimeMillis()
        logger.info( "ms: ${now - startTime}" )
    }


    static void displayLog(def env, def logName) {
        def dstLogH = new File(logName)
        dstLogH.readLines().each() {
            if (it.contains("updated")) {
                if (it.contains("The document with label"))
                    logger.info( "${env} : ${it}" )
                else if (it.contains("The lov with label"))
                    logger.info( "${env} : ${it}" )
                else if (it.contains("The dataset with label"))
                    logger.info( "${env} : ${it}" )
            }
        }
    }

}
