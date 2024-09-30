/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.insurance;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.auto.controller.insurance.InsurancePolicyProposal_Master;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class InsurancePolicyProposal implements GTransaction{
    GRider poGRider;
    String psBranchCd;
    boolean pbWtParent;
    int pnEditMode;
    String psTransStat;
    String psMessagex;
    public JSONObject poJSON;  
    
    InsurancePolicyProposal_Master poController;
    
    public InsurancePolicyProposal(GRider foAppDrver, boolean fbWtParent, String fsBranchCd){
        poController = new InsurancePolicyProposal_Master(foAppDrver,fbWtParent,fsBranchCd);
        
        poGRider = foAppDrver;
        pbWtParent = fbWtParent;
        psBranchCd = fsBranchCd.isEmpty() ? foAppDrver.getBranchCode() : fsBranchCd;
    }

    @Override
    public int getEditMode() {
        pnEditMode = poController.getEditMode();
        return pnEditMode;
    }
    
    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        return poController.setMaster(fnCol, foData);
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return poController.setMaster(fsCol, foData);
    }

    public Object getMaster(int fnCol) {
        if(pnEditMode == EditMode.UNKNOWN)
            return null;
        else 
            return poController.getMaster(fnCol);
    }

    public Object getMaster(String fsCol) {
        return poController.getMaster(fsCol);
    }

    @Override
    public JSONObject newTransaction() {
        poJSON = new JSONObject();
        try{
            poJSON = poController.newTransaction();
            
            if("success".equals(poJSON.get("result"))){
                pnEditMode = poController.getEditMode();
            } else {
                pnEditMode = EditMode.UNKNOWN;
            }
               
        }catch(NullPointerException e){
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
            pnEditMode = EditMode.UNKNOWN;
        }
        return poJSON;
    }

    @Override
    public JSONObject openTransaction(String fsValue) {
        poJSON = new JSONObject();
        
        poJSON = poController.openTransaction(fsValue);
        if("success".equals(poJSON.get("result"))){
            pnEditMode = poController.getEditMode();
        } else {
            pnEditMode = EditMode.UNKNOWN;
        }
        return poJSON;
    }

    @Override
    public JSONObject updateTransaction() {
        poJSON = new JSONObject();  
        poJSON = poController.updateTransaction();
        if("error".equals(poJSON.get("result"))){
            return poJSON;
        }
        pnEditMode = poController.getEditMode();
        return poJSON;
    }

    @Override
    public JSONObject saveTransaction() {
        poJSON = new JSONObject();  
        
        poJSON = computeAmount();
        if("error".equalsIgnoreCase((String)poJSON.get("result"))){
            return poJSON;
        }
        
        if (!pbWtParent) poGRider.beginTrans();
        
        poJSON =  poController.saveTransaction();
        if("error".equalsIgnoreCase((String) checkData(poJSON).get("result"))){
            if (!pbWtParent) poGRider.rollbackTrans();
            return checkData(poJSON);
        }
        if (!pbWtParent) poGRider.commitTrans();
        
        return poJSON;
    }
    
    private JSONObject checkData(JSONObject joValue){
        if(pnEditMode == EditMode.ADDNEW ||pnEditMode == EditMode.READY || pnEditMode == EditMode.UPDATE){
            if(joValue.containsKey("continue")){
                if(true == (boolean)joValue.get("continue")){
                    joValue.put("result", "success");
                    joValue.put("message", "Record saved successfully.");
                }
            }
        }
        return joValue;
    }
    
    public JSONObject searchTransaction(String fsValue, boolean fbByCode) {
        poJSON = new JSONObject();  
        poJSON = poController.searchTransaction(fsValue, fbByCode);
        if(!"error".equals(poJSON.get("result"))){
            poJSON = openTransaction((String) poJSON.get("sTransNox"));
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
    public JSONObject cancelTransaction(String fsValue) {
        return poController.cancelTransaction(fsValue);
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
    public InsurancePolicyProposal_Master getMasterModel() {
        return poController;
    }
    @Override
    public void setTransactionStatus(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public JSONObject computeAmount(){
        JSONObject loJSON = new JSONObject();
        
        return loJSON;
    }
    
    public JSONObject searchVSP(String fsValue, boolean fbByCode){
        JSONObject loJSON = new JSONObject();
        JSONObject loJSONDet = new JSONObject();
        loJSON = poController.searchVSP(fsValue,fbByCode);
        if(!"error".equals((String) loJSON.get("result"))){
            poController.getMasterModel().setVSPTranNo((String) loJSON.get("sTransNox"));
//            poController.getMasterModel().setVSPNo((String) loJSON.get("sVSPNOxxx"));
            poController.getMasterModel().setSerialID((String) loJSON.get("sSerialID"));
            poController.getMasterModel().setClientID((String) loJSON.get("sClientID"));
            poController.getMasterModel().setOwnrNm((String) loJSON.get("sBuyCltNm"));
            poController.getMasterModel().setClientTp((String) loJSON.get("cClientTp"));
            poController.getMasterModel().setAddress((String) loJSON.get("sAddressx"));
            poController.getMasterModel().setCSNo((String) loJSON.get("sCSNoxxxx"));
            poController.getMasterModel().setPlateNo((String) loJSON.get("sPlateNox"));
            poController.getMasterModel().setFrameNo((String) loJSON.get("sFrameNox"));
            poController.getMasterModel().setEngineNo((String) loJSON.get("sEngineNo"));
            poController.getMasterModel().setVhclFDsc((String) loJSON.get("sVhclFDsc"));
            
            poController.getMasterModel().setIsNew("0");
            
            poController.getMasterModel().setBrInsID((String) loJSON.get("sInsCodex")); //Compre
            
        } else {     
            poController.getMasterModel().setVSPTranNo("");
//            poController.getMasterModel().setVSPNo("");           
            poController.getMasterModel().setSerialID("");        
            poController.getMasterModel().setClientID("");        
            poController.getMasterModel().setOwnrNm("");          
            poController.getMasterModel().setClientTp("");        
            poController.getMasterModel().setAddress("");         
            poController.getMasterModel().setCSNo("");            
            poController.getMasterModel().setPlateNo("");         
            poController.getMasterModel().setFrameNo("");         
            poController.getMasterModel().setEngineNo("");        
            poController.getMasterModel().setVhclFDsc("");   
            
            poController.getMasterModel().setIsNew("");
            poController.getMasterModel().setBrInsID("");
        }
        return loJSON;
    }
    
}
