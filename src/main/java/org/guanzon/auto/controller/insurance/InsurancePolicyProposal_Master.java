/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.controller.insurance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.auto.general.CancelForm;
import org.guanzon.auto.general.SearchDialog;
import org.guanzon.auto.model.clients.Model_Client_Master;
import org.guanzon.auto.model.clients.Model_Vehicle_Serial_Master;
import org.guanzon.auto.model.insurance.Model_Insurance_Policy_Proposal;
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
//            poModel.setReferNo(MiscUtil.getNextCode(poModel.getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
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
    
    public JSONObject searchVSP(String fsValue, boolean fbByCode){
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
        
        if(fbByCode){
            lsSQL = MiscUtil.addCondition(lsSQL,  " a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                                                + " AND a.sTransNox = " + SQLUtil.toSQL(fsValue)
                                                + " GROUP BY a.sTransNox ");
        
        } else {
            lsSQL = MiscUtil.addCondition(lsSQL,  " a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                                                + " AND b.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%")
                                                + " GROUP BY a.sTransNox ");
        }
        System.out.println("SEARCH VSP: " + lsSQL);
        loJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                    lsHeader,
                    lsColName,
                    lsCriteria,
                    0);

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
     * Search Service Advisor
     * @param fsValue Employee name
     * @return 
     */
    public JSONObject searchClient(String fsValue){
        poJSON = new JSONObject();
        String lsHeader = "Client ID»Customer»CS No»Plate No";
        String lsColName = "sClientID»sBuyCltNm»sCSNoxxxx»sPlateNox"; 
        String lsCriteria = "a.sClientID»a.sCompnyNm»g.sCSNoxxxx»h.sPlateNox"; 
        String lsSQL =    " SELECT "                                                                       
                        + "    a.sClientID "                                                               
                        + "  , a.sLastName "                                                               
                        + "  , a.sFrstName "                                                               
                        + "  , a.sMiddName "                                                               
                        + "  , a.sMaidenNm "                                                               
                        + "  , a.sSuffixNm "                                                               
                        + "  , a.sTitlexxx "                                                               
                        + "  , a.cGenderCd "                                                               
                        + "  , a.cCvilStat "                                                               
                        + "  , a.sCitizenx "                                                               
                        + "  , a.dBirthDte "                                                               
                        + "  , a.sBirthPlc "                                                               
                        + "  , a.sTaxIDNox "                                                               
                        + "  , a.sLTOIDxxx "                                                               
                        + "  , a.sAddlInfo "                                                               
                        + "  , a.sCompnyNm "                                                               
                        + "  , a.sClientNo "                                                               
                        + "  , a.sSpouseID "                                                               
                        + "  , a.cClientTp "                                                               
                        + "  , a.cRecdStat "                                                               
                        + "  , a.sEntryByx "                                                               
                        + "  , a.dEntryDte "                                                               
                        + "  , a.sModified "                                                               
                        + "  , a.dModified "                                                               
                        + "  , IFNULL(CONCAT( IFNULL(CONCAT(c.sHouseNox,' ') , ''), "                      
                        + "  IFNULL(CONCAT(c.sAddressx,' ') , ''), "                                       
                        + "  IFNULL(CONCAT(d.sBrgyName,' '), ''),  "                                       
                        + "  IFNULL(CONCAT(e.sTownName, ', '),''), "                                       
                        + "  IFNULL(CONCAT(f.sProvName),'') )	, '') AS sAddressx "                         
                        + "  , g.sCSNoxxxx "                                                               
                        + "  , h.sPlateNox "                                                               
                        + "  , i.sDescript "                                                               
                        + " FROM client_master a "                                                         
                        + " LEFT JOIN client_address b ON b.sClientID = a.sClientID AND b.cPrimaryx = 1 "  
                        + " LEFT JOIN addresses c ON c.sAddrssID = b.sAddrssID "                           
                        + " LEFT JOIN barangay d ON d.sBrgyIDxx = c.sBrgyIDxx  "                           
                        + " LEFT JOIN towncity e ON e.sTownIDxx = c.sTownIDxx  "                           
                        + " LEFT JOIN province f ON f.sProvIDxx = e.sProvIDxx  "                           
                        + " LEFT JOIN vehicle_serial g ON g.sClientID = a.sClientID "                      
                        + " LEFT JOIN vehicle_serial_registration h ON h.sSerialID = g.sSerialID "         
                        + " LEFT JOIN vehicle_master i ON i.sVhclIDxx = g.sVhclIDxx "   ;      
        
        lsSQL = MiscUtil.addCondition(lsSQL, "a.sCompnyNm LIKE " + SQLUtil.toSQL(fsValue + "%")); 
        
        System.out.println("SEARCH CLIENT: " + lsSQL);
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                lsHeader,
                lsColName,
                lsCriteria,
                1);
        
        if (poJSON != null) {
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
        
        return poJSON;
    }
}
