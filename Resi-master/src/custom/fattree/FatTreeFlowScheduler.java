/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom.fattree;

import infrastructure.entity.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import network.elements.Packet;

/**
 *
 * @author Admin
 */
public class FatTreeFlowScheduler extends FatTreeRoutingAlgorithm {
	private FatTreeRoutingAlgorithm ra;
	private List<Integer> flows;					// record flow in fat-tree
	private List<Integer> paths;					// one path record by source, core switch and destination
	private List<Integer> listCoreSwitch;			// list core switch of fat-tree
	private Map<Integer, Integer> coreWithFlows; 	// register flow with core switch 
	private Map<Integer, Integer> nextPorts;		// register next node ( next port) with current node
	
	public FatTreeFlowScheduler(FatTreeGraph graph, boolean precomputed) {
		super(graph, precomputed);
		ra = new FatTreeFlowClassifier(g, false);
		flows = new ArrayList<Integer>();
		paths = new ArrayList<Integer>(); 
		listCoreSwitch = getListCoreSwitch();
		coreWithFlows = new HashMap<Integer, Integer>();
		nextPorts = new HashMap<Integer, Integer>();
	}
        
        
        
	@Override
	public int next(int source, int current, int destination) {
		return 0;
	}
	
	// function next with other parameter
	@Override
	public int next(Packet packet, Node node) {
		int current = node.getId();
		int destination = packet.getDestination();
		int source = packet.getSource();
                
		if (g.isHostVertex(current)) {
			return g.adj(current).get(0);
		} else if (g.adj(current).contains(destination)) {
			return destination;
		} else {
			int type = g.switchType(current);
			if (type == FatTreeGraph.CORE) {
                            
				return super.next(source, current, destination); // Find the next node in the path when the current
																	// switch is core switch
			} else {
                                
                                //return incomingPacket(source, current, destination);
                                if(type == FatTreeGraph.AGG){
                                    return aggType(current, destination);
                                }
                                else{
                                    // incomingPacket(packet, node);
                                    return edgeType(current, destination);
                                }
                                
			}
		}
	}
	
        public int edgeType(int current, int destination, boolean schduler){
            // set default threshold
            
            
            // get all current 
            
            // get packet is larger than the threshold
            
            // sent notification to celtral
            return 0;
        }
        public void CentralScheduler(int id){
            // 
        }
	/**
	 * Default out-going port with two level table
	 */
	public int defaultNext(int source, int current, int destination) {
		return ra.next(source, current, destination);
	}
	
	// hash function, with c = -1 mean is we only hash 2 parameter a and b
	public int hash(int a, int b, int c) {
		if(c == -1) {
			return a + b* 3313;
		} else {
			return a + b * 3313 + c * 131;
		}
	}
	
	// get pod of host
	public int getPodOfHost(int a) {
		int k = g.getK(); 
		int each_devices_in_hostpod = (k*k)/4+k; // number of devices in pod
		
		return a / each_devices_in_hostpod;
	}
	
	// get list of core switch with index, example : with k = 4, we have core switch is 32, 33, 34, 35
	public List<Integer> getListCoreSwitch(){
		int k = g.getK();
		int total_devices = k*k*k/4 + 5*k*k/4;
		
		List<Integer> list = new ArrayList<Integer>();
		for(int i=total_devices - k; i<total_devices; i++) {
			list.add(i);
		}
		
		return list;
	}
}
