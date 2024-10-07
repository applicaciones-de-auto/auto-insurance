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
public class InsurancePolicyMaster {
    
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
        
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/Model_Insurance_Policy.xml");
        
        
        String lsSQL =    " SELECT "                                                                                          
                        + "   a.sTransNox "                                                                                   
                        + " , a.dTransact "                                                                                   
                        + " , a.sReferNox "                                                                                   
                        + " , a.dValidFrm "                                                                                   
                        + " , a.dValidTru "                                                                                   
                        + " , a.sPolicyNo "                                                                                   
                        + " , a.sCOCNoxxx "                                                                                   
                        + " , a.sORNoxxxx "                                                                                   
                        + " , a.sMVFileNo "                                                                                   
                        + " , a.nODTCAmtx "                                                                                   
                        + " , a.nODTCRate "                                                                                   
                        + " , a.nODTCPrem "                                                                                   
                        + " , a.nAONCAmtx "                                                                                   
                        + " , a.nAONCRate "                                                                                   
                        + " , a.nAONCPrem "                                                                                   
                        + " , a.cAONCPayM "                                                                                   
                        + " , a.nBdyCAmtx "                                                                                   
                        + " , a.nBdyCPrem "                                                                                   
                        + " , a.nPrDCAmtx "                                                                                   
                        + " , a.nPrDCPrem "                                                                                   
                        + " , a.nPAcCAmtx "                                                                                   
                        + " , a.nPAcCPrem "                                                                                   
                        + " , a.nTPLAmtxx "                                                                                   
                        + " , a.nTPLPremx "                                                                                   
                        + " , a.nDocRatex "                                                                                   
                        + " , a.nDocAmtxx "                                                                                   
                        + " , a.nVATRatex "                                                                                   
                        + " , a.nVATAmtxx "                                                                                   
                        + " , a.nLGUTaxRt "                                                                                   
                        + " , a.nLGUTaxAm "                                                                                   
                        + " , a.nAuthFeex "                                                                                   
                        + " , a.nGrossAmt "                                                                                   
                        + " , a.nDiscAmtx "                                                                                   
                        + " , a.nNetTotal "                                                                                   
                        + " , a.nCommissn "                                                                                   
                        + " , a.nPayAmtxx "                                                                                   
                        + " , a.sRemarksx "                                                                                   
                        + " , a.cTranStat "                                                                                   
                        + " , a.sModified "                                                                                   
                        + " , a.dModified "                                                                                   
                        + " , CASE        "                                                                                   
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CLOSED)+" THEN 'APPROVE'      "       
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)+" THEN 'CANCELLED' "       
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_OPEN)+" THEN 'ACTIVE'         "       
                        + "  WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_POSTED)+" THEN 'POSTED'       "       
                        + "  ELSE 'ACTIVE' "                                                                                  
                        + "    END AS sTranStat "                                                                             
                        /*POLICY APPLICATION */                                                                               
                        + " , b.dTransact AS dApplicDt "                                                                      
                        + " , b.sReferNox AS sApplicNo "                                                                    
                        + " , b.sEmployID "  
                        /*POLICY PROPOSAL*/                                                                                   
                        + " , c.dTransact AS dPropslDt "                                                                      
                        + " , c.sReferNox AS sPropslNo "                                                                      
                        + " , c.sClientID "                                                                                   
                        + " , c.sSerialID "                                                                                   
                        + " , c.sVSPNoxxx AS sVSPTrnNo "                                                                      
                        + " , c.sBrInsIDx "                                                                                   
                        + " , c.sInsTypID "                                                                                   
                        + " , c.cIsNewxxx "                                                                                   
                        + " , c.nTaxRatex "                                                                                                
                        /*CLIENT INFO */                                                                                      
                        + " , d.sCompnyNm AS sOwnrNmxx "                                                                          
                        + " , d.cClientTp "                                                                                   
                        + " , IFNULL(CONCAT( IFNULL(CONCAT(f.sHouseNox,' ') , ''), "                                          
                        + "   IFNULL(CONCAT(f.sAddressx,' ') , ''), "                                                         
                        + "   IFNULL(CONCAT(g.sBrgyName,' '), ''),  "                                                         
                        + "   IFNULL(CONCAT(h.sTownName, ', '),''), "                                                         
                        + "   IFNULL(CONCAT(i.sProvName),'') )	, '') AS sAddressx "                                          
                        + " , m.sCompnyNm AS sCoOwnrNm  "                                                                     
                        + " , j.sCSNoxxxx "                                                                                   
                        + " , j.sFrameNox "                                                                                   
                        + " , j.sEngineNo "                                                                                   
                        + " , j.cVhclNewx "                                                                                   
                        + " , k.sPlateNox "                                                                                   
                        + " , l.sDescript AS sVhclFDsc "    
                        + "  , TRIM(CONCAT_WS(' ',la.sMakeDesc, lb.sModelDsc, lc.sTypeDesc, l.sTransMsn, l.nYearModl )) AS sVhclDesc "
                        + "  , l.cVhclSize "
                        + "  , lb.sUnitType "
                        + "  , lb.sBodyType "
                        + "  , ld.sColorDsc "                                                                   
                        + " , n.sBrInsNme "                                                                                   
                        + " , o.sInsurNme "                                                                                   
                        + " , p.sCompnyNm AS sEmpNamex "                                                                      
                        + " , q.sBrBankNm "                                                                                   
                        + " , r.sBankName "                                                                                   
                        + " FROM insurance_policy a "                                                                         
                        + " LEFT JOIN insurance_policy_application b  ON b.sTransNox = a.sReferNox "                          
                        + " LEFT JOIN insurance_policy_proposal c ON c.sTransNox = b.sReferNox "                              
                        + " LEFT JOIN client_master d ON d.sClientID = c.sClientID "  /*owner*/                               
                        + " LEFT JOIN client_address e ON e.sClientID = c.sClientID AND e.cPrimaryx = '1' "                   
                        + " LEFT JOIN addresses f ON f.sAddrssID = e.sAddrssID "                                              
                        + " LEFT JOIN barangay g ON g.sBrgyIDxx = f.sBrgyIDxx  "                                              
                        + " LEFT JOIN towncity h ON h.sTownIDxx = f.sTownIDxx  "                                              
                        + " LEFT JOIN province i ON i.sProvIDxx = h.sProvIDxx  "                                              
                        + " LEFT JOIN vehicle_serial j ON j.sSerialID = c.sSerialID "                                         
                        + " LEFT JOIN vehicle_serial_registration k ON k.sSerialID = c.sSerialID "                            
                        + " LEFT JOIN vehicle_master l ON l.sVhclIDxx = j.sVhclIDxx "   
                        + " LEFT JOIN vehicle_make la ON la.sMakeIDxx = l.sMakeIDxx  "
                        + " LEFT JOIN vehicle_model lb ON lb.sModelIDx = l.sModelIDx "
                        + " LEFT JOIN vehicle_type lc ON lc.sTypeIDxx = l.sTypeIDxx  "
                        + " LEFT JOIN vehicle_color ld ON ld.sColorIDx = l.sColorIDx "                                      
                        + " LEFT JOIN client_master m ON m.sClientID = j.sCoCltIDx  " /*co-owner*/                            
                        + " LEFT JOIN insurance_company_branches n ON n.sBrInsIDx = c.sBrInsIDx "                             
                        + " LEFT JOIN insurance_company o ON o.sInsurIDx = n.sInsurIDx "                                      
                        + " LEFT JOIN ggc_isysdbf.client_master p ON p.sClientID = b.sEmployID "                              
                        + " LEFT JOIN banks_branches q ON q.sBrBankID = b.sBrBankID "                                         
                        + " LEFT JOIN banks r ON r.sBankIDxx = q.sBankIDxx "
                        + " WHERE 0=1";
        
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        try {
            if (MiscUtil.resultSet2XML(instance, loRS, System.getProperty("sys.default.path.metadata"), "insurance_policy", "")){
                System.out.println("ResultSet exported.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
