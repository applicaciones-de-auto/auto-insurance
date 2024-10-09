/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.insurance;

import java.math.BigDecimal;
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
    
    /**
     * Search Insurance Coordinator
     * @param fsValue
     * @return 
     */
    public JSONObject searchInsuranceCoordinator(String fsValue){
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
        JSONObject loJSONChecker = new JSONObject();
        loJSON = poController.searchProposal(fsValue);
        
        if(!"error".equals((String) loJSON.get("result"))){
            loJSONChecker = poController.checkExistingApplication((String) loJSON.get("sTransNox"));
            if("error".equals((String)loJSONChecker.get("result"))){
                return loJSONChecker;
            }
            
            poController.getMasterModel().setReferNo((String) loJSON.get("sTransNox"));
            poController.getMasterModel().setPropslNo((String) loJSON.get("sReferNox"));
            poController.getMasterModel().setOwnrNm((String) loJSON.get("sOwnrNmxx"));
            poController.getMasterModel().setAddress((String) loJSON.get("sAddressx"));
            poController.getMasterModel().setCSNo((String) loJSON.get("sCSNoxxxx"));
            poController.getMasterModel().setPlateNo((String) loJSON.get("sPlateNox"));
            poController.getMasterModel().setEngineNo((String) loJSON.get("sEngineNo"));
            poController.getMasterModel().setFrameNo((String) loJSON.get("sFrameNox"));
            poController.getMasterModel().setVhclFDsc((String) loJSON.get("sVhclFDsc"));
            poController.getMasterModel().setVhclDesc((String) loJSON.get("sVhclDesc")); 
            poController.getMasterModel().setVhclSize((String) loJSON.get("cVhclSize"));  
            poController.getMasterModel().setBodyType((String) loJSON.get("sBodyType"));
            poController.getMasterModel().setUnitType((String) loJSON.get("sUnitType")); 
            poController.getMasterModel().setColorDsc((String) loJSON.get("sColorDsc"));
            
            poController.getMasterModel().setInsurNme((String) loJSON.get("sInsurNme"));
            poController.getMasterModel().setBrInsNme((String) loJSON.get("sBrInsNme"));
            poController.getMasterModel().setInsTypID((String) loJSON.get("sInsTypID"));
            poController.getMasterModel().setIsNew((String) loJSON.get("cIsNewxxx"));
            poController.getMasterModel().setVSPTrnNo((String) loJSON.get("sVSPNoxxx"));
            if((String) loJSON.get("sVSPNoxxx") != null ){
                if(!((String) loJSON.get("sVSPNoxxx")).trim().isEmpty()){
                    poController.getMasterModel().setFinType((String) loJSON.get("cPayModex"));
                    poController.getMasterModel().setBrBankID((String) loJSON.get("sBankIDxx"));
                    poController.getMasterModel().setBankName((String) loJSON.get("sBankname"));
                    poController.getMasterModel().setBrBankNm("");
                }
            }
            
            poController.getMasterModel().setAONCPayM((String) loJSON.get("cAONCPayM"));
            poController.getMasterModel().setODTCRate(Double.valueOf((String) loJSON.get("nODTCRate")));
            poController.getMasterModel().setAONCRate(Double.valueOf((String) loJSON.get("nAONCRate")));
            poController.getMasterModel().setTaxRate(Double.valueOf((String) loJSON.get("nTaxRatex")));
            poController.getMasterModel().setODTCAmt(new BigDecimal((String) loJSON.get("nODTCAmtx")));
            poController.getMasterModel().setODTCPrem(new BigDecimal((String) loJSON.get("nODTCPrem")));
            poController.getMasterModel().setAONCAmt(new BigDecimal((String) loJSON.get("nAONCAmtx")));
            poController.getMasterModel().setAONCPrem(new BigDecimal((String) loJSON.get("nAONCPrem")));
            poController.getMasterModel().setBdyCAmt(new BigDecimal((String) loJSON.get("nBdyCAmtx")));
            poController.getMasterModel().setBdyCPrem(new BigDecimal((String) loJSON.get("nBdyCPrem")));
            poController.getMasterModel().setPrDCAmt(new BigDecimal((String) loJSON.get("nPrDCAmtx")));
            poController.getMasterModel().setPrDCPrem(new BigDecimal((String) loJSON.get("nPrDCPrem")));
            poController.getMasterModel().setPAcCAmt(new BigDecimal((String) loJSON.get("nPAcCAmtx")));
            poController.getMasterModel().setPAcCPrem(new BigDecimal((String) loJSON.get("nPAcCPrem")));
            poController.getMasterModel().setTPLAmt(new BigDecimal((String) loJSON.get("nTPLAmtxx")));
            poController.getMasterModel().setTPLPrem(new BigDecimal((String) loJSON.get("nTPLPremx")));
            poController.getMasterModel().setTaxAmt(new BigDecimal((String) loJSON.get("nTaxAmtxx")));
            poController.getMasterModel().setTotalAmt(new BigDecimal((String) loJSON.get("nTotalAmt")));     
            
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
            poController.getMasterModel().setVhclDesc("");
            poController.getMasterModel().setVhclSize("");  
            poController.getMasterModel().setBodyType("");  
            poController.getMasterModel().setUnitType("");
            poController.getMasterModel().setColorDsc("");
            
            poController.getMasterModel().setInsurNme("");
            poController.getMasterModel().setBrInsNme("");
            poController.getMasterModel().setInsTypID("");
            poController.getMasterModel().setIsNew("");
            poController.getMasterModel().setFinType("");
            poController.getMasterModel().setBrBankID("");
            poController.getMasterModel().setBrBankNm("");
            
            poController.getMasterModel().setAONCPayM("");
            poController.getMasterModel().setODTCRate(0.00);
            poController.getMasterModel().setAONCRate(0.00);
            poController.getMasterModel().setTaxRate(0.00);
            poController.getMasterModel().setODTCAmt(new BigDecimal("0.00"));
            poController.getMasterModel().setODTCPrem(new BigDecimal("0.00"));
            poController.getMasterModel().setAONCAmt(new BigDecimal("0.00"));
            poController.getMasterModel().setAONCPrem(new BigDecimal("0.00"));
            poController.getMasterModel().setBdyCAmt(new BigDecimal("0.00"));
            poController.getMasterModel().setBdyCPrem(new BigDecimal("0.00"));
            poController.getMasterModel().setPrDCAmt(new BigDecimal("0.00"));
            poController.getMasterModel().setPrDCPrem(new BigDecimal("0.00"));
            poController.getMasterModel().setPAcCAmt(new BigDecimal("0.00"));
            poController.getMasterModel().setPAcCPrem(new BigDecimal("0.00"));
            poController.getMasterModel().setTPLAmt(new BigDecimal("0.00"));
            poController.getMasterModel().setTPLPrem(new BigDecimal("0.00"));
            poController.getMasterModel().setTaxAmt(new BigDecimal("0.00"));
            poController.getMasterModel().setTotalAmt(new BigDecimal("0.00"));
        }
        return loJSON;
    }
    
    /**
     * Search Bank
     * @param fsValue
     * @return 
     */
    public JSONObject searchbank(String fsValue){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchBank(fsValue);
        if(!"error".equals((String) loJSON.get("result"))){
            poController.getMasterModel().setBrBankID((String) loJSON.get("sBrBankID"));
            poController.getMasterModel().setBrBankNm((String) loJSON.get("sBrBankNm"));
            poController.getMasterModel().setBankName((String) loJSON.get("sBankName"));
            
        } else {     
            poController.getMasterModel().setBrBankID("");
            poController.getMasterModel().setBrBankNm("");
            poController.getMasterModel().setBankName("");
        }
        return loJSON;
    }
    
}
