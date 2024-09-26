
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

    @Test
    public void test01NewRecord() throws SQLException{
        System.out.println("--------------------------------------------------------------------");
        System.out.println("------------------------------NEW RECORD--------------------------------------");
        System.out.println("--------------------------------------------------------------------");
        
        json = model.newTransaction();
        if ("success".equals((String) json.get("result"))){

            json = model.getMasterModel().getMasterModel().setSerialID("M001VS240005");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setClientID("M00124000028");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setVSPNo("M001VSP24005");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setBrInsID("M001IN240001");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setInsTypID("0");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setIsNew("0");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setRemarks("TEST INSURANCE POLICY");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setAONCPayM("0");
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            } 
            
            json = model.getMasterModel().getMasterModel().setODTCAmt(new BigDecimal("10000.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setODTCRate(0.5);
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setODTCPrem(new BigDecimal("16000.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setAONCAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setAONCRate(0.2);
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setAONCPrem(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setBdyCAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setBdyCPrem(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setPrDCAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setPrDCPrem(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setPAcCAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setPacCPrem(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setTPLAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setTPLPrem(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setTaxRate(0.8);
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setTaxAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
            json = model.getMasterModel().getMasterModel().setTotalAmt(new BigDecimal("0.00"));
            if ("error".equals((String) json.get("result"))){
                System.err.println((String) json.get("message"));
                System.exit(1);
            }
            
        } else {
            System.err.println("result = " + (String) json.get("result"));
            fail((String) json.get("message"));
        }
        
    }
    
    @Test
    public void test01NewRecordSave(){
        System.out.println("--------------------------------------------------------------------");
        System.out.println("------------------------------NEW RECORD SAVING--------------------------------------");
        System.out.println("--------------------------------------------------------------------");
        
        json = model.saveTransaction();
        System.err.println((String) json.get("message"));
        
        if (!"success".equals((String) json.get("result"))){
            System.err.println((String) json.get("message"));
            result = false;
        } else {
            System.out.println((String) json.get("message"));
            result = true;
        }
        
        assertTrue(result);
        //assertFalse(result);
    }
    
//    @Test
//    public void test02OpenRecord(){
//        System.out.println("--------------------------------------------------------------------");
//        System.out.println("------------------------------RETRIEVAL--------------------------------------");
//        System.out.println("--------------------------------------------------------------------");
//        
//        json = model.openTransaction("M001JO240002");
//        
//        if (!"success".equals((String) json.get("result"))){
//            result = false;
//        } else {
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("JO MASTER");
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("sTransNox  :  " + model.getMasterModel().getMasterModel().getTransNo()); 
//            System.out.println("dTransact  :  " + model.getMasterModel().getMasterModel().getTransactDte()); 
//            System.out.println("sDSNoxxxx  :  " + model.getMasterModel().getMasterModel().getDSNo()); 
//            System.out.println("sSerialID  :  " + model.getMasterModel().getMasterModel().getSerialID()); 
//            System.out.println("sClientID  :  " + model.getMasterModel().getMasterModel().getClientID()); 
//            System.out.println("sWorkCtgy  :  " + model.getMasterModel().getMasterModel().getWorkCtgy()); 
//            System.out.println("sJobTypex  :  " + model.getMasterModel().getMasterModel().getJobType()); 
//            System.out.println("sLaborTyp  :  " + model.getMasterModel().getMasterModel().getLaborTyp()); 
//            System.out.println("nKMReadng  :  " + model.getMasterModel().getMasterModel().getKMReadng()); 
//            System.out.println("sEmployID  :  " + model.getMasterModel().getMasterModel().getEmployID()); 
//            System.out.println("sRemarksx  :  " + model.getMasterModel().getMasterModel().getRemarks()); 
//            System.out.println("cPaySrcex  :  " + model.getMasterModel().getMasterModel().getPaySrce()); 
//            System.out.println("sSourceNo  :  " + model.getMasterModel().getMasterModel().getSourceNo()); 
//            System.out.println("sSourceCD  :  " + model.getMasterModel().getMasterModel().getSourceCD()); 
//            System.out.println("dPromised  :  " + model.getMasterModel().getMasterModel().getPromisedDte()); 
//            System.out.println("sInsurnce  :  " + model.getMasterModel().getMasterModel().getInsurnce()); 
//            System.out.println("cCompUnit  :  " + model.getMasterModel().getMasterModel().getCompUnit()); 
//            System.out.println("sActvtyID  :  " + model.getMasterModel().getMasterModel().getActvtyID()); 
//            System.out.println("nLaborAmt  :  " + model.getMasterModel().getMasterModel().getLaborAmt()); 
//            System.out.println("nPartsAmt  :  " + model.getMasterModel().getMasterModel().getPartsAmt());   
//            System.out.println("nTranAmtx  :  " + model.getMasterModel().getMasterModel().getTranAmt());    
//            System.out.println("nRcvryAmt  :  " + model.getMasterModel().getMasterModel().getRcvryAmt());   
//            System.out.println("nPartFeex  :  " + model.getMasterModel().getMasterModel().getPartFee());    
//            System.out.println("cPrintedx  :  " + model.getMasterModel().getMasterModel().getPrinted());    
//            System.out.println("cTranStat  :  " + model.getMasterModel().getMasterModel().getTranStat());   
//            System.out.println("sEntryByx  :  " + model.getMasterModel().getMasterModel().getEntryBy());    
//            System.out.println("dEntryDte  :  " + model.getMasterModel().getMasterModel().getEntryDte());   
//            System.out.println("sModified  :  " + model.getMasterModel().getMasterModel().getModifiedBy()); 
//            System.out.println("dModified  :  " + model.getMasterModel().getMasterModel().getModifiedDte());
//            System.out.println("sOwnrNmxx  :  " + model.getMasterModel().getMasterModel().getOwnrNm());     
//            System.out.println("cClientTp  :  " + model.getMasterModel().getMasterModel().getClientTp());   
//            System.out.println("sAddressx  :  " + model.getMasterModel().getMasterModel().getAddress());    
//            System.out.println("sCoOwnrNm  :  " + model.getMasterModel().getMasterModel().getCoOwnrNm());   
//            System.out.println("sCSNoxxxx  :  " + model.getMasterModel().getMasterModel().getCSNo());       
//            System.out.println("sFrameNox  :  " + model.getMasterModel().getMasterModel().getFrameNo());    
//            System.out.println("sEngineNo  :  " + model.getMasterModel().getMasterModel().getEngineNo());   
//            System.out.println("cVhclNewx  :  " + model.getMasterModel().getMasterModel().getVhclNew());    
//            System.out.println("sPlateNox  :  " + model.getMasterModel().getMasterModel().getPlateNo());    
//            System.out.println("sDescript  :  " + model.getMasterModel().getMasterModel().getVhclDesc());   
//            System.out.println("sVSPNOxxx  :  " + model.getMasterModel().getMasterModel().getVSPNo());      
//            System.out.println("sBranchCD  :  " + model.getMasterModel().getMasterModel().getBranchCD());   
//            System.out.println("sBranchNm  :  " + model.getMasterModel().getMasterModel().getBranchNm());   
//            System.out.println("sCoBuyrNm  :  " + model.getMasterModel().getMasterModel().getCoBuyrNm());   
//            System.out.println("sSrvcAdvr  :  " + model.getMasterModel().getMasterModel().getEmployNm());   
//
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("JO LABOR");
//            System.out.println("--------------------------------------------------------------------");
//            for(int lnCtr = 0;lnCtr <= model.getJOLaborList().size()-1; lnCtr++){
//                System.out.println("sTransNox  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getTransNo()); 
//                System.out.println("nEntryNox  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getEntryNo()); 
//                System.out.println("sPayChrge  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getPayChrge());
//                System.out.println("sLaborCde  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getLaborCde());
//                System.out.println("sLbrPckCd  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getLbrPckCd());
//                System.out.println("nUnitPrce  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getUnitPrce());
//                System.out.println("nFRTxxxxx  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getFRTAmt());     
//                System.out.println("sEntryByx  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getEntryBy()); 
//                System.out.println("dEntryDte  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getEntryDte());
//                System.out.println("sLaborDsc  :  " +  model.getJOLaborModel().getDetailModel(lnCtr).getLaborDsc());
//            }
//            
//            System.out.println("--------------------------------------------------------------------");
//            System.out.println("JO PARTS");
//            System.out.println("--------------------------------------------------------------------");
//            for(int lnCtr = 0;lnCtr <= model.getJOPartsList().size()-1; lnCtr++){
//                System.out.println("sTransNox  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getTransNo()); 
//                System.out.println("nEntryNox  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getEntryNo()); 
//                System.out.println("sStockIDx  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getStockID()); 
//                System.out.println("sDescript  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getDescript());
//                System.out.println("sLbrPckCd  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getLbrPckCd());
//                System.out.println("nQtyEstmt  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getQtyEstmt());
//                System.out.println("nQtyUsedx  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getQtyUsed()); 
//                System.out.println("nQtyRecvd  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getQtyRecvd());
//                System.out.println("nQtyRtrnx  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getQtyRtrn()); 
//                System.out.println("nUnitPrce  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getUnitPrce());
//                System.out.println("sPayChrge  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getPayChrge());
//                System.out.println("sEntryByx  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getEntryBy()); 
//                System.out.println("dEntryDte  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getEntryDte());
//                System.out.println("sBarCodex  :  " +  model.getJOPartsModel().getDetailModel(lnCtr).getBarCode()); 
//            }
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
