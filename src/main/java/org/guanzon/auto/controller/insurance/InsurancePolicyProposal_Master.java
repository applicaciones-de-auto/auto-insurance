/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.controller.insurance;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.auto.general.CancelForm;
import org.guanzon.auto.general.SearchDialog;
import org.guanzon.auto.general.TransactionStatusHistory;
import org.guanzon.auto.model.clients.Model_Client_Master;
import org.guanzon.auto.model.clients.Model_Vehicle_Serial_Master;
import org.guanzon.auto.model.insurance.Model_Insurance_Policy_Proposal;
import org.guanzon.auto.model.parameter.Model_Insurance_Branches;
import org.guanzon.auto.model.sales.Model_VehicleSalesProposal_Master;
import org.guanzon.auto.validator.insurance.ValidatorFactory;
import org.guanzon.auto.validator.insurance.ValidatorInterface;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class InsurancePolicyProposal_Master implements GTransaction{
    GRider poGRider;
    String psBranchCd;
    boolean pbWtParent;
    int pnEditMode;
    String psTransStat;
    
    String psMessagex;
    public JSONObject poJSON;
    
    Model_Insurance_Policy_Proposal poModel;
    ArrayList<Model_Insurance_Policy_Proposal> paDetail;
    
    public InsurancePolicyProposal_Master(GRider foGRider, boolean fbWthParent, String fsBranchCd) {
        poGRider = foGRider;
        pbWtParent = fbWthParent;
        psBranchCd = fsBranchCd.isEmpty() ? foGRider.getBranchCode() : fsBranchCd;

        poModel = new Model_Insurance_Policy_Proposal(foGRider);
        pnEditMode = EditMode.UNKNOWN;
    }
    
    @Override
    public int getEditMode() {
        return pnEditMode;
    }
    
    @Override
    public Model_Insurance_Policy_Proposal getMasterModel() {
        return poModel;
    }
    
    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        JSONObject obj = new JSONObject();
        obj.put("pnEditMode", pnEditMode);
        if (pnEditMode != EditMode.UNKNOWN){
            // Don't allow specific fields to assign values
            if(!(fnCol == poModel.getColumn("sTransNox") ||
                fnCol == poModel.getColumn("cTranStat") ||
                fnCol == poModel.getColumn("sEntryByx") ||
                fnCol == poModel.getColumn("dEntryDte") ||
                fnCol == poModel.getColumn("sModified") ||
                fnCol == poModel.getColumn("dModified"))){
                poModel.setValue(fnCol, foData);
                obj.put(fnCol, pnEditMode);
            }
        }
        return obj;
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return setMaster(poModel.getColumn(fsCol), foData);
    }
    
    public Object getMaster(int fnCol) {
        if(pnEditMode == EditMode.UNKNOWN)
            return null;
        else 
            return poModel.getValue(fnCol);
    }

    public Object getMaster(String fsCol) {
        return getMaster(poModel.getColumn(fsCol));
    }
    
    @Override
    public JSONObject newTransaction() {
        poJSON = new JSONObject();
        try{
            pnEditMode = EditMode.ADDNEW;
            org.json.simple.JSONObject obj;

            poModel = new Model_Insurance_Policy_Proposal(poGRider);
            Connection loConn = null;
            loConn = setConnection();

            poModel.setTransNo(MiscUtil.getNextCode(poModel.getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
            poModel.setReferNo(MiscUtil.getNextCode(poModel.getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
            poModel.newRecord();
            
            if (poModel == null){
                poJSON.put("result", "error");
                poJSON.put("message", "initialized new record failed.");
                return poJSON;
            }else{
                poJSON.put("result", "success");
                poJSON.put("message", "initialized new record.");
                pnEditMode = EditMode.ADDNEW;
            }
        }catch(NullPointerException e){
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        
        return poJSON;
    }
    
    private Connection setConnection(){
        Connection foConn;
        if (pbWtParent){
            foConn = (Connection) poGRider.getConnection();
            if (foConn == null) foConn = (Connection) poGRider.doConnect();
        }else foConn = (Connection) poGRider.doConnect();
        return foConn;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        pnEditMode = EditMode.READY;
        poJSON = new JSONObject();
        
        poModel = new Model_Insurance_Policy_Proposal(poGRider);
        poJSON = poModel.openRecord(fsValue);
        
        return poJSON;
    }
    
    private JSONObject checkData(JSONObject joValue){
        if(pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE){
            if(joValue.containsKey("continue")){
                if(true == (boolean)joValue.get("continue")){
                    joValue.put("result", "success");
                    joValue.put("message", "Record saved successfully.");
                }
            }
        }
        return joValue;
    }

    @Override
    public JSONObject updateTransaction() {
        poJSON = new JSONObject();
        if (pnEditMode != EditMode.READY && pnEditMode != EditMode.UPDATE){
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid edit mode.");
            return poJSON;
        }
        
        pnEditMode = EditMode.UPDATE;
        poJSON.put("result", "success");
        poJSON.put("message", "Update mode success.");
        return poJSON;
    }

    @Override
    public JSONObject saveTransaction() {
        poJSON = new JSONObject();  
        
        ValidatorInterface validator = ValidatorFactory.make( ValidatorFactory.TYPE.Policy_Proposal, poModel);
        validator.setGRider(poGRider);
        if (!validator.isEntryOkay()){
            poJSON.put("result", "error");
            poJSON.put("message", validator.getMessage());
            return poJSON;
        }
        
        poJSON =  poModel.saveRecord();
        if("error".equalsIgnoreCase((String) poJSON.get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        } 
        
        return poJSON;
    }

    @Override
    public JSONObject deleteTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject closeTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject postTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject voidTransaction(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject cancelTransaction(String fsTransNox) {
        poJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.READY
                || poModel.getEditMode() == EditMode.UPDATE) {
            try {
                poJSON = poModel.setTranStat(TransactionStatus.STATE_CANCELLED);
                if ("error".equals((String) poJSON.get("result"))) {
                    return poJSON;
                }
                
                ValidatorInterface validator = ValidatorFactory.make( ValidatorFactory.TYPE.Policy_Proposal, poModel);
                validator.setGRider(poGRider);
                if (!validator.isEntryOkay()){
                    poJSON.put("result", "error");
                    poJSON.put("message", validator.getMessage());
                    return poJSON;
                }
                
                CancelForm cancelform = new CancelForm();
                if (!cancelform.loadCancelWindow(poGRider, poModel.getTransNo(), poModel.getReferNo(), "POLICY PROPOSAL")) {
                    poJSON.put("result", "error");
                    poJSON.put("message", "Cancellation failed.");
                    return poJSON;
                }
                
                poJSON = poModel.saveRecord();
            } catch (SQLException ex) {
                Logger.getLogger(InsurancePolicyProposal_Master.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }
    
    
    public JSONObject searchTransaction(String fsValue, boolean fbByCode) {
        String lsHeader = "Proposal Date»Proposal No»Customer»CS No»Plate No»Status";
        String lsColName = "dTransact»sReferNox»sOwnrNmxx»sCSNoxxxx»sPlateNox»sTranStat";
        String lsSQL = poModel.getSQL();
        System.out.println(lsSQL);
        JSONObject loJSON = SearchDialog.jsonSearch(
                    poGRider,
                    lsSQL,
                    "",
                    lsHeader,
                    lsColName,
                "0.1D»0.2D»0.3D»0.2D»0.2D»0.3D", 
                    "POLICY PROPOSAL",
                    0);
            
        if (loJSON != null && !"error".equals((String) loJSON.get("result"))) {
        }else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No Transaction loaded.");
            return loJSON;
        }
        return loJSON;
    }

    @Override
    public JSONObject searchWithCondition(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchTransaction(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchMaster(String string, String string1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject searchMaster(int i, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTransactionStatus(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public JSONObject searchVSP(String fsValue, boolean fbByCode, String fsInsType){
        JSONObject loJSON = new JSONObject(); 
        String lsID = "sVSPNOxxx";
        if(fbByCode){
            lsID = "sTransNox";
        }
        String lsHeader = "VSP No»Customer»CS No»Plate No";//VSP Date»
        String lsColName = lsID+"»sBuyCltNm»sCSNoxxxx»sPlateNox"; //"dTransact»"+
        String lsCriteria = "a."+lsID+"»b.sCompnyNm»p.sCSNoxxxx»q.sPlateNox"; //a.dTransact»
        Model_VehicleSalesProposal_Master loEntity = new Model_VehicleSalesProposal_Master(poGRider);
        String lsSQL = loEntity.getSQL() ; 
        //UNION DID NOT WORK FOR THE GENERIC SEARCH
//        String lsSQLSelect =   "  SELECT DISTINCT "                                                                      
//                            + "   a.sTransNox "                                        
//                            + " , a.dTransact "                                        
//                            + " , a.sVSPNOxxx "                                        
//                            + " , a.dDelvryDt "                                        
//                            + " , a.sInqryIDx "                                        
//                            + " , a.sClientID "                                        
//                            + " , a.sCoCltIDx "                                        
//                            + " , a.sSerialID "                                        
//                            + " , a.nUnitPrce "                                        
//                            + " , a.nInsurAmt "                                        
//                            + " , a.sInsurTyp "                                        
//                            + " , a.nInsurYrx "                                        
//                            + " , a.sInsTplCd "                                        
//                            + " , a.sInsCodex "                                        
//                            + " , a.sBranchCD "                                                             
//                            + " , a.cIsVhclNw "                                                             
//                            + " , a.cIsVIPxxx "                                                             
//                            + " , a.cTranStat "                                                             
//                            /*BUYING COSTUMER*/                                                             
//                            + " , b.sCompnyNm AS sBuyCltNm "                                                             
//                            + " , b.cClientTp "                                                               
//                            + " , TRIM(IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''), "                      
//                            + "   IFNULL(CONCAT(d.sAddressx,' ') , ''), "                                 
//                            + "   IFNULL(CONCAT(e.sBrgyName,' '), ''),  "                                 
//                            + "   IFNULL(CONCAT(f.sTownName, ', '),''), "                                 
//                            + "   IFNULL(CONCAT(g.sProvName),'') )	, '')) AS sAddressx "                      
//                            /*INQUIRY*/                                                            
//                            + " , h.sInqryIDx AS sInquryID  "                                                               
//                            + " , DATE(h.dTransact) AS dInqryDte "                                                  
//                            + " , h.sClientID AS sInqCltID "                                                
//                            + " , i.sCompnyNm AS sInqCltNm "                                                
//                            + " , i.cClientTp AS cInqCltTp "                                                
//                            + " , h.sContctID "                                                             
//                            + " , j.sCompnyNm AS sContctNm "                                                
//                            + " , h.sSourceCD "                                                             
//                            + " , h.sSourceNo "                                                             
//                            + " , k.sPlatform "                                                             
//                            + " , h.sAgentIDx "                                                             
//                            + " , l.sCompnyNm AS sAgentNmx "                                                
//                            + " , h.sEmployID "                                                             
//                            + " , m.sCompnyNm AS sSENamexx "                                                
//                            /*CO-CLIENT*/                                                                
//                            + " , o.sCompnyNm AS sCoCltNmx "                                                
//                            /*VEHICLE INFORMATION*/                                                         
//                            + " , p.sCSNoxxxx "                                                            
//                            + " , q.sPlateNox "                                                            
//                            + " , p.sFrameNox "                                                            
//                            + " , p.sEngineNo "                                                            
//                            + " , p.sKeyNoxxx "                                                            
//                            + " , r.sDescript AS sVhclFDsc "
//                            + " , TRIM(CONCAT_WS(' ',ra.sMakeDesc, rb.sModelDsc, rc.sTypeDesc, r.sTransMsn, r.nYearModl )) AS sVhclDesc "
//                            + " , rd.sColorDsc " ;
//        String lsTable = " FROM vsp_master a "                                                           
//                        /*BUYING CUSTOMER*/                                                              
//                        + " LEFT JOIN client_master b ON b.sClientID = a.sClientID  "                     
//                        + " LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = 1 "  
//                        + " LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID "                         
//                        + " LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx  "                         
//                        + " LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx  "                         
//                        + " LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx  "
//                        + " LEFT JOIN client_mobile ba ON ba.sClientID = b.sClientID AND ba.cPrimaryx = 1  "
//                        + " LEFT JOIN client_email_address bb ON bb.sClientID = b.sClientID AND bb.cPrimaryx = 1 "                          
//                        /*INQUIRY*/                                                                      
//                        + " LEFT JOIN customer_inquiry h ON h.sTransNox = a.sInqryIDx "                   
//                        + " LEFT JOIN client_master i ON i.sClientID = h.sClientID    "                   
//                        + " LEFT JOIN client_master j ON j.sClientID = h.sContctID    "                   
//                        + " LEFT JOIN online_platforms k ON k.sTransNox = h.sSourceNo "                   
//                        + " LEFT JOIN client_master l ON l.sClientID = h.sAgentIDx    "                   
//                        + " LEFT JOIN ggc_isysdbf.client_master m ON m.sClientID = h.sEmployID "              
//                        /*CO CLIENT*/                                                                    
//                        + " LEFT JOIN client_master o ON o.sClientID = a.sCoCltIDx  "                     
//                        /*VEHICLE INFORMATION*/                                                          
//                        + " LEFT JOIN vehicle_serial p ON p.sSerialID = a.sSerialID "                     
//                        + " LEFT JOIN vehicle_serial_registration q ON q.sSerialID = a.sSerialID "        
//                        + " LEFT JOIN vehicle_master r ON r.sVhclIDxx = p.sVhclIDxx  "  
//                        + " LEFT JOIN vehicle_make ra ON ra.sMakeIDxx = r.sMakeIDxx  "
//                        + " LEFT JOIN vehicle_model rb ON rb.sModelIDx = r.sModelIDx "
//                        + " LEFT JOIN vehicle_type rc ON rc.sTypeIDxx = r.sTypeIDxx  "
//                        + " LEFT JOIN vehicle_color rd ON rd.sColorIDx = r.sColorIDx " ;
//        
//        String lsTPLWhere = " WHERE a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
//                            + " AND b.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%")
//                            + " AND (NOT ISNULL(a.sInsTplCd) AND a.sInsTplCd != '') AND (a.sInsTplCd != a.sInsCodex)  "
//                            + " GROUP BY a.sTransNox ";
//        String lsCOMPREWhere = " WHERE a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
//                            + " AND b.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%")
//                            + " AND (NOT ISNULL(a.sInsCodex) AND a.sInsCodex != '') AND (a.sInsTplCd != a.sInsCodex)  "
//                            + " GROUP BY a.sTransNox ";
//        String lsBOTHWhere = " WHERE a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
//                            + " AND b.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%")
//                            + " AND (NOT ISNULL(a.sInsTplCd) AND a.sInsTplCd != '') AND (NOT ISNULL(a.sInsCodex) AND a.sInsCodex != '') AND (a.sInsTplCd = a.sInsCodex)  "
//                            + " GROUP BY a.sTransNox ";
        
        String lsCondition = "";
        switch(fsInsType){
            case "0": //TPL
                lsCondition = " (NOT ISNULL(a.sInsTplCd) AND a.sInsTplCd != '') ";
            break;
            case "1": //Compre
                lsCondition = " (NOT ISNULL(a.sInsCodex) AND a.sInsCodex != '') ";
            break;
            case "2": //Both
                lsCondition = " (NOT ISNULL(a.sInsTplCd) AND a.sInsTplCd != '') AND (NOT ISNULL(a.sInsCodex) AND a.sInsCodex != '') AND (a.sInsTplCd = a.sInsCodex) ";
            break;
        }
        
        if(fbByCode){
            lsSQL = MiscUtil.addCondition(lsSQL,  " a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                                                + " AND a.sTransNox = " + SQLUtil.toSQL(fsValue)
                                                + " AND " + lsCondition 
                                                + " GROUP BY a.sTransNox ");
        
        } else {
            lsSQL = MiscUtil.addCondition(lsSQL,  " a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                                                + " AND b.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%")
                                                + " AND " + lsCondition 
                                                + " GROUP BY a.sTransNox ");
//            lsSQL =  lsSQLSelect + " , 'TPL' AS sInsTypex " + lsTable + " " + lsTPLWhere
//                    + " UNION "
//                    + lsSQLSelect + " , 'COMPREHENSIVE' AS sInsTypex " + lsTable + " " + lsCOMPREWhere
//                    + " UNION "
//                    + lsSQLSelect + " , 'BOTH' AS sInsTypex " + lsTable + " " + lsBOTHWhere
//                    + " ORDER BY sTransNox ";
        }
        
        System.out.println("SEARCH VSP: " + lsSQL);
        loJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                    lsHeader,
                    lsColName,
                    lsCriteria,
                    fbByCode ? 0 : 1);

        if (loJSON != null) {
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded.");
            return loJSON;
        }
        return loJSON;
    }
    
     /**
     * Search General Client
     * @param fsValue Employee name
     * @return 
     */
    public JSONObject searchClient(String fsValue){
        JSONObject loJSON = new JSONObject();
        String lsHeader = "Client ID»Customer»CS No»Plate No";
        String lsColName = "sClientID»sCompnyNm»sCSNoxxxx»sPlateNox"; 
        String lsCriteria = "a.sClientID»a.sCompnyNm»g.sCSNoxxxx»h.sPlateNox"; 
        String lsSQL =    " SELECT "                                                                       
                        + "    a.sClientID "                                                               
                        + "  , a.sLastName "                                                               
                        + "  , a.sFrstName "                                                               
                        + "  , a.sMiddName "                                                               
                        + "  , a.sMaidenNm "                                                               
                        + "  , a.sSuffixNm "                                                             
                        + "  , a.sTaxIDNox "                                                               
                        + "  , a.sLTOIDxxx "                                                               
                        + "  , a.sCompnyNm "                                                             
                        + "  , a.cClientTp "                                                               
                        + "  , a.cRecdStat "                                                              
                        + "  , IFNULL(CONCAT( IFNULL(CONCAT(c.sHouseNox,' ') , ''), "                      
                        + "  IFNULL(CONCAT(c.sAddressx,' ') , ''), "                                       
                        + "  IFNULL(CONCAT(d.sBrgyName,' '), ''),  "                                       
                        + "  IFNULL(CONCAT(e.sTownName, ', '),''), "                                       
                        + "  IFNULL(CONCAT(f.sProvName),'') )	, '') AS sAddressx "                         
                        + "  , g.sSerialID "                                                      
                        + "  , g.sCSNoxxxx "                                                      
                        + "  , g.sEngineNo "                                                     
                        + "  , g.sFrameNox "                                                              
                        + "  , h.sPlateNox "                                                               
                        + "  , i.sDescript AS sVhclFDsc" 
                        + "  , TRIM(CONCAT_WS(' ',ia.sMakeDesc, ib.sModelDsc, ic.sTypeDesc, i.sTransMsn, i.nYearModl )) AS sVhclDesc "
                        + "  , i.cVhclSize "
                        + "  , ib.sUnitType "
                        + "  , ib.sBodyType "
                        + "  , id.sColorDsc "                                                                 
                        + " FROM client_master a "                                                         
                        + " LEFT JOIN client_address b ON b.sClientID = a.sClientID AND b.cPrimaryx = 1 "  
                        + " LEFT JOIN addresses c ON c.sAddrssID = b.sAddrssID "                           
                        + " LEFT JOIN barangay d ON d.sBrgyIDxx = c.sBrgyIDxx  "                           
                        + " LEFT JOIN towncity e ON e.sTownIDxx = c.sTownIDxx  "                           
                        + " LEFT JOIN province f ON f.sProvIDxx = e.sProvIDxx  "                           
                        + " INNER JOIN vehicle_serial g ON g.sClientID = a.sClientID "                      
                        + " LEFT JOIN vehicle_serial_registration h ON h.sSerialID = g.sSerialID "         
                        + " INNER JOIN vehicle_master i ON i.sVhclIDxx = g.sVhclIDxx "
                        + " LEFT JOIN vehicle_make ia ON ia.sMakeIDxx = i.sMakeIDxx  "
                        + " LEFT JOIN vehicle_model ib ON ib.sModelIDx = i.sModelIDx "
                        + " LEFT JOIN vehicle_type ic ON ic.sTypeIDxx = i.sTypeIDxx  "
                        + " LEFT JOIN vehicle_color id ON id.sColorIDx = i.sColorIDx "   ;      
        
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%")
                                               + " AND a.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE)); 
        
        System.out.println("SEARCH CLIENT: " + lsSQL);
        loJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                lsHeader,
                lsColName,
                lsCriteria,
                1);
        
        if (loJSON != null) {
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded.");
            return loJSON;
        }
        
        return loJSON;
    }
    
     /**
     * Search Insurance
     * @param fsValue Employee name
     * @return 
     */
    public JSONObject searchInsurance(String fsValue){
        JSONObject loJSON = new JSONObject();
        String lsHeader = "Branch ID»Insurance»Address";
        String lsColName = "sBrInsIDx»sInsurnce»xAddressx";
        String lsCriteria = "a.sBrInsIDx»CONCAT(b.sInsurNme,' ',a.sBrInsNme)»CONCAT(a.sAddressx, ' ', c.sTownName, ' ',  d.sProvName)";
        Model_Insurance_Branches loEntity = new Model_Insurance_Branches(poGRider);
        String lsSQL = loEntity.getSQL(); 
        
        lsSQL = MiscUtil.addCondition(lsSQL,  " a.cRecdStat = '1' "
                                                + " AND CONCAT(b.sInsurNme, ' ',  a.sBrInsNme) LIKE " + SQLUtil.toSQL(fsValue + "%"));
        System.out.println("SEARCH INSURANCE: " + lsSQL);
        loJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                    lsHeader,
                    lsColName,
                    lsCriteria,
                1);

        if (loJSON != null) {
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded.");
            return loJSON;
        }
        return loJSON;
    }
    
    public ArrayList<Model_Insurance_Policy_Proposal> getDetailList(){
        if(paDetail == null){
           paDetail = new ArrayList<>();
        }
        return paDetail;
    }
    public void setDetailList(ArrayList<Model_Insurance_Policy_Proposal> foObj){this.paDetail = foObj;}
    
    public void setDetail(int fnRow, int fnIndex, Object foValue){ paDetail.get(fnRow).setValue(fnIndex, foValue);}
    public void setDetail(int fnRow, String fsIndex, Object foValue){ paDetail.get(fnRow).setValue(fsIndex, foValue);}
    public Object getDetail(int fnRow, int fnIndex){return paDetail.get(fnRow).getValue(fnIndex);}
    public Object getDetail(int fnRow, String fsIndex){return paDetail.get(fnRow).getValue(fsIndex);}
    
    public Model_Insurance_Policy_Proposal getDetailModel(int fnRow) {
        return paDetail.get(fnRow);
    }
    
    public JSONObject loadForApproval(){
        /*
        -cTranStat	0	For Follow-up
        -cTranStat	1	On Process
        -cTranStat	2	Lost Sale
        -cTranStat	3	VSP
        -cTranStat	4	Sold
        -cTranStat	5	Cancelled
        */
        paDetail = new ArrayList<>();
        poJSON = new JSONObject();
        Model_Insurance_Policy_Proposal loEntity = new Model_Insurance_Policy_Proposal(poGRider);
        String lsSQL = MiscUtil.addCondition(loEntity.getSQL(), " a.cTranStat = "  + SQLUtil.toSQL(TransactionStatus.STATE_OPEN)
                                                                 + " ORDER BY a.sTransNox ASC ");
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        System.out.println(lsSQL);
       try {
            int lnctr = 0;
            if (MiscUtil.RecordCount(loRS) > 0) {
                while(loRS.next()){
                        paDetail.add(new Model_Insurance_Policy_Proposal(poGRider));
                        paDetail.get(paDetail.size() - 1).openRecord(loRS.getString("sTransNox"));
                        
                        pnEditMode = EditMode.UPDATE;
                        lnctr++;
                        poJSON.put("result", "success");
                        poJSON.put("message", "Record loaded successfully.");
                    } 
                
            }else{
//                paDetail = new ArrayList<>();
//                addDetail(fsValue);
                poJSON.put("result", "error");
                poJSON.put("continue", true);
                poJSON.put("message", "No record selected.");
            }
            MiscUtil.close(loRS);
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return poJSON;
    }
    
    public JSONObject approveTransaction(int fnRow){
        JSONObject loJSON = new JSONObject();
        paDetail.get(fnRow).setTranStat(TransactionStatus.STATE_CLOSED); //Approve
        loJSON = paDetail.get(fnRow).saveRecord();
        if(!"error".equals((String) loJSON.get("result"))){
            TransactionStatusHistory loEntity = new TransactionStatusHistory(poGRider);
            //Update to cancel all previous approvements
            loJSON = loEntity.cancelTransaction(paDetail.get(fnRow).getTransNo());
            if(!"error".equals((String) loJSON.get("result"))){
                loJSON = loEntity.newTransaction();
                if(!"error".equals((String) loJSON.get("result"))){
                    loEntity.getMasterModel().setApproved(poGRider.getUserID());
                    loEntity.getMasterModel().setApprovedDte(poGRider.getServerDate());
                    loEntity.getMasterModel().setSourceNo(paDetail.get(fnRow).getTransNo());
                    loEntity.getMasterModel().setTableNme(paDetail.get(fnRow).getTable());
                    loEntity.getMasterModel().setRefrStat(paDetail.get(fnRow).getTranStat());

                    loJSON = loEntity.saveTransaction();
                    if("error".equals((String) loJSON.get("result"))){
                        return loJSON;
                    }
                }
            }
        
        }
        return loJSON;
    }
    
    public JSONObject disapproveTransaction(int fnRow){
        JSONObject loJSON = new JSONObject();
        paDetail.get(fnRow).setTranStat(TransactionStatus.STATE_VOID); //Disapprove
        loJSON = paDetail.get(fnRow).saveRecord();
        return loJSON;
    }
}
