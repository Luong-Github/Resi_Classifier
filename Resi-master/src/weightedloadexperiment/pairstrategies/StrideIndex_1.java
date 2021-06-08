package weightedloadexperiment.pairstrategies;

import java.util.List;

public class StrideIndex_1 extends OverSubscription {
	private int stride;

	public StrideIndex_1(int stride) {
		super();
		this.stride = stride;

	}

	public StrideIndex_1(Integer[] allHosts, int stride) {
		super(allHosts);
		this.stride = stride;
		modulo = allHosts.length;

	}

	@Override
	public void pairHosts() {
		List<Integer> sources = getSources();
		List<Integer> destinations = getDestinations();

		Integer[] hosts = getAllHosts();
		for (int i = 0; i < hosts.length; i++) {
			int x = hosts[i];
			sources.add(x);
			destinations.add(hosts[(i + stride) % modulo]);
		}

		setSources(sources);
		setDestinations(destinations);
	}

}
