/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.resultSet2XML.insurance;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.TransactionStatus;

/**
 *
 * @author Arsiela
 */
public class InsurancePolicyProposalMaster {
    public static void main (String [] args){
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Maven_Systems";
        }
        else{
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        
        GRider instance = new GRider("gRider");

        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");
        
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Insurance_Policy_Proposal.xml");
        
        
        String lsSQL =    " SELECT "                                                                           
                        + "    a.sTransNox "                                                                   
                        + "  , a.dTransact "                                                                   
                        + "  , a.sReferNox "                                                                   
                        + "  , a.sClientID "                                                                   
                        + "  , a.sSerialID "                                                                   
                        + "  , a.sVSPNoxxx "                                                                   
                        + "  , a.sBrInsIDx "                                                                   
                        + "  , a.sInsTypID "                                                                   
                        + "  , a.cIsNewxxx "                                                                   
                        + "  , a.nODTCAmtx "                                                                   
                        + "  , a.nODTCRate "                                                                   
                        + "  , a.nODTCPrem "                                                                   
                        + "  , a.nAONCAmtx "                                                                   
                        + "  , a.nAONCRate "                                                                   
                        + "  , a.nAONCPrem "                                                                   
                        + "  , a.cAONCPayM "                                                                   
                        + "  , a.nBdyCAmtx "                                                                   
                        + "  , a.nBdyCPrem "                                                                   
                        + "  , a.nPrDCAmtx "                                                                   
                        + "  , a.nPrDCPrem "                                                                   
                        + "  , a.nPAcCAmtx "                                                                   
                        + "  , a.nPacCPrem "                                                                   
                        + "  , a.nTPLAmtxx "                                                                   
                        + "  , a.nTPLPremx "                                                                   
                        + "  , a.nTaxRatex "                                                                   
                        + "  , a.nTaxAmtxx "                                                                   
                        + "  , a.nTotalAmt "                                                                   
                        + "  , a.sRemarksx "                                                                   
                        + "  , a.cTranStat "                                                                   
                        + "  , a.sModified "                                                                   
                        + "  , a.dModified "                                                                   
                        + "  , a.sApproved "                                                                   
                        + "  , a.dApproved "  
                        + "  , CASE "          
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CLOSED)+" THEN 'APPROVE' "                     
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)+" THEN 'CANCELLED' "                  
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_OPEN)+" THEN 'ACTIVE' "                    
                        + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_POSTED)+" THEN 'POSTED' "                                      
                        + " 	ELSE 'ACTIVE'  "                                                          
                        + "    END AS sTranStat "
                        + "  , b.sCompnyNm AS sOwnrNmxx "                                                      
                        + "  , b.cClientTp "                                                                   
                        + "  , IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''), "                          
                        + "    IFNULL(CONCAT(d.sAddressx,' ') , ''), "                                         
                        + "    IFNULL(CONCAT(e.sBrgyName,' '), ''),  "                                         
                        + "    IFNULL(CONCAT(f.sTownName, ', '),''), "                                         
                        + "    IFNULL(CONCAT(g.sProvName),'') )	, '') AS sAddressx "                           
                        + "  , k.sCompnyNm AS sCoOwnrNm "                                                      
                        + "  , h.sCSNoxxxx "                                                                   
                        + "  , h.sFrameNox "                                                                   
                        + "  , h.sEngineNo "                                                                   
                        + "  , h.cVhclNewx "                                                                   
                        + "  , i.sPlateNox "                                                                   
                        + "  , j.sDescript AS sVhclFDsc "                                                      
                        + "  , l.sBrInsNme "                                                                   
                        + "  , m.sInsurNme "                                                                   
                        + " FROM insurance_policy_proposal a "                                                 
                        + " LEFT JOIN client_master b ON b.sClientID = a.sClientID "  /*owner*/                
                        + " LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = '1' "    
                        + " LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID "                               
                        + " LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx  "                               
                        + " LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx  "                               
                        + " LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx  "                               
                        + " LEFT JOIN vehicle_serial h ON h.sSerialID = a.sSerialID "                          
                        + " LEFT JOIN vehicle_serial_registration i ON i.sSerialID = a.sSerialID "             
                        + " LEFT JOIN vehicle_master j ON j.sVhclIDxx = h.sVhclIDxx "                          
                        + " LEFT JOIN client_master k ON k.sClientID = h.sCoCltIDx " /*co-owner*/              
                        + " LEFT JOIN insurance_company_branches l ON l.sBrInsIDx = a.sBrInsIDx "              
                        + " LEFT JOIN insurance_company m ON m.sInsurIDx = l.sInsurIDx " 
                        + " WHERE 0=1";
        
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), "insurance_policy_proposal", "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
