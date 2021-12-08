package handson5.pkg2;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.domain.FIPANames;

import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

public class CustomerAgent extends Agent {
	private int nResponders;
	
	protected void setup() { 
  	// Read names of responders as arguments
  	Object[] args = getArguments();
  	if (args != null && args.length > 0) {
  		nResponders = args.length - 1;
  		System.out.println("Mandando una solicitud de articulo a " + nResponders + "responder...");
  		
  		// Fill the CFP message
  		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
  		for (int i = 1; i < args.length; ++i) {
  			msg.addReceiver(new AID((String) args[i], AID.ISLOCALNAME));
  		}
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
			// We want to receive a reply in 10 secs
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent((String) args[0]);
                        
			addBehaviour(new ContractNetInitiator(this, msg) {
				
				protected void handlePropose(ACLMessage propose, Vector v) {
					System.out.println("Agente "+propose.getSender().getName()+" propuso "+propose.getContent());
				}
				
				protected void handleRefuse(ACLMessage refuse) {
					System.out.println("El agente "+refuse.getSender().getName()+"se rehuso");
				}
				
				protected void handleFailure(ACLMessage failure) {
					if (failure.getSender().equals(myAgent.getAMS())) {
						// FAILURE notification from the JADE runtime: the receiver
						// does not exist
						System.out.println("No existe ese cajero :c");
					}
					else {
						System.out.println("Agente "+failure.getSender().getName()+" fallo :c");
					}
					// Immediate failure --> we will not receive a response from this agent
					nResponders--;
				}
				
				protected void handleAllResponses(Vector responses, Vector acceptances) {
					if (responses.size() < nResponders) {
						// Some responder didn't reply within the specified timeout
						System.out.println("Tiempo expirado: se perdio "+(nResponders - responses.size())+" responder");
					}
					// Evaluate proposals.
					float bestProposal = 999999999;
					AID bestProposer = null;
					ACLMessage accept = null;
					Enumeration e = responses.elements();
					while (e.hasMoreElements()) {
						ACLMessage msg = (ACLMessage) e.nextElement();
						if (msg.getPerformative() == ACLMessage.PROPOSE) {
							ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
							acceptances.addElement(reply);
							float proposal = Float.parseFloat(msg.getContent());
							if (proposal < bestProposal) {
								bestProposal = proposal;
								bestProposer = msg.getSender();
								accept = reply;
							}
						}
					}
					// Accept the proposal of the best proposer
					if (accept != null) {
						System.out.println("Acepto la oferta de "+bestProposal+" del cajero "+bestProposer.getName());
						accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					}						
				}
				
				protected void handleInform(ACLMessage inform) {
					System.out.println("Ya consegui mi articulo del agente "+inform.getSender().getName());
				}
			} );
  	}
  	else {
  		System.out.println("No se especifico el responder...");
  	}
  } 
}

