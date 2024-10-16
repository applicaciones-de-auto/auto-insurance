/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.insurance;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
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
        
        poJSON = validateEntry();
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
        BigDecimal ldblODTCPrem = new BigDecimal("0.00");
        BigDecimal ldblONCPrem = new BigDecimal("0.00");
        BigDecimal ldblBasicPrem = new BigDecimal("0.00");
        BigDecimal ldblTaxAmt = new BigDecimal("0.00");
        BigDecimal ldblTotalAmt = new BigDecimal("0.00");
        Double ldblODTCRate = 0.00;
        Double ldblAONCRate = 0.00;
        Double ldblTaxRate = 0.00;
        //Own Damage/Theft * (rate / 100) = odt premium
        //nODTCAmtx * nODTCRate = nODTCPrem
        ldblODTCRate = poController.getMasterModel().getODTCRate() / 100;
        ldblODTCPrem = poController.getMasterModel().getODTCAmt().multiply(new BigDecimal(ldblODTCRate));
        poController.getMasterModel().setODTCPrem(ldblODTCPrem);
        
        //cAONCPayM = cha / foc
        //nAONCAmtx * (nAONCRate / 100) = nAONCPrem 
        if(poController.getMasterModel().getAONCPayM().equals("cha")){
            ldblAONCRate = poController.getMasterModel().getAONCRate() / 100;
            ldblONCPrem = poController.getMasterModel().getAONCAmt().multiply(new BigDecimal(ldblAONCRate));
            poController.getMasterModel().setAONCPrem(ldblONCPrem);
        } else {
            poController.getMasterModel().setAONCPrem(ldblONCPrem); //0.00
        }
        
        //BASIC PREMIUM = (nODTCPrem + nAONCPrem + nBdyCPrem + nPrDCPrem + nPAcCPrem + nTPLPremx)
        ldblBasicPrem = poController.getMasterModel().getODTCPrem().add(poController.getMasterModel().getAONCPrem()).add(poController.getMasterModel().getBdyCPrem())
                    .add(poController.getMasterModel().getPrDCPrem()).add(poController.getMasterModel().getPAcCPrem()).add(poController.getMasterModel().getTPLPrem());
        //BASIC PREMIUM * (nTaxRatex /100) = nTaxAmtxx
        ldblTaxRate = poController.getMasterModel().getTaxRate() / 100;
        ldblTaxAmt = ldblBasicPrem.multiply(new BigDecimal(ldblTaxRate));
        
        //BASIC PREMIUM + nTaxAmtxx = nTotalAmt 
        ldblTotalAmt = ldblBasicPrem.add(ldblTaxAmt);
        
        poController.getMasterModel().setTaxAmt(ldblTaxAmt); 
        poController.getMasterModel().setTotalAmt(ldblTotalAmt); 
        
        return loJSON;
    }
    
    public JSONObject validateEntry(){
        JSONObject loJSON = new JSONObject();
        BigDecimal ldblTotalAmt = poController.getMasterModel().getTotalAmt(); 
        
        if (ldblTotalAmt.compareTo(new BigDecimal("0.00")) < 0){
            loJSON.put("result", "error");
            loJSON.put("message", "Invalid Total Amount: " + ldblTotalAmt + " . ");
            return loJSON;
        }
        
        return loJSON;
    }
    /**
     * Select VSP
     * @param fsValue Client Name
     * @param fbByCode set true if search by VSP Code else false
     * @param fsInsType set 0 for TPL ; 1 for Comprehensive ; 2 for both
     * @return 
     */
    public JSONObject searchVSP(String fsValue, boolean fbByCode, String fsInsType){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchVSP(fsValue,fbByCode, fsInsType);
        if(!"error".equals((String) loJSON.get("result"))){
            
            poController.getMasterModel().setVSPTranNo((String) loJSON.get("sTransNox"));
//            poController.getMasterModel().setVSPNo((String) loJSON.get("sVSPNOxxx"));
            poController.getMasterModel().setSerialID((String) loJSON.get("sSerialID"));
            poController.getMasterModel().setClientID((String) loJSON.get("sClientID"));
            poController.getMasterModel().setOwnrNm((String) loJSON.get("sBuyCltNm"));
            poController.getMasterModel().setClientTp((String) loJSON.get("cClientTp"));
            poController.getMasterModel().setAddress(((String) loJSON.get("sAddressx")).trim());
            poController.getMasterModel().setCSNo((String) loJSON.get("sCSNoxxxx"));
            poController.getMasterModel().setPlateNo((String) loJSON.get("sPlateNox"));
            poController.getMasterModel().setFrameNo((String) loJSON.get("sFrameNox"));
            poController.getMasterModel().setEngineNo((String) loJSON.get("sEngineNo"));
            poController.getMasterModel().setVhclFDsc((String) loJSON.get("sVhclFDsc"));  
            poController.getMasterModel().setVhclDesc((String) loJSON.get("sVhclDesc"));  
            poController.getMasterModel().setVhclSize((String) loJSON.get("cVhclSize"));  
            poController.getMasterModel().setBodyType((String) loJSON.get("sBodyType"));
            poController.getMasterModel().setUnitType((String) loJSON.get("sUnitType"));    
            poController.getMasterModel().setColorDsc((String) loJSON.get("sColorDsc"));  
            
            poController.getMasterModel().setDelvryDt(SQLUtil.toDate((String) loJSON.get("dDelvryDt"), SQLUtil.FORMAT_SHORT_DATE));      
            poController.getMasterModel().setUnitPrce(new BigDecimal((String) loJSON.get("nUnitPrce")));
            
            poController.getMasterModel().setIsNew("y");
            switch(fsInsType){
                case "0":
                    poController.getMasterModel().setBrInsID((String) loJSON.get("sInsTplCd")); 
                    poController.getMasterModel().setBrInsNme((String) loJSON.get("sTPLBrIns"));
                    poController.getMasterModel().setInsurNme((String) loJSON.get("sTPLInsNm"));
                break;
                case "1":
                    poController.getMasterModel().setBrInsID((String) loJSON.get("sInsCodex"));
                    poController.getMasterModel().setBrInsNme((String) loJSON.get("sCOMBrIns"));
                    poController.getMasterModel().setInsurNme((String) loJSON.get("sCOMInsNm"));
                break;
                case "2":
                    if(((String) loJSON.get("sInsCodex")).equals((String) loJSON.get("sInsTplCd"))){
                        poController.getMasterModel().setBrInsID((String) loJSON.get("sInsCodex"));
                        poController.getMasterModel().setBrInsNme((String) loJSON.get("sCOMBrIns"));
                        poController.getMasterModel().setInsurNme((String) loJSON.get("sCOMInsNm"));
                    }
                break;
            }
            
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
            poController.getMasterModel().setVhclDesc("");  
            poController.getMasterModel().setVhclSize("");  
            poController.getMasterModel().setBodyType("");  
            poController.getMasterModel().setUnitType("");
            poController.getMasterModel().setColorDsc("");                              
            poController.getMasterModel().setDelvryDt(SQLUtil.toDate("1900-01-01", SQLUtil.FORMAT_SHORT_DATE));      
            poController.getMasterModel().setUnitPrce(new BigDecimal("0.00"));
            
            poController.getMasterModel().setIsNew("");
            poController.getMasterModel().setBrInsID("");
            poController.getMasterModel().setBrInsNme("");
            poController.getMasterModel().setInsurNme("");
        }
        return loJSON;
    }
    
    /**
     * Search General Client
     * @param fsValue Client Name
     * @return 
     */
    public JSONObject searchGeneralClient(String fsValue){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchClient(fsValue);
        if(!"error".equals((String) loJSON.get("result"))){
            poController.getMasterModel().setSerialID((String) loJSON.get("sSerialID"));
            poController.getMasterModel().setClientID((String) loJSON.get("sClientID"));
            poController.getMasterModel().setOwnrNm((String) loJSON.get("sCompnyNm"));
            poController.getMasterModel().setClientTp((String) loJSON.get("cClientTp"));
            poController.getMasterModel().setAddress((String) loJSON.get("sAddressx"));
            poController.getMasterModel().setCSNo((String) loJSON.get("sCSNoxxxx"));
            poController.getMasterModel().setPlateNo((String) loJSON.get("sPlateNox"));
            poController.getMasterModel().setFrameNo((String) loJSON.get("sFrameNox"));
            poController.getMasterModel().setEngineNo((String) loJSON.get("sEngineNo"));
            poController.getMasterModel().setVhclFDsc((String) loJSON.get("sVhclFDsc"));
            poController.getMasterModel().setVhclDesc((String) loJSON.get("sVhclDesc"));  
            poController.getMasterModel().setVhclSize((String) loJSON.get("cVhclSize"));  
            poController.getMasterModel().setBodyType((String) loJSON.get("sBodyType"));
            poController.getMasterModel().setUnitType((String) loJSON.get("sUnitType"));    
            poController.getMasterModel().setColorDsc((String) loJSON.get("sColorDsc"));   
        } else {  
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
            poController.getMasterModel().setVhclDesc("");  
            poController.getMasterModel().setVhclSize("");  
            poController.getMasterModel().setBodyType("");  
            poController.getMasterModel().setUnitType("");
            poController.getMasterModel().setColorDsc("");    
        }
        
        return loJSON;
    }
    
    /**
     * Search Insurance
     * @param fsValue Insurance Name
     * @return 
     */
    public JSONObject searchInsurance(String fsValue){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchInsurance(fsValue);
        if(!"error".equals((String) loJSON.get("result"))){
            poController.getMasterModel().setBrInsID((String) loJSON.get("sBrInsIDx"));
            poController.getMasterModel().setInsurNme((String) loJSON.get("sInsurNme"));
            poController.getMasterModel().setBrInsNme((String) loJSON.get("sBrInsNme"));
        } else {  
            poController.getMasterModel().setBrInsID("");
            poController.getMasterModel().setInsurNme("");
            poController.getMasterModel().setBrInsNme("");
        }
        
        return loJSON;
    }
    
    public ArrayList getProposalList(){return poController.getDetailList();}
    public InsurancePolicyProposal_Master getProposalModel(){return poController;} 
    
    /**
     * Load for approval transaction
     * @return 
     */
    public JSONObject loadProposalForApproval(){
        return poController.loadForApproval();
    }
    
    /**
     * Transaction Approval
     * @param fnRow selected row of transaction to be approved
     * @return 
     */
    public JSONObject approveProposal(int fnRow){
        return poController.approveTransaction(fnRow);
    }
    
    /**
     * Transaction Approve
     * @param fnRow selected row of transaction to be approved
     * @return 
     */
    public JSONObject disapproveProposal(int fnRow){
        return poController.disapproveTransaction(fnRow);
    }
}
