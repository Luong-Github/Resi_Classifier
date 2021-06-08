package custom.fattree;

import java.util.HashMap;
import java.util.Map;
import config.Constant;
import infrastructure.entity.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javatuples.Pair;
import network.elements.Packet;
import routing.RoutingAlgorithm;
import java.util.Timer;
import java.util.TimerTask;


public class FatTreeFlowClassifier extends FatTreeRoutingAlgorithm {

	public Map<Pair<Integer, Integer>, Long> flowSizesPerDuration = new HashMap<>();
	public Map<Integer, Long> outgoingTraffic = new HashMap<>();
	public Map<Pair<Integer, Integer>, Long> flowTable = new HashMap<>();
	private int currentNode;
        TimerTask task = new TimerTask() {
                    public void run() {
                        System.out.println("Ok");
                        rearrangeFlows();
                    }
                };
        Timer timer = new Timer("Timer");

        long delay = 1000;

	public int getCurrentNode() {
		return currentNode;
	}

	public FatTreeFlowClassifier(FatTreeGraph g, boolean precomputed) {
            super(g, precomputed);
            
	}

	private int time = 0;

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
        
        public void count(){
            System.out.println("1");
        }
        
	/**
	 * This method is used to find the next node from the current node in the path
	 * 
	 * @param source      This is the source node
	 * @param current     This is the current node
	 * @param destination This is the destination node
	 */
	@Override
	public int next(int source, int current, int destination) {
            
		if (g.isHostVertex(current)) {
			return g.adj(current).get(0);
		} else if (g.adj(current).contains(destination)) {
			return destination;
		} else {
			int type = g.switchType(current);
                        
			if (type == FatTreeGraph.CORE) {
				return super.next(source, current, destination); // Find the next node in the path when the current
																	// switch is core switch

			} else if (type == FatTreeGraph.AGG) {
				return aggType(current, destination); // Find the next node in the path when the current switch is
														// aggregation switch

			} else {
				return edgeType(current, destination); // Find the next node in the path when the current switch is edge
														// switch
			}
		}
	}

	/**
	 * This method is used to find the next node to send the packet
	 * 
	 * @param packet This is the packet which needs to be sent
	 * @param node   This is the current node where the packet stays
	 */
	@Override
	public int next(Packet packet, Node node) {
                /*int current = node.getId();
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
																	// switch is core switc
			} else {
                            //rearrangeFlows();
				if (type == FatTreeGraph.AGG) {
                                    
					return aggType(current, destination); // Find the next node in the path when the current switch is
					//return 0;										// aggregation switch

				} else {
                                    
					return edgeType(current, destination); // Find the next node in the path when the current switch is
					//return 0;										// edge switch
				}
                            //return next(packet, node);
			}
		}*/
                //return 0;
                //return next(packet, node, true);
                return next(packet, node, true);
	}
        
        /* Flow classifier with IncomingPacket and RearrangeFlow */
	public Map<Integer, Integer> seen = new HashMap<Integer, Integer>(); // check a flow with source and destination has been passed
	public Map<Integer, Integer> countPacketsUsePort = new HashMap<Integer, Integer>(); // each line connects 2 points representing 1 port  <--| 
	public Map<Integer, List<Packet>> listAllPacketsUsePort = new HashMap<Integer, List<Packet>>(); // list packet in each port      __________|
	public Map<Integer, Integer> preNodeIDs = new HashMap<Integer, Integer>(); // return previous node id of packet // after move packet to 
	
	private int k = g.getK();;
	private int each_devices_in_hostpod = ( k * k ) / 4 + k;
	
	/** 
	 * This method is used to find the next node from the current node in the path with flow classifier if flow is true
	 * @param source      This is the source node
	 * @param current     This is the current node
	 * @param destination This is the destination node
	 * @param flow 	      This is choose use flow classifier
	 */
	public int next(int source, int current, int destination, boolean flow) {
            /*f(flow == true){
                // cal rearrange-Flows function
                
                //timer.schedule(task, delay);
                rearrangeFlows();
                int k = incomingPacket(source, current, destination);
                return k;
            }
            else{
                return super.next(source, current, destination);
            }*/
            //int current = current;
		//int destination = packet.getDestination();
		//int source = packet.getSource();
                
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
                            rearrangeFlows();
				if (type == FatTreeGraph.AGG) {
                                    
                                    
					return aggType(current, destination); // Find the next node in the path when the current switch is
					//return 0;										// aggregation switch

				} else {
                                    
                                    
					return edgeType(current, destination); // Find the next node in the path when the current switch is
					//return 0;										// edge switch
				}
			}
		}
	}
        
        public int next(Packet packet, Node node, boolean flow){
            
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
                                rearrangeFlows();
                                //return incomingPacket(source, current, destination);
                                if(type == FatTreeGraph.AGG){
                                    return aggType(current, destination);
                                }
                                else{
                                    return incomingPacket(packet, node);
                                    //return edgeType(current, destination);
                                }
                                
			}
		}
        }
        
	/* Check that host number a and b are on the same pod
	 */
	public boolean checkDevicesInPod(int a, int b) {
            
            
            
            // address based on 10.p.e.h
            /*Address[] k = g.getAddress();
            
            Address hostA = new Address(0,0,0,0);
            Address hostB = new Address(0,0,0,0);
            
            for(int i = 0;i < k.length; i++){
                // get address host a
                if(k[i]._4 == a){
                    hostA = k[i];
                }
                // get address host b
                if(k[i]._4 == b){
                    hostB = k[i];
                }
            }*/
            // get lists host
            //Address hostA = g.getAddress(a);
           //int host =  g.hosts().get(a);
            //Address hostB = g.getAddress(b);
            
            
            int p1 = a / each_devices_in_hostpod;
            int p2 = b / each_devices_in_hostpod;
            
            if(p1 == p2){
                return true;
            }
            return false;
	}
	
	/* Flow classifier
	 * */
        public int incomingPacket(Packet packet, Node node)
        {
            // set condition packet from core switch to pod
            
            
            
            // add current value node to listAllUsePort
            int type = g.switchType(node.getId());
            // edge get port 0 - 1 at each edge switch
            
            // agg get port 2 - 3 at each agg switch
            
            int a = hash(packet.getSource(), packet.getDestination(), -1);
            //System.out.println("Start");
            
            int checkDestination = -1;
            for(Map.Entry<Integer, Integer> key : seen.entrySet()){
                int lKey = key.getKey();
                if(lKey == a){
                    if(seen.get(lKey) != null){
                        checkDestination = seen.get(lKey);
                    }// get port out
                }
            }
            
            if(checkDestination != -1){
                int portNum = 0;
                
                // check follow EDGE and AGG
                
                if(type == FatTreeGraph.EDGE){
                    //listAllPacketsUsePort.get(checkDestination).add(packet);
                    if(listAllPacketsUsePort.get(checkDestination) == null){
                                List<Packet> pList = new ArrayList<Packet>();
                                pList.add(packet);
                                listAllPacketsUsePort.put(checkDestination, pList);
                            }else{
                                listAllPacketsUsePort.get(checkDestination).add(packet);
                           }
                    return checkDestination;
                }
                if(type == FatTreeGraph.AGG){
                    listAllPacketsUsePort.get(checkDestination).add(packet);
                    return checkDestination;
                }
                
                for(Object key: listAllPacketsUsePort.keySet()){
                    int lKey = (int) key;
                    List<Packet> list = listAllPacketsUsePort.get(lKey);
                    for(int i = 0; i < list.size();i++){
                        if(list.get(i).getSource() == packet.getSource() && list.get(i).getDestination() == packet.getDestination()){
                            portNum = lKey;
                            break;
                        }
                    }
                }
                // insert Packet to port x
                listAllPacketsUsePort.get(portNum).add(packet); // node id for port x
                
                return portNum;
                
            }
            else{
                // seen.put(a, checkDestination);
                // find the least-loaded upward port x
                if(type == FatTreeGraph.EDGE){
                    // port 1-2 per edge use fattree table
                    // port 1 - connect to node id is 10.p.0.1
                    // port 2 - connect to node id is 10.p.1.1
                    // 10.p.e.h
                    // e = port 
                    // port 1 - 10.p.2.1
                    // port 2 - 10.p.3.1
                    /*Address[] p = g.getAddress();
                    for(Address k : p){
                        if(k._2 == g.getAddress(node.getId())._2 && k._3 == 2 && k._4 == 1){
                            // get node id cua port 1
                            // gia truyen mot goi tin su dung table fat tree
                            //int buf = super.edgeType(node.getId(), )
                            
                        }
                    }*/
                    // register to seen
                    // port 0 va port 1
                    int idPort0 = 0; //port 0
                    int idPort1 = 0; //port 1
                    //idPort1 = super.edgeType(node.getId(), 3); // lay duoc id port 1 
                    //idPort0 = super.edgeType(node.getId(), 2); // lay duoc id port 0
                    // get id cua node tai agg switch
                    if(node.getId() % 2 == 0){
                        idPort0 = super.edgeType(node.getId(), 2);
                        idPort1 = super.edgeType(node.getId(), 3);
                        
                    }
                    else{
                        idPort1 = super.edgeType(node.getId(), 2);
                        idPort0 = super.edgeType(node.getId(), 3);
                    }
                    
                    int countp1 = 0;
                    int countp2 = 0;
                    //check idPort1 and idPort2 is existed in listAllPacketsUsePort
                    List<Packet> p1 = listAllPacketsUsePort.get(idPort1);
                    if(p1 != null){
                        // neu da ton tai
                        countp1 = p1.size();
                    }
                    else{
                        if(listAllPacketsUsePort.containsKey(idPort1)){
                            countp1 = 0;
                        }
                        else{
                            listAllPacketsUsePort.put(idPort1, null);
                            //listAllPacketsUsePort.keySet().add(idPort1);
                        }
                        
                    }
                    
                    
                    List<Packet> p2 = listAllPacketsUsePort.get(idPort0);
                    if(p2 != null){
                        // neu da ton tai
                        countp2 = p2.size();
                    }
                    else{
                        if(listAllPacketsUsePort.containsKey(idPort0)){
                            countp2 = 0;
                        }
                        else{
                            listAllPacketsUsePort.put(idPort0, null);
                            //listAllPacketsUsePort.keySet().add(idPort0);
                        }
                        
                    }
                    // compare countp1 and countp2
                    if(countp2 == 0 && countp1 == 0){
                        int bufSuperEdge = super.edgeType(node.getId(), packet.getDestination());
                        List<Packet> akList = listAllPacketsUsePort.get(bufSuperEdge);
                        
                            if(listAllPacketsUsePort.get(bufSuperEdge) == null){
                                List<Packet> pList = new ArrayList<Packet>();
                                pList.add(packet);
                                listAllPacketsUsePort.put(bufSuperEdge, pList);
                            }else{
                                listAllPacketsUsePort.get(bufSuperEdge).add(packet);
                           }
                        
                        seen.put(a, bufSuperEdge);
                        return bufSuperEdge;
                    }
                    if(countp2 < countp1){
                        // tao 1 flow
                        //listAllPacketsUsePort.get(idPort0).add(packet);
                        if(listAllPacketsUsePort.get(countp2) == null){
                                List<Packet> pList = new ArrayList<Packet>();
                                pList.add(packet);
                                listAllPacketsUsePort.put(countp2, pList);
                            }else{
                                listAllPacketsUsePort.get(countp2).add(packet);
                           }
                        seen.put(a, idPort0);
                        return idPort0;
                    }
                    if(countp2 > countp1){
                        if(listAllPacketsUsePort.get(countp1) == null){
                                List<Packet> pList = new ArrayList<Packet>();
                                pList.add(packet);
                                listAllPacketsUsePort.put(countp1, pList);
                            }else{
                                listAllPacketsUsePort.get(countp1).add(packet);
                           }
                        seen.put(a, idPort1);
                        return idPort1;
                    }
                    // tuong ung voi moi id port
                    // voi id port0
                    // kiem tra port0 va port1 cai nao co luong load it hon
                    
                    
                }
                if(type == FatTreeGraph.AGG){
                    // port 2 - 3, dung bang fattreetable
                    int buf = super.aggType(node.getId(), packet.getDestination());
                    seen.put(a, buf);
                    return buf;
                }
                /*if(listAllPacketsUsePort == null){
                    listAllPacketsUsePort.keySet().add(packet.getSource());
                    listAllPacketsUsePort.get(packet.getSource()).add(packet);
                }
                else{
                    Map.Entry<Integer, List<Packet>> count = null;
                    for (Map.Entry<Integer, List<Packet>> entry : listAllPacketsUsePort.entrySet())
                    {
                        if (count == null || entry.getValue().size() < count.getValue().size())
                        {
                            count = entry;
                        }
                    }
                    node.getId();
                    // add packet to smallestUpward
                    listAllPacketsUsePort.get(count.getKey())
                            .add(packet); 

                    return count.getKey();
                }*/
            }
            return 0;
        }
        
        
	public int incomingPacket(int source, int current, int destination) { // flow classifier
            // hash source and destination
            int a = hash(source, destination, -1);
            
            if(seen.get(source) == destination){
                // look up previously assigned port x;
                int portNum = 0;
                
                // kiem tra source va destination nay dang o port nao
                for(Object obj : listAllPacketsUsePort.keySet()){
                    int lKey = (int) obj; // port
                    List<Packet> list = listAllPacketsUsePort.get(lKey);
                    for(int i = 0; i < list.size(); i++){
                        if(list.get(i).getSource() == source && list.get(i).getDestination() == destination){
                            portNum = lKey;
                            
                            break;
                        }
                    }
                }
                // lay cai port x da ton tai va gui Packet vao port do
                
                
                // return to port x send packet to port x
                return 0;
            }
            else{
                // record new flow f to useFlow hash variables
                seen.put(source, destination);
                
                // find the least-loaded upward port x
                
                // add to least-loaded upward port x
                // se gui flow / packet vao port co so luong it nhat
                
                
                
                // return port x
                return 0;
            }
            
            //return 0;
	}
	
	// hash function, with c = -1 mean is we only hash 2 parameter a and b
	public int hash(int a, int b, int c) {
		if(c == -1) {
			return a + b* 3313;
		} else {
			return a + b * 3313 + c * 131;
		}
	}
	
	/* Removes the port that just sent the packet to the current node
	 * */
	public List<Integer> getNodeCanSent(List<Integer> list, int current){
		List<Integer> listResult = new ArrayList<Integer>();
		
		for(int i=0; i<list.size(); i++) {
			if(list.get(i) > current) {
				listResult.add(list.get(i));
			}
		}
		
		return listResult;
	}
	
	/**
	 * This method call every t second ( t = 1 in this project)
	 * This method help sort flow with size of packet in flow
	 */
	public void rearrangeFlows() {
		int total_devices = k*k*k/4 + 5*k*k/4; 
		for(int currentID=0; currentID<total_devices; currentID++) {
			int type = g.switchType(currentID); 
			
			if(type == FatTreeGraph.AGG && type == FatTreeGraph.EDGE) { // only rearrangeFlows with AGG and EDGE switch
				List<Integer> listNeiborNodes = getNodeCanSent(g.adj(currentID), currentID); // get neighborhood node of node i-th
				int minSizeNodeID = -1;
				int maxSizeNodeID = -1;
				int minSize = Integer.MAX_VALUE;
				int maxSize = 0;
				
				for(int i=0; i<listNeiborNodes.size(); i++) {
					int nextNodeID = listNeiborNodes.get(i);
					int portID = hash(currentID, nextNodeID, -1);
					
					int sumSize = 0;
					for(int j=0; j<listAllPacketsUsePort.get(portID).size(); j++) {
						sumSize += listAllPacketsUsePort.get(portID).get(j).getSize();
					}
					
					if(sumSize < minSize) {
						minSize = sumSize;
						minSizeNodeID = nextNodeID;
					}
					
					if(sumSize > maxSize) {
						maxSize = sumSize;
						maxSizeNodeID = nextNodeID;
					}
				}
				
				int diffSize = maxSize - minSize;
				
				int portMinID = hash(currentID, minSizeNodeID, -1);
				int portMaxID = hash(currentID, maxSizeNodeID, -1);
				int maxSizePacketID = -1;
				maxSize = 0;
				
				// find packet have size max and less diffSize, location packet found save variable maxSizepacketID
				for(int j=0; j<listAllPacketsUsePort.get(portMaxID).size(); j++) {
					if(listAllPacketsUsePort.get(portMaxID).get(j).getSize() < diffSize
							&& listAllPacketsUsePort.get(portMaxID).get(j).getSize() > maxSize) {
						maxSize = listAllPacketsUsePort.get(portMaxID).get(j).getSize();
						maxSizePacketID = j;
					}
				}
				
				// assign flow f from port-max to port-min
				listAllPacketsUsePort.get(portMinID)
                                        .add(listAllPacketsUsePort.get(portMaxID)
					.get(maxSizePacketID));  
				listAllPacketsUsePort.get(portMaxID).remove(maxSizePacketID);
				
				// reduce and increase the number of flow in countPacketsUsePort
				countPacketsUsePort.put(portMinID, countPacketsUsePort.get(portMinID)+1);
				countPacketsUsePort.put(portMaxID, countPacketsUsePort.get(portMinID)-1);
			}
		}
	}
        
	/*End algorithm flow classifier*/
        
	@Override
	public RoutingAlgorithm build(Node node) throws CloneNotSupportedException {
            
                
                // setTimer
                
                // call after 
            
		currentNode = node.getId();
		RoutingAlgorithm ra = super.build(node);
		if (ra instanceof FatTreeFlowClassifier) {
			FatTreeFlowClassifier ftfc = (FatTreeFlowClassifier) ra;
			ftfc.outgoingTraffic = new HashMap<>();
			ftfc.flowSizesPerDuration = new HashMap<>();
			ftfc.flowTable = new HashMap<>();
			return ftfc;
		}
		return ra;
	}

	/**
	 * This method is used to update the result of routing table
	 * 
	 * @param p    This is the packet which needs to be sent
	 * @param node This is the current node where the packet stays
	 */
	@Override
	public void update(Packet p, Node node) {
		int src = p.getSource();
		int dst = p.getDestination();
		int currentTime = (int) node.physicalLayer.simulator.time();
		if (currentTime - time >= Constant.TIME_REARRANGE) {
			time = currentTime;
			flowSizesPerDuration = new HashMap<>();
		} else {
			Pair<Integer, Integer> flow = new Pair<>(src, dst);
			long value = p.getSize();
			if (flowSizesPerDuration.containsKey(flow)) {
				value += flowSizesPerDuration.get(flow);
			}
			flowSizesPerDuration.put(flow, value);
			value = p.getSize();
			int idNextNode = node.getId();
			if (outgoingTraffic.containsKey(idNextNode)) {
				value += outgoingTraffic.get(idNextNode);
			}
			outgoingTraffic.put(idNextNode, value);
		}
	}
}
