package handson5.pkg2;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.clipsrules.jni.*;
import java.util.Random;

public class SellerAgent1 extends Agent{
    Environment clips = new Environment();
    double performed = 0.0;
    
    protected void setup() {
                System.out.println("Cargando base de conocimientos...");
                try {
                clips.load("base1.clp");
                clips.reset();
                } catch (CLIPSLoadException ex) {
                    Logger.getLogger(SellerAgent1.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CLIPSException ex) {
                    Logger.getLogger(SellerAgent1.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Carga completada!");
               
		System.out.println("El agente "+getLocalName()+" espera por la petición...");
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );

		addBehaviour(new ContractNetResponder(this, template) {
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
				System.out.println("El agente "+getLocalName()+" tiene una petición! El agente "+cfp.getSender().getName()+" quiere "+ cfp.getContent());
                                double proposal = evaluateAction(cfp.getContent());
				if (proposal > 0.0) {
					// We provide a proposal
					System.out.println("Agente "+getLocalName()+" propone un costo de " + proposal);
					ACLMessage propose = cfp.createReply();
					propose.setPerformative(ACLMessage.PROPOSE);
					propose.setContent(Double.toString(proposal));
					return propose;
				}
				else {
					// We refuse to provide a proposal
					System.out.println("Agente "+getLocalName()+" dio para atras la negociación.");
					throw new RefuseException("evaluation-failed");
				}
			}

			@Override
			protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
				System.out.println("El Agente "+getLocalName()+" tuvo exito en su propuesta...");
				if (performAction()) {
                                        System.out.println("Ofrece: ");
                                        System.out.println("Costo total: " + performed);
                                    try {
                                        clips.eval("(run)");
                                    } catch (CLIPSException ex) {
                                        Logger.getLogger(SellerAgent2.class.getName()).log(Level.SEVERE, null, ex);
                                    }
					ACLMessage inform = accept.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					return inform;
				}
				else {
					System.out.println("El Agente "+getLocalName()+" fallo en su ejecución :c");
					throw new FailureException("unexpected-error");
				}		
			}

			protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
				System.out.println("Propuesta del agente "+getLocalName()+" fue rechazada :c");
			}
		} );
	}
        
	private double evaluateAction(String content){
                double product = 0.0;
                Random r = new Random();
                int orderNumber = r.nextInt(100-1) + 1;
                try{
                    // Simulate an evaluation by generating a random number
                    String[] consult;
                    String[] separate = content.split("/");
                    String[] products = separate[0].split("-");
                    for(int i = 0; i < products.length; i++){
                        consult = products[i].split(" ");
                        MultifieldValue find = (MultifieldValue) clips.eval("(find-fact ((?p product))(and (eq ?p:name " + consult[0] + ")(eq ?p:color " + consult[1] + ")))");
                        if(find.toString().equals("()")){
                            System.out.println("Revise mi inventario pero no tengo lo que quieres. Vuelve más tarde :c");
                        } else {
                            FactAddressValue fact = (FactAddressValue) find.get(0);
                            product += Double.parseDouble(fact.getSlotValue("price").toString());
                            clips.assertString("(car (name " + consult[0] + ")(order " + orderNumber + ")(type " + consult[2] + "))");
                        }
                    }
                    clips.assertString("(order (card " + separate[1] + ")(id " + orderNumber + "))");
                } 
                catch(CLIPSException ex){}
                performed = product;
                return product;
	}

	private boolean performAction() {
		// Simulate action execution by generating a random number
		return (performed > 0.0);
	}
    
}
