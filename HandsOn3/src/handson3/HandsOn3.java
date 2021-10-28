package handson3;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.clipsrules.jni.*;

public class HandsOn3 extends Agent{
    Environment clips = new Environment();
    
    public void setup(){
        System.out.println("Starting agent...");
        addBehaviour(new Tell());
        addBehaviour(new Ask());
    }
    
    private class Tell extends OneShotBehaviour {
        public void action(){
            System.out.println("/**** TELL ****/");
            System.out.println("Loading Knowloadge base...");
            try{
                clips.load("load-templates.clp");
                clips.load("load-facts.clp");
                clips.load("load-rues.clp");
                clips.reset();
            } catch (CLIPSLoadException ex) {
                Logger.getLogger(HandsOn3.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CLIPSException ex) {
                Logger.getLogger(HandsOn3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private class Ask extends OneShotBehaviour {
        public void action(){
            System.out.println("/**** ASK ****/");
            System.out.println("Facts:");
            try {
                clips.eval("(rules)");
                
                List<FactAddressValue> customer = clips.findAllFacts("customer");
                List<FactAddressValue> product = clips.findAllFacts("product");
                List<FactAddressValue> order = clips.findAllFacts("order");
                List<FactAddressValue> line = clips.findAllFacts("line-item");
                
                System.out.println("ID / NAME / ADDRESS / PHONE");
                for(FactAddressValue c : customer){
                    System.out.println(c.getSlotValue("customer-id") + "\t\t" + c.getSlotValue("name") + "\t\t" + c.getSlotValue("address") + "\t\t" + c.getSlotValue("phone"));
                }
                
                System.out.println("PART-NUMBER / NAME / CATEGORY / PRICE");
                for(FactAddressValue p : product){
                    System.out.println(p.getSlotValue("part-number") + "\t" + p.getSlotValue("name") + "\t\t" + p.getSlotValue("category") + "\t\t" + p.getSlotValue("price"));
                }
                
                System.out.println("ORDER-NUMBER / CUSTOMER-ID");
                for(FactAddressValue o : order){
                    System.out.println(o.getSlotValue("order-number") + "\t\t" + o.getSlotValue("customer-id"));
                }
                
                System.out.println("ORDER-NUMBER / PART-NUMBER / CUSTOMER-ID / QUANTITY");
                for(FactAddressValue l : line){
                    System.out.println(l.getSlotValue("order-number") + "\t\t" + l.getSlotValue("part-number") + "\t\t" + l.getSlotValue("customer-id") + "\t\t" + l.getSlotValue("quantity"));
                }
                
                clips.run();
                
            } catch (CLIPSException ex) {
                Logger.getLogger(HandsOn3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public int onEnd(){
            myAgent.doDelete();
            return super.onEnd();
        }
    }
}
