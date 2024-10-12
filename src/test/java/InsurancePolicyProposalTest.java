
import java.math.BigDecimal;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.auto.main.insurance.InsurancePolicyProposal;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arsiela
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InsurancePolicyProposalTest {
    static InsurancePolicyProposal model;
    JSONObject json;
    boolean result;
    static GRider instance;
    public InsurancePolicyProposalTest(){}
    
    @BeforeClass
    public static void setUpClass() {   
        
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Maven_Systems";
        }
        else{
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        instance = new GRider("gRider");
        if (!instance.logUser("gRider", "M001000001")){
            System.err.println(instance.getMessage() + instance.getErrMsg());
            System.exit(1);
        }
        System.out.println("Connected");
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        
        
        JSONObject json;
        
        System.out.println("sBranch code = " + instance.getBranchCode());
        model = new InsurancePolicyProposal(instance,false, instance.getBranchCode());
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    /**
     * COMMENTED TESTING TO CLEAN AND BUILD PROPERLY
     * WHEN YOU WANT TO CHECK KINDLY UNCOMMENT THE TESTING CASES (@Test).
     * ARSIELA 
     */

//    @Test
//    public void test01NewRecord() throws SQLException{
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------NEW RECORD--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.newTransaction();
//        if ("success".equals((String) json.get("result"))){
//
//            json = model.getMasterModel().getMasterModel().setSerialID("M001VS240005");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setClientID("M00124000028");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setVSPNo("M001VSP24005");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setBrInsID("M001IN240001");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setInsTypID("0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setIsNew("0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setRemarks("TEST INSURANCE POLICY");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setAONCPayM("0");
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            } 
//            
//            json = model.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal("10000.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setODTCRate(0.5);
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setODTCPrem(new BigDecimal("16000.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setAONCRate(0.2);
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setAONCPrem(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setPacCPrem(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setTaxRate(0.8);
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setTaxAmt(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//            json = model.getMasterModel().getMasterModel().setTotalAmt(new BigDecimal("0.00"));
//            if ("error".equals((String) json.get("result"))){
//                System.err.println((String) json.get("message"));
//                System.exit(1);
//            }
//            
//        } else {
//            System.err.println("result = " + (String) json.get("result"));
//            fail((String) json.get("message"));
//        }
//        
//    }
//    
//    @Test
//    public void test01NewRecordSave(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------NEW RECORD SAVING--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.saveTransaction();
//        System.err.println((String) json.get("message"));
//        
//        if (!"success".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            System.out.println((String) json.get("message"));
//            result = true;
//        }
//        
//        assertTrue(result);
//        //assertFalse(result);
//    }
    
//    @Test
//    public void test02OpenRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------RETRIEVAL--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.openTransaction("M00124000001");
//        
//        if (!"success".equals((String) json.get("result"))){
//            result = false;
//        } else {
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("INSURANCE POLICY PROPOSAL MASTER");
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("sTransNox  :  " + model.getMasterModel().getMasterModel().getTransNo());         
//            System.out.println("dTransact  :  " + model.getMasterModel().getMasterModel().getTransactDte());        
//            System.out.println("sReferNox  :  " + model.getMasterModel().getMasterModel().getReferNo());         
//            System.out.println("sClientID  :  " + model.getMasterModel().getMasterModel().getClientID());        
//            System.out.println("sSerialID  :  " + model.getMasterModel().getMasterModel().getSerialID());        
//            System.out.println("sVSPNoxxx  :  " + model.getMasterModel().getMasterModel().getVSPNo());           
//            System.out.println("sBrInsIDx  :  " + model.getMasterModel().getMasterModel().getBrInsID());         
//            System.out.println("sInsTypID  :  " + model.getMasterModel().getMasterModel().getInsTypID());        
//            System.out.println("cIsNewxxx  :  " + model.getMasterModel().getMasterModel().getIsNew());           
//            System.out.println("nODTCAmtx  :  " + model.getMasterModel().getMasterModel().getODTCAmt());         
//            System.out.println("nODTCRate  :  " + model.getMasterModel().getMasterModel().getODTCRate());        
//            System.out.println("nODTCPrem  :  " + model.getMasterModel().getMasterModel().getODTCPrem());        
//            System.out.println("nAONCAmtx  :  " + model.getMasterModel().getMasterModel().getAONCAmt());         
//            System.out.println("nAONCRate  :  " + model.getMasterModel().getMasterModel().getAONCRate());        
//            System.out.println("nAONCPrem  :  " + model.getMasterModel().getMasterModel().getAONCPrem());        
//            System.out.println("cAONCPayM  :  " + model.getMasterModel().getMasterModel().getAONCPayM());        
//            System.out.println("nBdyCAmtx  :  " + model.getMasterModel().getMasterModel().getBdyCAmt());         
//            System.out.println("nBdyCPrem  :  " + model.getMasterModel().getMasterModel().getBdyCPrem());        
//            System.out.println("nPrDCAmtx  :  " + model.getMasterModel().getMasterModel().getPrDCAmt());         
//            System.out.println("nPrDCPrem  :  " + model.getMasterModel().getMasterModel().getPrDCPrem());        
//            System.out.println("nPAcCAmtx  :  " + model.getMasterModel().getMasterModel().getPAcCAmt());         
//            System.out.println("nPacCPrem  :  " + model.getMasterModel().getMasterModel().getPacCPrem());        
//            System.out.println("nTPLAmtxx  :  " + model.getMasterModel().getMasterModel().getTPLAmt());          
//            System.out.println("nTPLPremx  :  " + model.getMasterModel().getMasterModel().getTPLPrem());         
//            System.out.println("nTaxRatex  :  " + model.getMasterModel().getMasterModel().getTaxRate());         
//            System.out.println("nTaxAmtxx  :  " + model.getMasterModel().getMasterModel().getTaxAmt());          
//            System.out.println("nTotalAmt  :  " + model.getMasterModel().getMasterModel().getTotalAmt());        
//            System.out.println("sRemarksx  :  " + model.getMasterModel().getMasterModel().getRemarks());         
//            System.out.println("cTranStat  :  " + model.getMasterModel().getMasterModel().getTranStat());        
//            System.out.println("sModified  :  " + model.getMasterModel().getMasterModel().getModifiedBy());      
//            System.out.println("dModified  :  " + model.getMasterModel().getMasterModel().getModifiedDte());     
//            System.out.println("sApproved  :  " + model.getMasterModel().getMasterModel().getApprovedBy());      
//            System.out.println("dApproved  :  " + model.getMasterModel().getMasterModel().getApprovedDte());     
//
//            System.out.println("sOwnrNmxx  :  " + model.getMasterModel().getMasterModel().getOwnrNm());          
//            System.out.println("cClientTp  :  " + model.getMasterModel().getMasterModel().getClientTp());        
//            System.out.println("sAddressx  :  " + model.getMasterModel().getMasterModel().getAddress());         
//            System.out.println("sCoOwnrNm  :  " + model.getMasterModel().getMasterModel().getCoOwnrNm());        
//            System.out.println("sCSNoxxxx  :  " + model.getMasterModel().getMasterModel().getCSNo());            
//            System.out.println("sFrameNox  :  " + model.getMasterModel().getMasterModel().getFrameNo());         
//            System.out.println("sEngineNo  :  " + model.getMasterModel().getMasterModel().getEngineNo());        
//            System.out.println("cVhclNewx  :  " + model.getMasterModel().getMasterModel().getVhclNew());         
//            System.out.println("sPlateNox  :  " + model.getMasterModel().getMasterModel().getPlateNo());         
//            System.out.println("sVhclFDsc  :  " + model.getMasterModel().getMasterModel().getVhclFDsc());        
//            System.out.println("sBrInsNme  :  " + model.getMasterModel().getMasterModel().getBrInsNme());        
//            System.out.println("sInsurNme  :  " + model.getMasterModel().getMasterModel().getInsurNme());        
//
//            result = true;
//        }
//        assertTrue(result);
//        //assertFalse(result);
//    }
//    
//    @Test
//    public void test03UpdateRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------UPDATE RECORD--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.updateTransaction();
//        System.err.println((String) json.get("message"));
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            result = true;
//        }
//        
//        json = model.getMasterModel().getMasterModel().setTaxRate(1.8);
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            System.exit(1);
//        }
//
//        json = model.getMasterModel().getMasterModel().setTaxAmt(new BigDecimal("0.00"));
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            System.exit(1);
//        }
//
//        json = model.getMasterModel().getMasterModel().setTotalAmt(new BigDecimal("0.00"));
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            System.exit(1);
//        }
//        
//        json = model.getMasterModel().getMasterModel().setRemarks("TEST INSURANCE POLICY EDIT SAVE");
//        if ("error".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            System.exit(1);
//        } 
//    
//        assertTrue(result);
//        //assertFalse(result);
//    }
//    
//    @Test
//    public void test03UpdateRecordSave(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------UPDATE RECORD SAVING--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.saveTransaction();
//        System.err.println((String) json.get("message"));
//        
//        if (!"success".equals((String) json.get("result"))){
//            System.err.println((String) json.get("message"));
//            result = false;
//        } else {
//            System.out.println((String) json.get("message"));
//            result = true;
//        }
//        assertTrue(result);
//        //assertFalse(result);
//    }
}
