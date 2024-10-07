/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.main.insurance;

import java.math.BigDecimal;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GTransaction;
import org.guanzon.auto.controller.insurance.InsurancePolicy_Master;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class InsurancePolicy  implements GTransaction{
    GRider poGRider;
    String psBranchCd;
    boolean pbWtParent;
    int pnEditMode;
    String psTransStat;
    String psMessagex;
    public JSONObject poJSON;  
    
    InsurancePolicy_Master poController;
    
    public InsurancePolicy(GRider foAppDrver, boolean fbWtParent, String fsBranchCd){
        poController = new InsurancePolicy_Master(foAppDrver,fbWtParent,fsBranchCd);
        
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
    public InsurancePolicy_Master getMasterModel() {
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
        BigDecimal ldblGrossAmt = new BigDecimal("0.00");
        BigDecimal ldblNetTotl = new BigDecimal("0.00");
        BigDecimal ldblDocAmt = new BigDecimal("0.00");
        BigDecimal ldblVATAmt = new BigDecimal("0.00");
        BigDecimal ldblLGUTaxAm = new BigDecimal("0.00");
        BigDecimal ldblAuthFee = new BigDecimal("0.00");
        BigDecimal ldblDiscount = new BigDecimal("0.00");
        BigDecimal ldblCommissn = new BigDecimal("0.00");
        BigDecimal ldblPayAmt = new BigDecimal("0.00");
        Double ldblODTCRate = poController.getMasterModel().getODTCRate();
        Double ldblAONCRate = poController.getMasterModel().getAONCRate();
        Double ldblDocRate = poController.getMasterModel().getDocRate();
        Double ldblVATRate = poController.getMasterModel().getVATRate();
        Double ldblLGUTaxRt = poController.getMasterModel().getLGUTaxRt();
        
        //Own Damage/Theft * (rate / 100) = odt premium
        //nODTCAmtx * nODTCRate = nODTCPrem
        ldblODTCRate = ldblODTCRate / 100;
        ldblODTCPrem = poController.getMasterModel().getODTCAmt().multiply(new BigDecimal(ldblODTCRate));
        poController.getMasterModel().setODTCPrem(ldblODTCPrem);
        
        //cAONCPayM = cha / foc
        //nAONCAmtx * (nAONCRate / 100) = nAONCPrem 
        if(poController.getMasterModel().getAONCPayM().equals("cha")){
            ldblAONCRate = ldblAONCRate / 100;
            ldblONCPrem = poController.getMasterModel().getAONCAmt().multiply(new BigDecimal(ldblAONCRate));
            poController.getMasterModel().setAONCPrem(ldblONCPrem);
        } else {
            poController.getMasterModel().setAONCPrem(ldblONCPrem); //0.00
        }
        
        //BASIC PREMIUM = (nODTCPrem + nAONCPrem + nBdyCPrem + nPrDCPrem + nPAcCPrem + nTPLPremx)
        ldblBasicPrem = poController.getMasterModel().getODTCPrem().add(poController.getMasterModel().getAONCPrem()).add(poController.getMasterModel().getBdyCPrem())
                    .add(poController.getMasterModel().getPrDCPrem()).add(poController.getMasterModel().getPAcCPrem()).add(poController.getMasterModel().getTPLPrem());
        
        //BASIC PREMIUM * (nDocRatex /100) = nDocAmtxx
        ldblDocRate = ldblDocRate / 100;
        ldblDocAmt = ldblBasicPrem.multiply(new BigDecimal(ldblDocRate));
        //BASIC PREMIUM * (nVATRatex /100) = nVATAmtxx
        ldblVATRate = ldblVATRate / 100;
        ldblVATAmt = ldblBasicPrem.multiply(new BigDecimal(ldblVATRate));
        //BASIC PREMIUM * (nLGUTaxRt /100) = nLGUTaxAm
        ldblLGUTaxRt = ldblLGUTaxRt / 100;
        ldblLGUTaxAm = ldblBasicPrem.multiply(new BigDecimal(ldblLGUTaxRt));
        //Authentication fee
        ldblAuthFee = poController.getMasterModel().getAuthFee();
        //.add(ldblTaxAmt).add(ldblTaxAmt) // nTaxAmtxx
        //BASIC PREMIUM  + nDocAmtxx + nVATAmtxx + nLGUTaxAm + nAuthFeex = nGrossAmt 
        ldblGrossAmt = ldblBasicPrem.add(ldblDocAmt).add(ldblVATAmt).add(ldblLGUTaxAm).add(ldblAuthFee);
        ldblDiscount = poController.getMasterModel().getDiscAmt();
        ldblNetTotl = ldblGrossAmt.subtract(ldblDiscount);
        ldblCommissn = poController.getMasterModel().getCommissn();
        ldblPayAmt = ldblGrossAmt.subtract(ldblCommissn);
        
        poController.getMasterModel().setDocAmt(ldblDocAmt); 
        poController.getMasterModel().setVATAmt(ldblVATAmt); 
        poController.getMasterModel().setLGUTaxAm(ldblLGUTaxAm); 
        poController.getMasterModel().setGrossAmt(ldblGrossAmt); 
        poController.getMasterModel().setNetTotal(ldblNetTotl); 
        poController.getMasterModel().setPayAmt(ldblPayAmt); 
        
        return loJSON;
    }
    
    public JSONObject validateEntry(){
        JSONObject loJSON = new JSONObject();
        BigDecimal ldblNetTotalAmt = poController.getMasterModel().getNetTotal(); 
        BigDecimal ldblGrosslAmt = poController.getMasterModel().getGrossAmt(); 
        Double ldblDocRate = poController.getMasterModel().getDocRate();
        Double ldblVATRate = poController.getMasterModel().getVATRate();
        Double ldblLGUTaxRt = poController.getMasterModel().getLGUTaxRt();
        Double ldblTaxRateTotal = ldblDocRate + ldblVATRate + ldblLGUTaxRt;
        
        //Do not allow when total tax rate is greater that the tax rate settled in application
        if(ldblTaxRateTotal >= poController.getMasterModel().getTaxRate() ){
            loJSON.put("result", "error");
            loJSON.put("message", "Total tax rate cannot be greater than the tax settled in Policy Application. ");
            return loJSON;
        }
        
        if (ldblGrosslAmt.compareTo(new BigDecimal("0.00")) < 0){
            loJSON.put("result", "error");
            loJSON.put("message", "Invalid Gross Amount: " + ldblGrosslAmt + " . ");
            return loJSON;
        }
        
        if (ldblNetTotalAmt.compareTo(new BigDecimal("0.00")) < 0){
            loJSON.put("result", "error");
            loJSON.put("message", "Invalid Net Total Amount: " + ldblNetTotalAmt + " . ");
            return loJSON;
        }
        
        return loJSON;
    }
    
    
    /**
     * Search Insurance Policy Application
     * @param fsValue
     * @return 
     */
    public JSONObject searchPolicyApplication(String fsValue){
        JSONObject loJSON = new JSONObject();
        loJSON = poController.searchPolicyApplication(fsValue);
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
            poController.getMasterModel().setVhclDesc((String) loJSON.get("sVhclDesc"));  
            poController.getMasterModel().setVhclSize((String) loJSON.get("cVhclSize"));  
            poController.getMasterModel().setBodyType((String) loJSON.get("sBodyType"));
            poController.getMasterModel().setUnitType((String) loJSON.get("sUnitType"));    
            poController.getMasterModel().setColorDsc((String) loJSON.get("sColorDsc")); 
            
            poController.getMasterModel().setInsurNme((String) loJSON.get("sInsurNme"));
            poController.getMasterModel().setBrInsNme((String) loJSON.get("sBrInsNme"));
            poController.getMasterModel().setInsTypID((String) loJSON.get("sInsTypID"));
            poController.getMasterModel().setIsNew((String) loJSON.get("cIsNewxxx"));
            poController.getMasterModel().setBrBankNm((String) loJSON.get("sBrBankNm"));
            poController.getMasterModel().setBankName((String) loJSON.get("sBankname"));
            poController.getMasterModel().setValidFrmDte(SQLUtil.toDate((String) loJSON.get("dValidFrm"), SQLUtil.FORMAT_SHORT_DATE));
            poController.getMasterModel().setValidTruDte(SQLUtil.toDate((String) loJSON.get("dValidTru"), SQLUtil.FORMAT_SHORT_DATE));
            poController.getMasterModel().setApplicDte(SQLUtil.toDate((String) loJSON.get("dTransact"), SQLUtil.FORMAT_SHORT_DATE));
            
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
//            poController.getMasterModel().setTaxAmt(new BigDecimal((String) loJSON.get("nTaxAmtxx")));
//            poController.getMasterModel().setTotalAmt(new BigDecimal((String) loJSON.get("nTotalAmt")));  
            
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
            poController.getMasterModel().setBrBankNm("");
            poController.getMasterModel().setBankName("");
            poController.getMasterModel().setValidFrmDte(SQLUtil.toDate("1900-01-01", SQLUtil.FORMAT_SHORT_DATE));
            poController.getMasterModel().setValidTruDte(SQLUtil.toDate("1900-01-01", SQLUtil.FORMAT_SHORT_DATE));
            poController.getMasterModel().setApplicDte(SQLUtil.toDate("1900-01-01", SQLUtil.FORMAT_SHORT_DATE));
            
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
        }
        return loJSON;
    }
    
}
