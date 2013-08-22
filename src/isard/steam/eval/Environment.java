package isard.steam.eval;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Environment extends LinkedHashMap<String,Value> {
	public Environment() {super();}
	public Environment(Map<String,Value> map) {super(map);}
}
