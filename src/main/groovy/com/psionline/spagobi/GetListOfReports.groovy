package com.psionline.spagobi; /**
 * Created by dharrison on 4/16/2015.
 * David B. Harrison
 */

//TODO Parameterize SpagoBI Environment
//TODO Parameterize Document Type
//TODO Parameterize Document Path in Functional Hierarchy

import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;
import it.eng.spagobi.sdk.documents.bo.SDKDocument;
import java.rmi.RemoteException;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class GetListOfReports {
    private static final Logger logger = LoggerFactory.getLogger("GetListOfReports")

    public static void main(String[] args) {
        String spagobiBaseURL = "http://localhost:8080/SpagoBI/sdk/DocumentsService";
        String spagobiUser = "biadmin";
        String spagobiPassword = "biadmin";
        DocumentsServiceProxy proxy = null;
        String docPath = null;

        logger.info("Main");
        try {
            proxy = new DocumentsServiceProxy(spagobiUser, spagobiPassword);
            proxy.setEndpoint(spagobiBaseURL);

            SDKDocument[] documents = proxy.getDocumentsAsList("REPORT", null, docPath);
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
