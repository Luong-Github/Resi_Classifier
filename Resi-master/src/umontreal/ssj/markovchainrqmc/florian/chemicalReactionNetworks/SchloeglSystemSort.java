package umontreal.ssj.markovchainrqmc.florian.chemicalReactionNetworks;

import umontreal.ssj.util.sort.florian.MultDimToOneDimSort;
/**
 * Implements a #MultDimToOneDimSort for the #SchloeglSystem class, where the score
 * function is a bivariate polynomial. The coefficients to define the
 * polynomial can either be passed in the constructor or, for the specific
 * example in our paper, taken from a predefined set (example specific!).
 * 
 * 
 * @author florian
 *
 */
public class SchloeglSystemSort extends MultDimToOneDimSort {
/**
	 * The coefficients of the multivariate polynomial that defines the score
	 * function. The order must match the order of the #varIndices.
	 */
	double[] coeffs;
	/**
	 * Whether the polynomial has a constant term or ot.
	 */
	boolean bias;
	/**
	 * The coordinate indices that define the monomials in the polynomial. For
	 * instance, the tuple @f$(i,j,k)@f$ identifies the monomial $x_ix_jx_k$.
	 */
	int[][] varIndices;
//	int step;

	// format: {{b_0^(1),b_0^(2),...,b_0^(numSteps-1)}, ...
	// ,{b_numCoeffs^(1),b_numCoeffs^(2),...,b_numCoeffs^(numSteps-1)}}
	// n=2^20, with bias, smoothing factor 0.8
//	private final double[][] coeffMat = {
//			{ 63606.24769714793, 23988.880278573197, -4612.331447642056, -20116.69156690551, -22859.812778691143,
//					-18822.634119836126, -13251.110092840156, -8844.08896628573, -5881.3433704336985,
//					-3866.553049933748, -2506.1807507993854, -1500.9783383987453, -669.3014170799456,
//					76.95142312509495 },
//			{ -436.32762821695263, -167.35365672540348, 34.37526545089176, 147.64172996382746, 166.2811717706371,
//					134.46085444399512, 92.39052722966187, 60.646528189211296, 40.42213434964793, 27.239773180539558,
//					18.68791297255875, 12.229528653468005, 6.308826001534655, 0.39944173638227287 },
//			{ -0.5996218952032085, -0.21849952896116043, 0.05711677073449545, 0.2069091007441311, 0.23219285478081655,
//					0.19097067422973069, 0.13481107107796717, 0.09043814212748194, 0.06057545040084339,
//					0.040223698928127695, 0.026426362300088827, 0.016157242831852048, 0.007570682198319228,
//					-2.1476180948528604E-4 },
//			{ 0.7049248165483423, 0.2667324752419855, -0.07766278241906382, -0.27347495182208365, -0.2979722908501169,
//					-0.23469613701452974, -0.1588336596686347, -0.10512102911449742, -0.07222054578514002,
//					-0.05054902883907519, -0.03576633670539399, -0.02385666258196462, -0.012380307504911566,
//					-6.539094849168786E-4 },
//			{ 0.0038888756360253066, 0.0013897588511355328, -4.919930452791537E-4, -0.0015516047375856519,
//					-0.0017055608651063712, -0.0013739321600116353, -9.468891157899187E-4, -6.256538119678082E-4,
//					-4.206120379791449E-4, -2.863466488309051E-4, -1.9833342438653546E-4, -1.307507727166149E-4,
//					-6.762435582928048E-5, -3.69132167781835E-6 },
//			{ -2.620581891830619E-4, -1.5728958315848905E-4, -8.134116496267586E-5, -3.911439069329404E-5,
//					-2.061362731302423E-5, -1.3923138418629506E-5, -1.1692762030859235E-5, -1.0661707491528412E-5,
//					-9.89632145338873E-6, -9.197348268950732E-6, -8.393524574702188E-6, -7.399080937868234E-6,
//					-6.109782921922133E-6, -4.507708591605574E-6 },
//			{ -5.039478037188443E-6, -1.4413730302312498E-6, 1.436366145633882E-6, 3.079797642842302E-6,
//					3.1824334837316436E-6, 2.492732223483054E-6, 1.7106965497763979E-6, 1.1611498653313694E-6,
//					8.231433809209921E-7, 5.983180459697138E-7, 4.415665199119516E-7, 3.1149607237425944E-7,
//					1.8245784098867478E-7, 4.759719645546216E-8 } };
	
	// n=2^20, with bias, smoothing factor 0.6
//			private final double[][] coeffMat = {
//					{60701.06780441805,24916.098240225452,-2611.847590650599,-18403.882297456363,-22547.72488447977,-19539.04393976978,-14159.076607589026,-9368.515719413102,-6002.725084332817,-3824.2698690453626,-2429.841258800452,-1445.0114840190697,-640.356178055591,96.18236896873645},
//					{-417.3948808178061,-171.8175897390281,21.25967000121605,134.34006430311678,163.128570424626,139.5733162141182,99.33826396603268,64.66360116302552,41.29074697743373,26.863391682701753,18.021205363408,11.682703336218228,6.031871799659779,0.42147432527434336},
//					{-0.5716991776649782,-0.22721765042521597,0.03799455591831752,0.19011235691503017,0.22892408557355862,0.1978702826145401,0.14372174330266058,0.09562252306901046,0.06178480762113165,0.039810321866607046,0.02566472185798315,0.015588984411420247,0.007273837915588227,-3.9552859565653454E-4},
//					{0.6752398246973196,0.27020754660689444,-0.05599210518123371,-0.24804839967635384,-0.29156703743915635,-0.24445165869887972,-0.17179610165179784,-0.11213026102256517,-0.0733026999536768,-0.04954954516369316,-0.03448847187093275,-0.02290719217386783,-0.011949561965647006,-7.946865064054724E-4},
//					{0.003713136887587652,0.0014281703490812829,-3.712791080768842E-4,-0.0014230054746007105,-0.0016727418517473122,-0.0014220486262170465,-0.0010142133077585154,-6.64924990026812E-4,-4.2913805485754507E-4,-2.826470534054913E-4,-1.9166247110980166E-4,-1.2514410851229425E-4,-6.474200456553435E-5,-4.117995790954118E-6},
//					{-2.547956148102164E-4,-1.6035599393011046E-4,-8.685120235215233E-5,-4.180262486034126E-5,-2.0196539188911233E-5,-1.2337712826489002E-5,-1.0384389651082617E-5,-1.0045118504212702E-5,-9.760000743641895E-6,-9.222299124239076E-6,-8.420003750118348E-6,-7.375674477714749E-6,-6.078081512856584E-6,-4.573074267913742E-6},
//					{-4.797004123616265E-6,-1.452489717611041E-6,1.2609558032213415E-6,2.844641820513746E-6,3.1146513869520215E-6,2.5786264371649244E-6,1.8311354544095229E-6,1.226996274437568E-6,8.329881006906603E-7,5.884439733977825E-7,4.288285201384149E-7,3.015967398869824E-7,1.777889331582991E-7,4.9744297256014534E-8}
//			};
	
	// n=2^20, with bias, smoothing factor 0.4
				private final double[][] coeffMat = {
						{56924.54956132881,25365.315282965985,-56.36484550115347,-15810.147372661679,-21424.723128026846,-19949.363222866254,-15301.971756490057,-10406.747519765913,-6574.395107051252,-3986.8761190806963,-2364.928371360914,-1313.5499066502375,-527.5743775764831,167.83038273679892},	
						{-391.80793479851553,-173.78547018491102,4.064336537138388,115.46656572080032,154.4162930876381,142.28350630277268,107.75772143420018,72.39033902403553,45.49833394065931,27.962579777896334,17.367021844619224,10.53618601671564,5.153036706396652,0.09989359055344682},						
						{-0.5352545069279118,-0.231391306685789,0.013408376695584812,0.16486522080519106,0.21783536757626168,0.20176076699840506,0.154864762604714,0.10580145368665878,0.06740616496860774,0.0414117831393781,0.02501673969884616,0.014277473121891773,0.006150531321544155,-0.001096962623385122},						
						{0.6331711805749533,0.2714406851697212,-0.027344340082115337,-0.21442374731569097,-0.2758278972800917,-0.24959573665496315,-0.18712153733205888,-0.12575308439957678,-0.08030549956985031,-0.051008429610744394,-0.0330285532579324,-0.020788296883669666,-0.010479449135084298,-4.65644222893611E-4},						
						{0.00347348014839551,0.0014443956002488365,-2.1093522463360294E-4,-0.0012430351781388795,-0.0015879079481069026,-0.0014469314573446873,-0.0010949649144468985,-7.395516733490265E-4,-4.698512225997491E-4,-2.9317740292077144E-4,-1.8502783651504304E-4,-1.1365540438574827E-4,-5.6008303828661235E-5,-1.1869923743667486E-6},						
						{-2.459484611380793E-4,-1.620063569219624E-4,-9.308335006690799E-5,-4.681105151678315E-5,-2.1740204581413115E-5,-1.1250934713350386E-5,-8.425322414504743E-6,-8.452529759083707E-6,-8.904146140133433E-6,-8.92930788503641E-6,-8.398029965102519E-6,-7.423446502154814E-6,-6.140686701616237E-6,-4.6845016938722E-6},						
						{-4.442122259628198E-6,-1.4520533172313666E-6,1.0209041668321933E-6,2.5449519677951294E-6,2.9681312700131406E-6,2.621990668061276E-6,1.9703134547469178E-6,1.3518698787657664E-6,8.968630370563273E-7,6.007924702386267E-7,4.138026636849452E-7,2.804512603569203E-7,1.6343386040115195E-7,4.757477890715273E-8}						
				};
	
	// n=2^20, with bias, smoothing factor 0.2
//		private final double[][] coeffMat = {
//				{50512.50033768607,25070.457036853164,3545.2946083965253,-11224.73619013295,-18327.572071197173,-19214.719654619148,-16401.283721491054,-12235.413452439847,-8231.394778383175,-5060.611034003964,-2824.3866539861156,-1313.5020909974614,-231.11280849978812,677.5339728149713},
//				{-347.4468396930434,-170.91577285771004,-20.659297230044587,82.81151634315525,131.9485072415389,136.87792420375138,115.80132973872625,85.74543078059432,57.4771936826841,35.53992869233325,20.371936298393102,10.23951929178137,2.9267140810120864,-3.3156815731728377},
//				{-0.4732120450145625,-0.22840524160313055,-0.021382291871260922,0.1203189570966418,0.18761657704915352,0.19453142250299738,0.16557115265005343,0.12367286752756756,0.0836138088131705,0.0519078168689304,0.0294850604048664,0.01423662326875761,0.003210777568501761,-0.0061096208816866495},
//				{0.5588544130408147,0.2653671723096068,0.01431673780529491,-0.15790320620538,-0.23699088867756912,-0.24092193541251522,-0.201869484864454,-0.1491885873546173,-0.10071148227819761,-0.06343019167367409,-0.037543074740313945,-0.01986074802266731,-0.006625993121217885,0.004976919194892553},
//				{0.0030559377346679583,0.0014158087479792625,2.141412979110818E-5,-9.330322148004697E-4,-0.0013733222578804064,-0.001395052763729835,-0.0011722186628777254,-8.679400535331536E-4,-5.848795560396143E-4,-3.6559785603018896E-4,-2.1318613356621652E-4,-1.0998260563709456E-4,-3.4028697735048036E-5,3.1722379391433944E-5},
//				{-2.31475584710569E-4,-1.6170474518543114E-4,-1.0119685343325806E-4,-5.615675580305699E-5,-2.7654885530438348E-5,-1.2680569093682393E-5,-6.676613511965619E-6,-5.464126828750562E-6,-6.114027505404134E-6,-6.975999072777715E-6,-7.340978981591665E-6,-7.0769245668119E-6,-6.332042764822814E-6,-5.348223011439553E-6},
//				{-3.8062006136374208E-6,-1.393204140403999E-6,6.644430754222783E-7,2.048428588359003E-6,2.623026338355265E-6,2.5458113583929256E-6,2.1053267939466276E-6,1.5646862669129556E-6,1.0805952824763067E-6,7.104950481512981E-7,4.5075829457881785E-7,2.6818359533057306E-7,1.2615219807005707E-7,-1.5981859547025824E-9},
//		};

				/**
				 * Constructor for predefined values. This is only meant to be used for the
				 * example in our paper! Here, the coefficients are transformed using cubic
				 * smoothing splines.
				 * 
				 * @param coeffs     the coefficients for the sort.
				 * @param varIndices the variable indices identifying monomials.
				 * @param bias       whether we the coefficients contain a constant term or not.
				 * @param step       the step for which this sort is taken.
				 */
	public SchloeglSystemSort(double[] coeffs, int[][] varIndices, boolean bias) {
		this.dimension = 2;
		this.coeffs = new double[coeffs.length];
		for (int i = 0; i < coeffs.length; i++)
			this.coeffs[i] = coeffs[i];

		this.bias = bias;
		this.varIndices = new int[varIndices.length][];
		for (int i = 0; i < varIndices.length; i++) {
			this.varIndices[i] = new int[varIndices[i].length];
			for (int j = 0; j < this.varIndices[i].length; j++)
				this.varIndices[i][j] = varIndices[i][j];
		}
	}
	
	/**
	 * Constructor for general example.
	 * 
	 * @param coeffs     The coefficients for the sort.
	 * @param varIndices the indices to identify the monomials.
	 * @param bias       whether the score function contains a constant term or not.
	 */

	public SchloeglSystemSort(double[] coeffs, int[][] varIndices, boolean bias, int step) {
		this.dimension = 2;
//		this.step = step;
		this.coeffs = new double[coeffs.length];
		for (int i = 0; i < coeffs.length; i++)
			this.coeffs[i] = coeffMat[i][step-1];

		this.bias = bias;
		this.varIndices = new int[varIndices.length][];
		for (int i = 0; i < varIndices.length; i++) {
			this.varIndices[i] = new int[varIndices[i].length];
			for (int j = 0; j < this.varIndices[i].length; j++)
				this.varIndices[i][j] = varIndices[i][j];
		}
	}

	@Override
	public double scoreFunction(double[] v) {
		double score = 0.0;
		if (bias) {
			score = coeffs[0];
			for (int j = 0; j < varIndices.length; j++) {
				double temp = 1.0;
				for (int col : varIndices[j])
					temp *= v[col];
				score += coeffs[j + 1] * temp;
			}
		} else {
			for (int j = 0; j < varIndices.length; j++) {
				double temp = 1.0;
				for (int col : varIndices[j])
					temp *= v[col];
				score += coeffs[j] * temp;
			}
		}

		return score;
	}

	@Override
	public String toString() {
		return "Schloegl-Sort";
	}

}