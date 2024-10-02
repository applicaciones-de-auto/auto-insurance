/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.insurance;

import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.auto.controller.insurance.InsurancePolicyApplication_Master;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class InsurancePolicyApplication  implements GTransaction{
    GRider poGRider;
    String psBranchCd;
    boolean pbWtParent;
    int pnEditMode;
    String psTransStat;
    String psMessagex;
    public JSONObject poJSON;  
    
    InsurancePolicyApplication_Master poController;
    
    public InsurancePolicyApplication(GRider foAppDrver, boolean fbWtParent, String fsBranchCd){
        poController = new InsurancePolicyApplication_Master(foAppDrver,fbWtParent,fsBranchCd);
        
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
    public InsurancePolicyApplication_Master getMasterModel() {
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
    
    public JSONObject searchEmployee(String fsValue){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchEmployee(fsValue);
        if(!"error".equals((String) loJSON.get("result"))){
            poController.getMasterModel().setEmployID((String) loJSON.get("sEmployID"));
            poController.getMasterModel().setEmpName((String) loJSON.get("sCompnyNm"));
            
        } else {     
            poController.getMasterModel().setEmployID("");
            poController.getMasterModel().setEmpName("");
        }
        return loJSON;
    }
    
    /**
     * Search Insurance Policy Proposal
     * @param fsValue
     * @return 
     */
    public JSONObject searchProposal(String fsValue){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchProposal(fsValue);
        if(!"error".equals((String) loJSON.get("result"))){
            poController.getMasterModel().setReferNo((String) loJSON.get("sTransNox"));
            poController.getMasterModel().setPropslNo((String) loJSON.get("sReferNox"));
            poController.getMasterModel().setOwnrNm((String) loJSON.get("sOwnrNmxx"));
            poController.getMasterModel().setAddress((String) loJSON.get("sAddressx"));
            poController.getMasterModel().setCSNo((String) loJSON.get("sCSNoxxxx"));
            poController.getMasterModel().setPlateNo((String) loJSON.get("sPlateNox"));
            poController.getMasterModel().setEngineNo((String) loJSON.get("sEngineNo"));
            poController.getMasterModel().setFrameNo((String) loJSON.get("sFrameNox"));
            poController.getMasterModel().setVhclFDsc((String) loJSON.get("sVhclFDsc"));
            
            poController.getMasterModel().setInsurNme((String) loJSON.get("sInsurNme"));
            poController.getMasterModel().setBrInsNme((String) loJSON.get("sBrInsNme"));
            poController.getMasterModel().setInsTypID((String) loJSON.get("sInsTypID"));
            poController.getMasterModel().setIsNew((String) loJSON.get("cIsNewxxx"));
            poController.getMasterModel().setBrBankID((String) loJSON.get("sBankIDxx"));
            poController.getMasterModel().setBrBankNm((String) loJSON.get("sBankname"));
            
        } else {     
            poController.getMasterModel().setReferNo("");
            poController.getMasterModel().setPropslNo("");
            poController.getMasterModel().setOwnrNm("");
            poController.getMasterModel().setAddress("");
            poController.getMasterModel().setCSNo("");
            poController.getMasterModel().setPlateNo("");
            poController.getMasterModel().setEngineNo("");
            poController.getMasterModel().setFrameNo("");
            poController.getMasterModel().setVhclFDsc("");
            
            poController.getMasterModel().setInsurNme("");
            poController.getMasterModel().setBrInsNme("");
            poController.getMasterModel().setInsTypID("");
            poController.getMasterModel().setIsNew("");
            poController.getMasterModel().setBrBankID("");
            poController.getMasterModel().setBrBankNm("");
        }
        return loJSON;
    }
    
}
