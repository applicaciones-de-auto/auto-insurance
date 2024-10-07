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
public class InsurancePolicyApplicationMaster {
    
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
        
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Insurance_Policy_Application.xml");
        
        
        String lsSQL =     " SELECT "                                                                                             
                        + "    a.sTransNox "                                                                                     
                        + "  , a.dTransact "                                                                                     
                        + "  , a.sReferNox "                                                                                     
                        + "  , a.dValidFrm "                                                                                     
                        + "  , a.dValidTru "                                                                                     
                        + "  , a.cFinTypex "                                                                                     
                        + "  , a.sBrBankID "                                                                                     
                        + "  , a.sEmployID "                                                                                     
                        + "  , a.cTranStat "                                                                                     
                        + "  , a.sModified "                                                                                     
                        + "  , a.dModified "                                                                                     
                        + "  , CASE "                                                                                            
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CLOSED)+" THEN 'APPROVE' "               
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)+" THEN 'CANCELLED' "          
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_OPEN)+" THEN 'ACTIVE' "                  
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_POSTED)+" THEN 'POSTED' "                
                        + "  ELSE 'ACTIVE' "                                                                                     
                        + "    END AS sTranStat "                                                                                
                         /*POLICY PROPOSAL*/                                                                                     
                        + " , DATE(b.dTransact) AS dPropslDt"                                                                                      
                        + " , b.sReferNox AS sPropslNo "                                                                         
                        + " , b.sClientID "                                                                                      
                        + " , b.sSerialID "                                                                       
                        + " , b.sVSPNoxxx AS sVSPTrnNo "                                                                                       
                        + " , b.sBrInsIDx "                                                                                      
                        + " , b.sInsTypID "                                                                                      
                        + " , b.cIsNewxxx "                                                                                      
                        + " , b.nODTCAmtx "                                                                                      
                        + " , b.nODTCRate "                                                                                      
                        + " , b.nODTCPrem "                                                                                      
                        + " , b.nAONCAmtx "                                                                                      
                        + " , b.nAONCRate "                                                                                      
                        + " , b.nAONCPrem "                                                                                      
                        + " , b.cAONCPayM "                                                                                      
                        + " , b.nBdyCAmtx "                                                                                      
                        + " , b.nBdyCPrem "                                                                                      
                        + " , b.nPrDCAmtx "                                                                                      
                        + " , b.nPrDCPrem "                                                                                      
                        + " , b.nPAcCAmtx "                                                                                      
                        + " , b.nPAcCPrem "                                                                                      
                        + " , b.nTPLAmtxx "                                                                                      
                        + " , b.nTPLPremx "                                                                                      
                        + " , b.nTaxRatex "                                                                                      
                        + " , b.nTaxAmtxx "                                                                                      
                        + " , b.nTotalAmt "                                                                                      
                         /*CLIENT INFO */                                                                                        
                        + " , c.sCompnyNm AS sOwnrNmxx "                                                                         
                        + " , c.cClientTp "                                                                                      
                        + " , IFNULL(CONCAT( IFNULL(CONCAT(e.sHouseNox,' ') , ''), "                                             
                        + "   IFNULL(CONCAT(e.sAddressx,' ') , ''),  "                                                           
                        + "   IFNULL(CONCAT(f.sBrgyName,' '), ''),   "                                                           
                        + "   IFNULL(CONCAT(g.sTownName, ', '),''),  "                                                           
                        + "   IFNULL(CONCAT(h.sProvName),'') )	, '') AS sAddressx "                                             
                        + " , l.sCompnyNm AS sCoOwnrNm "                                                                         
                        + " , i.sCSNoxxxx "                                                                                      
                        + " , i.sFrameNox "                                                                                      
                        + " , i.sEngineNo "                                                                                      
                        + " , i.cVhclNewx "                                                                                      
                        + " , j.sPlateNox "                                                                                      
                        + " , k.sDescript AS sVhclFDsc "  
                        + "  , TRIM(CONCAT_WS(' ',ka.sMakeDesc, kb.sModelDsc, kc.sTypeDesc, k.sTransMsn, k.nYearModl )) AS sVhclDesc "
                        + "  , k.cVhclSize "
                        + "  , kb.sUnitType "
                        + "  , kb.sBodyType "
                        + "  , kd.sColorDsc "                                                                         
                        + " , m.sBrInsNme "                                                                                      
                        + " , n.sInsurNme "
                        + " , o.sCompnyNm AS sEmpNamex "
                        + " , p.sBrBankNm "
                        + " , q.sBankName "    
                        + " , r.sPolicyNo "                                                                                     
                        + " FROM insurance_policy_application a "                                                                 
                        + " LEFT JOIN insurance_policy_proposal b ON b.sTransNox = a.sReferNox "                                  
                        + " LEFT JOIN client_master c ON c.sClientID = b.sClientID "  /*owner*/                                   
                        + " LEFT JOIN client_address d ON d.sClientID = b.sClientID AND d.cPrimaryx = '1' "                       
                        + " LEFT JOIN addresses e ON e.sAddrssID = d.sAddrssID "                                                  
                        + " LEFT JOIN barangay f ON f.sBrgyIDxx = e.sBrgyIDxx  "                                                  
                        + " LEFT JOIN towncity g ON g.sTownIDxx = e.sTownIDxx  "                                                  
                        + " LEFT JOIN province h ON h.sProvIDxx = g.sProvIDxx  "                                                  
                        + " LEFT JOIN vehicle_serial i ON i.sSerialID = b.sSerialID "                                             
                        + " LEFT JOIN vehicle_serial_registration j ON j.sSerialID = b.sSerialID "                                
                        + " LEFT JOIN vehicle_master k ON k.sVhclIDxx = i.sVhclIDxx "     
                        + " LEFT JOIN vehicle_make ka ON ka.sMakeIDxx = k.sMakeIDxx  "
                        + " LEFT JOIN vehicle_model kb ON kb.sModelIDx = k.sModelIDx "
                        + " LEFT JOIN vehicle_type kc ON kc.sTypeIDxx = k.sTypeIDxx  "
                        + " LEFT JOIN vehicle_color kd ON kd.sColorIDx = k.sColorIDx "                                         
                        + " LEFT JOIN client_master l ON l.sClientID = i.sCoCltIDx  " /*co-owner*/                                
                        + " LEFT JOIN insurance_company_branches m ON m.sBrInsIDx = b.sBrInsIDx  "                                
                        + " LEFT JOIN insurance_company n ON n.sInsurIDx = m.sInsurIDx " 
                        + " LEFT JOIN ggc_isysdbf.client_master o ON o.sClientID = a.sEmployID " 
                        + " LEFT JOIN banks_branches p ON p.sBrBankID = a.sBrBankID  "                     
                        + " LEFT JOIN banks q ON q.sBankIDxx = p.sBankIDxx "                            
                        + " LEFT JOIN insurance_policy r ON r.sReferNox = a.sTransNox AND r.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                        + " WHERE 0=1";
        
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), "insurance_policy_application", "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
