package com.psionline.spagobi; /**
 * Created by dharrison on 4/16/2015.
 */

import it.eng.spagobi.sdk.proxy.DocumentsServiceProxy;
import it.eng.spagobi.sdk.documents.bo.SDKDocument;

import java.rmi.RemoteException;

public class GetListOfReports {
    public static void main(String[] args) {
        String spagobiBaseURL = "http://localhost:8080/SpagoBI/sdk/DocumentsService";
        String spagobiUser = "biadmin";
        String spagobiPassword = "biadmin";
        DocumentsServiceProxy proxy = null;
        String docPath = null;

        System.out.println("Main");
//        docPath = "/Functionalities/Analytical documents/Reports/USPS_CLIENT_ADMIN";

        try {
            proxy = new DocumentsServiceProxy(spagobiUser, spagobiPassword);
            proxy.setEndpoint(spagobiBaseURL);

            SDKDocument[] documents = proxy.getDocumentsAsList("REPORT", null, docPath);
            for (SDKDocument doc : documents) {
                System.out.println(doc.getId() + ", "+ doc.getLabel()+", "+doc.getDescription());
            }
        }
        catch (RemoteException reE) {
            System.out.println(reE);
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
}
